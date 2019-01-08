package com.ibkglobal.integrator.engine.bean.rest.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.common.validator.ValidatorService;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.IBKExceptionAPI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;
import com.ibkglobal.message.struct.resource.ResourceAPI;

public class PreProcess {
	
	@Autowired
	ResourceAPI resourceAPI;
	
	@Autowired
	ValidatorService validatorService;
	
	@Autowired
	RestMsgParser restMsgParser;
	
	private Logger logger = LoggerFactory.getLogger(PreProcess.class);
	
	public void execute(Exchange exchange) throws Exception {
		logger.info("::::::::::::::PreProcess::::::::::::{}",exchange.getIn());
		logger.info("::::::::::::::PreProcess::::::::::::{}",exchange.getIn().getHeaders());
		
		//1. 헤더값 초기화
		initSetHeader(exchange);
		
		//2. 파싱 및 초기화
		init(exchange);
	}

	private void init(Exchange exchange) throws IBKExceptionAPI {
		restMsgParser.encode(exchange);
		//필요하면 여기서 유효성 체크를 실시
		IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
		logger.info("PreProcss IBKMessage --> {}",ibkMessage);
	}

	private void initSetHeader(Exchange exchange) {
		
		Message message = exchange.getIn();
		message.setHeader(ConstantCode.SEQ, "2");
		message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "N");
		message.setHeader(ConstantCode.INFRA_TYPE, InfraType.API);
		message.setHeader(ConstantCode.IN_OUT, "OUT"); //MCA LayOut이므로 outbound 전문을 취해옴, 정상적인 경우는 IN
		message.setHeader("Content-Type", "application/text");
		
	}
}
