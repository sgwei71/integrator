package com.ibkglobal.integrator.engine.bean.fep.recovery;

import java.util.Set;

import org.apache.camel.Exchange;

import com.ibkglobal.integrator.exception.CommonException;

public interface RecoveryJournal {
	
	public void putRecoveryJournal(Exchange exchange) throws CommonException;
	
	public void getRecoveryJournal(Exchange exchange) throws CommonException;
	
	// 전문복원이 되었다는 것을 표시
	public void getRecoveryJournal(String key, boolean updateFlag);
	
	public Set<String> getRecoveryJournalKey();
}
