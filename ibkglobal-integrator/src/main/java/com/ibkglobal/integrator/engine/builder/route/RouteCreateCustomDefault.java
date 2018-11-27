package com.ibkglobal.integrator.engine.builder.route;

import org.apache.camel.builder.RouteBuilder;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;

import lombok.Getter;
import lombok.Setter;

public abstract class RouteCreateCustomDefault extends RouteBuilder implements RouteCreate {
	
	@Getter
	@Setter
	private RouteCreateInfo builderInfo;
	
	@Getter
	private static String excludePatterns[] = { "connection", "content-length", "Content-Type" };

	@Override
	public RouteCreateInfo getRouteCreateInfo() {
		return builderInfo;
	}

	@Override
	public void setRouteCreateInfo(RouteCreateInfo routeCreateInfo) {
		setBuilderInfo(routeCreateInfo);
	}
	
	@Override
	public void create() {
		try {
			configure();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get() {
		return (T) this.getRouteCollection().getRoutes();
	}

}
