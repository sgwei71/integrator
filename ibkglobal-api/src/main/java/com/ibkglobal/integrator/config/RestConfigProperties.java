package com.ibkglobal.integrator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties(prefix = "rest.configuration")
@Data
@Configuration
public class RestConfigProperties {
	
	private String host;
	private int port;
	private int backlog;
	private String component;
	private String bidingMode;
	private boolean prettyPrint;
	private String apiHost;
	private String contextPath;

	private String apiContextPath;
	private String apiTitle;
	private String apiVersion;
	private boolean enableCors;
	
}
