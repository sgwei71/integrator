package com.ibkglobal.common;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.ibkglobal.integrator.config.CamelConfig;

import lombok.Getter;

public class ApplicationReadyListener implements  ApplicationListener<ApplicationReadyEvent> {

	@Getter
	@Autowired
	CamelContext camelContext;
	
	@Autowired
	CamelConfig camelConfig;
	
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println(camelContext);
		System.out.println(camelConfig);
	}
}
