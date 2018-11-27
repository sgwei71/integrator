package com.ibkglobal.integrator.engine.bean.common;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.model.IBKTimeoutInfo;
import com.ibkglobal.integrator.engine.timer.IBKTimeout;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.integrator.util.SystemUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.struct.resource.ResourceFEP;

@Component
public class TimerComBeanIBK {
	
	@Autowired
	CamelConfig camelConfig;
	
	@Autowired
	ResourceFEP resourceFEP;
	
	@Autowired
	IBKTimeout<IBKTimeoutInfo> ibkTimeoutTimeCom;

	/**
	 * Register Timer Internal
	 * @param exchange
	 * @throws Exception
	 */
	public void registerTimerInternal(Exchange exchange) throws IBKExceptionFEP {
		Message in = exchange.getIn();
		String interfaceId = in.getHeader(ConstantCode.IBK_INTERFACE_ID, String.class);
		Interface interface1 = resourceFEP.getInterface(interfaceId);
		try {
			String timeoutStr = interface1.getCommon().getAttribute().getOnline().getExternal().getMopfVl();			
			if (!StringUtils.isEmpty(timeoutStr) && timeoutStr.trim().length() > 0) {					
				long timeOut = Long.parseLong(timeoutStr.trim());
				if (timeOut > 0) {
					in.setHeader(ConstantCode.TIMEOUT_VALUE, timeOut);
					registerTimer(exchange);
				}
			}
		} catch (NumberFormatException nfe) {
//			CommonLogger.debugLog(exchange.getIn().getHeader(ConstantCode.BIZ_CODE, String.class),
//					"TimerComBeanIBK.registerTimerInternal", "INVALID TIMEOUT Value! ["+nfe.getMessage()+"]");
		} catch (Exception e) {
//			CommonLogger.errorLog(exchange.getIn().getHeader(ConstantCode.BIZ_CODE, String.class),
//					"TimerComBeanIBK.registerTimerInternal", "REGISTER TIMEOUT FAIL! ", e);
		}
	}
	
	/**
	 * Register Timer
	 * @param exchange
	 * @throws Exception
	 */
	public void registerTimer(Exchange exchange) throws IBKExceptionFEP {
		
		Exchange new_exchange = null; 
		try {
			Message in = exchange.getIn();
			IBKMessage ibkMessage = in.getBody(IBKMessage.class);
			
			String interfaceId = in.getHeader(ConstantCode.IBK_INTERFACE_ID, String.class);						
			String key = IBKMessageUtil.getRecvKey(exchange, interfaceId);	
			
			new_exchange = exchange.copy();
			new_exchange.getIn().setBody(ibkMessage);
			
//			CommonLogger.debugLog(in.getHeader(ConstantCode.BIZ_CODE, String.class), "TimerComBeanIBK.registerTimer()", "타이머 처리  : registerTimer("+key+")");
			
			long timeOut = in.getHeader(ConstantCode.TIMEOUT_VALUE, long.class)*1000;
			registerTimerToQueue(key, timeOut, new_exchange);
			
		} catch (Throwable t) {
			//ErrorCatchUtil.setErrorThrow(exchange, new CommonException(t));
		}
		
	}
	
	/**
	 * Register Timer To Queue
	 * @param key
	 * @param timeOut
	 * @param exchange
	 * @throws Throwable
	 */
	public void registerTimerToQueue(String key, long timeOut, Exchange exchange) throws Throwable{
		
		IBKTimeoutInfo ibkTimeoutInfo = new IBKTimeoutInfo();
		ibkTimeoutInfo.setKey(key);
		ibkTimeoutInfo.setBizCode(exchange.getIn().getHeader(ConstantCode.BIZ_CODE, String.class));
		ibkTimeoutInfo.setOrgCode(exchange.getIn().getHeader(ConstantCode.ORG_CODE, String.class));
		ibkTimeoutInfo.setCurrentDate(SystemUtil.convertDateToString(new Date(), "yyyyMMdd"));
		ibkTimeoutInfo.setCurrentTime(System.currentTimeMillis());
		ibkTimeoutInfo.setTimeOut(timeOut);
		ibkTimeoutInfo.setExchange(exchange);
		
		ibkTimeoutTimeCom.put(ibkTimeoutInfo.getKey(), ibkTimeoutInfo, ibkTimeoutInfo.getTimeOut());
	}
	
	/**
	 * Remove Timer
	 * @param exchange
	 * @throws Exception
	 */
	public void removeTimer(Exchange exchange) throws IBKExceptionFEP {
		
		try {
			long start = System.currentTimeMillis();
			boolean result = false;
			int retry = 0;
			
			Message in = exchange.getIn ();			
			String ifid = in.getHeader(ConstantCode.REMOVE_TIMER_IFIDS, String.class);
			String key = IBKMessageUtil.getRecvKey(exchange, ifid);
			
//			CommonLogger.debugLog(in.getHeader(ConstantCode.BIZ_CODE, String.class), "TimerComBeanIBK", "타이머 처리  : removeTimer("+key+")");
//			CommonUtil.bytesToJLMessage("TimerComBeanIBK",exchange);
			
			if (!StringUtils.isEmpty(key)) {				
				result = ibkTimeoutTimeCom.remove(key);
				// 제거 실패시 재시도
				if (!result) {
					result = ibkTimeoutTimeCom.remove(key);
					retry++;
				}
			}
			
			if(!result){
//				CommonLogger.errorLog(in.getHeader(ConstantCode.BIZ_CODE, String.class), "TimerComBeanIBK", "타이머 제거 실패: ("+key+"){"+retry+"} elapsed Time{"+(System.currentTimeMillis()-start)+"}");
			}else{
//				if((System.currentTimeMillis()-start) > 100) 
//					CommonLogger.debugLog(in.getHeader(ConstantCode.BIZ_CODE, String.class), "TimerComBeanIBK", "타이머  정상 제거 처리 ,("+key+"){"+retry+"} elapsed Time{"+(System.currentTimeMillis()-start)+"} "+result);
			}			
		} catch (Throwable t) {
			//ErrorCatchUtil.setErrorThrow(exchange, new CommonException(t));
		}
	}
}
