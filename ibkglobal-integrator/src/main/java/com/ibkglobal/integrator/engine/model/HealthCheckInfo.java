package com.ibkglobal.integrator.engine.model;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthCheckInfo extends HealthCheckDefault {
	private String                userInfo;   // 접속자 정보(6)
	private String                ip;         // 접속자 IP(16)
	private HealthStatus          status;     // 접속상태
	private long                  healthTime; // CheckTime
	private ChannelHandlerContext context;    // Context
	
	public static enum HealthStatus {
		LOGIN,
		NORMAL,
		LOGOUT
	}
}
