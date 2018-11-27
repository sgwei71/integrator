package com.ibkglobal.integrator.manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibkglobal.integrator.manager.RouteManager;
import com.ibkglobal.integrator.manager.instance.InstanceRouteType;
import com.ibkglobal.integrator.manager.model.RouteInfo;
import com.ibkglobal.integrator.manager.model.RouteStat;

@Service
public class RouteService {
	
	@Autowired
	RouteManager routeManager;
	
	public List<RouteStat> routeStatusList() {
		return routeManager.routeStatusList();
	}
	
	public void start(String routeId) {
		routeManager.start(routeId);
	}
	
	public void stop(String routeId) {
		routeManager.stop(routeId);
	}
	
	public void remove(String routeId) {
		routeManager.remove(routeId);
	}
	
	public void allStart() {
		routeManager.allStart();
	}
	
	public void allStop() {
		routeManager.allStop();
	}
	
	public void addBuilder(RouteInfo routeInfo) {	
		routeManager.addBuilder(routeInfo);
	}
	
	public void removeBuilder(RouteInfo routeInfo) {
		routeManager.removeBuilder(routeInfo);
	}
	
	/**
	 * Get RouteInfo
	 * @param routeId
	 * @return
	 */
	public RouteInfo getRouteInfo(String routeId) {
		return routeManager.getRouteInfo(routeId);
	}
	
	/**
	 * Get RouteInfoAll
	 * @return
	 */
	public List<RouteInfo> getAllRouteInfo() {
		return routeManager.getAllRouteInfo();
	}
	
	/**
	 * Get RouteInfoAll(Type)
	 * @return
	 */
	public List<RouteInfo> getAllRouteInfo(InstanceRouteType instanceRouteType) {
		return routeManager.getAllRouteInfo(instanceRouteType);
	}
}
