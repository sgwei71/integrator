package com.ibkglobal.integrator.engine.bean.fep.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.util.RuleCommonUtil;

public class ComHostBean extends ComBean {
	
	// 큐 관련 테스트...
	public void testTest(Exchange exchange) throws Exception {
		
		try {
			camelConfig.getProducerTemplate().asyncRequestBody("jms:queue:SIM0.SIM1.HI.Q?replyToDeliveryPersistent=false&asyncConsumer=false", exchange.getIn().getBody());
			
			Object recvExchange = camelConfig.getConsumerTemplate().receiveBody("jms:queue:SIM0.SIM1.HO.Q");
			
			System.out.println("수신 데이터 : " + recvExchange);
			
			exchange.getOut().setBody(recvExchange);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 업무 데이터 전송
	 * @param exchange
	 * @throws Exception
	 */
	public void sendToHost(Exchange exchange) throws Exception {
		
		try {
			Message message = exchange.getIn();
			
			String interfaceId = message.getHeader(ConstantCode.IBK_INTERFACE_ID, String.class);
			
			Interface intf = resourceFEP.getInterface(interfaceId);
			
			String bizCode = intf.getInterfaceType().getTarget().getBswr().getCode();
			String sysCode = intf.getInterfaceType().getTarget().getSystem().getCode();
			
			String syncChk = intf.getCommon().getAttribute().getOnline().getExternal().getSync().getCode();
			
			String dName = getHostDirect(bizCode, sysCode);
			
			Exchange sendExchange = exchange.copy();
			
			camelConfig.getProducerTemplate().send(dName, sendExchange);
			
			message.setBody(sendExchange.getOut().getBody());
			
//			switch (syncChk) {
//			case "0" :
//				String dName = getHostDirect(bizCode, sysCode);
//				camelConfig.getProducerTemplate().send(dName, exchange);
//				break;
//			case "1" :
//				String qNameSend = getBizQueue(bizCode, sysCode, "O");
//				camelConfig.getProducerTemplate().send(qNameSend, exchange);				
//				
//				// Recv 큐 정보 생성해서 넣어야함
//				//String qNameRecv = getHostQueue();
//				break;
//			default :
//				break;
//			}
		} catch (Exception e) {
			throw new IBKExceptionFEP(null, "");
		}
	}
	
	/**
	 * 호스트로 보내는 OUT BOUND 생성
	 * @param bizCode
	 * @param sysCode
	 * @return
	 * @throws IBKExceptionFEP
	 */
	public String getHostDirect(String bizCode, String sysCode) throws IBKExceptionFEP {
		
		String dName = null;
		
		if (StringUtils.isEmpty(bizCode) || StringUtils.isEmpty(sysCode)) {
			throw new IBKExceptionFEP(null, "");
		}
		
		// ex) F.CHC0.GCB0.ADAPTER.OUT
		dName = EndpointCode.DIRECT + "F." + RuleCommonUtil.checkFixName(bizCode) + "." + RuleCommonUtil.checkFixName(sysCode) + "." + "ADAPTER.OUT";
		
		return dName;
	}
	
	/**
	 * 업무 Queue명 생성
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	public String getBizQueue(String bizCode, String sysCode, String inOut) throws IBKExceptionFEP {
		
		String qName = null;
		
		if (StringUtils.isEmpty(bizCode) || StringUtils.isEmpty(sysCode)) {
			throw new IBKExceptionFEP(null, "");
		}
		
		// ex) F.CHC0.GCB0.HO.Q
		qName = "F." + RuleCommonUtil.checkFixName(bizCode) + "." + RuleCommonUtil.checkFixName(sysCode) + "." + "H" + inOut +".Q";
		
		return qName;
	}
}
