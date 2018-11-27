package com.ibkglobal.integrator.engine.builder.route.mca;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.mca.log.LoggingMCA;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class MCADefaultAdapterIn extends RouteCreateDefault {

	public MCADefaultAdapterIn(RouteCreateInfo builderInfo) {
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
		
		setDefaultHeader("1");
		
		this.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				// AdapterIn Logging
				LoggingMCA.logging(exchange);
			}
		});
		
		// 파싱
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));	
		this.bean(com.ibkglobal.integrator.engine.bean.mca.common.ParsingMCA.class, "parsing");

		// to
		createEndpoint("to");

		setDefaultHeader("6");
		
		// 컴포징
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));
		this.bean(com.ibkglobal.integrator.engine.bean.mca.common.ComposingMCA.class, "composing");
		
		this.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				// AdapterOut Logging
				LoggingMCA.logging(exchange);
			}
		});
		
		removeHeaders("*", getExcludePatterns());
	}
}
