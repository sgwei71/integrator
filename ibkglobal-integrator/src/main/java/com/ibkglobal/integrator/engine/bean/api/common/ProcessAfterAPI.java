package com.ibkglobal.integrator.engine.bean.api.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.message.InfraType;

public class ProcessAfterAPI {

	@Autowired
	ParsingAPI parsingAPI;
	
	public void afterProcess(Exchange exchange) throws Exception {		
		// 헤더 초기화
		initSetHeader(exchange);

		// 파싱 및 초기화
		init(exchange);
	}
	
	protected void initSetHeader(Exchange exchange) throws Exception {
		Message message = exchange.getIn();

		message.setHeader(ConstantCode.SEQ, "5");
		message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
		message.setHeader(ConstantCode.INFRA_TYPE, InfraType.MCA);
	}
	//flat to Telegram 
	protected void init(Exchange exchange) throws Exception {
		parsingAPI.parsingFromHost(exchange);
	}
}
