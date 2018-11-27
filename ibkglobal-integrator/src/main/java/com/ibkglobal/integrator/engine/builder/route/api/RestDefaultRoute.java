package com.ibkglobal.integrator.engine.builder.route.api;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteBuilderCreate;

public class RestDefaultRoute extends RouteBuilderCreate {

	public RestDefaultRoute(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		//onMCAException();
		create();
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

}
