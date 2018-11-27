package com.ibkglobal.integrator.engine.manager;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public class SessionManager {
	
	private static Map<String, ChannelHandlerContext> sessionList = new HashMap<>();
	
	public static Map<String, ChannelHandlerContext> getSessionList() {
		synchronized (sessionList) {
			return sessionList;
		}
	}
}
