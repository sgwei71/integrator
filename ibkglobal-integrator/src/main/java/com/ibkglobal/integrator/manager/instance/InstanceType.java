package com.ibkglobal.integrator.manager.instance;

import java.util.Arrays;

import lombok.Getter;

public enum InstanceType {
	MCA("M"), FEP("F"), EAI("E"), ETC("C"), API("A");
	
	@Getter
	private String code;
	
	InstanceType(String code) {
		this.code = code;
	}
	
	public static InstanceType of(String code) {
		return Arrays.stream(values()).filter(v -> v.code.equals(code)).findFirst().get();
	}
}
