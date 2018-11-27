package com.ibkglobal.message.converter.telegram;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.common.validator.sttl.SttlField;
import com.ibkglobal.message.common.normal.DataSetCode;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.ConverterByte;

public class TelegramToFlat {
	
	/**
	 * Telegram 전문 -> Flat 전문
	 * @param standardTelegram
	 * @return
	 * @throws Exception 
	 */
	public static byte[] telegramToFlat(StandardTelegram standardTelegram, List<Tlgr> fieldList) throws Exception {
		List<byte[]> result = new ArrayList<>();
		
		// StandardTelegram으로 포함 된 스키마 데이터 추출(종료부 제외)
		List<Object> sttlList = CommonConverter.getSttlList(standardTelegram);
		
		sttlList.forEach(data -> {
			Field[] fields = CommonConverter.getFieldList(data);
			
			byte[] fieldValue = null;
			
			// 공통부별 데이터(공통부별 체크 및 최종 길이 셋)
			Map<String, byte[]> fieldResult = new LinkedHashMap<>();
			
			for (Field field : fields) {
				field.setAccessible(true);
				
				// 제외 할 필드명 검사
				if (!CommonConverter.excludeFieldList().contains(CommonConverter.getCheckKey(data, field.getName()))) {
					if (field.getAnnotation(SttlField.class) != null) {
						// 일반 필드
						fieldValue = getNormalField(data, field);
					} else if (field.getName().equals("data")) {
						// 개별부 데이터 할당
						List<byte[]> userDataList = new ArrayList<>();
						
						ConverterByte.mapToFlat(userDataList, standardTelegram.getUserData().getData(), fieldList);
						
						// 개별부 데이터
						fieldValue = CommonConverter.convertArrayToByteArray(userDataList);
					} else {
						// 가변 필드
						fieldValue = getValiableField(data, field);
					}
					
					fieldResult.put(field.getName(), fieldValue);
				}
				
				// 추가 할 필드 명 검사
				if (CommonConverter.includeFieldList(CommonConverter.getCheckKey(data, field.getName())) != null) {
					try {
						fieldResult.put(field.getName() + "Temp", CommonConverter.includeFieldList(CommonConverter.getCheckKey(data, field.getName())).getBytes("MS949"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			// 필드명 dtstLen에 공통부별 데이터 길이 셋
			if (fieldResult.containsKey("dtstLen")) {
				Integer length = CommonConverter.getByteArrayLength(fieldResult.values(), 8);
				
				try {
					fieldResult.replace("dtstLen", lengthCheck("INTEGER", length, 6, 0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			result.addAll(fieldResult.values());
		});
		
		// 종료부 추가
		result.add("@@".getBytes("MS949"));
		
		byte[] resultData = CommonConverter.convertArrayToByteArray(result);
		
		// 시스템 공통부에 대한 길이 최종설정
		System.arraycopy(lengthCheck("INTEGER", resultData.length - 6, 6, 0), 0, resultData, 0, 6);
		
		return resultData;
	}
	
	/**
	 * 일반 필드(Telegram 전문 -> Flat 전문)
	 * @param data
	 * @param field
	 * @return
	 */
	public static byte[] getNormalField(Object data, Field field) {
		byte[] result = null;
		
		SttlField sttlField = field.getAnnotation(SttlField.class);
		
		try {
			Object value = field.get(data);
			
			result = getTypeCastValue(sttlField.length(), field.getType(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 가변 필드(Telegram 전문 -> Flat 전문)
	 * @param data
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static byte[] getValiableField(Object data, Field field) {
		List<byte[]> fieldDataList = new ArrayList<>();
		
		try {
			if (field.getType().equals(List.class)) {
				Object childListData = field.get(data);
				
				List<Object> childList = (List<Object>) childListData;
				
				for (Object childData : childList) {
					Field[] fields = childData.getClass().getDeclaredFields();
					
					for (Field childField : fields) {								
						childField.setAccessible(true);
						
						byte[] temp = getNormalField(childData, childField);
						
						fieldDataList.add(temp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return CommonConverter.convertArrayToByteArray(fieldDataList);
	}
	
	/**
	 * 형 변환
	 * @param type
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public static byte[] getTypeCastValue(Integer length, Class<?> type, Object value) throws Exception {
		
		byte[] result = null;
		
		if (type.equals(Integer.class)) {
			result = lengthCheck("INTEGER", value, length, 0);
		} else if (type.equals(BigDecimal.class)) {
			result = lengthCheck("BIGDECIMAL", value, length, 0);
		} else if (type.equals(DataSetCode.class)) {
			String code = ((DataSetCode) value).getJsonValue();			
			result = lengthCheck("DATASETCODE", code, length, 0);
		} else {
			result = lengthCheck("STRING", value, length, 0);
		}
		
		return result;
	}
	
	/**
	 * 필드 길이 체크
	 * @param type
	 * @param value
	 * @param defaultLength
	 * @param scale
	 * @return
	 */
	public static byte[] lengthCheck(String type, Object value, int defaultLength, int scale) throws Exception {

		ByteBuffer result = ByteBuffer.allocate(defaultLength);
		
		String data = "";
		
		switch (type) {
		case "STRING" :
		case "DATASETCODE" :
			data = String.format("%1$-" + defaultLength + "s", value == null ? "" : value);
			break;
		case "INTEGER" :
		case "NUMERIC" :
			String strValueIn = (value != null ? String.valueOf(value) : "0");
			data = String.format("%0" + defaultLength + "d", new Integer(strValueIn));
			break;
		case "BIGDECIMAL" :
		case "DOUBLE" :
		case "FLOAT" :
			String strValueFl = (value != null ? String.valueOf(value) : "0");
			if (scale == 0 && strValueFl.indexOf(".") > -1) {
				scale = strValueFl.substring(strValueFl.indexOf(".") + 1).length();
			}
			
			data = String.format("%0" + defaultLength + "." + String.valueOf(scale) + "f", new BigDecimal(strValueFl));
			break;
			
			default :
				break;
		}
		
		result.put(data.getBytes("MS949"), 0, defaultLength);
		
		return result.array();
	}
}
