package com.ibkglobal.integrator.engine.bean.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.spi.ExecutorServiceManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

public class ExecutorWorker {
	
	private ExecutorService executorService;
	
	private void send() {
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		//executorService.submit(task)
	}
}
