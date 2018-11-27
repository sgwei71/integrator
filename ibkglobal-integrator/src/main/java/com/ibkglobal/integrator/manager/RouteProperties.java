package com.ibkglobal.integrator.manager;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ibkglobal.integrator.manager.instance.InstanceType;

import lombok.Data;

@ConfigurationProperties(prefix = "integrator.config")
@Data
public class RouteProperties {
	
	private String  routeBaseType;
	
	private String  jsonFileUrl;
	
	private List<InstanceType>  instanceType;
	
	private Map<InstanceType, List<String>> customLoader;
}
