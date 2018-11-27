package com.ibkglobal.integrator.util;

import java.util.Date;

import org.apache.camel.Exchange;

import com.ibkglobal.integrator.engine.model.BidInfo;
import com.ibkglobal.integrator.engine.model.BidInfo.BidStatus;
import com.ibkglobal.message.common.normal.StandardTelegram;

public class BidUtil {
	
	public static BidInfo bidCreate(Exchange exchange, StandardTelegram standardTelegram) {
		
		String key = getBidKey(standardTelegram);
		
		BidInfo bidInfo = new BidInfo();
		bidInfo.setName(key);
		bidInfo.setBeforeExchange(exchange);
		bidInfo.setStatus(BidStatus.WAIT);
		bidInfo.setTimeStamp(currentTime());
		
		return bidInfo;
	}
	
	public static String getBidKey(StandardTelegram standardTelegram) {
		
		String key = standardTelegram.getSttlSysCopt().getWhbnSttlWrtnYmd() 
				   + standardTelegram.getSttlSysCopt().getWhbnSttlCretSysNm()
				   + standardTelegram.getSttlSysCopt().getWhbnSttlSrn();
		
		return key;
	}
	
	public static long currentTime() {
		
		long currentTime = new Date().getTime() / 1000;
		
		return currentTime;
	}
}
