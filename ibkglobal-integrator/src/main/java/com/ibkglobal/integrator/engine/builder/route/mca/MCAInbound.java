package com.ibkglobal.integrator.engine.builder.route.mca;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class MCAInbound extends RouteCreateDefault {
	
	public MCAInbound(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onMCAException();
		create();
	}

	@Override
	public void create() {
		createEndpoint("from");
		
		routeId(getBuilderInfo().getRouteId())		
		
		// 라우터 전 처리 : 헤더 설정 & Valid Check & Body 스키마 생성
		.bean(com.ibkglobal.integrator.engine.bean.mca.common.ProcessPreMCA.class, "preProcess")
		
		// 매핑
		.bean(com.ibkglobal.integrator.engine.bean.mca.common.MappingMCA.class, "mappingExecute")
		
		// 전 처리 업무
		.bean(com.ibkglobal.integrator.engine.bean.mca.work.MCAWorkPreProcess.class, "execute");
		
		createEndpoint("to");
		
		// 라우터 후 처리 : 헤더 설정 & Body 스키마 생성
		bean(com.ibkglobal.integrator.engine.bean.mca.common.ProcessAfterMCA.class, "afterProcess")
		
		// 매핑
		.bean(com.ibkglobal.integrator.engine.bean.mca.common.MappingMCA.class, "mappingExecute")
		
		// 후 처리 업무
		.bean(com.ibkglobal.integrator.engine.bean.mca.work.MCAWorkAfterProcess.class, "execute");
	}
}
