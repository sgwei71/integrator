package com.ibkglobal.integrator.engine.builder.route.api;

import java.util.List;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestParamType;

import com.ibkglobal.integrator.engine.builder.model.RestHeader;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

import lombok.Getter;
import lombok.Setter;

public class ApiRestAdapterInboundTemplate extends RouteCreateDefault{
	
	@Getter @Setter
	RestDefinition restDefinition;

	public ApiRestAdapterInboundTemplate(RouteCreateInfo builderInfo) {
		super.setRouteCreateInfo(builderInfo);
		onAPIException();
		create();
	}

	@Override
	public void create() {
		
		RestDefinition restDefinition = new RestDefinition();
		
		EndpointInfo fromEndpoint = super.getRouteCreateInfo().getFromEndpoint();
		EndpointInfo toEndpoint	=	super.getRouteCreateInfo().getToEndpoint();
		
		restDefinition.setTag(fromEndpoint.getSwaggerTag());
		restDefinition.description(super.getRouteCreateInfo().getRouteDescription());
		restDefinition.setId(getRouteCreateInfo().getRouteDescription());
		restDefinition.verb(fromEndpoint.getHttpMehtod(), fromEndpoint.getPathNm()+"@requestTimeout=1000&serverInitializerFactory=#ibkHttpServerInitializerFactory"); //from
		
		restDefinition.to("direct:"+toEndpoint.getEndpointDirect());
		restDefinition.setConsumes(fromEndpoint.getConsumes());
		restDefinition.setProduces(fromEndpoint.getProduces());
		restDefinition.responseMessage().code(400).message("400 에러").endResponseMessage();
		restDefinition.responseMessage().code(401).message("401에러").endResponseMessage();
		List<RestHeader> headers = fromEndpoint.getHeaders();

		headers.forEach((header) -> {
			restDefinition.param().type(RestParamType.header).name(header.getHeaderName())
			.description(header.getHeaderDescription()).required(header.isHeaderRequired()).endParam();
		});
		
		restDefinition.param().type(RestParamType.body).name("body")
		.description(fromEndpoint.getSwaggerBodyDescription()).endParam();
		

		this.restDefinition=restDefinition ;
	}



	@SuppressWarnings("unchecked")
	@Override
	public <T> T get() {
		// TODO Auto-generated method stub
		return (T) restDefinition;
	}
}
