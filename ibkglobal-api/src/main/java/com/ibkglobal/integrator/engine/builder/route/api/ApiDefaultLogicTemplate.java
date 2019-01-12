package com.ibkglobal.integrator.engine.builder.route.api;

import org.apache.camel.builder.Builder;

import com.ibkglobal.integrator.config.ConstantCodeAPI;
import com.ibkglobal.integrator.engine.bean.log.LoggerBean;
import com.ibkglobal.integrator.engine.bean.rest.common.ComposingAPI;
import com.ibkglobal.integrator.engine.bean.rest.common.PostProcess;
import com.ibkglobal.integrator.engine.bean.rest.common.PreProcess;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;

public class ApiDefaultLogicTemplate  extends RouteCreateDefault {
	

	public ApiDefaultLogicTemplate(RouteCreateInfo builderInfo) {
		setBuilderInfo(builderInfo);
		onAPIException();
		create();
	}

	@Override
	public void create() {
		
		createEndpoint("from");
		
		this.routeId(getBuilderInfo().getRouteId());
		
		setDefaultHeader("1");
		
		this.bean(LoggerBean.class,"logging");	
		
		this.bean(PreProcess.class, "execute");
//
//		Decoder
//		
//		Transformer
		//라우터 전처리 : 헤더 설정 & Valid Check & Body 스키마 생성
		// 라우터 전 처리 : 헤더 설정 & Valid Check & Body 스키마 생성
		//json 변환 -> 객체화 ParsinMCA-parsing RouteProperties의 instanceRouteType -> AdapterIn(IBKMEssage)
		//IBKMessage 만들어야 하는 시점
		//표준전문헤더를 생성 -> PreProcess(전처리에서는 map으로) body에 linked list
		//개별부 flat 바꾸기 composingMca ,composingFEP

		
		createEndpoint("to");
		//1. 표준전문의 응답코드 보고, 에러처리 정상처리, 자체에러 onAPIException( ErrorCatchMCA
		//1.1 에러 처리 인경우 미리 정된 메싲, erorrCode, erroMessage(메시지공통부)
		//2. 정상처리
		//2.1 표준전문헤더 빼고 composing
		//2.2 flat -> json변경 
		//2.3 setOut(IBKMessage) 
		//MCADEfaultSA

		this.setHeader(ConstantCodeAPI.COMPOSING_HEADER, Builder.constant(getEndpointType(getBuilderInfo().getFromEndpoint())));
		this.bean(ComposingAPI.class,"composing");
		this.bean(ComposingAPI.class, "composingFromHost");
		setDefaultHeader("4");
		this.bean(LoggerBean.class,"logging");	
		this.bean(PostProcess.class, "execute");
	}
}
