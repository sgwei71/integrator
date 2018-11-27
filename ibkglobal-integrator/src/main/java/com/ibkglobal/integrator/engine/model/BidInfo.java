package com.ibkglobal.integrator.engine.model;

import org.apache.camel.Exchange;

import lombok.Data;

@Data
public class BidInfo {
	
	private String    name;                   // Bid 업무 이름
	private Exchange  beforeExchange;         // 이전 데이터
	private Exchange  afterExchange;          // 이후 데이터
	private BidStatus status;                 // 상태
	private String    etc;                    // 기타정보
	private long      timeStamp;              // 생성 된 시간
	private long      defaultTimeOut = 10000; // 기본 타임아웃
	
	private Thread    thread;                 // 대기시간이 너무 길어졌을 경우 처리(인터페이스 전문이 아닌 내부 스레드에서 판단)
	
	public static enum BidStatus {
		WAIT,
		COMPLETE,
		TIMEOUT,
		INNERTIMEOUT
	}
}
