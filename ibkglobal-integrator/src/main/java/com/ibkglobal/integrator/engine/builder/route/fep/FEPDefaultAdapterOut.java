package com.ibkglobal.integrator.engine.builder.route.fep;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.engine.bean.fep.log.LoggingFEP;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class FEPDefaultAdapterOut extends RouteCreateDefault {

	public FEPDefaultAdapterOut(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onFEPException();
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
		
		setDefaultHeader("3");
		
		this.process(new Processor() {			
			@Override
			public void process(Exchange exchange) throws Exception {
				// AdapterIn Logging
				LoggingFEP.logging(exchange);
			}
		});
		
		removeHeaders("*", getExcludePatterns());
		
		createEndpoint("to");
		
		setDefaultHeader("4");
		
		this.process(new Processor() {			
			@Override
			public void process(Exchange exchange) throws Exception {
				// AdapterIn Logging
				LoggingFEP.logging(exchange);
			}
		});
	}
}
