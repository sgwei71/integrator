package com.ibkglobal.integrator.engine.builder.route.timer;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;
import com.ibkglobal.integrator.engine.manager.HealthCheckManager;
import com.ibkglobal.integrator.engine.model.HealthCheckInfo;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.HealthCheckUtil;
import com.ibkglobal.message.converter.service.ConverterService;

public class MCAHealthCheckTimer extends RouteCreateDefault {
	
	@Autowired
	CamelConfig camelConfig;
	
	@Autowired
	ConverterService converterService;
	
	long checkTime = 60000; // 상태정보 1분주기로 검사

	@Override
	public void create() {
		from("timer:healthCheck?period=" + checkTime)
		.routeId("MCAHealthCheckTimer")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws IBKExceptionMCA {
				long currentTime = HealthCheckUtil.currentTime();
				
				LinkedHashMap<String, HealthCheckInfo> disconnectList = new LinkedHashMap<>();
				
				// HealthCheck
				HealthCheckManager.getClientList().forEach((k, v) -> {
					// 5분 주기
					if (currentTime - v.getHealthTime() > (60 * 5)) {
						v.getContext().close();					
						
						disconnectList.put(k, v);
					}
				});
				
				// Logout And DisConnect
				disconnectList.forEach((k, v) -> {
//					Exchange result = exchange.copy();
					
//					StandardTelegram standardTelegram = HealthCheckUtil.logoutMessage();
//					
//					standardTelegram.getUserData().getData().put("empNo", v.getUserInfo());
//					standardTelegram.getUserData().getData().put("secrtNo", "1234");
//					
//					try {
//						result.getIn().setBody(converterService.objectToJson(standardTelegram));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//					camelConfig.getProducerTemplate().asyncSend("netty4-http:http://172.18.116.36:9080/service/test?httpMethodRestrict=POST", result);
					
					HealthCheckManager.getClientList().remove(k);
				});
			}
		})
		.end();
	}
}
