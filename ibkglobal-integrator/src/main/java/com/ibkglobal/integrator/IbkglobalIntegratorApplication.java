package com.ibkglobal.integrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ibkglobal")
public class IbkglobalIntegratorApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(IbkglobalIntegratorApplication.class, args);
	}
}
