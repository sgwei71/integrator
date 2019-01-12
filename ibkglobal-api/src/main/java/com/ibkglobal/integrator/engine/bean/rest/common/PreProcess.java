package com.ibkglobal.integrator.engine.bean.rest.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.common.validator.ValidatorService;
import com.ibkglobal.integrator.config.ConstantCodeAPI;
import com.ibkglobal.integrator.engine.builder.model.ParsingType;
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
		logger.info("[IBK INTEGRATOR]___ PreProcess::::::::::::{}",exchange.getIn());
		
		
		//1. 헤더값 초기화
		Message message = exchange.getIn();
		message.setHeader(ConstantCodeAPI.SEQ, "2");
		//표준 전문 적용 여부
		String parsingType = message.getHeader(ConstantCodeAPI.PARSING_TYPE, String.class);
		if(parsingType.indexOf("_") !=-1 ) {
			String fromParsingType = parsingType.substring(0,parsingType.indexOf("_"));
			String toParsingType = parsingType.substring(parsingType.indexOf("_")+1);
			message.setHeader(ConstantCodeAPI.FROM_PARSING_TYPE, fromParsingType);
			message.setHeader(ConstantCodeAPI.TO_PARSING_TYPE, toParsingType);
		}
//		message.setHeader(ConstantCodeAPI.IBK_NORMAL_MESSAGE_YN, "N");
		message.setHeader(ConstantCodeAPI.INFRA_TYPE, InfraType.API);
		message.setHeader(ConstantCodeAPI.IN_OUT, "OUT"); //MCA LayOut이므로 outbound 전문을 취해옴, 정상적인 경우는 IN
		logger.debug("[IBK INTEGRATOR]___ INIT Headers {}",exchange.getIn().getHeaders());
		
		//2. 파싱 및 초기화
		init(exchange);
	}

	private void init(Exchange exchange) throws IBKExceptionAPI {
		restMsgParser.encode(exchange);
		//필요하면 여기서 유효성 체크를 실시
		IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
		logger.info("PreProcss IBKMessage --> {}",ibkMessage);
	}


	public static void main(String args[]) {
		String s = ParsingType.FLAT_JSON.toString();
		System.out.println(s);
		String a = "A_B";
		System.out.println(a.substring(0,a.indexOf("_")));
		System.out.println(a.substring(a.indexOf("_")+1));
		
	}
}
