package com.ibkglobal.integrator.engine.builder.route.eai;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class EAIInbound extends RouteCreateDefault {

	public EAIInbound(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onEAIException();
		create();
	}
	
	@Override
	public void create() {
		createEndpoint("from");
		
		routeId(getBuilderInfo().getRouteId());
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ProcessPreEAI.class, "preProcess");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.log.LoggingEAI.class, "logging");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.MappingEAI.class, "mapping");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.work.EAIWorkPreProcess.class, "execute");
		
		createEndpoint("to");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.ProcessAfterEAI.class, "afterProcess");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.log.LoggingEAI.class, "logging");
		
		this.bean(com.ibkglobal.integrator.engine.bean.eai.common.MappingEAI.class, "mapping");
	}
}
