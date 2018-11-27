package com.ibkglobal.message.struct.resource;

public abstract class ResourceDefault {
	
	String type; // 전문 타입
	
	public abstract void init(String type) throws Exception;
	public abstract void load() throws Exception;
}
