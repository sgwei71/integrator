package com.ibkglobal.message;

import java.util.Arrays;

import lombok.Getter;

public enum InfraType {
	MCA("1"), EAI("2"), FEP("3");
	
	@Getter
	private String code;
	
	InfraType(String code) {
		this.code = code;
	}
	
	public static InfraType of(String code) {
		return Arrays.stream(values()).filter(v -> v.code.equals(code)).findFirst().get();
	}
}
