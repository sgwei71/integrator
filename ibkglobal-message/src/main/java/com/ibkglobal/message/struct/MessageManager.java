package com.ibkglobal.message.struct;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.ibkglobal.message.IBKMessageProperties;
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
	
	@PostConstruct
	void init() throws Exception {
		resourceMCA.init(ibkMessageProperties.getRepositoryType());
		resourceFEP.init(ibkMessageProperties.getRepositoryType());
		resourceEAI.init(ibkMessageProperties.getRepositoryType());
	}
}
