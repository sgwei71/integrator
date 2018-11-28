package com.ibkglobal.integrator.engine.bean.api.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.common.validator.ValidatorService;
import com.ibkglobal.common.validator.model.ValidResult;
import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.fep.common.ParsingFEP;
import com.ibkglobal.integrator.engine.bean.fep.recovery.RecoveryJournalRepo;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.CommonUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;
import com.ibkglobal.message.struct.resource.ResourceFEP;
import com.ibkglobal.message.struct.resource.ResourceMCA;

public class ProcessPreAPI {
	
	@Autowired
	ParsingAPI parsingAPI;
	
	@Autowired
	ResourceMCA resourceMCA;
	@Autowired
	ValidatorService validatorService;
	
	@Autowired
	CamelConfig camelConfig;
	
	
	public void preProcess(Exchange exchange) throws Exception {
		// 헤더 초기화
		System.out.println("ProcessPreAPI init");

		initSetHeader(exchange);
		
		// 파싱 및 초기화
		init(exchange);
		System.out.println("init success");
	}
	
	protected void initSetHeader(Exchange exchange) throws CommonException {
		Message message = exchange.getIn();
		HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
		//IF ID Setting
		message.setHeader(ConstantCode.IBK_INTERFACE_ID, request.getHeader("INTERFACEID"));
	    
		message.setHeader(ConstantCode.SEQ, "2");
	    message.setHeader(ConstantCode.INFRA_TYPE, InfraType.API);
	    
	}
	
	protected void init(Exchange exchange) throws Exception {
		
		parsingAPI.parsing(exchange);
		// ValidCheck
		IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
	}
	
}
