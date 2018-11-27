package com.ibkglobal.integrator.engine.builder.route.eai;

import org.apache.camel.builder.Builder;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class EAIDefaultAdapterIn extends RouteCreateDefault {
	
	public EAIDefaultAdapterIn(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onEAIException();
		create();
	}

	@Override
	public void create() {
		createEndpoint("from");
		
		this.routeId(getBuilderInfo().getRouteId());
		
		setDefaultHeader("1");
		
		// 파싱
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ParsingEAI.class, "parsing");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.log.LoggingEAI.class, "logging");
		
		createEndpoint("to");
		
		setDefaultHeader("6");
		this.bean(com.ibkglobal.integrator.engine.bean.eai.log.LoggingEAI.class, "logging");
		
		// 컴포징
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ComposingEAI.class, "composing");
		
		removeHeaders("*", getExcludePatterns());
	}
}
