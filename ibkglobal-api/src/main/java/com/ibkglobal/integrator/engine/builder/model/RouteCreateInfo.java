package com.ibkglobal.integrator.engine.builder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibkglobal.integrator.engine.builder.RouteType;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.manager.instance.InstanceRouteType;
import com.ibkglobal.integrator.manager.instance.InstanceType;

import lombok.Data;

@Data
@JsonInclude(Include.ALWAYS)
public class RouteCreateInfo {

	private String routeId; // 라우트 아이디
	private InstanceType instanceType; // 라우트 업무타입
	private InstanceRouteType instanceRouteType; // 라우트 타입(ADAPTER, ROUTER, ...)
	private RouteType routeType; // 라우트 타입(업무별 default 라우트)
	private ParsingType parsingType; // Protocol 타입(JSON, FLAT, ISO, ...)
	private String routeDescription; // 라우트 세부내용
	private String routeInOut; // 라우트 In / Out 구분
	private boolean autoStart; // 기동 시 자동 실행 유무

	private EndpointInfo fromEndpoint; // FROM
	private EndpointInfo toEndpoint; // TO

	private String className; // 클래스명(클래스명으로 라우트 생성할 때)

	private String sysCd; // 시스템
	private String bizCd; // 업무
	private String orgCd; // 기관

	private String logLevel; // 로그레벨
	private String logName; // 이력파일명
}
