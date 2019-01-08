package com.ibkglobal.integrator.engine.netty.factory;

import org.apache.camel.component.netty4.NettyConsumer;
import org.apache.camel.component.netty4.ServerInitializerFactory;
import org.apache.camel.component.netty4.handlers.ServerChannelHandler;
import org.apache.camel.component.netty4.http.NettyHttpConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibkglobal.integrator.engine.netty.channel.SessionChannel;
import com.ibkglobal.integrator.engine.netty.handler.EchoServerHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.util.concurrent.EventExecutorGroup;

public class IBKHttpServerInitializerFactory extends ServerInitializerFactory{
	
	private NettyConsumer consumer;
	
	public IBKHttpServerInitializerFactory() {}
	
	public IBKHttpServerInitializerFactory(NettyConsumer consumer) {
		this.consumer = consumer;
	}
	
	@Override
	public ServerInitializerFactory createPipelineFactory(NettyConsumer consumer) {
		return new IBKHttpConsumerInitializer(consumer);
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
System.out.println("진짜로. 정말.. 답답하네................");		
		// Connection Channel
		addToPipeline("sessionManager", channelPipeline, new SessionChannel(consumer));
	    
	    if (consumer.getConfiguration().isUsingExecutorService()) {
	    	EventExecutorGroup applicationExecutor = consumer.getEndpoint().getComponent().getExecutorService();
	    	addToPipeline("handler", channelPipeline, applicationExecutor, new ServerChannelHandler(consumer));
	    } else {
	    	addToPipeline("handler", channelPipeline, new ServerChannelHandler(consumer));
	    }
	}
	
	private void addToPipeline(String name, ChannelPipeline pipeline, ChannelHandler handler) {
        pipeline.addLast(name, handler);
    }
    
    private void addToPipeline(String name, ChannelPipeline pipeline, EventExecutorGroup executor, ChannelHandler handler) {
        pipeline.addLast(executor, name, handler);
    }
}
