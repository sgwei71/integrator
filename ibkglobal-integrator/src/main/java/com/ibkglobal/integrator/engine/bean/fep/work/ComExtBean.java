package com.ibkglobal.integrator.engine.bean.fep.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.util.RuleCommonUtil;
import com.ibkglobal.message.common.normal.StandardTelegram;

public class ComExtBean extends ComBean {
	
	/**
	 * 기관으로 데이터 전송
	 * @param exchange
	 */
	public void sendToExt(Exchange exchange) {
		
		try {
			Message message = exchange.getIn();
			
			StandardTelegram standardTelegram = message.getHeader(ConstantCode.IBK_NORMAL_HEADER, StandardTelegram.class);
			
			String bizCode = standardTelegram.getSttlTrnCopt().getExtTrnBswrDcd();
			String orgCode = standardTelegram.getSttlTrnCopt().getExtTrnInttDcd();
			
			String dName = getExtDirect(bizCode, orgCode);
			
			Exchange sendExchange = exchange.copy();
			
			camelConfig.getProducerTemplate().send(dName, sendExchange);
			
			message.setBody(sendExchange.getOut().getBody());
			
//			String syncChk = "sync";
//			
//			switch (syncChk) {
//			case "sync" :
//				String dName = getExtDirect(bizCode, orgCode);
//				camelConfig.getProducerTemplate().send(dName, exchange);
//				break;
//			case "async" :
//				break;
//			default :
//				break;
//			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 기관으로 보내는 OUT BOUND 생성
	 * @param bizCode
	 * @param orgCode
	 * @return
	 * @throws IBKExceptionFEP
	 */
	public String getExtDirect(String bizCode, String orgCode) throws IBKExceptionFEP {
		
		String dName = null;
		
		if (StringUtils.isEmpty(bizCode) || StringUtils.isEmpty(orgCode)) {
			throw new IBKExceptionFEP(null, "");
		}
		
		// ex) F.ATMP.ATMP.ADAPTER.OUT
		dName = "F." + RuleCommonUtil.checkFixName(bizCode) + "." + RuleCommonUtil.checkFixName(orgCode) + "." + "ADAPTER.OUT";
		
		return dName;
	}
}
