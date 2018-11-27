package com.ibkglobal.integrator.engine.bean.fep.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.util.RuleCommonUtil;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;

public class ComCommBean extends ComBean {
	
	@Autowired
	ConverterService converterService;

	/**
	 * 업무로 전송
	 * @param exchange
	 * @throws IBKExceptionFEP
	 */
	public void sendToBiz(Exchange exchange) throws IBKExceptionFEP {
		
		Message message = exchange.getIn();
		
		try {
			StandardTelegram standardTelegram = msgParsing(message);
			
			String bizCode = standardTelegram.getSttlTrnCopt().getExtTrnBswrDcd();
			String orgCode = standardTelegram.getSttlTrnCopt().getExtTrnInttDcd();
			
			String dName   = getBizDirect(bizCode, orgCode);
			
			camelConfig.getProducerTemplate().send(dName, exchange);	
		} catch (Exception e) {
			throw new IBKExceptionFEP(null, "");
		}
	}
	
	/**
	 * 기관으로 라우터로 보내는 OUT BOUND 생성
	 * @param bizCode
	 * @param orgCode
	 * @return
	 * @throws IBKExceptionFEP
	 */
	public String getBizDirect(String bizCode, String orgCode) throws IBKExceptionFEP {
		
		String dName = null;
		
		if (StringUtils.isEmpty(bizCode) || StringUtils.isEmpty(orgCode)) {
			throw new IBKExceptionFEP(null, "");
		}
		
		// ex) F.ATMP.ATMP.ROUTE.OUT
		dName = "F." + RuleCommonUtil.checkFixName(bizCode) + "." + RuleCommonUtil.checkFixName(orgCode) + "." + "ROUTE.OUT";
		
		return dName;
	}
	
	/**
	 * 전문파싱
	 * @param message
	 * @return
	 * @throws IBKExceptionFEP
	 */
	public StandardTelegram msgParsing(Message message) throws IBKExceptionFEP {
		
		StandardTelegram standardTelegram = null;
		
		try {							
			String messageData = null;
			
			if (message.getBody() instanceof byte[]) {
				messageData = new String(message.getBody(byte[].class));
			} else {
				messageData = message.getBody(String.class);
			}
			
			standardTelegram = converterService.jsonToObject(messageData, StandardTelegram.class);
		} catch (Exception e) {
			throw new IBKExceptionFEP(null, "");
		}
		
		return standardTelegram;
	}
}
