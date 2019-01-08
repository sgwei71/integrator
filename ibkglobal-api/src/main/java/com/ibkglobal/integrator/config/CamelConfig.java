package com.ibkglobal.integrator.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.netty4.http.HttpServerBootstrapFactory;
import org.apache.camel.component.netty4.http.NettyHttpComponent;
import org.apache.camel.component.netty4.http.NettyHttpConfiguration;
import org.apache.camel.component.netty4.http.NettySharedHttpServer;
import org.apache.camel.component.netty4.http.NettySharedHttpServerBootstrapConfiguration;
import org.apache.camel.component.rest.RestComponent;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.interceptor.DefaultTraceFormatter;
import org.apache.camel.processor.interceptor.TraceFormatter;
import org.apache.camel.processor.interceptor.TraceInterceptor;
import org.apache.camel.processor.interceptor.Tracer;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.spi.RestConfiguration.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ibkglobal.common.util.StringUtils;
import com.ibkglobal.integrator.manager.RouteProperties;
import com.ibkglobal.integrator.manager.instance.InstanceType;

import lombok.Getter;

@Configuration
@SuppressWarnings("deprecation")
public class CamelConfig {
	
	@Getter
	@Autowired
	CamelContext camelContext;
	
	@Getter
	@Autowired
	ProducerTemplate producerTemplate;
	
	@Getter
	@Autowired
	ConsumerTemplate consumerTemplate;
	
	@Autowired
	ActiveMQConnectionFactory factory;
	
	@Autowired
	JmsComponent jmsComponent;
	

	@Autowired
	RestConfigProperties restConfigProperties;
	
	private Logger logger = LoggerFactory.getLogger(CamelConfig.class);
	// Context 설정 변경 시
	public void init(RouteProperties routeProperties) {	
		
		jmsComponent.setConnectionFactory(factory);
				
		initTracer();
//		if(routeProperties.getInstanceType().stream().findFirst().get().equals(InstanceType.API))
			initRestConfiguration();
	}
	
	public void initRestConfiguration() {
		logger.error("CamelConfig initRestConfiguration");
		RestConfiguration restConfiguration = new RestConfiguration();

		if("json".equals(restConfigProperties.getBidingMode())) {
			restConfiguration.setBindingMode(RestBindingMode.auto);
		}else if("xml".equals(restConfigProperties.getBidingMode())){
			restConfiguration.setBindingMode(RestBindingMode.xml);
		}else {
			restConfiguration.setBindingMode(RestBindingMode.auto);
		}
		
		Map<String, Object> dataFormatProperties = new HashMap<String, Object>();
		dataFormatProperties.put("prettyPrint", restConfigProperties.isPrettyPrint());
		restConfiguration.setDataFormatProperties(dataFormatProperties);
		restConfiguration.setHost(restConfigProperties.getHost());
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put("backlog", 100);
		consumerProperties.put("corePoolSize", 50);
		consumerProperties.put("maxPoolSize", 50);
		consumerProperties.put("workerCount", 50);
		consumerProperties.put("maxThreads", 50);
		consumerProperties.put("minThreads", 10);
		restConfiguration.setConsumerProperties(consumerProperties);
		restConfiguration.setComponentProperties(consumerProperties);
		restConfiguration.setPort(restConfigProperties.getPort());
		restConfiguration.setApiHost(restConfigProperties.getApiHost());
		restConfiguration.setContextPath(restConfigProperties.getContextPath());
		restConfiguration.setApiContextPath(restConfigProperties.getApiContextPath());
		
		Map<String, Object> apiProperties = new HashMap<String, Object>();
		apiProperties.put("api.title", restConfigProperties.getApiTitle());
		apiProperties.put("api.version", restConfigProperties.getApiVersion());
		restConfiguration.setApiProperties(apiProperties);

		//사용하는 컴포넌트 추가 필요
//		restConfiguration.setComponent("restlet");
//		restConfiguration.setPort(8082);
//		NettySharedHttpServerBootstrapConfiguration bootstrapConfiguration = new NettySharedHttpServerBootstrapConfiguration();
//		bootstrapConfiguration.setHost("0.0.0.0");
//		bootstrapConfiguration.setPort(8081);
//		bootstrapConfiguration.setBacklog(50);
//		
//		NettyHttpConfiguration configuration = new NettyHttpConfiguration();
//		configuration.setHost("0.0.0.0");
//		configuration.setPort(9081);
//		nettyHttpComponent.setConfiguration(configuration);
//		
//		Map<String, Object> properties = new HashMap<String, Object>();
//		bootstrapConfiguration.setSslContextParameters(sslContextParameters);
//		bootstrapConfiguration.setSsl(false);
//		properties.put("nettyServerBootstrapConfiguration", bootstrapConfiguration);
//		restConfiguration.setComponentProperties(properties);
//		restConfiguration.setend
		camelContext.setRestConfiguration(restConfiguration);
	}


	/**
	 * Tracer 설정
	 */
	public void initTracer() {
		try {
			logger.error("CamelConfig initTracer");
			camelContext.setTracing(true);
			
			DefaultTraceFormatter formatter = new DefaultTraceFormatter();
			formatter.setShowBreadCrumb(false);
			formatter.setShowHeaders(false);
			Tracer tracer = new Tracer();
			tracer.setLogLevel(LoggingLevel.INFO);
			tracer.setLogName(ConstantCode.TRACER_LOGGER);
//			tracer.setFormatter(formatter);
			tracer.setFormatter(new TraceFormatter() {
				@Override
				public Object format(TraceInterceptor interceptor, ProcessorDefinition<?> node, Exchange exchange) {
					Message in = exchange.getIn();
					return formatter.format(interceptor, node, exchange);
				}
			});
//			camelContext.getProperties().put(Exchange.LOG_DEBUG_BODY_STREAMS, "true");
			camelContext.getProperties().put(Exchange.LOG_DEBUG_BODY_MAX_CHARS, "10000");
			camelContext.addInterceptStrategy(tracer);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {

		System.out.println(StringUtils.convert2CamelCase("AEST_SGWEI_ABCD"));
		System.out.println(StringUtils.convert2UDSCase("aestSgweiAbcd"));
	}
}
