package com.ibkglobal.integrator.engine.bean.eai.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;

public class ProcessAfterEAI extends ProcessDefaultEAI {

	public void afterProcess(Exchange exchange) throws IBKExceptionEAI {
		// 헤더 초기화
		initSetHeader(exchange);

		// 파싱 및 초기화
		init(exchange);
	}

	protected void initSetHeader(Exchange exchange) throws IBKExceptionEAI {
		Message message = exchange.getIn();

		message.setHeader(ConstantCode.SEQ, "5");
		message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
		message.setHeader(ConstantCode.INFRA_TYPE, InfraType.EAI);
		message.setHeader(ConstantCode.EAI_PARSING, "OUT");
	}

	protected void init(Exchange exchange) throws IBKExceptionEAI {
		Message message = exchange.getIn();
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);

		bodyParsing(ibkMessage);
		
		checkInterface(message.getHeader(ConstantCode.EAI_PARSING, String.class), ibkMessage);
	}
}
