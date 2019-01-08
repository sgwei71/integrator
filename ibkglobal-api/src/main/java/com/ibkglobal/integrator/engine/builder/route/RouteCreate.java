package com.ibkglobal.integrator.engine.builder.route;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;

public interface RouteCreate {
	
	public RouteCreateInfo getRouteCreateInfo();
	
	public void setRouteCreateInfo(RouteCreateInfo routeCreateInfo);
	
	public void create();
	
	public <T> T get();
}
