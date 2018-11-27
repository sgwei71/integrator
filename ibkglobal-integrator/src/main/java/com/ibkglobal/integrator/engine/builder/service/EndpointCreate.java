package com.ibkglobal.integrator.engine.builder.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointField;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointInfo;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointType;

public class EndpointCreate {
	
	public static String createEndpoint(String type, EndpointInfo endpointInfo) {
		String result = null;
		
		try {			
			EndpointType endpointCd = endpointInfo.getEndpointType();
			
			switch (endpointCd) {
			case DIRECT :
				result = createDirect(endpointInfo);
				break;
			case HTTP :
				result = createHttp(type, endpointInfo);
				break;
			case TCP :
				result = createTcp(type, endpointInfo);
				break;
			case QUEUE :
				result = createQueue(endpointInfo);
				break;
			case DYNAMIC :
				result = createDynamic(endpointInfo);
				default :
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String createDirect(EndpointInfo endpointInfo) throws Exception {
		String base      = EndpointCode.DIRECT + endpointInfo.getEndpointDirect();
		
		return base;
	}
	
	public static String createHttp(String type, EndpointInfo endpointInfo) throws Exception {
		String base      = EndpointCode.HTTP 
				           + endpointInfo.getEndpointIp() 
				           + (endpointInfo.getEndpointPort() != null ? ":" + endpointInfo.getEndpointPort() : "")
				           + endpointInfo.getPathNm();
		
		switch (type) {
		case "consumer" :
			base += "?serverInitializerFactory=#ibkHttpConsumerInitializer";
			break;
		case "producer" :
			base += "?clientInitializerFactory=#ibkHttpProducerInitializer";
			break;
			default :
				break;
		}
				           
		String parameter = createParameter(endpointInfo);
		
		return base + parameter;
	}
	
	public static String createTcp(String type, EndpointInfo endpointInfo) throws Exception {
		String base      = EndpointCode.TCP 
				           + endpointInfo.getEndpointIp() 
				           + (endpointInfo.getEndpointPort() != null ? ":" + endpointInfo.getEndpointPort() : "");
		
		switch (type) {
		case "consumer" :
			base += "?serverInitializerFactory=#ibkTcpConsumerInitializer";
			break;
		case "producer" :
			base += "?clientInitializerFactory=#ibkTcpProducerInitializer";
			break;
			default :
				break;
		}
				           
		String parameter = createParameter(endpointInfo);
		
		return base + parameter;
	}
	
	public static String createQueue(EndpointInfo endpointInfo) throws Exception {
		String base      = EndpointCode.QUEUE + endpointInfo.getEndpointQueue();
		
		String parameter = endpointInfo.getMaxConcurrent() != null ? endpointInfo.getMaxConcurrent().toString() : "";
		
		return base + parameter;
	}
	
	public static String createDynamic(EndpointInfo endpointInfo) throws Exception {
		return EndpointCode.DYNAMIC;
	}
	
	public static List<String> createLoadBalance(String type, EndpointInfo endpointInfo) {
		List<String> result = new ArrayList<>();
		
		endpointInfo.getEndpointList().forEach(endpoint -> {
			result.add(createEndpoint(type, endpoint));
		});
		
		return result;
	}
	
	public static String createParameter(EndpointInfo endpointInfo) throws Exception {
		String parameter = "";
		
		Map<String, Object> parameterMap = getParameter(endpointInfo);
		
		for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
			if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue())) {
				parameter += "&" + entry.getKey() + "=" + entry.getValue();
//				if (!parameter.contains("?")) {
//					parameter += "?" + entry.getKey() + "=" + entry.getValue();
//				} else {
//					parameter += "&" + entry.getKey() + "=" + entry.getValue();
//				}
			}
		}
		
		return parameter;
	}
	
	public static Map<String, Object> getParameter(EndpointInfo endpointInfo) throws Exception {
		LinkedHashMap<String, Object> parameterMap = new LinkedHashMap<>();
		
		Field[] fields = endpointInfo.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			field.setAccessible(true);
			
			if (field.getAnnotation(EndpointField.class) != null) {
				// 일반 필드
				parameterMap.put(field.getAnnotation(EndpointField.class).fieldName(), field.get(endpointInfo));
			}
		}
		
		return parameterMap;
	}
}
