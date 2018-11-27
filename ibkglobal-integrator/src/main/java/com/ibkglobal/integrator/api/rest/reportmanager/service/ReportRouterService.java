package com.ibkglobal.integrator.api.rest.reportmanager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.model.RouteStatus;
import com.ibkglobal.integrator.engine.monitor.CamelMonitor;
import com.ibkglobal.integrator.manager.instance.InstanceAdmin;
import com.ibkglobal.integrator.manager.instance.InstanceRouteType;
import com.ibkglobal.integrator.manager.model.RouteInfo;
import com.ibkglobal.message.converter.service.ConverterService;

/**
 * Report Route Service
 *
 */
@Service
public class ReportRouterService {

	@Autowired
	InstanceAdmin instanceAdmin;
	
	@Autowired
	ConverterService converterService;
	
	@Autowired
	CamelMonitor camelMonitor;
		
	/**
	 * Get Report Route List
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReportRouteList() {
		
		List<Map<String, Object>> result = new ArrayList<>();
		List<RouteInfo> adapterList = instanceAdmin.getAllRouteInfo(InstanceRouteType.ROUTER);
		adapterList.stream().map(m -> m.getRouteCreate().getRouteCreateInfo()).forEach(E -> {
			Map<String, Object> tempMap = new HashMap<>();
			
			try {
				tempMap.putAll(converterService.objectToObject(E, HashMap.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
			RouteStatus routeStatsModel = camelMonitor.getRouteStatus(E.getRouteId());
			if (routeStatsModel != null) {
				try {
					tempMap.put("dumpRouteStats", converterService.objectToObject(routeStatsModel, HashMap.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result.add(tempMap);
		});
		return result;
	}
	
	/**
	 * Get Report Route Detail
	 * @param id
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getReportRouteDetail(String id) throws Exception {
		
		RouteCreateInfo info = null;
		try {
			info = instanceAdmin.getAllRouteInfo(InstanceRouteType.ROUTER).stream()
									.filter(f -> f.getRouteId().equals(id))
									.map(m -> m.getRouteCreate().getRouteCreateInfo())
									.findFirst()
									.get();
		} catch (Exception e) {}
		if (info == null) return null;
		
		Map<String, Object> result = new HashMap<>();
		result.putAll(converterService.objectToObject(info, HashMap.class));
		RouteStatus routeStatsModel = camelMonitor.getRouteStatus(info.getRouteId());
		if (routeStatsModel != null) {
			result.put("dumpRouteStats", converterService.objectToObject(routeStatsModel, HashMap.class));
		}
		
		return result;
	}
}
