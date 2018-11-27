package com.ibkglobal.integrator.engine.bean.mca.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.ibkglobal.integrator.engine.manager.HealthCheckManager;
import com.ibkglobal.integrator.engine.model.HealthCheckInfo;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.HealthCheckUtil;

import io.netty.channel.ChannelHandlerContext;

public class HealthCheck {
	
	public void execute(Exchange exchange) throws CommonException {
		try {
			Message message = exchange.getIn();
			
			parsing(message);
			
			composing(message);
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.HEALTH_CHECK, e.getMessage(), e);
		}
	}
	
	public void parsing(Message message) throws CommonException {
		if (message.getBody() instanceof byte[]) {
			HealthCheckInfo healthCheckInfo = HealthCheckUtil.createInfo(message);
			
			String ip = healthCheckInfo.getIp();
			
			healthCheckInfo.setContext((ChannelHandlerContext)message.getHeader(HealthCheckUtil.CONTEXTHEADER));
			HealthCheckManager.getClientList().put(ip, healthCheckInfo);
			
			message.setHeader(HealthCheckUtil.CHECKRESULT, true);
		} else {
			message.setHeader(HealthCheckUtil.CHECKRESULT, false);
		}
	}
	
	public void composing(Message message) {
		if (message.getHeader(HealthCheckUtil.CHECKRESULT) == null) {
			message.setBody(HealthCheckUtil.nakMessage());
			return;
		}
		
		boolean result = (boolean) message.getHeader(HealthCheckUtil.CHECKRESULT);
		
		if (result) {
			message.setBody(HealthCheckUtil.ackMessage());
		} else {
			message.setBody(HealthCheckUtil.nakMessage());
		}
	}
}
