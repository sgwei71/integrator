package com.ibkglobal.integrator.engine.netty;

import org.apache.camel.component.netty4.NettyServerBootstrapConfiguration;
import org.apache.camel.component.netty4.NettyServerBootstrapFactory;
import org.apache.camel.component.netty4.http.NettyHttpBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibkglobal.integrator.engine.netty.factory.IBKHttpServerInitializerFactory;
import com.ibkglobal.integrator.engine.netty.http.IBKNettyHttpBinding;
import com.ibkglobal.integrator.engine.netty.factory.HttpProducerInitializer;
import com.ibkglobal.integrator.engine.netty.factory.IBKHttpConsumerInitializer;

@Configuration
public class NettyHandlerBean extends NettyBean {
	
	private static final Logger logger = LoggerFactory.getLogger(NettyHandlerBean.class);
	
	@Bean
	public NettyServerBootstrapConfiguration nettyServerBootstrapConfiguration() {
		NettyServerBootstrapConfiguration nettyServerBootstrapConfiguration = new NettyServerBootstrapConfiguration();
		nettyServerBootstrapConfiguration.setPort(18080);
		return nettyServerBootstrapConfiguration;
	}
	@Bean
	public IBKNettyHttpBinding ibkNettyHttpBinding() {
		logger.debug("[IBK INTEGRATOR]__________ NettyHandlerBean ibkNettyHttpBinding");
		IBKNettyHttpBinding ibkNettyHttpBinding = new IBKNettyHttpBinding();
		return ibkNettyHttpBinding;
	}
	@Bean
	public IBKHttpServerInitializerFactory ibkHttpServerInitializerFactory() {
		logger.debug("[IBK INTEGRATOR]__________ NettyHandlerBean httpConsumerInitializer");
		IBKHttpServerInitializerFactory ibkHttpServerInitializerFactory = new IBKHttpServerInitializerFactory();
		return ibkHttpServerInitializerFactory;
	}

	@Bean
	public HttpProducerInitializer httpProducerInitializer() {
		logger.debug("[IBK INTEGRATOR]__________ NettyHandlerBean httpProducerInitializer");
		return null;
	}
}
