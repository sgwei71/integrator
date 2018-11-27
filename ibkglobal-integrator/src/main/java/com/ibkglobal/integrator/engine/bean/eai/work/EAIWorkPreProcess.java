package com.ibkglobal.integrator.engine.bean.eai.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.struct.resource.ResourceEAI;

public class EAIWorkPreProcess {

	@Autowired
	ResourceEAI resourceEAI;

	public void execute(Exchange exchange) throws Exception {
		// 다이나믹 경로 변경
		dynamicCheck(exchange);
	}

	public void dynamicCheck(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);

		Interface intf = resourceEAI.getInterface(ibkMessage.getInterfaceId());

		// default
		String code = "direct:E.EAI0.0000.ADAPTER.OUT";
		
		try {
			switch (intf.getInterfaceType().getTarget().getSystem().getCode()) {
			case "GBP" :
				code = "direct:E.GBP0.BPM0.ADAPTER.OUT";
				break;
			default :
				code = "direct:E.EAI0.0000.ADAPTER.OUT";
				break;
			}
		} catch (Exception e) {
			code = "direct:E.EAI0.0000.ADAPTER.OUT";
		}

		message.setHeader(EndpointCode.DYNAMIC_ENDPOINT, code);
	}
}
