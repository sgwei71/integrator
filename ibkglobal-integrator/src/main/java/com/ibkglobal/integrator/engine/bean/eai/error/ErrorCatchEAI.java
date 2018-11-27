package com.ibkglobal.integrator.engine.bean.eai.error;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.util.ErrorUtil;
import com.ibkglobal.message.common.ism.IsmWorkInfo;
import com.ibkglobal.message.common.ism.IsmWorkInfo.IsmWorkResult;
import com.ibkglobal.message.converter.service.ConverterService;

public class ErrorCatchEAI {
	
	@Autowired
	ConverterService converterService;
	
	public void catchError(Exchange exchange) {
		init(exchange);
	}
	
	public void init(Exchange exchange) {
		Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		
		// 에러 코드 생성
	    ErrorUtil.setErrorCode(exchange, throwable);

	    // 에러 전문 Set
	    ErrorUtil.setErrorMessage(exchange);
		
		exchange.getOut().copyFrom(exchange.getIn());
	}
	
	public void catchBatchError(Exchange exchange) {
		Message message = exchange.getIn();
		
		Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		
		IsmWorkInfo ismWorkInfo = new IsmWorkInfo();
		
		// 오류 메시지 생성
		ismWorkInfo.setIsmWorkResult(new IsmWorkResult(false, ErrorUtil.createErrorMsg(throwable)));
		
		message.setHeader(ConstantCode.ERROR_HEADER, ismWorkInfo);
	}
}
