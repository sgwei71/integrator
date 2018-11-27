package com.ibkglobal.integrator.engine.bean.eai.common;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.ParsingType;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.ism.IsmWorkInfo;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceEAI;

public class ParsingEAI {
	
	@Autowired
	ResourceEAI resourceEAI;
	
	@Autowired
	ConverterService converterService;
	
	public void parsing(Exchange exchange) throws IBKExceptionEAI {
		
		Message message = exchange.getIn();

		StandardTelegram standardTelegram = null;
		
		IsmWorkInfo ismWorkInfo = null;
		
		IBKMessage ibkMessage = new IBKMessage();
		
		String parsing = message.getHeader(ConstantCode.EAI_PARSING, String.class);
		
		// MCA 파싱
		try {
			ParsingType parsingType = message.getHeader(ConstantCode.PARSING_TYPE, ParsingType.class);

			switch (parsingType) {
			case JSON:
				String messageData = null;

				if (message.getBody() instanceof byte[]) {
					messageData = new String(message.getBody(byte[].class));
				} else {
					messageData = message.getBody(String.class);
				}

				standardTelegram = converterService.jsonToObject(messageData, StandardTelegram.class);
				
				ibkMessage.setInterfaceId(standardTelegram.getSttlSysCopt().getSttlIntfId());
				break;
			case EAI_FLAT:				
				byte[] byteData = message.getBody(byte[].class);
				
				standardTelegram = converterService.flatToTelegram(byteData, getStructList(parsing, byteData));
				
				ibkMessage.setInterfaceId(standardTelegram.getSttlSysCopt().getSttlIntfId());
				break;
			case ISM :
				String ismData = null;

				if (message.getBody() instanceof byte[]) {
					ismData = new String(message.getBody(byte[].class));
				} else {
					ismData = message.getBody(String.class);
				}
				
				ismWorkInfo = converterService.jsonToObject(ismData, IsmWorkInfo.class);
				
				ibkMessage.setInterfaceId(ismWorkInfo.getInterfaceId());
			default:
				break;
			}

			ibkMessage.setBaseData(message.getBody());
			ibkMessage.setStandardTelegram(standardTelegram);
			ibkMessage.setIsmWorkInfo(ismWorkInfo);

			// 메시지 최종 셋
			message.setBody(ibkMessage);

		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.PARSING, "Parsing error : " + e.getMessage());
		}

		// 인터페이스 아이디 확인
		if (StringUtils.isEmpty(ibkMessage.getInterfaceId())) {
			throw new IBKExceptionEAI(ErrorType.MESSAGE, "Can't find interface");
		}
	}
	
	public List<Tlgr> getStructList(String parsing, byte[] byteData) {

		IoCacheMCA ioCacheMCA = null;
		List<Tlgr> structList = null;

		// 인터페이스 아이디 추출
		byte[] intf = new byte[12];
		System.arraycopy(byteData, 198, intf, 0, 12);
		String interfaceId = resourceEAI.getEaiKey(new String(intf));

		switch (parsing) {
		case "IN":
			ioCacheMCA = resourceEAI.getSourceIo(interfaceId, parsing);
			structList = ioCacheMCA.getInBound().getFieldList();
			break;
		case "OUT":
			ioCacheMCA = resourceEAI.getTargetIo(interfaceId, parsing);
			structList = ioCacheMCA.getOutBound().getFieldList();
			break;
		default:
			break;
		}

		return structList;
	}
}
