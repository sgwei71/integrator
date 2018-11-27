package com.ibkglobal.integrator.engine.bean.fep;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.struct.resource.ResourceFEP;

public class SendComBean {
	
	@Autowired
	CamelConfig camelConfig;
	
	@Autowired
	ResourceFEP resourceFEP;
	
	int queueLength = 4;
	
	/**
	 * 업무로 보내는 Q생성
	 * @param exchange
	 * @throws Exception
	 */
	public void sendToHost(Exchange exchange) throws Exception {
		try {
			String qName = "jms:queue:" + getHostQueue(exchange);			
			//Endpoint endpoint = null;
			
			camelConfig.getProducerTemplate().send(qName, exchange);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 기관으로 보내는 Q생성
	 * @param exchange
	 * @throws Exception
	 */
	public void sendToExt(Exchange exchange) throws Exception {
		try {
			String qName = "jms:queue:" + getExtQueue(exchange);			
			//Endpoint endpoint = null;
			
			camelConfig.getProducerTemplate().send(qName, exchange);			
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 업무 Queue명 생성
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	public String getHostQueue(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		
		String interfaceId = message.getHeader(ConstantCode.IBK_INTERFACE_ID, String.class);
		if (interfaceId == null) {
			throw new CommonException("");
		}
		
		Interface intf = resourceFEP.getInterface(interfaceId);
		if (intf == null) {
			throw new CommonException("");
		}
		
		String qName   = null;
		String sysCode = intf.getCommon().getSystem().getCode();
		String bizCode = intf.getCommon().getBswr().getCode();
		
		qName = checkFixName(sysCode) + checkFixName(bizCode);
		
		if (StringUtils.isEmpty(qName)) {
			qName = "F." + qName + "HO.Q";
		}
		
		return qName;
	}
	
	/**
	 * 기관 Queue명 생성
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	public String getExtQueue(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		
		StandardTelegram standardTelegram = message.getBody(StandardTelegram.class);
		
		String interfaceId = standardTelegram.getSttlSysCopt().getSttlIntfId();		
		if (interfaceId == null) {
			throw new CommonException("");
		}
		
		Interface intf = resourceFEP.getInterface(interfaceId);
		if (intf == null) {
			throw new CommonException("");
		}
		
		String qName   = null;
		String extBswr = intf.getCommon().getAttribute().getOnline().getExternal().getExtBswr().getCode();
		String otisCd  = intf.getCommon().getAttribute().getOnline().getExternal().getOtisCd().getCode();
		
		qName = checkFixName(extBswr) + checkFixName(otisCd);
		
		if (StringUtils.isEmpty(qName)) {
			qName = "F." + qName + "HI.Q";
		}
		
		return qName;
	}
	
	/**
	 * Queue 이름 고정길이 4
	 * @param data
	 * @return
	 */
	public String checkFixName(String data) {
		if (StringUtils.isEmpty(data) || (data.length() > queueLength)) {
			return "";
		} else {
			int value = queueLength - data.length();
			
			for (int n = 0; n < value; n++) {
				data += "0";
			}
		}
		
		return data;
	}
}
