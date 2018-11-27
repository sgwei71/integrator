package com.ibkglobal.integrator.engine.bean.mca.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.common.validator.ValidatorService;
import com.ibkglobal.common.validator.model.ValidResult;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.mca.log.LoggingMCA;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;

public class ProcessPreMCA extends ProcessDefaultMCA {
		
	@Autowired
	ValidatorService validatorService;
	
	public void preProcess(Exchange exchange) throws IBKExceptionMCA {
		// 헤더 초기화
		initSetHeader(exchange);
		
		// Valid Check 및 바디 파싱
		init(exchange);
		
		// PreProcess Logging
		LoggingMCA.logging(exchange);
	}

	protected void initSetHeader(Exchange exchange) throws IBKExceptionMCA {
		Message message = exchange.getIn();
		
	    message.setHeader(ConstantCode.SEQ, "2");
	    message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
	    message.setHeader(ConstantCode.INFRA_TYPE, InfraType.MCA);
	}
	
	protected void init(Exchange exchange) throws IBKExceptionMCA {
		
		IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
		
		// ValidCheck
	    ValidResult validResult = validatorService.validationSttl(ibkMessage.getStandardTelegram());
	    if (!validResult.isResult()) {
	    	String errorContent = "Valid Error";
			if (validResult.getErrorList().size() > 0) {
				errorContent = validResult.getErrorList().get(0);
			}
			throw new IBKExceptionMCA(ErrorType.VALID, errorContent);
	    }
	    
	    // Body Parsing
	    if (!ibkMessage.getInterfaceId().equals("ITRO00000035")) {
	    	bodyParsing(ibkMessage);
	    }
	}
}
