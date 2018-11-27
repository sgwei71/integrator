package com.ibkglobal.integrator.engine.bean.fep.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;

public class ComLocalBean extends ComBean {
	
	public void sendToLocal(Exchange exchange) throws Exception {
		
		try {
			Message message = exchange.getIn();
			
			//String destinationIp = getLocalIp(message);
			String destinationIp = "10.104.162.61";
			
			message.setHeader(EndpointCode.LOCAL_ENDPOINT, EndpointCode.HTTP + destinationIp + ":9080/service/test?httpMethodRestrict=POST&disconnect=true&requestTimeout=60000");
			
			camelConfig.getProducerTemplate().send(getLocalDirect(), exchange);
		} catch (Exception e) {
			throw new IBKExceptionFEP(ErrorType.FEP, "전송 시 오류");
		}
	}
	
	public String getLocalDirect() throws IBKExceptionFEP {
		String dName = EndpointCode.DIRECT + "F.LOCAL.LOCAL.ADAPTER.OUT";
		return dName;
	}
	
	public String getLocalIp(Message message) throws IBKExceptionFEP {
	
		String ip = "";
		
		try {
			ip = message.getHeader("CamelNettyRemoteAddress", String.class);
			
			ip = ip.replace("/", "");
			ip = ip.split(":")[0];
		} catch (Exception e) {
			throw new IBKExceptionFEP(ErrorType.FEP, "IP 생성 오류");
		}
		
		return ip;
	}
}
