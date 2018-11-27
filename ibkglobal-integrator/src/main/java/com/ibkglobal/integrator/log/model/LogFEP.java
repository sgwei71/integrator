package com.ibkglobal.integrator.log.model;

import java.util.LinkedHashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LogFEP extends LogData {
	
	private LinkedHashMap<String, Object> header;
	private LinkedHashMap<String, Object> body;
}
