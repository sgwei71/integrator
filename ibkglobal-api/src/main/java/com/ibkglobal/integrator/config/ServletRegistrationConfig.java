package com.ibkglobal.integrator.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibkglobal.integrator.mms.MmsServlet;

@Configuration
public class ServletRegistrationConfig {
	
	@Bean
	public ServletRegistrationBean getServletRegistrationBean() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MmsServlet());
		registrationBean.addUrlMappings("/mms");
		return registrationBean;
	}
}
