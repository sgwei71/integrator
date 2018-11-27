package com.ibkglobal.integrator.engine.netty.channel;

import org.apache.camel.component.netty4.NettyConsumer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class SessionChannel implements ChannelHandler {
	
	NettyConsumer consumer;
	
	public SessionChannel(NettyConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
	}

}
