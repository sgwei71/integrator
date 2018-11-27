package com.ibkglobal.integrator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibkglobal.integrator.engine.bean.fep.recovery.RecoveryJournal;
import com.ibkglobal.integrator.engine.bean.fep.recovery.RecoveryJournalRepo;

@Configuration
public class JournalConfig {
	
	//@Value(value = "${recovery.type}")
	private String type;
	
	@Bean
	RecoveryJournal recoveryJournal() {
		if (type != "repo") {
			return null;
		}
		
		return new RecoveryJournalRepo();
	}
}
