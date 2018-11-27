package com.ibkglobal.integrator.tc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.integrator.engine.builder.RouteType;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointType;
import com.ibkglobal.integrator.engine.builder.service.RouteCreateService;
import com.ibkglobal.integrator.manager.instance.InstanceRouteType;
import com.ibkglobal.integrator.manager.instance.InstanceType;
import com.ibkglobal.integrator.manager.model.RouteInfo;

@Component
public class RouteCreateTest {
	
	@Autowired
	RouteCreateService routeCreateService;
	
	/**
	 * 어댑터 / 라우터 정보 생성 및 인스턴스에 필요한 정보들 생성
	 * @return
	 */
	public RouteInfo createRouteInfo() {
		RouteCreateInfo routeCreateInfo = new RouteCreateInfo();
		
		routeCreateInfo.setInstanceType(InstanceType.MCA);
		routeCreateInfo.setInstanceRouteType(InstanceRouteType.ADAPTER);
		routeCreateInfo.setSysCd("GCB");
		routeCreateInfo.setBizCd("KR");
		routeCreateInfo.setRouteDescription("테스트용");
		
		routeCreateInfo.setRouteId("TEST.ADAPTER.ID");
		routeCreateInfo.setRouteType(RouteType.MCA_INBOUND);
		routeCreateInfo.setRouteInOut("IN/OUT");
		routeCreateInfo.setAutoStart(true);
		
		// 엔드포인트 생성(From)
		EndpointInfo infoFrom = new EndpointInfo();		
		infoFrom.setEndpointIp("localhost");	// IP
		infoFrom.setEndpointPort(76421);		// PORT
		infoFrom.setMaxSession(10);				// 최대 접속 수
		infoFrom.setCodecNm("codecTest");		// 다양한 코덱
		infoFrom.setSyncCd(true);				// 동기 비동기 설정
		infoFrom.setLengthLen(0);				// 바이트 길이 설정
		infoFrom.setLengthOffset(4);			// 시작점		
		infoFrom.setEndpointType(EndpointType.TCP);
		
		// 엔드포인트 생성(To)
		EndpointInfo infoTo = new EndpointInfo();		
		infoTo.setEndpointType(EndpointType.DIRECT);
		infoTo.setEndpointDirect("TEST.DIRECT");		
		routeCreateInfo.setFromEndpoint(infoFrom);
		routeCreateInfo.setToEndpoint(infoTo);
		
		// 로그 파일 생성 할 명 지정
		routeCreateInfo.setLogName("TESTLOGNAME");
		
		// 인스턴스에서 사용할 정보 생성
		RouteInfo routeInfo = routeCreateService.createRouteInfo(routeCreateInfo);
		
		return routeInfo;
	}
}
