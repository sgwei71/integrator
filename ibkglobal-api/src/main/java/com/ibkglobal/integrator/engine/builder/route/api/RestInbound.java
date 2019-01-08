package com.ibkglobal.integrator.engine.builder.route.api;

import org.apache.camel.builder.Builder;

import com.ibkglobal.integrator.config.ConstantCodeAPI;
import com.ibkglobal.integrator.engine.bean.log.LoggerBean;
import com.ibkglobal.integrator.engine.bean.rest.common.ComposingAPI;
import com.ibkglobal.integrator.engine.bean.rest.common.PostProcess;
import com.ibkglobal.integrator.engine.bean.rest.common.PreProcess;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointType;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;
import com.ibkglobal.integrator.engine.builder.service.EndpointCreate;

import lombok.Getter;
import lombok.Setter;

public class RestInbound  extends RouteCreateDefault {
	
	@Getter @Setter
	private RouteCreateInfo createInfo;
	
	private static String excludePatterns[] = {"connection","Content-Length","Content-Type", "LOCAL_ENDPOINT", "Authorization" };
	
	@Override
	public RouteCreateInfo getRouteCreateInfo() {
		return createInfo;
	}

	@Override
	public void setRouteCreateInfo(RouteCreateInfo routeCreateInfo) {
		this.createInfo=routeCreateInfo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get() {
		// TODO Auto-generated method stub
		return (T)this;
	}

	public RestInbound(RouteCreateInfo builderInfo) {
		setCreateInfo(builderInfo);
		onAPIException();
		create();
	}

	@Override
	public void create() {
		createEndpoint("from");
		routeId(getCreateInfo().getRouteId());
		
		this.setHeader(ConstantCodeAPI.PARSING_TYPE, Builder.constant(getCreateInfo().getParsingType()));
		
		setDefaultHeader("1");
		
		this.bean(LoggerBean.class,"logging");	
		
		this.bean(PreProcess.class, "execute");

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
		this.bean(PostProcess.class, "execute");
		this.setHeader(ConstantCodeAPI.COMPOSING_HEADER, Builder.constant(getEndpointType(getCreateInfo().getFromEndpoint())));
		this.bean(ComposingAPI.class,"composing");
		this.bean(ComposingAPI.class, "composingFromHost");
		setDefaultHeader("4");
		this.bean(LoggerBean.class,"logging");	
	}

	protected void createEndpoint(String endpointType) {
		EndpointInfo endpointInfo = null;
		
		if(endpointType.equals("from")) {
			endpointInfo = createInfo.getFromEndpoint();
			
			switch(endpointInfo.getEndpointType()) {
			case BEAN:
				endpointInfo.getBeanpoint().forEach((key,value)->{
					try {
						this.bean(Class.forName(key),value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				break;
			case LOADBALANCE:
				this.from(EndpointCreate.createLoadBalance("consumer", endpointInfo).stream().toArray(String[]::new));
				break;
			default:
				this.from(EndpointCreate.createEndpoint("consumer", endpointInfo));
			}
		}else if(endpointType.equals("to")) {
			endpointInfo = createInfo.getToEndpoint();
			
			switch(endpointInfo.getEndpointType()) {
			case BEAN:
				endpointInfo.getBeanpoint().forEach((key,value)->{
					try {
						this.bean(Class.forName(key),value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				break;
			case LOADBALANCE:
				this.to(EndpointCreate.createLoadBalance("producer", endpointInfo).stream().toArray(String[]::new));
				break;
			case DYNAMIC:
				this.toD(EndpointCreate.createEndpoint("producer", endpointInfo));
				break;
			default:
				this.to(EndpointCreate.createEndpoint("producer", endpointInfo));
				break;
			}
		}
	}

	/**
	 * 헤더 기본 Set
	 * 
	 * @param seq
	 */
	protected void setDefaultHeader(String seq) {
		this.setHeader(ConstantCode.SEQ, Builder.constant(seq));
		this.setHeader(ConstantCode.LOGGER_KEY, Builder.constant(createInfo.getLogName()));
		this.setHeader(ConstantCode.ORG_CODE, Builder.constant(createInfo.getOrgCd()));
		this.setHeader(ConstantCode.BIZ_CODE, Builder.constant(createInfo.getBizCd()));
		this.setHeader(ConstantCode.SYS_CODE, Builder.constant(createInfo.getSysCd()));
	}

	protected EndpointType getEndpointType(EndpointInfo endpointInfo) {
		EndpointType endpointType = null;
		switch(endpointInfo.getEndpointType()) {
		case LOADBALANCE:
			if(endpointInfo.getEndpointList() != null && endpointInfo.getEndpointList().size() > 0) {
				endpointType = endpointInfo.getEndpointList().get(0).getEndpointType();
			}
			break;
		default:
			endpointType = endpointInfo.getEndpointType();
			break;		
		}
		return endpointType;
	}
}
