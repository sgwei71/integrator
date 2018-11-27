package com.ibkglobal.integrator.engine.bean.mca.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.mca.log.LoggingMCA;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;
import com.ibkglobal.message.struct.resource.ResourceMCA;

public class ProcessAfterMCA extends ProcessDefaultMCA {

	@Autowired
	ResourceMCA resourceMCA;

	public void afterProcess(Exchange exchange) throws IBKExceptionMCA {
		// 헤더 초기화
		initSetHeader(exchange);

		// 파싱 및 초기화
		init(exchange);

		// AfterProcess Logging
		LoggingMCA.logging(exchange);
	}

	protected void initSetHeader(Exchange exchange) throws IBKExceptionMCA {
		Message message = exchange.getIn();

		message.setHeader(ConstantCode.SEQ, "5");
		message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
		message.setHeader(ConstantCode.INFRA_TYPE, InfraType.MCA);
	}

	protected void init(Exchange exchange) throws IBKExceptionMCA {
		Message message = exchange.getIn();

		IBKMessage ibkMessage = message.getBody(IBKMessage.class);

		// Body Parsing
		if (!ibkMessage.getInterfaceId().equals("ITRO00000035")) {
			bodyParsing(ibkMessage);
		}
	}
}
