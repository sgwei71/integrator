package com.ibkglobal.integrator.engine.bean.fep.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.common.repository.CacheType;
import com.ibkglobal.common.repository.model.MsgRecvDataSql;
import com.ibkglobal.common.repository.service.CacheService;
import com.ibkglobal.integrator.config.ConstantCode;

public class ComSimBean extends ComBean {
	
	@Autowired
	CacheService cacheService;
	
	public void sendToAutoResponse(Exchange exchange) {
		
		try {
			Message message = exchange.getIn();
			
			String workCode = message.getHeader(ConstantCode.WORK_CODE, String.class);
			
			MsgRecvDataSql msgRecvDataSql = cacheService.find(CacheType.IBK_FEP_RECV_MSG, workCode);
			
			message.setBody(msgRecvDataSql.getRcdaCon().getBytes());
			
		} catch (Exception e) {
			
		}
	}
}
