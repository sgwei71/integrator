package com.ibkglobal.integrator.engine.bean.eai.common;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.ParsingType;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.ism.IsmWorkInfo;
import com.ibkglobal.message.common.ism.IsmWorkInfo.IsmWorkResult;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceEAI;

public class ComposingEAI {
 
	@Autowired
	ConverterService converterService;

	@Autowired
	ResourceEAI resourceEAI;

	public void composing(Exchange exchange) throws IBKExceptionEAI {
		//TEST
		try {
			Message message = exchange.getIn();

			if (message.getBody() instanceof IBKMessage) {
				IBKMessage ibkMessage = message.getBody(IBKMessage.class);

				ParsingType type = message.getHeader(ConstantCode.PARSING_TYPE, ParsingType.class);

				switch (type) {
				case JSON:
					// EAI에서 최종 전송
					IBKMessageUtil.replyMessageDefaultSet(ibkMessage.getStandardTelegram(), ConstantCode.EAI_CODE);
					message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()));
					break;
				case JSON_FLAT:
					// EAI에서 최종 전송
					IBKMessageUtil.replyMessageDefaultSet(ibkMessage.getStandardTelegram(), ConstantCode.EAI_CODE);
					message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()).getBytes());
					break;
				case EAI_FLAT:
					// EAI에서 최종 전송
					IBKMessageUtil.replyMessageDefaultSet(ibkMessage.getStandardTelegram(), ConstantCode.EAI_CODE);
					
					// 대내 EAI일 경우
					String parsing = message.getHeader(ConstantCode.EAI_PARSING, String.class);
					
					message.setBody(converterService.telegramToFlat(ibkMessage.getStandardTelegram(), getStructList(parsing, ibkMessage.getInterfaceId())));
					break;
				case ISM:
					ibkMessage.getIsmWorkInfo().setIsmWorkResult(new IsmWorkResult(true, ""));
					message.setBody(converterService.objectToJson(ibkMessage.getIsmWorkInfo()));
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}

	/**
	 * Error Composing
	 * 
	 * @param exchange
	 */
	public void errorComposing(Exchange exchange) throws IBKExceptionEAI {

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
			throw new IBKExceptionEAI(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}
	
	/**
	 * Batch Error Composing
	 * @param exchange
	 * @throws IBKExceptionEAI
	 */
	public void errorBatchComposing(Exchange exchange) throws IBKExceptionEAI {
		
		try {
			Message message = exchange.getIn();
			
			IsmWorkInfo ismWorkInfo = message.getHeader(ConstantCode.ERROR_HEADER, IsmWorkInfo.class);
			
			message.setBody(converterService.objectToJson(ismWorkInfo));
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}

	public List<Tlgr> getStructList(String parsing, String interfaceId) {

		IoCacheMCA ioCacheMCA = null;
		List<Tlgr> structList = null;

		switch (parsing) {
		case "IN":
			ioCacheMCA = resourceEAI.getTargetIo(interfaceId, parsing);
			structList = ioCacheMCA.getInBound().getFieldList();
			break;
		case "OUT":
			ioCacheMCA = resourceEAI.getSourceIo(interfaceId, parsing);
			structList = ioCacheMCA.getOutBound().getFieldList();
			break;
		default:
			break;
		}

		return structList;
	}
}
