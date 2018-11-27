package com.ibkglobal.message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.ibkglobal.message.common.ism.IsmWorkInfo;
import com.ibkglobal.message.common.normal.StandardTelegram;

import lombok.Data;

@Data
public class IBKMessage implements Serializable {
	
	private static final long serialVersionUID = -7235625387908344246L;
	private String interfaceId;
	private InfraType infraType;
	
	/*
	 * 기본 데이터
	 */
	private Object baseData;
	
	/*
	 * StandardTelegram
	 */
	private StandardTelegram standardTelegram;
	
	/*
	 * IsmWorkInfo
	 */
	private IsmWorkInfo ismWorkInfo;
	
	/*
	 * SendData
	 */
	private byte[] sendData;
	
	/*
	 * 헤더, 바디 데이터
	 */
	private LinkedHashMap<String, Object> header;
	private LinkedHashMap<String, Object> body;
	
	public Object getValue(String key) {
		Object value = header.get(key);
		if (value == null) {
			value = body.get(key);
		}
		return value;
	}
	
	public byte[] getByte() {
		LinkedHashMap<String, Object> messageMap = new LinkedHashMap<>();
		messageMap.putAll(header);
		messageMap.putAll(body);
		
		StringBuffer sb = new StringBuffer();
		Iterator<String> itr = messageMap.keySet().iterator();
		while (itr.hasNext()) {
			sb.append(messageMap.get(itr.next()));
		}
		return (sb.toString()).getBytes();
	}
}