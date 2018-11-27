package com.ibkglobal.message.converter.telegram;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.common.validator.sttl.SttlField;
import com.ibkglobal.common.validator.sttl.SttlFieldUtil;
import com.ibkglobal.message.common.normal.DataSetCode;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.ConverterByte;

public class FlatToTelegram {
	
	/**
	 * Flat 전문 -> Telegram 전문(개별부 스키마로 데이터 삽입)
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static StandardTelegram flatToTelegram(byte[] message, List<Tlgr> fieldList) throws Exception {
		// 메시지 셋
		ByteBuffer buffer = ByteBuffer.wrap(message);
		
		StandardTelegram standardTelegram = SttlFieldUtil.defaultInit();
		
		List<Object> sttlList = CommonConverter.getDefaultSttlList(standardTelegram);
		
		// Default로 지정 된 시스템 공통부와 거래 공통부를 처리
		sttlList.forEach(data -> {
			Field[] fields = CommonConverter.getFieldList(data);
			
			try {
				setField(buffer, data, fields, standardTelegram, fieldList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// 그 외의 표준 전문 데이터 처리
		while (buffer.position() < buffer.capacity()) {
			buffer.mark();
			
			// 공통부의 길이 추출			
			byte[] headerCd = new byte[2];
			buffer.get(headerCd);
			
			Object data = CommonConverter.getCommonHeader(standardTelegram, headerCd);
			
			// 종료부
			if (!data.equals("@@")) {
				Field[] fields = CommonConverter.getFieldList(data);
				
				// 공통헤더로 position reset
				buffer.reset();				
				
				setField(buffer, data, fields, standardTelegram, fieldList);
			}
		}
		
		return standardTelegram;
	}
	
	/**
	 * 표준전문 필드 셋
	 * @param buffer
	 * @param data
	 * @param fields
	 * @param standardTelegram
	 * @param fieldList
	 */
	public static void setField(ByteBuffer buffer, Object data, Field[] fields, StandardTelegram standardTelegram, List<Tlgr> fieldList) throws Exception {
		for (Field field : fields) {
			field.setAccessible(true);
			
			// 제외 할 필드명 검사
			if (!CommonConverter.excludeFieldList().contains(CommonConverter.getCheckKey(data, field.getName()))) {
				if (field.getAnnotation(SttlField.class) != null) {
					// 일반 필드
					getNormalField(buffer, data, field);
				} else if (field.getName().equals("data")) {
					// 개별부 데이터
					LinkedHashMap<String, Object> userData = ConverterByte.byteArrayToMap(buffer, fieldList);					
					standardTelegram.getUserData().setData(userData);
				} else {
					// 가변 필드
					getValiableField(buffer, data, field);
				}
			}
			
			// TO-BE 표준전문에 없는 데이터 체크(길이 만큼 버퍼 position 수정)
			if (CommonConverter.getIncludeFieldEai().containsKey(CommonConverter.getCheckKey(data, field.getName()))) {
				Integer length = CommonConverter.includeFieldList(CommonConverter.getCheckKey(data, field.getName())).getBytes("MS949").length;
				buffer.position(buffer.position() + length);
			}
		}
	}
	
	/**
	 * 일반 필드(Flat 전문 -> Telegram 전문)
	 * @param buffer
	 * @param data
	 * @param field
	 */
	public static void getNormalField(ByteBuffer buffer, Object data, Field field) {
		
		SttlField sttlField = field.getAnnotation(SttlField.class);
		
		try {
			byte[] bytes = new byte[sttlField.length()];
			buffer.get(bytes);
			
			Object result = getTypeCastValue(field.getType(), bytes);
			
			field.set(data, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 가변 필드(Flat 전문 -> Telegram 전문)
	 * @param buffer
	 * @param data
	 * @param field
	 */
	public static void getValiableField(ByteBuffer buffer, Object data, Field field) {
		
		try {
			if (field.getType().equals(List.class)) {
				List<Object> list = new ArrayList<>();
				
				ParameterizedType type = (ParameterizedType) field.getGenericType();
				
				Class<?> clazz = (Class<?>) type.getActualTypeArguments()[0];
				
				Field[] fields = clazz.getDeclaredFields();
				
				// 필드 길이 추출
				Integer fieldLength = CommonConverter.getFieldArrayLength(data, field);
				
				// 필드의 반복 횟수에 맞춰서 리스트 추가
				for (int n = 0; n < fieldLength; n++) {
					Object childClazz = clazz.newInstance();
					
					for (Field childField : fields) {
						
						childField.setAccessible(true);
						
						if (childField.getAnnotation(SttlField.class) != null) {
							getNormalField(buffer, childClazz, childField);
						} else {
							getValiableField(buffer, childClazz, childField);
						}
					}
					
					list.add(childClazz);
				}
				
				field.set(data, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 형 변환
	 * @param type
	 * @param value
	 * @return
	 */
	public static Object getTypeCastValue(Class<?> type, byte[] value) throws Exception {
		
		Object result = null;
		
		String data = new String(value, "MS949").trim();
		
		if (!StringUtils.isEmpty(data)) {
			if (type.equals(Integer.class)) {
				result = Integer.parseInt(data);
			} else if (type.equals(BigDecimal.class)) {
				result = new BigDecimal(data);
			} else if (type.equals(DataSetCode.class)) {
				String code = data;
				result = DataSetCode.of(code);
			} else {
				result = data;
			}
		}
		
		return result;
	}
}
