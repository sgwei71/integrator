package com.ibkglobal.integrator.api.rest.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibkglobal.integrator.engine.monitor.CamelMonitor;
import com.ibkglobal.integrator.manager.service.RouteService;

@Service
public class ManagerAdapterService {
	
	@Autowired
	RouteService routeService;
	
	@Autowired
	CamelMonitor camelMonitor;
	
	public boolean adapterStart(String routeId) throws Exception {
		
		routeService.start(routeId);
		
		return camelMonitor.getRouteStatus(routeId).getState().equalsIgnoreCase("Started");
	}
	
	public boolean adapterStop(String routeId) throws Exception {
		
		routeService.stop(routeId);
		
		return camelMonitor.getRouteStatus(routeId).getState().equalsIgnoreCase("Stopped");
	}
	
	public boolean adapterRemove(String routeId) throws Exception {
		
		routeService.remove(routeId);
		
		return true;
	}
}
