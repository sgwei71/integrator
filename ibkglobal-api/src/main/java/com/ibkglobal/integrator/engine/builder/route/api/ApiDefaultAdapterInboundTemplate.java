package com.ibkglobal.integrator.engine.builder.route.api;

import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.engine.bean.log.LoggerBean;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class ApiDefaultAdapterInboundTemplate extends RouteCreateDefault {

	public ApiDefaultAdapterInboundTemplate(RouteCreateInfo builderInfo) {
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
		
		this.bean(LoggerBean.class,"logging");

		// to
		createEndpoint("to");

		removeHeaders("*", getExcludePatterns());
		
		this.bean(LoggerBean.class,"logging");
	}
	
	@Override
	protected void setDefaultHeader(String seq) {
		// TODO Auto-generated method stub
		super.setDefaultHeader(seq);
	}
}
