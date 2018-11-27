package com.ibkglobal.message.converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.common.validator.sttl.SttlField;

public class ConverterByte {

	/**
	 * transform
	 * @param data
	 * @param fieldList
	 * @return
	 */
	public static LinkedHashMap<String, Object> transform(byte[] data, List<Tlgr> fieldList) {
		
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		return byteArrayToMap(buffer, fieldList);
	}
	
	/**
	 * ByteArrayDelimiter To Map
	 * 필드에 대한 길이 신경 쓰지 않고 딜리미터 단위로 데이터를 분해
	 * @param buffer
	 * @param fieldList
	 * @param delimiterValue
	 * @return
	 */
	public static LinkedHashMap<String, Object> byteArrayDelimiterToMap(ByteBuffer buffer, List<Tlgr> fieldList, String delimiterValue) {
		
		Collections.sort(fieldList, new ConverterAscending());
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		
		fieldList.forEach(field -> {
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				List<Map<String, Object>> childList = new LinkedList<>();
				int length = Integer.parseInt(result.get(field.getAllDataLenCon()).toString());
				for (int i = 0; i < length; i++) {
					childList.add(byteArrayDelimiterToMap(buffer, field.getFieldList(), delimiterValue));
				}
				result.put(field.getEnsnFldNm(), childList);
			}
			else {				
				int length = Integer.parseInt(field.getAllDataLenCon());
				byte[] bytes = new byte[length];
				buffer.get(bytes);				
				result.put(field.getEnsnFldNm(), new String(bytes));
			}
		});
		
		return result;
	}
	
	/**
	 * ByteArray To Map
	 * @param buffer
	 * @param fieldList
	 * @return
	 */
	public static LinkedHashMap<String, Object> byteArrayToMap(ByteBuffer buffer, List<Tlgr> fieldList) {
		
		Collections.sort(fieldList, new ConverterAscending());
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		
		fieldList.forEach(field -> {
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				List<Map<String, Object>> childList = new LinkedList<>();
				int length = Integer.parseInt(result.get(field.getAllDataLenCon()).toString());
				for (int i = 0; i < length; i++) {
					childList.add(byteArrayToMap(buffer, field.getFieldList()));
				}
				result.put(field.getEnsnFldNm(), childList);
			}
			else {
				int length = Integer.parseInt(field.getAllDataLenCon());
				byte[] bytes = new byte[length];
				buffer.get(bytes);				
				result.put(field.getEnsnFldNm(), new String(bytes));
			}
		});
		
		return result;
	}
	
	/**
	 * Map To ByteArray
	 * @param buffer
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ByteBuffer mapToByteArray(ByteBuffer buffer, LinkedHashMap<String, Object> data) {
		
		data.forEach((k, v) -> {
			if (v instanceof List) {
				((List<Map<String, Object>>) v).stream()
				                               .map(m -> mapToByteArray(buffer, (LinkedHashMap<String, Object>)m))
				                               .collect(Collectors.toList());				                               
			}
			else {
				buffer.put(String.valueOf(v).getBytes());
			}
		});
		
		return buffer;
	}
	
	/**
	 * Map To ByteArray
	 * @param buffer
	 * @param data
	 * @param fieldList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ByteBuffer mapToByteArray(ByteBuffer buffer, LinkedHashMap<String, Object> data, List<Tlgr> fieldList) {
		
		fieldList.forEach(field -> {
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				mapToByteArray(buffer, (LinkedHashMap<String, Object>) data.get(field.getEnsnFldNm()), field.getFieldList());
			} else {
				String value = fieldStringFormat(field.getDataTypeNm(), data.get(field.getEnsnFldNm()), Integer.parseInt(field.getAllDataLenCon()), field.getDcfrDataLen());
				buffer.put(value.getBytes());
			}
		});
		return buffer;
	}
	
	/**
	 * Map To Flat(2018-09-04)
	 * @param buffer
	 * @param data
	 * @param fieldList
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void mapToFlat(ByteBuffer buffer, LinkedHashMap<String, Object> data, List<Tlgr> fieldList) {
		
		fieldList.forEach(field -> {
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				((LinkedList) data.get(field.getEnsnFldNm())).forEach(map -> {
					mapToFlat(buffer, (LinkedHashMap<String, Object>) map, field.getFieldList());
				});
			} else {
				String value = fieldStringFormat(field.getDataTypeNm(), data.get(field.getEnsnFldNm()), Integer.parseInt(field.getAllDataLenCon()), field.getDcfrDataLen());
				buffer.put(value.getBytes());
			}
		});
	}
	
	/**
	 * Map To Flat(2018-09-07)
	 * @param flatData
	 * @param data
	 * @param fieldList
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void mapToFlat(List<byte[]> flatData, LinkedHashMap<String, Object> data, List<Tlgr> fieldList) {
		
		fieldList.forEach(field -> {
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				((LinkedList) data.get(field.getEnsnFldNm())).forEach(map -> {
					mapToFlat(flatData, (LinkedHashMap<String, Object>) map, field.getFieldList());
				});
			} else {
				String value = fieldStringFormat(field.getDataTypeNm(), data.get(field.getEnsnFldNm()), Integer.parseInt(field.getAllDataLenCon()), field.getDcfrDataLen());
				flatData.add(value.getBytes());
			}
		});
	}
	
	public static String fieldStringFormat(String type, Object value, int defaultLength, int scale) {
		
		if (value == null) {
			if ("STRING".equals(type) || "DATASETCODE".equals(type)) value = "";
			else value = 0;
		}
		
		String result = "";
		switch (type) {
		
		case "STRING" :
		case "DATASETCODE" :
			result = String.format("%1$-" + defaultLength + "s", value);
			break;
		
		case "INTEGER" :
		case "NUMERIC" :
			result = String.format("%0" + defaultLength + "d", new Integer(String.valueOf(value)));
			break;
			
		case "BIGDECIMAL" :
		case "DOUBLE" :
		case "FLOAT" :
			
			String strValue = String.valueOf(value);
			if (scale == 0 && strValue.indexOf(".") > -1) {
				scale = strValue.substring(strValue.indexOf(".") + 1).length();
			}
			
			result = String.format("%0" + defaultLength + "." + String.valueOf(scale) + "f", new BigDecimal(strValue));
			break;
			
		default :
			break;
		}
		return result;
	}
	
	/**
	 * Telegram To ByteArray
	 * @param classType
	 * @param dataMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Deprecated
	public static Map<String, Object> telegramToByteArray(Class<?> classType, LinkedHashMap<String, Object> dataMap) {
		
		Map<String, Object> result = new HashMap<>();		
		Pattern pattern = Pattern.compile("<(.*?)>");
		Matcher matcher = null;
		int total = 0;
		
		List<byte[]> byteList = new LinkedList<>();
		
		for (Field field : classType.getDeclaredFields()) {
			
			Object value = dataMap.get(field.getName());			
			SttlField sttlField = field.getAnnotation(SttlField.class);
			String feildType = field.getType().getSimpleName().toUpperCase();
			String formatValue = null;
			
			if ("LIST".equals(feildType)) {
				if (value != null) {		
					matcher = pattern.matcher(field.getGenericType().getTypeName());
					if (matcher.find()) {
						try {
							Class<?> childClassType = Class.forName(matcher.group(1));
							List<Map> childDataMapList = (List<Map>) value;
							
							for (Map<String, Object> childFromObject : childDataMapList) {
								for (Field childField : childClassType.getDeclaredFields()) {
									
									Object childValue = childFromObject.get(childField.getName());
									
									SttlField childSttlField = childField.getAnnotation(SttlField.class);
									String childFeildType = childField.getType().getSimpleName().toUpperCase();
									
									formatValue = fieldStringFormat(childFeildType, childValue, childSttlField.length(), 0);
									byteList.add(formatValue.getBytes());
									total += formatValue.getBytes().length;
								}
							}
						} catch (ClassNotFoundException e) {}
					}
				}
			} else {
				formatValue = fieldStringFormat(feildType, value, sttlField.length(), 0);
				byteList.add(formatValue.getBytes());
				total += formatValue.getBytes().length;
			}
		}
		
		result.put("total", total);
		result.put("list", byteList);
		
		return result;
	}
}
