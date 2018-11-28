package com.ibkglobal.integrator.engine.builder.route.api;

import static org.apache.camel.model.rest.RestParamType.body;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.RouteCreateInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointType;
import com.ibkglobal.integrator.engine.builder.route.RouteCreateDefault;
import com.ibkglobal.message.IBKMessage;


public class APIDefaultAdapterIn extends RouteCreateDefault {
	CamelContext camelContext= null;
	RouteCreateInfo builderInfo = null;
	public APIDefaultAdapterIn(RouteCreateInfo builderInfo,CamelContext camelContext) {
		super.setBuilderInfo(builderInfo);
		onMCAException();
		//this.camelContext = camelContext;
		if(EndpointType.REST==builderInfo.getFromEndpoint().getEndpointType()) {
			//라우터 별도로 하나만들자 
			this.camelContext = camelContext;
			this.builderInfo = builderInfo;
			create();
		//	createREST();
		}else {
		create();
		}
	}
	public void createREST() {
		System.out.println("createREST");
		  try {

			  camelContext.addRoutes(new RouteBuilder() {
				  //@Autowired 
				 // RouteCreateInfo builderInfo;
				    @Override
				    public void configure() throws Exception {
				    	System.out.println("====");
				   // 	buillderInfo.endpointInfo.getEndpointIp()
				   	int port =  builderInfo.getFromEndpoint().getEndpointPort();
				   	String path = builderInfo.getFromEndpoint().getPathNm();
				   	String endpointTo = builderInfo.getFromEndpoint().getEndpointDirect();
				    	String root = "/api";
				   	System.out.println("Port"+builderInfo.getFromEndpoint().getEndpointPort());
				   	 
				   	//.setBindingMode(RestConfiguration.RestBindingMode.off);
				    	restConfiguration()
						.component("jetty")
						.bindingMode(RestBindingMode.json.off)
						
						.dataFormatProperty("prettyPrint", "true")
						.port(port)
						.contextPath(root)
						.enableCORS(true)
						.apiContextPath("/api-doc")
						.apiProperty("api.title", "IBK기업은행 API")
						.apiProperty("api.version", "1.0.0");

						rest(path).description("콜롬버스")
						.tag("[IBK Bank API] bank")
						.consumes("application/json")
						.produces("application/json")
						.post().description("IBK BOX 콜롬버스")
						.param().name("body").type(body).endParam()
						.to("log:process?level=DEBUG&showAll=true&multiline=true")			
						
						.to("direct:"+endpointTo);
						
						
					    }
				});
			  
			  
			  
			  
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  
		  
		  
	}
	@Override
	public void create() {
	
		if(EndpointType.REST==super.getBuilderInfo().getFromEndpoint().getEndpointType()) {
			createREST();
		}
		createEndpoint("from");
		
		// routeId
		if (!StringUtils.isEmpty(getBuilderInfo().getRouteId())) {
			this.routeId(getBuilderInfo().getRouteId());
		}
	
		
		setDefaultHeader("1");
		this.setHeader(ConstantCode.COMPOSING_HEADER, Builder.constant(getEndpointType(getBuilderInfo().getFromEndpoint())));
		//this.bean(com.ibkglobal.integrator.engine.bean.api.log.LoggingAPI.class, "logging");

		// to
		createEndpoint("to");

		this.setHeader(ConstantCode.COMPOSING_HEADER, Builder.constant(getEndpointType(getBuilderInfo().getFromEndpoint())));
		this.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
				System.out.println("composing전1"+ibkMessage);
			
			}
			
		});
		this.bean(com.ibkglobal.integrator.engine.bean.api.common.ComposingAPI.class, "composing");
		this.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				byte[] ibkMessage = exchange.getIn().getBody(byte[].class);
				System.out.println("composing후1"+new String(ibkMessage));
			
			}
			
		});
		
		setDefaultHeader("6");
//		this.process(new Processor() {
//
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				// TODO Auto-generated method stub
//				byte[] ibkMessage = exchange.getIn().getBody(byte[].class);
//				System.out.println("logAdapterInAfter 6"+new String(ibkMessage));
//				
//			}
//			
//		});
		this.bean(com.ibkglobal.integrator.engine.bean.api.log.LoggingAPI.class, "logging");
		
//		this.process(new Processor() {
//
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				// TODO Auto-generated method stub
//				byte[] ibkMessage = exchange.getIn().getBody(byte[].class);
//				System.out.println("logAdapterIn="+new String(ibkMessage));
//			
//			}
//			
//		});
		this.bean(com.ibkglobal.integrator.engine.bean.api.common.ComposingAPI.class, "composingFromHost");
		
		
		removeHeaders("*", getExcludePatterns());
	}

	
}
