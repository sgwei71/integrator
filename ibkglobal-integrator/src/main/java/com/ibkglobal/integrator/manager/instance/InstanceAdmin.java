package com.ibkglobal.integrator.manager.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.common.file.FileType;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateLoader;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.service.RouteCreateService;
import com.ibkglobal.integrator.manager.RouteProperties;
import com.ibkglobal.integrator.manager.model.RouteInfo;

import lombok.Getter;

@Component
public class InstanceAdmin {
	
	@Autowired
	RouteCreateService routeCreateService;
	
	@Autowired
	FileReader fileReader;
	
	@Autowired
	Converter converter;
	
	RouteProperties routeProperties;
	
	@Getter
	private Map<InstanceType, InstanceRoute> instanceRoutes;
	
	public void init(RouteProperties routeProperties) throws Exception {
		this.routeProperties = routeProperties;
		
		instanceRoutes = new HashMap<>();
		
		if (routeProperties.getInstanceType() == null) 
			return;
		
		// Instance 정보 초기화(MCA, FEP, EAI, ETC)
		routeProperties.getInstanceType().forEach(value -> {
			InstanceRoute instanceRoute = new InstanceRoute();
			instanceRoute.init();
			
			instanceRoutes.put(value, instanceRoute);
		});
		
		// 어댑터 라우터 정보 초기화
		RouteBaseInit();
		
		// 사용자가 등록한 라우터 정보 초기화
		RouteCustomInit();
	}
	
	/**
	 * 등록 된 어댑터/라우터 정보 초기화, 파일방식
	 * @throws Exception 
	 */
	public void RouteBaseInit() throws Exception {
		fileReader.readFileToMap(FileType.ROUTE, RouteCreateInfo.class).values().forEach(data -> {
			RouteInfo routeInfo = routeCreateService.createRouteInfo((RouteCreateInfo) data);
			addRouteInfo(routeInfo);
		});
	}
	
	/**
	 * 사용자가 정의한 라우터 정보 초기화
	 */
	public void RouteCustomInit() {	
		
		if (routeProperties.getCustomLoader() == null) 
			return;
		
		routeProperties.getCustomLoader().forEach((key, value) -> {
			if (routeProperties.getInstanceType().contains(key)) {
				value.forEach(path -> {
					RouteCreateLoader routeCreateInfoList = null;
					
					try {
						routeCreateInfoList = fileReader.readFileToObject(path, RouteCreateLoader.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (routeCreateInfoList != null && routeCreateInfoList.getRouteCreateInfoList() != null) {
						routeCreateInfoList.getRouteCreateInfoList().forEach(data -> {
							RouteInfo routeInfo = routeCreateService.createRouteInfo(data);
							addRouteInfo(routeInfo);
						});
					}
				});
			}
		});
	}
	
	/**
	 * 라우터 아이디로 정보 Get
	 * @param routeId
	 * @return
	 */
	public synchronized RouteInfo getRouteInfo(String routeId) {
		return getAllRouteInfo().stream()
				                .findFirst()
				                .get();
	}
	
	/**
	 * 라우터 정보 추가
	 * @param routeInfo
	 */
	public synchronized void addRouteInfo(RouteInfo routeInfo) {
		Map<String, RouteInfo> routeInfoList = getRouteInfoList(routeInfo);
		
		if (routeInfoList != null) {
			routeInfoList.put(routeInfo.getRouteId(), routeInfo);
		}
	}
 	
	/**
	 * 라우터 정보 삭제
	 * @param routeInfo
	 */
	public synchronized void deleteRouteInfo(RouteInfo routeInfo) {
		Map<String, RouteInfo> routeInfoList = getRouteInfoList(routeInfo);
		
		if (routeInfoList != null) {
			routeInfoList.remove(routeInfo.getRouteId());
		}
	}
	
	/**
	 * 인스턴스에 해당하고 타입에 해당하는 맵정보 Get
	 * @param routeInfo
	 * @return
	 */
	public synchronized Map<String, RouteInfo> getRouteInfoList(RouteInfo routeInfo) {
		InstanceRoute instanceRoute = instanceRoutes.get(routeInfo.getInstanceType());
		
		if (instanceRoute == null) {
			return null;
		}
		
		Map<String, RouteInfo> routeInfoList = null;
		
		if (routeInfo.getInstanceRouteType() != null) {				
			switch (routeInfo.getInstanceRouteType()) {
			case ADAPTER :
				routeInfoList = instanceRoute.getAdapterList();
				break;
			case ROUTER :
				routeInfoList = instanceRoute.getRouteList();
				break;
				default :
					routeInfoList = instanceRoute.getEtcList();
					break;
			}
		} else {
			routeInfoList = instanceRoute.getEtcList();
		}
		
		return routeInfoList;
	}
	
	/**
	 * 전체 라우터 정보 Get
	 * @return
	 */
	public synchronized List<RouteInfo> getAllRouteInfo() {
		List<RouteInfo> routeInfoAll = new ArrayList<>();
		
		instanceRoutes.values().forEach(value -> {
			routeInfoAll.addAll(value.getAdapterList().values());
			routeInfoAll.addAll(value.getRouteList().values());
			routeInfoAll.addAll(value.getEtcList().values());
		});
		
		return routeInfoAll;
	}
	
	/**
	 * 전체 라우터 정보 Get(Type)
	 * @return
	 */
	public synchronized List<RouteInfo> getAllRouteInfo(InstanceRouteType instanceRouteType) {
		List<RouteInfo> routeInfoAll = new ArrayList<>();
		
		instanceRoutes.values().forEach(value -> {
			switch (instanceRouteType) {
			case ADAPTER :
				routeInfoAll.addAll(value.getAdapterList().values());
				break;
			case ROUTER :
				routeInfoAll.addAll(value.getRouteList().values());
				break;
				default :
					routeInfoAll.addAll(value.getEtcList().values());
					break;
			}
		});
		
		return routeInfoAll;
	}
}
