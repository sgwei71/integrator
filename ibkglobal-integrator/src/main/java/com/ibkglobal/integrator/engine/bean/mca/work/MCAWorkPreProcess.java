package com.ibkglobal.integrator.engine.bean.mca.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.netty4.NettyConfiguration;
import org.apache.camel.component.netty4.NettyEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.converter.ConverterByte;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceMCA;

public class MCAWorkPreProcess {

	@Autowired
	CamelConfig camelConfig;

	@Autowired
	ResourceMCA resourceMCA;

	@Autowired
	ConverterService converterService;

	public void execute(Exchange exchange) throws Exception {
		// 비드 거래 업무일 경우
		if (!bidHttp(exchange)) {
			// 로컬 거래 업무일 경우
			if (!localWork(exchange)) {				
				// 일반업무
				nomalWork(exchange);
			}
		}
	}

	public void nomalWork(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		Interface intf = resourceMCA.getInterface(ibkMessage.getInterfaceId());
		
		// default
		String code = "direct:GCB0.KR00.HC9080";
		
		// 임시로 등록
		try {			
			switch (intf.getInterfaceType().getTarget().getSystem().getCode()) {
			case "GCB" :
				code = "direct:GCB0.KR00.HC9080";
				break;
			case "GED" :
				code = "direct:GED0.KR00.HC9070";
				break;
			default :
				break;
			}
			
		} catch (Exception e) {
			code = "direct:GCB0.KR00.HC9080";
		}

		message.setHeader(EndpointCode.DYNAMIC_ENDPOINT, code);
	}

	/**
	 * Bid Http
	 * 
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	public boolean bidHttp(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);

		// ITRO00000035

		// "6" 일때 비드 응답
		// if
		// ("6".equals(ibkMessage.getStandardTelegram().getSttlSysCopt().getOtptTmgtDcd()))
		// {
		if ("ITRO00000035".equals(ibkMessage.getStandardTelegram().getSttlSysCopt().getSttlIntfId())) {
			// 비드응답수신노드IP
			String bidRspnRcvNdIp = ibkMessage.getStandardTelegram().getSttlSysCopt().getBidRspnRcvNdIp();
			// 비드응답수신포트번호
			Integer bidRspnRcvPortNo = ibkMessage.getStandardTelegram().getSttlSysCopt().getBidRspnRcvPortNo();

			String sendMessage = converterService.objectToJson(ibkMessage.getStandardTelegram());
			sendMessage = ConverterByte.fieldStringFormat("INTEGER", sendMessage.getBytes().length, 8, 0) + sendMessage;

			NettyEndpoint nettyEndpoint = camelConfig.getCamelContext().getEndpoint(
					"netty4:tcp://" + bidRspnRcvNdIp + ":" + bidRspnRcvPortNo + "?textline=true", NettyEndpoint.class);
			NettyConfiguration nettyConfiguration = nettyEndpoint.getConfiguration();
			nettyConfiguration.setDisconnect(true);
			nettyConfiguration.setSync(false);
			nettyConfiguration.setRequestTimeout(10000);

			try {
				camelConfig.getProducerTemplate().sendBody(nettyEndpoint, sendMessage);
			} catch (Exception ex) {
				throw new IBKExceptionMCA(ErrorType.BID, "전송오류");
			}

			IBKMessageUtil.replyMessageBidDefaultSet(ibkMessage.getStandardTelegram(), ConstantCode.MCA_CODE,
					ConstantCode.IBK_RES_FLAG_VALUE, "0");

			exchange.getOut().setFault(true);
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
			exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);

			exchange.getOut().setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()));

			return true;
		}
		return false;
	}

	public boolean localWork(Exchange exchange) throws Exception {
		
		boolean chk = false;

		Message message = exchange.getIn();
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		if ("L".equals(ibkMessage.getStandardTelegram().getSttlSysCopt().getSysEnvrInfoDcd())) {
			
			message.setHeader(EndpointCode.DYNAMIC_ENDPOINT, "direct:LOCAL.LOCAL.ADAPTER.OUT");
			
			String destinationIp = ibkMessage.getStandardTelegram().getSttlSysCopt().getSttlIp();
			
			message.setHeader(EndpointCode.LOCAL_ENDPOINT, EndpointCode.HTTP + destinationIp + ":9080/service/test?httpMethodRestrict=POST&disconnect=true&requestTimeout=60000");
			
			chk = true;
		}
		
		return chk;

//		if ("L".equals(ibkMessage.getStandardTelegram().getSttlSysCopt().getSysEnvrInfoDcd())) {
//			IBKMessageUtil.replyMessageDefaultSet(ibkMessage.getStandardTelegram(), ConstantCode.MCA_CODE);
//
//			String destinationIp = ibkMessage.getStandardTelegram().getSttlSysCopt().getSttlIp();
//			
//			// 값 셋팅
//			message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()));
//
//			// 로컬 용 생성
//			Exchange localExchange = exchange.copy();
//			
//			LogManager.getLogger(LogType.DYNAMIC).info("--- LocalSend Start : " + destinationIp);
//
//			try {
//				camelConfig.getProducerTemplate().send(
//						"netty4-http:http://" + destinationIp + ":9080/service/test?httpMethodRestrict=POST&disconnect=true&requestTimeout=60000",
//						localExchange);
//			} catch (Exception e) {
//				throw new IBKExceptionMCA(ErrorType.ROUTE, "[통신오류] - [오류IP]" + destinationIp);
//			}
//			
//			LogManager.getLogger(LogType.DYNAMIC).info("--- LocalSend End : " + destinationIp);
//
//			message.setBody(localExchange.getOut().getBody());
//
//			message.setFault(true);
//			message.setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
//			
//			LogManager.getLogger(LogType.DYNAMIC).info("--- Log Adapter Out : " + message.getHeader("CamelNettyChannelHandlerContext").toString());
//		}
	}
}
