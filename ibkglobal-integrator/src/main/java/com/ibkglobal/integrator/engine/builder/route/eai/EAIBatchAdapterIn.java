package com.ibkglobal.integrator.engine.builder.route.eai;

import org.apache.camel.builder.Builder;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class EAIBatchAdapterIn extends RouteCreateDefault {

	public EAIBatchAdapterIn(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onEAIBatchException();
		create();
	}

	@Override
	public void create() {
		createEndpoint("from");

		// routeId
		if (!StringUtils.isEmpty(getBuilderInfo().getRouteId())) {
			this.routeId(getBuilderInfo().getRouteId());
		}
		
		setDefaultHeader("1");
		
		// 파싱
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ParsingEAI.class, "parsing");
		
		createEndpoint("to");
		
		// 컴포징
		this.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()));
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ComposingEAI.class, "composing");
		
		removeHeaders("*", getExcludePatterns());
	}
}
