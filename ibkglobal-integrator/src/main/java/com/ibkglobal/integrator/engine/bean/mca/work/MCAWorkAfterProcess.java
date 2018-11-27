package com.ibkglobal.integrator.engine.bean.mca.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.engine.manager.BidManager;
import com.ibkglobal.integrator.engine.model.BidInfo;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.BidUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;

public class MCAWorkAfterProcess {

	@Autowired
	BidManager bidManager;
	
	public void execute(Exchange exchange) throws IBKExceptionMCA {
		Message message = exchange.getIn();
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		StandardTelegram standardTelegram = ibkMessage.getStandardTelegram();
		
		if (standardTelegram != null) {
			// 더미체크
			String dummyCheck = standardTelegram.getSttlSysCopt().getOtptTmgtDcd();
			
			if (dummyCheck != null && dummyCheck.equals("4")) {
				bidWait(exchange, standardTelegram);
			}
		}
	}
	
	public void bidWait(Exchange exchange, StandardTelegram standardTelegram) throws IBKExceptionMCA {				
		// Bid 정보 생성
		BidInfo bidInfo = BidUtil.bidCreate(exchange, standardTelegram);
		
		// Bid 업무시작
		bidManager.bidStart(bidInfo);
	}
}
