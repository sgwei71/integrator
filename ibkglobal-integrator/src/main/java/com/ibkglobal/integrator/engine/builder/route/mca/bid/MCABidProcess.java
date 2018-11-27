package com.ibkglobal.integrator.engine.builder.route.mca.bid;

import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class MCABidProcess extends RouteCreateDefault {

	public MCABidProcess(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onMCAException();
		create();
	}
	
	@Override
	public void create() {
		createEndpoint("from");
		
		// routeId
		if (!StringUtils.isEmpty(getBuilderInfo().getRouteId())) {
			this.routeId(getBuilderInfo().getRouteId());
		}
		
	    // 비드 처리
		this.bean(com.ibkglobal.integrator.engine.bean.mca.work.MCABidHandle.class, "execute");
	}
}
