package com.ibkglobal.integrator.engine.netty.handler;

import java.nio.charset.Charset;

import org.apache.camel.component.netty4.NettyConsumer;
import org.apache.camel.component.netty4.NettyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoServerHandler extends SimpleChannelInboundHandler<Object>{

	private static final Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);
	
    private final NettyConsumer consumer;
    public EchoServerHandler() {
		// TODO Auto-generated constructor stub
    	consumer = null;
	}
    public EchoServerHandler(NettyConsumer consumer) {
    	this.consumer=consumer;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // to keep track of open sockets
        consumer.getNettyServerBootstrapFactory().addChannel(ctx.channel());
        
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // to keep track of open sockets
        consumer.getNettyServerBootstrapFactory().removeChannel(ctx.channel());
        
        super.channelInactive(ctx);
    }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("수신한 문자열 {}",((ByteBuf)msg).toString(Charset.defaultCharset()));
		ctx.write(msg);
		super.channelRead(ctx, msg);
	}

	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (consumer.isRunAllowed()) {
            // let the exception handler deal with it
            consumer.getExceptionHandler().handleException("Closing channel as an exception was thrown from Netty", cause);
            // close channel in case an exception was thrown
            NettyHelper.close(ctx.channel());
        }
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("수신한 문자열 {}",((ByteBuf)msg).toString(Charset.defaultCharset()));
		ctx.write(msg);
		super.channelRead(ctx, msg);
		
	}
}
