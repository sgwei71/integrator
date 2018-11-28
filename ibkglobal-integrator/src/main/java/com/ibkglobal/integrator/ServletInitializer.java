package com.ibkglobal.integrator;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		//변경 업데이트 테스트
		return application.sources(IbkglobalIntegratorApplication.class);
	}

}
