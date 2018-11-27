package com.ibkglobal.integrator.engine.bean.mca.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.ParsingType;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceMCA;

public class ParsingMCA {
	
	@Autowired
	ResourceMCA resourceMCA;

	@Autowired
	ConverterService converterService;

	/**
	 * 표준 전문으로 변환
	 * @param exchange
	 * @throws Exception
	 */
	public void parsing(Exchange exchange) throws IBKExceptionMCA {
		Message message = exchange.getIn();

		StandardTelegram standardTelegram = null;
		
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
				break;
			case FLAT:
				break;
			case EAI_FLAT:
				break;
			case ISO:
				break;
			default:
				break;
			}
			
			IBKMessage ibkMessage = new IBKMessage();
			
			ibkMessage.setBaseData(message.getBody());
			ibkMessage.setInterfaceId(standardTelegram.getSttlSysCopt().getSttlIntfId());
			ibkMessage.setStandardTelegram(standardTelegram);
			
			// 메시지 최종 셋
			message.setBody(ibkMessage);			
			
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.PARSING, "Parsing error : " + e.getMessage());
		}
		
		// 인터페이스 아이디 확인
		if (StringUtils.isEmpty(standardTelegram.getSttlSysCopt().getSttlIntfId())) {
			throw new IBKExceptionMCA(ErrorType.MESSAGE, "Can't find interface");
		}
	}
}
