package com.ibkglobal.integrator.engine.netty.factory;

import java.util.List;
import java.util.Map;

import org.apache.camel.component.netty4.ChannelHandlerFactory;
import org.apache.camel.component.netty4.NettyConsumer;
import org.apache.camel.component.netty4.ServerInitializerFactory;
import org.apache.camel.component.netty4.handlers.ServerChannelHandler;

import com.ibkglobal.integrator.engine.netty.channel.SessionChannel;
import com.ibkglobal.integrator.engine.netty.util.NettyUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.util.concurrent.EventExecutorGroup;

public class IBKHttpConsumerInitializer extends ServerInitializerFactory {
	
	private NettyConsumer consumer;
	
	public IBKHttpConsumerInitializer() {}
	
	public IBKHttpConsumerInitializer(NettyConsumer consumer) {
		this.consumer = consumer;
	}
	
	@Override
	public ServerInitializerFactory createPipelineFactory(NettyConsumer consumer) {
		return new IBKHttpConsumerInitializer(consumer);
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
		
		// Connection Channel
		addToPipeline("sessionManager", channelPipeline, new SessionChannel(consumer));
		
		Map<String, Object> option = consumer.getConfiguration().getOptions();
		
		if (option != null) {			
			// Encoder
			List<ChannelHandler> encoders = NettyUtil.getEncoder(option);
		    for (int x = 0; x < encoders.size(); x++) {
		    	ChannelHandler encoder = encoders.get(x);
		    	
		    	if (encoder instanceof ChannelHandlerFactory) {
		    		encoder = ((ChannelHandlerFactory) encoder).newChannelHandler();
		    	}
		    	
		    	addToPipeline("encoder-" + x, channelPipeline, encoder);
		    }
		    
		    // Decoder
		    List<ChannelHandler> decoders = NettyUtil.getDecoder(option);
		    for (int x = 0; x < decoders.size(); x++) {
		    	ChannelHandler decoder = decoders.get(x);
		    	
		    	if (decoder instanceof ChannelHandlerFactory) {
		    		decoder = ((ChannelHandlerFactory) decoder).newChannelHandler();
		    	}
		    	
		    	addToPipeline("decoder-" + x, channelPipeline, decoder);
		    }
		}
	    
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
