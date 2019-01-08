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

public class TelegramToFlatAPI extends TelegramToFlat{

public static byte[] telegramToFlatAPI(StandardTelegram standardTelegram, List<Tlgr> fieldList) throws Exception {
		
		System.out.println("TelegramToFlat"+standardTelegram);
		List<byte[]> result = new ArrayList<>();
		
		
		// StandardTelegram으로 포함 된 스키마 데이터 추출(종료부 제외)
		List<Object> sttlList = CommonConverter.getSttlList(standardTelegram);
		
		sttlList.forEach(data -> {
			Field[] fields = CommonConverter.getFieldList(data);
			System.out.println("getFieldList"+data);
			byte[] fieldValue = null;
			
			// 공통부별 데이터(공통부별 체크 및 최종 길이 셋)
			Map<String, byte[]> fieldResult = new LinkedHashMap<>();
			
			for (Field field : fields) {
				field.setAccessible(true);
				
				//System.out.println("filedName="+field.getName());
				// 제외 할 필드명 검사
				if (!CommonConverter.excludeFieldList().contains(CommonConverter.getCheckKey(data, field.getName()))) {
					//System.out.println("filedName2="+field.getName());

					if (field.getAnnotation(SttlField.class) != null) {
						// 일반 필드
						fieldValue = getNormalField(data, field);
					//	System.out.println("filedName3="+field.getName()+":"+fieldValue);

					} 
					else if (field.getName().equals("data")) {
						// 개별부 데이터 할당
						List<byte[]> userDataList = new ArrayList<>();
						System.out.println("Tes 개수 t:"+userDataList.size());
						System.out.println("Tes 개수 t:"+standardTelegram.getUserData().getData());
						System.out.println("Tes 개수 t:"+fieldList);
						
						
						if(userDataList.size()!=0)
							ConverterByte.mapToFlat(userDataList, standardTelegram.getUserData().getData(), fieldList);
//						
//						// 개별부 데이터
						fieldValue = CommonConverter.convertArrayToByteArray(userDataList);
						System.out.println("[PHJ]"+new String(fieldValue));
					} 
				else {
						// 가변 필드
						fieldValue = getValiableField(data, field);
					}
					
					fieldResult.put(field.getName(), fieldValue);
				}
//				
//				// 추가 할 필드 명 검사
//				if (CommonConverter.includeFieldList(CommonConverter.getCheckKey(data, field.getName())) != null) {
//					try {
//						fieldResult.put(field.getName() + "Temp", CommonConverter.includeFieldList(CommonConverter.getCheckKey(data, field.getName())).getBytes("MS949"));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
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
		//result.add("@@".getBytes("MS949"));
		
		byte[] resultData = CommonConverter.convertArrayToByteArray(result);
		
		// 시스템 공통부에 대한 길이 최종설정
		System.out.println("[RESULT]"+new String(resultData)+"["+resultData.length+"]");
//		System.arraycopy(lengthCheck("INTEGER", resultData.length - 6, 6, 0), 0, resultData, 0, 6);
		System.out.println("[RESULT]"+new String(resultData)+"["+resultData.length+"]");
		
		return resultData;
	}
}