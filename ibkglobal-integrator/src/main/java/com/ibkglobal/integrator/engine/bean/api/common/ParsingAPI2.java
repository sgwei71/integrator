package com.ibkglobal.integrator.engine.bean.api.common;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.model.Io;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.TelegramConstants;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceMCA;

@Component
public class ParsingAPI2 {

	@Autowired
	ResourceMCA resourceMCA;

	@Autowired
	ConverterService converterService;
	
	public void parsing(Exchange exchange) throws Exception {

		Message message = exchange.getIn();

		// IBK 메시지 셋
		IBKMessage ibkMessage = convert(message);
		System.out.println("[PHJ11]");
		// 최종 셋
		message.setBody(ibkMessage);
	}
	
	public IBKMessage convert(Message message) throws Exception {
		
		// 전문 파싱
		IBKMessage ibkMessage = jsonParsing(message);
		
		// 전문 바디 파싱(매핑을 위한 준비)
		// 1. UserData 없을 경우 패스
		// 2. L일 경우는 bypass 할 수 있도록 하드코딩 요청
		if (ibkMessage.getStandardTelegram().getUserData() != null
				&& ibkMessage.getStandardTelegram().getUserData().getData() != null
				&& !"L".equals(ibkMessage.getStandardTelegram().getSttlSysCopt().getSysEnvrInfoDcd())) {
			//bodyParsing(ibkMessage);
			// 2018.10.04(바디파싱 안하도록 임시 조치)
			System.out.println("[PHJ10]"+ibkMessage.getStandardTelegram().getUserData().getData());
			
			ibkMessage.setBody(ibkMessage.getStandardTelegram().getUserData().getData());
		}
		// 현재 SMR에 비드 전문이 없어서 하드코딩
		else if ("ITRO00000035".equals(ibkMessage.getInterfaceId())) {
			//ibkMessage.setBody(new LinkedHashMap<>());
		}
		
		return ibkMessage;
	}
	
	public IBKMessage jsonParsing(Message message) throws Exception {
		
		StandardTelegram standardTelegram = null;
		
		try {
			// 파싱 실행
			String messageData = null;
			if (message.getBody() instanceof byte[]) {
				messageData = new String(message.getBody(byte[].class));
			} else {
				messageData = message.getBody(String.class);
			}
			System.out.println("[PHJ9]"+messageData.toString());

			standardTelegram = converterService.jsonToObject((String) messageData, StandardTelegram.class);
			
			System.out.println("[PHJ9.1.1]"+messageData.toString());

			//byte[] result = converterService.telegramToFlat(standardTelegram, null);
			System.out.println("[PHJ9.1.2]"+messageData.toString());

			//System.out.println("표준전문 -> 대내 EAI 플랫전문 변환 결과 : " + new String(result, "MS949"));

			System.out.println("[PHJ9.1]sta.getUserDada()"+standardTelegram);

			message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.PARSING, "Parsing error : " + e.getMessage());
		}
		
		String interfaceId = standardTelegram.getSttlSysCopt().getSttlIntfId();
		
		// 2018.10.02 임시 하드코딩 (ITRO00000035) 비드때문
		if (StringUtils.isEmpty(interfaceId)) {
			throw new IBKExceptionMCA(ErrorType.MESSAGE, "Can't find interface : Parsing.stringParsing : " + interfaceId);
		}
		
		IBKMessage ibkMessage = new IBKMessage();
		
		// 원본 데이터 Set
		ibkMessage.setBaseData(message.getBody());
		ibkMessage.setInterfaceId(interfaceId);
		ibkMessage.setStandardTelegram(standardTelegram);
		
		return ibkMessage;
	}
	
	private void bodyParsing(IBKMessage ibkMessage) throws Exception {

		LinkedHashMap<String, Object> data = ibkMessage.getStandardTelegram().getUserData().getData();
		LinkedHashMap<String, Object> body = new LinkedHashMap<>();
		Io io = null;

		String mpngYn = null;

		try {
			// bypass 여부(Y / N)
			mpngYn = resourceMCA.getInterface(ibkMessage.getInterfaceId()).getCommon().getAttribute().getMpngYn().getCode();
			
			if ("Y".contains(mpngYn)) {
				// 'S'=요청 , 'R'=응답, 'K'=ACK
				String rqstRspnDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getRqstRspnDcd();
				if ("S".equals(rqstRspnDcd)) {
					io = resourceMCA.getSourceIo(ibkMessage.getInterfaceId(), "IN").getInBound();
				} else if ("R".equals(rqstRspnDcd)) {
					io = resourceMCA.getTargetIo(ibkMessage.getInterfaceId(), "OUT").getOutBound();
				}
			}

			if (io != null) {
				String inptTmgtDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd();
				body.putAll(converterService.listToMap(data, io.getFieldList(), isNullable(inptTmgtDcd)));
				ibkMessage.setBody(body);
			}
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.MESSAGE, "Message Search Error : " + ibkMessage.getInterfaceId());
		}
	}
	
	/**
	 * Is null able
	 * 
	 * @param code
	 * @return
	 */
	private boolean isNullable(String code) {
		boolean nullable = true;
		TelegramConstants.InptTmgtDcd inptTmgtDcd = TelegramConstants.InptTmgtDcd.of(code);

		switch (inptTmgtDcd) {
		case MASS_OUT_CONTINUE_REQUEST:
		case MASS_OUT_STOP_REQUEST:
			nullable = false;
			break;
		default:
			nullable = true;
			break;
		}
		return nullable;
	}
}
