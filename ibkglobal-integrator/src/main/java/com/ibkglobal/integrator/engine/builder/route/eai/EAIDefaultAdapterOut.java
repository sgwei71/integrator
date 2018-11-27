package com.ibkglobal.integrator.engine.builder.route.eai;

import org.apache.camel.builder.Builder;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class EAIDefaultAdapterOut extends RouteCreateDefault {

	public EAIDefaultAdapterOut(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onEAIException();
		create();
	}
	
	@Override
	public void create() {
		createEndpoint("from");
		
		this.routeId(getBuilderInfo().getRouteId());
		
		setDefaultHeader("3");
		this.bean(com.ibkglobal.integrator.engine.bean.eai.log.LoggingEAI.class, "logging");
		
		// 컴포징
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ComposingEAI.class, "composing");
		
		removeHeaders("*", getExcludePatterns());
		
		// to
		createEndpoint("to");
		
		setDefaultHeader("4");
		
		// 파싱
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ParsingEAI.class, "parsing");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.log.LoggingEAI.class, "logging");
	}
}
