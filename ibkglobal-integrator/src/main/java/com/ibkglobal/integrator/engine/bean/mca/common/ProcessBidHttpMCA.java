package com.ibkglobal.integrator.engine.bean.mca.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.netty4.NettyConfiguration;
import org.apache.camel.component.netty4.NettyEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.converter.ConverterByte;
import com.ibkglobal.message.converter.service.ConverterService;

public class ProcessBidHttpMCA  {
	
	@Autowired
	CamelConfig camelConfig;
	
	@Autowired
	ConverterService converterService;
	
	public void process(Exchange exchange) throws CommonException {
		
		try {
			Message message = exchange.getIn();
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);
			
			// 비드응답수신노드IP 
			String bidRspnRcvNdIp = ibkMessage.getStandardTelegram().getSttlSysCopt().getBidRspnRcvNdIp();
			// 비드응답수신포트번호
			int bidRspnRcvPortNo = ibkMessage.getStandardTelegram().getSttlSysCopt().getBidRspnRcvPortNo();
			
			String sendMessage = converterService.objectToJson(ibkMessage.getStandardTelegram());
			sendMessage = ConverterByte.fieldStringFormat("INTEGER", sendMessage.getBytes().length, 8, 0) + sendMessage;
										
			NettyEndpoint nettyEndpoint = camelConfig.getCamelContext().getEndpoint("netty4:tcp://" + bidRspnRcvNdIp + ":" + bidRspnRcvPortNo + "?textline=true", NettyEndpoint.class);
			NettyConfiguration nettyConfiguration = nettyEndpoint.getConfiguration();
			nettyConfiguration.setDisconnect(true);
			nettyConfiguration.setSync(false);
			nettyConfiguration.setRequestTimeout(10000);
						
			camelConfig.getProducerTemplate().sendBody(nettyEndpoint, sendMessage);
//			camelConfig.getProducerTemplate().sendBody("netty4:tcp://" + bidRspnRcvNdIp + ":" + bidRspnRcvPortNo + "?disconnect=true&sync=false&textline=true&requestTimeout=10000"
//			, sendMessage);
									
			exchange.getMessage().setBody(ibkMessage.getBaseData());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IBKExceptionEAI(ErrorType.BID, e.getMessage(), e);
		}
	}
}
