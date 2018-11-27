package com.ibkglobal.integrator.log.model;

import com.ibkglobal.message.common.normal.StandardTelegram;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LogEAI {

	private String           seq;       // 거래구분(1, 2, 3, 4...)
	private String           globalId;  // 글로벌ID(34자리)
	
	private StandardTelegram standardTelegram;
}
