package com.ibkglobal.integrator.engine.builder.route;

import java.nio.charset.Charset;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.mca.log.LoggingMCA;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointType;
import com.ibkglobal.integrator.engine.builder.service.EndpointCreate;

import io.netty.handler.timeout.ReadTimeoutException;
import lombok.Getter;
import lombok.Setter;

public abstract class RouteCreateDefault extends RouteDefinition implements RouteCreate {

	@Getter
	@Setter
	private RouteCreateInfo builderInfo;

	@Getter
	private static String excludePatterns[] = { "connection", "content-length", "Content-Type", "LOCAL_ENDPOINT", "Authorization" };

	@Override
	public RouteCreateInfo getRouteCreateInfo() {
		return builderInfo;
	}

	@Override
	public void setRouteCreateInfo(RouteCreateInfo routeCreateInfo) {
		setBuilderInfo(routeCreateInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get() {
		return (T) this;
	}

	/**
	 * On MCA Exception
	 */
	protected void onMCAException() {

		onException(Exception.class).handled(true)
				.bean(com.ibkglobal.integrator.engine.bean.mca.error.ErrorCatchMCA.class, "catchError")
				
				.choice()
					.when(p -> getBuilderInfo().getParsingType() != null)
					.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()))
				.end()
				
				.bean(com.ibkglobal.integrator.engine.bean.mca.common.ComposingMCA.class, "errorComposing")
				.setHeader(ConstantCode.SEQ, Builder.constant("0"))
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						LoggingMCA.loggingError(exchange);
					}
				})
				.removeHeaders("*", excludePatterns)
				// .log(LoggingLevel.ERROR, "${exception.stacktrace}")
				.end();

		onException(ReadTimeoutException.class).handled(true)
				.bean(com.ibkglobal.integrator.engine.bean.mca.error.ErrorCatchMCA.class, "catchReadTimeoutException")
				
				.choice()
					.when(p -> getBuilderInfo().getParsingType() != null)
					.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()))
				.end()
				
				.bean(com.ibkglobal.integrator.engine.bean.mca.common.ComposingMCA.class, "errorComposing")
				.setHeader(ConstantCode.SEQ, Builder.constant("0"))
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						LoggingMCA.loggingError(exchange);
					}
				})
				.removeHeaders("*", excludePatterns)
				// .log(LoggingLevel.ERROR, "${exception.stacktrace}")
				.end();
	}

	/**
	 * On FEP Exception
	 */
	protected void onFEPException() {

		onException(Exception.class).handled(true)
				.bean(com.ibkglobal.integrator.engine.bean.fep.error.ErrorCatchFEP.class, "catchError")
				// .log(LoggingLevel.ERROR, "${exception.stacktrace}")
				.end();
	}

	/**
	 * On EAI Exception
	 */
	protected void onEAIException() {

		onException(Exception.class).handled(true)
				.bean(com.ibkglobal.integrator.engine.bean.eai.error.ErrorCatchEAI.class, "catchError")
			
				.choice()
					.when(p -> getBuilderInfo().getParsingType() != null)
					.setHeader(ConstantCode.PARSING_TYPE, Builder.constant(getBuilderInfo().getParsingType()))
				.end()
				
				.bean(com.ibkglobal.integrator.engine.bean.eai.common.ComposingEAI.class, "errorComposing")
				.setHeader(ConstantCode.SEQ, Builder.constant("0"))
				.bean(com.ibkglobal.integrator.engine.bean.eai.log.LoggingEAI.class, "logging")
				.removeHeaders("*", excludePatterns).end();
	}
	
	protected void onEAIBatchException() {
		
		onException(Exception.class).handled(true)
				.bean(com.ibkglobal.integrator.engine.bean.eai.error.ErrorCatchEAI.class, "catchBatchError")
				.bean(com.ibkglobal.integrator.engine.bean.eai.common.ComposingEAI.class, "errorBatchComposing")
				.removeHeaders("*", excludePatterns)
				.end();
	}

	/**
	 * 헤더 기본 Set
	 * 
	 * @param seq
	 */
	protected void setDefaultHeader(String seq) {

		this.setHeader(ConstantCode.SEQ, Builder.constant(seq));
		this.setHeader(ConstantCode.LOGGER_KEY, Builder.constant(builderInfo.getLogName()));
		this.setHeader(ConstantCode.ORG_CODE, Builder.constant(builderInfo.getOrgCd()));
		this.setHeader(ConstantCode.BIZ_CODE, Builder.constant(builderInfo.getBizCd()));
		this.setHeader(ConstantCode.SYS_CODE, Builder.constant(builderInfo.getSysCd()));
	}

	/**
	 * Endpoint 생성
	 * 
	 * @param endpointType
	 */
	protected void createEndpoint(String endpointType) {
		EndpointInfo endpointInfo = null;

		if (endpointType.equals("from")) {
			endpointInfo = builderInfo.getFromEndpoint();

			switch (endpointInfo.getEndpointType()) {
			case BEAN:
				endpointInfo.getBeanpoint().forEach((key, value) -> {
					try {
						this.bean(Class.forName(key), value);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				});
				break;
			case LOADBALANCE:
				this.from(EndpointCreate.createLoadBalance("consumer", endpointInfo).stream().toArray(String[]::new));
				break;
			default:
				this.from(EndpointCreate.createEndpoint("consumer", endpointInfo));
				break;
			}
		} else if (endpointType.equals("to")) {
			endpointInfo = builderInfo.getToEndpoint();
			
			// Basic Auth 기능 추가
			if (!StringUtils.isEmpty(endpointInfo.getAuthNm()) && !StringUtils.isEmpty(endpointInfo.getAuthPw())) {
				this.setHeader("Authorization", Builder.constant(createAuthKey(endpointInfo.getAuthNm(), endpointInfo.getAuthPw())));
			}

			switch (endpointInfo.getEndpointType()) {
			case BEAN:
				endpointInfo.getBeanpoint().forEach((key, value) -> {
					try {
						this.bean(Class.forName(key), value);
					} catch (ClassNotFoundException e) {
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

	protected EndpointType getEndpointType(EndpointInfo endpointInfo) {

		EndpointType endpointType = null;

		switch (endpointInfo.getEndpointType()) {
		case LOADBALANCE:
			if (endpointInfo.getEndpointList() != null && endpointInfo.getEndpointList().size() > 0) {
				endpointType = endpointInfo.getEndpointList().get(0).getEndpointType();
			}
			break;
		default:
			endpointType = endpointInfo.getEndpointType();
			break;
		}

		return endpointType;
	}
	
	/**
	 * Basic Auth Key 생성
	 * @param id
	 * @param password
	 * @return
	 */
	public String createAuthKey(String id, String password) {
		String autorizationKey = "Basic " + Base64Utils.encodeToString((id + ":" + password).getBytes(Charset.forName("UTF-8")));
		
		return autorizationKey;
	}
}
