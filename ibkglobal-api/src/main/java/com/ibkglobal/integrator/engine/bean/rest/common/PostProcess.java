package com.ibkglobal.integrator.engine.bean.rest.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.message.InfraType;

public class PostProcess {
	
	@Autowired
	RestMsgParser  restMsgParser;
	
	private Logger logger = LoggerFactory.getLogger(PostProcess.class);
	
	public void execute(Exchange exchange) throws Exception {
		logger.info(":::::::::::::::PostProcess:::::::::::::::::::::::::::::");
		logger.info("message {}",new String(exchange.getIn().getBody(byte[].class),"MS949"));
		initSetHeader(exchange);
		// 파싱 및 초기화
		init(exchange);
	}
	protected void initSetHeader(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		message.setHeader(ConstantCode.SEQ, "5");
		message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
		message.setHeader(ConstantCode.INFRA_TYPE, InfraType.API);
	}
	
	//flat to Telegram 
	protected void init(Exchange exchange) throws Exception {
		restMsgParser.parsingFromHost(exchange);
	}
}
