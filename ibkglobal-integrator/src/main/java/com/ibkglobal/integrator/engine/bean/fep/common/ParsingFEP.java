package com.ibkglobal.integrator.engine.bean.fep.common;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibk.ibkglobal.data.io.model.Header;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.ParsingType;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;

public class ParsingFEP extends ParsingDefaultFEP {

	public void parsing(Exchange exchange) throws IBKExceptionFEP {
		Message message = exchange.getIn();

		IBKMessage ibkMessage = null;

		ParsingType parsingType = null;

		try {
			parsingType = message.getHeader(ConstantCode.PARSING_TYPE, ParsingType.class);

			switch (parsingType) {
			case JSON:
				ibkMessage = jsonParsing(message);
				break;
			case FLAT:
				ibkMessage = flatParsing(message);
				break;
			case ISO:
				ibkMessage = isoParsing(message);
				break;
			case DELIMITER:
				ibkMessage = delimiterParsing(message);
				break;
			default:
				break;
			}

			// 메시지 최종 셋
			message.setBody(ibkMessage);

		} catch (Exception e) {
			throw new IBKExceptionFEP(ErrorType.PARSING, "Parsing error : " + e.getMessage());
		}

		// 인터페이스 아이디 확인
		if (StringUtils.isEmpty(ibkMessage.getInterfaceId())) {
			throw new IBKExceptionFEP(ErrorType.MESSAGE, "Can't find interface");
		}
		
		message.setHeader(ConstantCode.IBK_INTERFACE_ID, ibkMessage.getInterfaceId());
	}

	/**
	 * FEP Json 파싱
	 * @param message
	 * @return
	 * @throws Exception
	 */
	protected IBKMessage jsonParsing(Message message) throws Exception {

		IBKMessage ibkMessage = new IBKMessage();

		StandardTelegram standardTelegram = null;

		String messageData = null;

		if (message.getBody() instanceof byte[]) {
			messageData = new String(message.getBody(byte[].class), "UTF-8");
		} else {
			messageData = message.getBody(String.class);
		}
		
		System.out.println("메시지 데이터 확인 : " + messageData);

		standardTelegram = converterService.jsonToObject((String) messageData, StandardTelegram.class);

		String interfaceId = standardTelegram.getSttlSysCopt().getSttlIntfId();

		ibkMessage.setBaseData(message.getBody());
		ibkMessage.setInterfaceId(interfaceId);
		ibkMessage.setStandardTelegram(standardTelegram);
		
		if (standardTelegram.getSttlSysCopt().getSttlErcd().contains("E")) {
			throw new IBKExceptionFEP(ErrorType.ETC, standardTelegram.getSttlMsgCopt().toString());
		}

		// 개별부 데이터가 있을 때 개별부 파싱 실행
		if (ibkMessage.getStandardTelegram().getUserData() != null && ibkMessage.getStandardTelegram().getUserData().getData() != null) {
			// 헤더부 파싱
			ibkMessage.setHeader(jsonHeaderParsing(ibkMessage));
			// 개별부 바디 파싱
			ibkMessage.setBody(jsonBodyParsing(ibkMessage));
		}

		return ibkMessage;
	}

	/**
	 * FEP Flat 파싱
	 * @param message
	 * @return
	 * @throws IBKExceptionFEP
	 */
	protected IBKMessage flatParsing(Message message) throws IBKExceptionFEP {

		IBKMessage ibkMessage = new IBKMessage();
		
		byte[] messageData = message.getBody(byte[].class);
		
		LinkedHashMap<String, Object> headerData = new LinkedHashMap<>();
		LinkedHashMap<String, Object> bodyData   = new LinkedHashMap<>();

		ByteBuffer messageBuffer = ByteBuffer.wrap(messageData);
		
		// 헤더 아이디로 헤더를 찾는다(헤더 하나(업무 라우터에 입력)로 다수의 인터페이스를 사용하기위함)
		Header header = getHeader(message);
		
		// 기존 시스템 중 헤더를 다중으로 사용하는 경우가 있어서 넣음
		List<Tlgr> headerIoList = getHeaderList(header);				
				
		// 헤더를 먼저 Set한다.
		headerData.putAll(converterService.byteArrayToMap(messageBuffer, headerIoList));
		
		// 헤더와 헤더정보로 인터페이스 아이디를 찾는다.
		String interfaceId = getInterfaceId(message, header, headerData);
		
		// Body 데이터를 Set한다.
		IoCacheFEP bodyEntity = resourceFEP.getSourceBodyEntity(interfaceId);
		bodyData.putAll(converterService.byteArrayToMap(messageBuffer, bodyEntity.getData().getFieldList()));
		
		// IbkMessage Set
		ibkMessage.setInterfaceId(interfaceId);
		ibkMessage.setHeader(headerData);
		ibkMessage.setBody(bodyData);
		ibkMessage.setStandardTelegram(null);

		return ibkMessage;
	}

	/**
	 * FEP Iso 파싱
	 * @param message
	 * @return
	 * @throws IBKExceptionFEP
	 */
	protected IBKMessage isoParsing(Message message) throws Exception {

		IBKMessage ibkMessage = new IBKMessage();
		
		byte[] messageData = message.getBody(byte[].class);
		
		// Header Set
		Header header = getHeader(message);
		List<Tlgr> headerIoList = getHeaderList(header);
		
		LinkedHashMap<String, Object> headerData = converterService.isoToHeaderData(messageData, headerIoList);
		
		String interfaceId = getInterfaceId(message, header, headerData);
		
		// Body Set
		IoCacheFEP bodyEntity = resourceFEP.getSourceBodyEntity(interfaceId);		
		LinkedHashMap<String, Object> bodyData = converterService.isoToBodyData(messageData, bodyEntity.getData().getFieldList());
		
		// IbkMessage Set
		ibkMessage.setInterfaceId(interfaceId);
		ibkMessage.setHeader(headerData);
		ibkMessage.setBody(bodyData);
		ibkMessage.setStandardTelegram(null);

		return ibkMessage;
	}
	
	/**
	 * FEP Delimiter 파싱
	 * @param message
	 * @return
	 * @throws IBKExceptionFEP
	 */
	protected IBKMessage delimiterParsing(Message message) throws Exception {
		
		IBKMessage ibkMessage = new IBKMessage();
		
		byte[] messageData = message.getBody(byte[].class);
		
		// Header Set
		Header header = getHeader(message);
		List<Tlgr> headerIoList = getHeaderList(header);
		
		LinkedHashMap<String, Object> headerData = converterService.delimiterToHeaderData(messageData, headerIoList);
		
		String interfaceId = getInterfaceId(message, header, headerData);
		
		// Body Set
		IoCacheFEP bodyEntity = resourceFEP.getSourceBodyEntity(interfaceId);
		LinkedHashMap<String, Object> bodyData = converterService.delimiterToBodyData("|", messageData, headerIoList, bodyEntity.getData().getFieldList());
		
		// IbkMessage Set
		ibkMessage.setInterfaceId(interfaceId);
		ibkMessage.setHeader(headerData);
		ibkMessage.setBody(bodyData);
		ibkMessage.setStandardTelegram(null);

		return ibkMessage;
	}
}
