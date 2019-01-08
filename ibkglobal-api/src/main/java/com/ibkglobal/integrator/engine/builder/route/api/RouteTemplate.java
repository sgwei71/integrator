package com.ibkglobal.integrator.engine.builder.route.api;

import org.apache.camel.Endpoint;
import org.apache.camel.Expression;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.RouteDefinition;

import com.ibkglobal.integrator.engine.builder.model.ParsingType;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.manager.instance.InstanceRouteType;
import com.ibkglobal.integrator.manager.instance.InstanceType;
import com.sun.mail.handlers.handler_base;

public class RouteTemplate extends RouteDefinition {

	
	public void createReoutDefinition(RouteCreateInfo createInfo) {
		//1. Route Header Information setting
		
		//2. from
		
		
		//3. to
		
		//
		EndpointInfo fromEndpoint = createInfo.getFromEndpoint();
		
		InstanceRouteType instanceRouteType = createInfo.getInstanceRouteType(); //Route Type(Adapter or Router)
		
		InstanceType instanceType = createInfo.getInstanceType(); //Integrator type(API or MCA)
		
		ParsingType parsingType = createInfo.getParsingType(); //ParsingType
		
		
		EndpointInfo toEndpoint = createInfo.getToEndpoint();
	}

}
