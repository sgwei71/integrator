package com.ibkglobal.integrator.engine.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.integrator.engine.model.BidInfo;
import com.ibkglobal.integrator.engine.model.BidInfo.BidStatus;
import com.ibkglobal.integrator.engine.timer.IBKTimeout;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;

@Component
public class BidManager {
	
	@Autowired
	IBKTimeout<BidInfo> ibkTimeoutBid;
	
	private Map<String, BidInfo> bidInfoList = new HashMap<>();
	
	public synchronized Map<String, BidInfo> getBidInfoList() {
		synchronized (bidInfoList) {
			return bidInfoList;
		}
	}
	
	public void bidStart(BidInfo bidInfo) throws IBKExceptionMCA {
		try {
			// 비드 정보 추가
			getBidInfoList().put(bidInfo.getName(), bidInfo);
			
			// 비드 타이머 시작
			ibkTimeoutBid.put(bidInfo.getName(), bidInfo, bidInfo.getDefaultTimeOut());
			
			// 비드 업무 시작(Wait)
			workWait(bidInfo);
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.MCA_BID, e.getMessage(), e);
		}
	}
	
	public void bidTimeout(String key) {
		BidInfo bidInfo = getBidInfoList().get(key);
		
		workNotify(bidInfo);
		
		getBidInfoList().remove(key);
	}
	
	public void bidResult(String key, Exchange exchange) {
		// 비드 타이머 정보 삭제
		ibkTimeoutBid.remove(key);
		
		BidInfo bidInfo = getBidInfoList().get(key);
		
		bidInfo.setStatus(BidStatus.COMPLETE);
		bidInfo.setAfterExchange(exchange);
		
		// 정상종료 후 일처리
		bidInfo.getBeforeExchange().getIn().setBody(bidInfo.getAfterExchange().getIn().getBody());
		
		// 비드 업무 시작(Notify)
		workNotify(bidInfo);
		
		getBidInfoList().remove(key);
	}
	
	public void workWait(Object info) {		
		synchronized(info) {
			try {
				info.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void workNotify(Object info) {		
		synchronized (info) {
			info.notify();
		}		
	}
}
