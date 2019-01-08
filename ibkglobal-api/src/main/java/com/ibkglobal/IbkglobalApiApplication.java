package com.ibkglobal;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import com.ibkglobal.common.ApplicationReadyListener;

@SpringBootApplication
@ComponentScan("com.ibkglobal")
public class IbkglobalApiApplication {

	public static void main(String[] args) {
		// SpringApplication.run(IbkglobalApiApplication.class, args)
		// .addApplicationListener(new ApplicationReadyListener());

		new SpringApplicationBuilder(IbkglobalApiApplication.class).listeners(new ApplicationReadyListener()).run(args);

	}
}
