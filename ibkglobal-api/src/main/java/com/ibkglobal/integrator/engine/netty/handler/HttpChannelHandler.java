package com.ibkglobal.integrator.engine.netty.handler;

import org.apache.camel.component.netty4.handlers.ServerChannelHandler;
import org.apache.camel.component.netty4.http.NettyHttpConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;


public class HttpChannelHandler extends ServerChannelHandler{

	private static final Logger logger = LoggerFactory.getLogger(HttpChannelHandler.class);
	private final NettyHttpConsumer consumer;
	
	public HttpChannelHandler(NettyHttpConsumer consumer) {
		super(consumer);
		this.consumer=consumer;
	}
	
	public NettyHttpConsumer getConsumer() {
		return consumer;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		HttpRequest request = (HttpRequest) msg;
		
		logger.debug("[IBK INTEGRATOR] Message Received : {}",request);
		
		super.channelRead(ctx, msg);
	}
	

	
	
}
