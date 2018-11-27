package com.ibkglobal.integrator.engine.bean.fep.error;

import org.apache.camel.Exchange;

import com.ibkglobal.integrator.util.ErrorUtil;

public class ErrorCatchFEP {
	
	public void catchError(Exchange exchange) throws Exception {
		init(exchange);
	}
	
	private void init(Exchange exchange) throws Exception {
		Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		
		// 에러 코드 생성
		ErrorUtil.setErrorCode(exchange, throwable);
		
		// 에러 내용 생성
		exchange.getOut().setBody(throwable.getMessage().getBytes("UTF-8"));
	}
}
