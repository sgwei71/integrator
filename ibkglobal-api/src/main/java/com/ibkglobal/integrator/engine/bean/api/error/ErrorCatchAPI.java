package com.ibkglobal.integrator.engine.bean.api.error;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.log.LoggerBean;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;
import com.ibkglobal.integrator.util.ErrorUtilAPI;
import com.ibkglobal.message.common.ism.IsmWorkInfo;
import com.ibkglobal.message.common.ism.IsmWorkInfo.IsmWorkResult;
import com.ibkglobal.message.converter.service.ConverterService;

public class ErrorCatchAPI {
	
	@Autowired
	ConverterService converterService;
	
	public void catchError(Exchange exchange) {
		init(exchange);
	}
	
	public void init(Exchange exchange) {
		Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		
		// 에러 코드 생성
	    ErrorUtilAPI.setErrorCode(exchange, throwable);
	    // 에러 전문 Set
	    ErrorUtilAPI.setErrorMessage(exchange);
		exchange.getOut().copyFrom(exchange.getIn());
	    exchange.getOut().removeHeaders("*", RouteCreateDefault.getExcludePatterns());

	    //로그 적재
	    LoggerBean.loggingError(exchange);
		
	}
}
