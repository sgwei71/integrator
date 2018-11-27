package com.ibkglobal.integrator.engine.bean.mca.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.Synchronization;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.config.TelegramConstants.StandardTelegramType;
import com.ibkglobal.integrator.engine.manager.BidManager;
import com.ibkglobal.integrator.engine.model.BidInfo;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.BidUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.ConverterByte;
import com.ibkglobal.message.converter.service.ConverterService;

public class MCABidHandle {
	
	@Autowired
	BidManager bidManager;
	
	@Autowired
	CamelConfig camelConfig;
	
	@Autowired
	ConverterService converterService;
	
	public void execute(Exchange exchange) throws IBKExceptionMCA {
		
		try {
			Message message = exchange.getIn();
			
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);
			
			StandardTelegram standardTelegram = ibkMessage.getStandardTelegram();
			
			String bidWorkCheck = standardTelegram.getSttlSysCopt().getOtptTmgtDcd();
			
			// 비드 구분
			switch (StandardTelegramType.fromString(bidWorkCheck)) {
			case BID:
				bidWork(exchange);
				break;
			case RCV_CONFIRM_BID:
				bidWorkWait(exchange, standardTelegram);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.MCA_BID, "비드 업무 처리중 에러");
		}
	}
	
	/**
	 * 일단 비드 처리
	 * @param exchange
	 * @throws IBKExceptionMCA
	 */
	public void bidWork(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		String sendMessage = converterService.objectToJson(ibkMessage.getStandardTelegram());
		sendMessage = ConverterByte.fieldStringFormat("INTEGER", sendMessage.getBytes().length, 8, 0) + sendMessage;
		
		String uri = EndpointCode.TCP + "" + "?textline=true&disconnect=true&sync=false&requestTimeout=60000";
		
		camelConfig.getProducerTemplate().asyncCallbackSendBody(uri, sendMessage, new Synchronization() {
			
			@Override
			public void onFailure(Exchange exchange) {
			}
			
			@Override
			public void onComplete(Exchange exchange) {
			}
		});
	}
	
	/**
	 * Wait 비드 처리
	 * @param exchange
	 * @param standardTelegram
	 * @throws IBKExceptionMCA
	 */
	public void bidWorkWait(Exchange exchange, StandardTelegram standardTelegram) throws IBKExceptionMCA {
		
		String key = BidUtil.getBidKey(standardTelegram);
		
		BidInfo bidInfo = bidManager.getBidInfoList().get(key);	
		
		if (bidInfo != null) {
			//BidUtil.timeOutCheck(bidInfo);
			
			bidManager.bidResult(key, exchange);
		}
	}
}
