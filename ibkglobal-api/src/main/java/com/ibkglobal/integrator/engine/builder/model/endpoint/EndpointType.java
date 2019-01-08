package com.ibkglobal.integrator.engine.builder.model.endpoint;

public enum EndpointType {
	DIRECT,      // 직접 입력
	HTTP,        // HTTP
	CAMEL_HTTP,        // HTTP
	CAMEL_HTTP4,        // HTTP
	TCP,         // TCP
	QUEUE,       // QUEUE
	BEAN,        // BEAN
	LOADBALANCE, // 로드밸런스
	REST,		 //REST
	DYNAMIC      // DYNAMIC
	
}
