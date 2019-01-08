package com.ibkglobal.integrator.engine.builder.route;

import org.apache.camel.Exchange;
import org.apache.camel.model.RouteDefinition;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;

import lombok.Getter;
import lombok.Setter;

@Deprecated
public abstract class RestRouteCreateDefault extends RouteCreateDefault implements RouteCreate{
	
	@Getter @Setter
	private RouteCreateInfo routeCreateInfo;
	
	@Getter
	private static String excludePatterns[] =  { Exchange.HTTP_RESPONSE_CODE,"OUT-TRAN-ID","X-IBM-Client-Id","Accept","connection", "Content-Length", "Content-Type", "LOCAL_ENDPOINT", "Authorization","IBK_INTERFACE_ID" };
	
}
