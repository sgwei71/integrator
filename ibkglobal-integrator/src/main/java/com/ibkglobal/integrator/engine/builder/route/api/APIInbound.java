package com.ibkglobal.integrator.engine.builder.route.api;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;
import com.ibkglobal.message.IBKMessage;

public class APIInbound extends RouteCreateDefault {
	
	public APIInbound(RouteCreateInfo builderInfo) {
		super.setBuilderInfo(builderInfo);
		onMCAException();
		create();
	}

	@Override
	public void create() {
		createEndpoint("from");
		
		routeId(getBuilderInfo().getRouteId())		
		
		// 라우터 전 처리 : 헤더 설정 & 파싱 & IBKMessage 생성
		.bean(com.ibkglobal.integrator.engine.bean.api.common.ProcessPreAPI.class, "preProcess")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				
				IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
				System.out.println("log전 "+ibkMessage);
				
			}
			
		})
		.bean(com.ibkglobal.integrator.engine.bean.api.log.LoggingAPI.class, "logging")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				
				IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
				System.out.println("log후"+ibkMessage);
			
			}
			
		});
		
		// 전 처리 업무
		//.bean(com.ibkglobal.integrator.engine.bean.mca.work.MCAWorkPreProcess.class, "execute");
		
		// 매핑
		//.bean(com.ibkglobal.integrator.engine.bean.mca.common.MappingMCA.class, "mappingExecute");
		
		createEndpoint("to");
		this.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("createEndPoint to ");
			}
			
		})
		// 라우터 후 처리 : 헤더 설정 & 파싱 & IBKMessage 생성
		.bean(com.ibkglobal.integrator.engine.bean.api.common.ProcessAfterAPI.class, "afterProcess")
		 
		.bean(com.ibkglobal.integrator.engine.bean.api.log.LoggingAPI.class, "logging");
		
		// 후 처리 업무
		//.bean(com.ibkglobal.integrator.engine.bean.mca.work.MCAWorkAfterProcess.class, "execute");
		
		// 매핑
		//.bean(com.ibkglobal.integrator.engine.bean.mca.common.MappingMCA.class, "mappingExecute");
	}
}
