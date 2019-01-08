package com.ibkglobal.integrator.engine.builder.route.api;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.log.LoggerBean;
import com.ibkglobal.integrator.engine.bean.rest.common.ComposingAPI;
import com.ibkglobal.integrator.engine.bean.rest.common.SttlSender;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;
import com.ibkglobal.message.IBKMessage;

public class RestDefaultAdapterOut extends RouteCreateDefault{
	
	private Logger logger = LoggerFactory.getLogger(RestDefaultAdapterOut.class);
	public RestDefaultAdapterOut(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onAPIException();
		create();
	}

	@Override
	public void create() {
		//from
		createEndpoint("from");
		
		//routeId
		if(!StringUtils.isEmpty(getBuilderInfo().getRouteId())) {
			this.routeId(getBuilderInfo().getRouteId());
		}
		this.setHeader(ConstantCode.COMPOSING_HEADER, Builder.constant(getEndpointType(getBuilderInfo().getToEndpoint())))
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
				logger.info("composing 전 --> {}",ibkMessage);
			}
		});
		
		this.bean(ComposingAPI.class, "composing")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				byte[] ibkMessage = exchange.getIn().getBody(byte[].class);
				logger.info("composing 후 --> {}", new String(ibkMessage,"MS949")	);
			}
		});

		removeHeaders("*",getExcludePatterns());
		process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				EndpointInfo toEndpointInfo=getBuilderInfo().getToEndpoint();
				exchange.getIn().setHeader("EndpointIp", toEndpointInfo.getEndpointIp());
				exchange.getIn().setHeader("EndpointPort",toEndpointInfo.getEndpointPort());
				exchange.getIn().setHeader("EndpointPathNm",toEndpointInfo.getPathNm());
			}
		});
		setDefaultHeader("2");
		this.bean(LoggerBean.class,"logging");	
		createEndpoint("to");
		setDefaultHeader("3");
		this.bean(LoggerBean.class,"logging");	
//		this.bean(SttlSender.class,"sender");
		//to
		//createEndpoint("to");
	}
}
