package com.ibkglobal.integrator.tc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.common.validator.ValidatorService;
import com.ibkglobal.common.validator.model.ValidResult;
import com.ibkglobal.message.common.normal.StandardTelegram;

@Component
public class ValidTest {
	
	@Autowired
	ValidatorService validatorService;
	
	/**
	 * 표준전문 검증(현재는 시스템 공통부와 거래 공통부만 Valid 체크중)
	 * @param standardTelegram
	 * @return
	 */
	public ValidResult validator(StandardTelegram standardTelegram) {
		return validatorService.validationSttl(standardTelegram);
	}
}
