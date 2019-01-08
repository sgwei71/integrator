package com.ibkglobal.integrator.engine.builder.model.endpoint;

import java.util.List;
import java.util.Map;

import com.ibkglobal.integrator.engine.builder.model.RestHeader;

import lombok.Data;

@Data
public class EndpointInfo {
	
	private EndpointType endpointType;       // 엔드포인트코드
	private Integer      endpointSeq;        // 엔드포인트순번
	
	// Direct
	private String       endpointDirect;     // 엔드포인트DIRECT
	
	// Queue
	private String       endpointQueue;      // 엔드포인트QUEUE
	private Integer      maxConcurrent;      // 큐 접속설정
	
	// Common(TCP, HTTP)
	private String       endpointIp;         // 엔드포인트IP
	private Integer      endpointPort;       // 엔드포인트PORT
	@EndpointField(fieldName="option.maxSession")
	private Integer      maxSession;         // 최대 접속 세션수
	@EndpointField(fieldName="connectTimeout")
	private Integer      conTimeOut;         // 커넥션 타입아웃
	@EndpointField(fieldName="requestTimeout")
	private Integer      reqTimeOut;         // 요청 타임아웃
	@EndpointField(fieldName="sync")
	private boolean      syncCd = true;      // 동기/비동기
	@EndpointField(fieldName="disconnect")
	private boolean      disconnect = false; // 세션유지 
	@EndpointField(fieldName="reconnect")
	private boolean      reconnect = true;   // 재접속 여부
	@EndpointField(fieldName="reconnectInterval")
	private Integer      reconnectInterval;  // 재접속 시간
	@EndpointField(fieldName="option.encoding")
	private String       encoding;           // 인코딩 타입
	
	// HTTP
	private String       pathNm;             // 경로명
	private String       authNm;             // BasicAuth Nm
	private String       authPw;             // BasicAuth Pw
	
	// TCP
	@EndpointField(fieldName="option.lengthOffset")
	private Integer      lengthOffset;       // 시작점
	@EndpointField(fieldName="option.lengthLen")
	private Integer      lengthLen;          // 길이
	
	// Bean
	private Map<String, String> beanpoint;   // 어댑터 Bean
	
	// LOADBALANCE
	private List<EndpointInfo> endpointList;
	
	//REST
	private String httpMehtod;
	private String consumes;
	private String produces;
	private List<RestHeader> headers;
	
	//REST SWAGGER
	private String swaggerBodyDescription;
	private String swaggerTag;
	
}
