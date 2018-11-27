package com.ibkglobal.integrator.tc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.integrator.manager.model.RouteInfo;
import com.ibkglobal.integrator.manager.service.RouteService;

@Component
public class RouteTest {
	
	@Autowired
	RouteService routeService;
	
	/**
	 * 카멜 전체 기동
	 */
	public void routeAllStart() {
		routeService.allStart();
	}
	
	/**
	 * 카멜 전체 기동종료
	 */
	public void routeAllStop() {
		routeService.allStop();
	}
	
	/**
	 * 개별 라우트 기동
	 * @param routeId
	 */
	public void routeStart(String routeId) {
		routeService.start(routeId);
	}
	
	/**
	 * 개별 라우트 기동종료
	 * @param routeId
	 */
	public void routeStop(String routeId) {
		routeService.stop(routeId);
	}
	
	/**
	 * 라우트 추가
	 * @param routeInfo
	 */
	public void routeAdd(RouteInfo routeInfo) {
		routeService.addBuilder(routeInfo);
	}
	
	/**
	 * 라우트 제거
	 * @param routeId
	 */
	public void routeRemove(String routeId) {
		routeService.remove(routeId);
	}
}
