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

public class FlatToTelegramAPI extends FlatToTelegram{
	
	/**
	 * Flat 전문 -> Telegram 전문(개별부 스키마로 데이터 삽입)
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static StandardTelegram flatToTelegramAPI(byte[] message, List<Tlgr> fieldList) throws Exception {
		// 메시지 셋
		ByteBuffer buffer = ByteBuffer.wrap(message);
		
		StandardTelegram standardTelegram = SttlFieldUtil.defaultInit();
		
		List<Object> sttlList = CommonConverter.getDefaultSttlList(standardTelegram);
		
		// Default로 지정 된 시스템 공통부와 거래 공통부를 처리
		sttlList.forEach(data -> {
			Field[] fields = CommonConverter.getFieldList(data);
			
			try {
				setFieldAPI(buffer, data, fields, standardTelegram, fieldList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		//buffer.get
		// 그 외의 표준 전문 데이터 처리
		while (buffer.position() < buffer.capacity()) {
			buffer.mark();
			System.out.println("Object data[buffer.tostring]="+buffer.toString());			
			
			System.out.println("Object data[buffer.tostring]="+buffer.toString());			
			
			// 공통부의 길이 추출			
			byte[] headerCd = new byte[2];
			buffer.get(headerCd);
			System.out.println("Object data="+new String(headerCd));			
			Object data = CommonConverter.getCommonHeader(standardTelegram, headerCd);
			System.out.println("Object data="+data);
			// 종료부
			if (!data.equals("@@")) {
				Field[] fields = CommonConverter.getFieldList(data);
				System.out.println("fields"+fields);
				// 공통헤더로 position reset
				buffer.reset();				
				System.out.println("fieldList"+fieldList);
				System.out.println("buffer"+buffer);
					
				setFieldAPI(buffer, data, fields, standardTelegram, fieldList);
			}
		}
		
		return standardTelegram;
	}
	public static void setFieldAPI(ByteBuffer buffer, Object data, Field[] fields, StandardTelegram standardTelegram, List<Tlgr> fieldList) throws Exception {
		for (Field field : fields) {
			field.setAccessible(true);
			System.out.println("setField:"+field.getName());
			// 제외 할 필드명 검사
			if (!CommonConverter.excludeFieldList().contains(CommonConverter.getCheckKey(data, field.getName()))) {
				if (field.getAnnotation(SttlField.class) != null) {
					// 일반 필드
					getNormalField(buffer, data, field);
				} else if (field.getName().equals("data")) {
					// 개별부 데이터
					System.out.println("setField:"+buffer+":"+fieldList);

					LinkedHashMap<String, Object> userData = ConverterByte.byteArrayToMap(buffer, fieldList);					
					standardTelegram.getUserData().setData(userData);
				} else {
					// 가변 필드
					getValiableField(buffer, data, field);
				}
			}
			
			// TO-BE 표준전문에 없는 데이터 체크(길이 만큼 버퍼 position 수정)
//			if (CommonConverter.getIncludeFieldEai().containsKey(CommonConverter.getCheckKey(data, field.getName()))) {
//				Integer length = CommonConverter.includeFieldList(CommonConverter.getCheckKey(data, field.getName())).getBytes("MS949").length;
//					System.out.println("setField"+length);
//				buffer.position(buffer.position() + length);
//			}
		}
	}
	
}
