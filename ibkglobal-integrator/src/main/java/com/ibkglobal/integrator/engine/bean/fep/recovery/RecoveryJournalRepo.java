package com.ibkglobal.integrator.engine.bean.fep.recovery;

import java.util.Hashtable;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.message.common.normal.StandardTelegram;

@Component
public class RecoveryJournalRepo extends RecoveryJournalDefault {

	private static Hashtable<String, Object> journalRepository = new Hashtable<String, Object>();
	
	@Override
	public void putRecoveryJournal(Exchange exchange) throws IBKExceptionFEP {
		Message in = exchange.getIn();
		
		// 공통부 저장
		String interfaceId = in.getHeader(ConstantCode.IBK_INTERFACE_ID, String.class);
		
		if (interfaceId == null) {
			throw new IBKExceptionFEP(ErrorType.FEP_RECOVERY, ConstantCode.IBK_INTERFACE_ID + " is not set.");
		}
		
		String recvKey = IBKMessageUtil.getRecvKey(exchange, interfaceId);
		
		journalRepository.put(recvKey, exchange.copy());
	}

	@Override
	public void getRecoveryJournal(Exchange exchange) throws CommonException {
		Message in          = exchange.getIn();
		
		String interfaceIds = in.getHeader(ConstantCode.IBK_INTERFACE_ID_REF, String.class);
		String recvKey      = IBKMessageUtil.getRecvKey(exchange, interfaceIds);
		
//		boolean updateFlag = true;
//		String logUpdate = PropUtil.getPropertiesValue("config", "rlogUpdate");
//		if (logUpdate == null || !logUpdate.trim().equalsIgnoreCase("y")) {
//			updateFlag = false;
//		}
		
		Exchange data = (Exchange) journalRepository.remove(recvKey);	
		in.setHeader(ConstantCode.IBK_NORMAL_HEADER, data.getIn().getHeader(ConstantCode.IBK_NORMAL_HEADER, StandardTelegram.class));		
	}
	
	@Override
	public void getRecoveryJournal(String key, boolean updateFlag) {
	}

	@Override
	public Set<String> getRecoveryJournalKey() {
		return journalRepository.keySet();
	}
}
