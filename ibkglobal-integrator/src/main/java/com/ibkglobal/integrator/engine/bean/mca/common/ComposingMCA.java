package com.ibkglobal.integrator.engine.bean.mca.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.ParsingType;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;

public class ComposingMCA {

	@Autowired
	ConverterService converterService;

	public void composing(Exchange exchange) throws IBKExceptionMCA {

		try {
			Message message = exchange.getIn();
			
			if (message.getBody() instanceof IBKMessage) {
				IBKMessage ibkMessage = message.getBody(IBKMessage.class);

				ParsingType type = message.getHeader(ConstantCode.PARSING_TYPE, ParsingType.class);

				// MCA에서 최종 전송
				IBKMessageUtil.replyMessageDefaultSet(ibkMessage.getStandardTelegram(), ConstantCode.MCA_CODE);

				switch (type) {
				case JSON:
					message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()));
					break;
				case JSON_FLAT:
					message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()).getBytes());
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}
	
	/**
	 * Error Composing
	 * 
	 * @param exchange
	 */
	public void errorComposing(Exchange exchange) throws IBKExceptionMCA {
		
		try {
			Message message = exchange.getIn();

			ParsingType type = message.getHeader(ConstantCode.PARSING_TYPE, ParsingType.class);
			StandardTelegram standardTelegram = message.getHeader(ConstantCode.ERROR_HEADER, StandardTelegram.class);

			if (type != null && standardTelegram != null) {
				switch (type) {
				case JSON:
					message.setBody(converterService.objectToJson(standardTelegram));
					break;
				case JSON_FLAT:
					message.setBody(converterService.objectToJson(standardTelegram).getBytes());
					break;
				default:
					message.setBody(converterService.objectToJson(standardTelegram));
					break;
				}
			} else {
				message.setBody(converterService.objectToJson(standardTelegram));
			}

		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}
}
