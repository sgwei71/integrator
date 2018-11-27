package com.ibkglobal.integrator.engine.netty.util;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.component.netty4.ChannelHandlerFactories;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.engine.netty.codec.IBKDecoder;
import com.ibkglobal.integrator.engine.netty.codec.IBKEncoder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldPrepender;

public class NettyUtil {
	
	public static String getKey(ChannelHandlerContext ctx) {
		String address = getHost(ctx);
		String port    = getPort(ctx);
		
		return address + port;
	}
	
	public static String getHost(ChannelHandlerContext ctx) {
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		
		return socketAddress.getAddress().getHostAddress();
	}
	
	public static String getPort(ChannelHandlerContext ctx) {
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		
		Integer result = socketAddress.getPort();
		
		return result.toString();
	}
	
	/**
	 * IBK 디코더
	 * @param option
	 * @return
	 */
	public static List<ChannelHandler> getDecoder(Map<String, Object> option) {
		List<ChannelHandler> decoders = new ArrayList<>();
		
		Integer lengthOffset = (Integer) option.get(EndpointCode.LENGTH_OFFSET);
		Integer lengthLen    = (Integer) option.get(EndpointCode.LENGTH_LEN);
		String  codecNm      = option.get(EndpointCode.CODEC).toString();
		
		// 길이 지정
		if (lengthOffset != null && lengthLen != null) {
			decoders.add(ChannelHandlerFactories.newLengthFieldBasedFrameDecoder(1048576, lengthOffset, lengthLen, lengthOffset, lengthLen));
		}
		
		// 디코더 지정
		if (StringUtils.isEmpty(codecNm)) {
			decoders.add(new IBKDecoder());
		}
		
		return decoders;
	}
	
	/**
	 * IBK 인코더
	 * @param option
	 * @return
	 */
	public static List<ChannelHandler> getEncoder(Map<String, Object> option) {
		List<ChannelHandler> encoders = new ArrayList<>();
		
		Integer lengthOffset = (Integer) option.get(EndpointCode.LENGTH_OFFSET);
		Integer lengthLen    = (Integer) option.get(EndpointCode.LENGTH_LEN);
		String  codecNm      = option.get(EndpointCode.CODEC).toString();
		
		// 길이 지정
		if (lengthOffset != null && lengthLen != null) {
			encoders.add(new LengthFieldPrepender(lengthLen));
		}
		
		// 인코더 지정
		if (StringUtils.isEmpty(codecNm)) {
			encoders.add(new IBKEncoder());
		}
		
		return encoders;
	}
}
