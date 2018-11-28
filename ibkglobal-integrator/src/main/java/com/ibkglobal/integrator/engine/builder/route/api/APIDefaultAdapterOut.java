package com.ibkglobal.integrator.engine.builder.route.api;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;
import com.ibkglobal.message.IBKMessage;

public class APIDefaultAdapterOut extends RouteCreateDefault {

	public APIDefaultAdapterOut(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onMCAException();
		create();
	}

	@Override
	public void create() {

		// from
		createEndpoint("from");

		// routeId
		if (!StringUtils.isEmpty(getBuilderInfo().getRouteId())) {
			this.routeId(getBuilderInfo().getRouteId());
		}
		
		this.setHeader(ConstantCode.COMPOSING_HEADER, Builder.constant(getEndpointType(getBuilderInfo().getToEndpoint())))
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
				System.out.println("composing 전[Out]:"+ibkMessage);
			
			}
			
		});
		this.bean(com.ibkglobal.integrator.engine.bean.api.common.ComposingAPI.class, "composing")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				byte[] ibkMessage = exchange.getIn().getBody(byte[].class);
				System.out.println("composing후byte[]="+new String(ibkMessage));
			
			}
			
		});
		;
		
		setDefaultHeader("3");
	   // this.bean(com.ibkglobal.integrator.engine.bean.mca.log.LoggingMCA.class, "logging");
	    
	    removeHeaders("*", getExcludePatterns());
		
	    // to
	    createEndpoint("to");
		
		setDefaultHeader("4");	   
		this.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("to 후 ");
				
			}
			
		});
	   // this.bean(com.ibkglobal.integrator.engine.bean.mca.log.LoggingMCA.class, "logging");
	}
}
