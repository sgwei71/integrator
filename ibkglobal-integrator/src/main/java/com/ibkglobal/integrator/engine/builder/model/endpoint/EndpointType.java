package com.ibkglobal.integrator.engine.builder.model.endpoint;

public enum EndpointType {
	DIRECT,      // 직접 입력
	HTTP,        // HTTP
	TCP,         // TCP
	QUEUE,       // QUEUE
	BEAN,        // BEAN
	LOADBALANCE, // 로드밸런스
	DYNAMIC      // DYNAMIC
}
