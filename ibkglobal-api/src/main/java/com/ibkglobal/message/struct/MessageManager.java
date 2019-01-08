package com.ibkglobal.message.struct;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.ibkglobal.integrator.manager.RouteProperties;
import com.ibkglobal.integrator.manager.instance.InstanceType;
import com.ibkglobal.message.IBKMessageProperties;
import com.ibkglobal.message.struct.resource.ResourceAPI;
import com.ibkglobal.message.struct.resource.ResourceEAI;
import com.ibkglobal.message.struct.resource.ResourceFEP;
import com.ibkglobal.message.struct.resource.ResourceMCA;

@Configuration
public class MessageManager {
	
	@Value("${integrator.config.json-file-url}")
	String jsonFileUrl;
			
	@Autowired
	IBKMessageProperties ibkMessageProperties;
	
	@Autowired
	ResourceMCA resourceMCA;
	
	@Autowired
	ResourceFEP resourceFEP;
	
	@Autowired
	ResourceEAI resourceEAI;
	
	@Autowired
	ResourceAPI resourceAPI;
	
	@Autowired
	RouteProperties routeProperties;
	
	@PostConstruct
	void init() throws Exception {
		List<InstanceType> instanceTypes = routeProperties.getInstanceType();
		for (InstanceType instanceType : instanceTypes) {
			String type = instanceType.getCode();
			switch (type) {
			case "E":
				resourceEAI.init(ibkMessageProperties.getRepositoryType());
				break;
			case "F":
				resourceFEP.init(ibkMessageProperties.getRepositoryType());
				break;
			case "M":
				resourceMCA.init(ibkMessageProperties.getRepositoryType());			
				break;
			case "A":
				resourceAPI.init(ibkMessageProperties.getRepositoryType());		
				break;
			default:
				break;
			}
		}


	}
	
	public static void main(String args[]) {
	
	}
}
