package com.ibkglobal.integrator.engine.bean.eai.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.common.validator.ValidatorService;
import com.ibkglobal.common.validator.model.ValidResult;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;

public class ProcessPreEAI extends ProcessDefaultEAI {
	
	@Autowired
	ValidatorService validatorService;

	public void preProcess(Exchange exchange) throws IBKExceptionEAI {
		// 헤더 초기화
		initSetHeader(exchange);
		
		// 파싱 및 초기화
		init(exchange);
	}

	protected void initSetHeader(Exchange exchange) throws IBKExceptionEAI {
		Message message = exchange.getIn();
		
		message.setHeader(ConstantCode.SEQ, "2");
		message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
	    message.setHeader(ConstantCode.INFRA_TYPE, InfraType.EAI);
	    message.setHeader(ConstantCode.EAI_PARSING, "IN");
	}
	
	protected void init(Exchange exchange) throws IBKExceptionEAI {
		Message message = exchange.getIn();
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);

		// ValidCheck
		ValidResult validResult = validatorService.validationSttl(ibkMessage.getStandardTelegram());
		if (!validResult.isResult()) {
			String errorContent = "Valid Error";
			if (validResult.getErrorList().size() > 0) {
				errorContent = validResult.getErrorList().get(0);
			}
			throw new IBKExceptionEAI(ErrorType.VALID, errorContent);
		}
		
		// Body Parsing
	    bodyParsing(ibkMessage);
	    
	    // Set Service
	    serviceSet(ibkMessage);
		
		// 인터페이스 체크
		checkInterface(message.getHeader(ConstantCode.EAI_PARSING, String.class), ibkMessage);
	}
}
