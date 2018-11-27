package com.ibkglobal.integrator.engine.netty.factory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.component.netty4.ChannelHandlerFactory;
import org.apache.camel.component.netty4.ClientInitializerFactory;
import org.apache.camel.component.netty4.NettyProducer;
import org.apache.camel.component.netty4.handlers.ClientChannelHandler;

import com.ibkglobal.integrator.engine.netty.codec.IBKDecoder;
import com.ibkglobal.integrator.engine.netty.codec.IBKEncoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class IBKTcpProducerInitializer extends ClientInitializerFactory {
	
	private NettyProducer producer;
	
	public IBKTcpProducerInitializer() {}
	
	public IBKTcpProducerInitializer(NettyProducer producer) {
		this.producer = producer;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
		
		List<ChannelHandler> decoders = producer.getConfiguration().getDecoders();		
		
		// 기본 디코더 생성
		if (decoders == null) {
			addToPipeline("decoder-default", channelPipeline, new IBKDecoder());
		} else {
			for (int x = 0; x < decoders.size(); x++) {
	            ChannelHandler decoder = decoders.get(x);
	            if (decoder instanceof ChannelHandlerFactory) {
	                // use the factory to create a new instance of the channel as it may not be shareable
	                decoder = ((ChannelHandlerFactory) decoder).newChannelHandler();
	            }
	            addToPipeline("decoder-" + x, channelPipeline, decoder);
	        }
		}

        List<ChannelHandler> encoders = producer.getConfiguration().getEncoders();
        
        // 기본 인코더 생성
        if (encoders == null) {
        	addToPipeline("encoder-default", channelPipeline, new IBKEncoder());
        } else {
        	for (int x = 0; x < encoders.size(); x++) {
                ChannelHandler encoder = encoders.get(x);
                if (encoder instanceof ChannelHandlerFactory) {
                    // use the factory to create a new instance of the channel as it may not be shareable
                    encoder = ((ChannelHandlerFactory) encoder).newChannelHandler();
                }
                addToPipeline("encoder-" + x, channelPipeline, encoder);
            }
        }
        
        if (producer.getConfiguration().getRequestTimeout() > 0) {
            ChannelHandler timeout = new ReadTimeoutHandler(producer.getConfiguration().getRequestTimeout(), TimeUnit.MILLISECONDS);
            addToPipeline("timeout", channelPipeline, timeout);
        }
        
        addToPipeline("handler", channelPipeline, new ClientChannelHandler(producer));
	}

	@Override
	public ClientInitializerFactory createPipelineFactory(NettyProducer producer) {
		return new IBKTcpProducerInitializer(producer);
	}
	
	private void addToPipeline(String name, ChannelPipeline pipeline, ChannelHandler handler) {
        pipeline.addLast(name, handler);
    }
}
