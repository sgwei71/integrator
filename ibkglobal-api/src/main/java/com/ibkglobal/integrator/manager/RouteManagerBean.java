package com.ibkglobal.integrator.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.manager.instance.InstanceAdmin;

@Configuration
@EnableConfigurationProperties(RouteProperties.class)
public class RouteManagerBean {
	
	@Autowired
	CamelConfig camelConfig;
	
	@Autowired
	InstanceAdmin instanceAdmin;
	
	private Logger logger = LoggerFactory.getLogger(RouteManagerBean.class);
	
	@Bean
	RouteManager routeManager(RouteProperties routeProperties) throws Exception {
		logger.debug(":::::::::::::::::::::::::::::::::  routeManager Start  ::::::::::::::::::::::::::::::::::::");
		RouteManager routeManager = new RouteManager(camelConfig, routeProperties, instanceAdmin);
		routeManager.init();
		return routeManager;
	}
}
