package com.ibkglobal.message.common.ifep;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.util.StringUtils;

/**
 * 표준 전문 : 공통부
 * 
 * @author IBK Internet
 *
 */
public class Common {

	protected LinkedHashMap<String, String> messageMap = null;
	
	public LinkedHashMap<String, String> getMessage() {
		if (messageMap == null) {
			initMessage();
		}
		return messageMap;
	}
	
	/**
	 * 초기화 된 "표준 전문" 공통부
	 */
	public void initMessage() {
		messageMap = new LinkedHashMap<String, String>();
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(XmlElement.class)) {
				Min m = field.getAnnotation(Min.class);
				XmlElement x = field.getAnnotation(XmlElement.class);
				
				String value = null;
				if ("int".equals(field.getGenericType().getTypeName())) {
					value = String.format("%0" + m.value() + "d", Integer.parseInt(x.defaultValue()));
				} else {
					value = String.format("%1$-" + m.value() + "s", x.defaultValue());
				}
						
				messageMap.put(x.name(), value);
			}
		}
	}
	
	/**
	 * 표준 전문 공통부 put
	 * 
	 * @param key
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public void put(String key, String value) throws NoSuchFieldException, SecurityException {
		
		Field field = Arrays.stream(this.getClass().getDeclaredFields())
				.filter(f -> f.getAnnotation(XmlElement.class).name().equals(key))
				.findFirst()
				.get();
		Min m = field.getAnnotation(Min.class);		
		value = StringUtils.isEmpty(value) ? "" : value;
		
		if ("int".equals(field.getGenericType().getTypeName())) {
			value = String.format("%0" + m.value() + "d", Integer.parseInt(value));
		} else {
			value = String.format("%1$-" + m.value() + "s", value);
		}
		
		messageMap.put(key, value);
	}
	
	/**
	 * 표준 전문 공통부 key
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return messageMap.get(key);
	}
	
	/**
	 * 표준 전문 공통부 getByte
	 * 
	 * @return
	 */
	public byte[] getByte() {
		StringBuffer sb = new StringBuffer();
		Iterator<String> itr = messageMap.keySet().iterator();
		while (itr.hasNext()) {
			sb.append(messageMap.get(itr.next()));
		}
		return (sb.toString()).getBytes();
	}
		
	/**
	 * Get Total Length
	 * @return
	 */
	public int getTotalLength() {
		return Arrays.stream(this.getClass().getDeclaredFields())
				.mapToInt(m -> Integer.parseInt(String.valueOf(m.getAnnotation(Min.class).value())))
				.sum();
	}
	
	/**
	 * Get Length
	 * @param key
	 * @return
	 */
	public int getLength(String key) {
		return Arrays.stream(this.getClass().getDeclaredFields())
				.filter(f -> f.getAnnotation(XmlElement.class).name().equals(key))
				.mapToInt(m -> Integer.parseInt(String.valueOf(m.getAnnotation(Min.class).value())))
				.findFirst()
				.getAsInt();
	}
}
