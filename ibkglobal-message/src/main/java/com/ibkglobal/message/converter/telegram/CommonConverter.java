package com.ibkglobal.message.converter.telegram;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibkglobal.common.validator.sttl.SttlFieldArray;
import com.ibkglobal.message.common.normal.AbstractFlexibleDataSet;
import com.ibkglobal.message.common.normal.DataSetCode;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.common.normal.copt.SttlAmgcCopt;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt;
import com.ibkglobal.message.common.normal.copt.SttlNfchCopt;
import com.ibkglobal.message.common.normal.copt.SttlOpatCopt;
import com.ibkglobal.message.common.normal.copt.SttlOtfxCopt;
import com.ibkglobal.message.common.normal.copt.SttlSvatCopt;

import lombok.Getter;

public class CommonConverter {
	
	// EAI와 신규 전문 비교 시 제외 해야 할 항목
	private static String[] excludeFieldEai = {"SttlTrnCopt.ntcd", "SttlTrnCopt.bncd", "SttlNfchCopt.brcd", "SttlNfchCopt.tln"};
	
	@Getter
	// EAI와 신규 전문 비교 시 검사 해야 할 항목(기존에는 있었는데 이번 전문 스키마에는 제외 된 항목)
	private static Map<String, String> includeFieldEai = new LinkedHashMap<>();
	
	static {
		// 앞의 필드 일 때, 뒤에 디폴드 값이 들어가거나 빠지게 된다.
		includeFieldEai.put("SttlTrnCopt.blngFncmCd", "000");
		includeFieldEai.put("SttlAmgcCopt.idcrScanSrn", "0000");
	}
	
	/**
	 * 변환 할 기본 전문 항목 설정(시스템 공통부, 거래 공통부는 전문에 항상 들어간다.)
	 * @return
	 */
	public static List<Object> getDefaultSttlList(StandardTelegram standardTelegram) {
		List<Object> sttlList = new ArrayList<>();
		
		sttlList.add(standardTelegram.getSttlSysCopt()); // 시스템 공통부
		sttlList.add(standardTelegram.getSttlTrnCopt()); // 거래 공통부
		
		return sttlList;
	}
	
	/**
	 * 표준전문을 플랫으로 바꿀 때의 구조 생성
	 * @param standardTelegram
	 * @return
	 * @throws Exception
	 */
	public static List<Object> getSttlList(StandardTelegram standardTelegram) throws Exception {
		List<Object> sttlList = new ArrayList<>();
		
		Field[] fields = standardTelegram.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			
			if (!field.getName().equals("endSignal")) {
				field.setAccessible(true);
				
				Object fieldData = field.get(standardTelegram);
				
				if (fieldData != null && getSttlUseChk(fieldData)) {
					sttlList.add(fieldData);
				}
			}
		}
		
		return sttlList;
	}
	
	public static boolean getSttlUseChk(Object fieldData) {
		
		boolean check = true;
		
		if (fieldData instanceof AbstractFlexibleDataSet) {
			if (((AbstractFlexibleDataSet)fieldData).getDtstDcd() == null) {
				check = false;
			}
		}
		
		return check;
	}
	
	public static String getCheckKey(Object data, String fieldName) {
		return data.getClass().getSimpleName() + "." + fieldName;
	}
	
	/**
	 * super + this 필드 반환
	 * @param data
	 * @return
	 */
	public static Field[] getFieldList(Object data) {
		Field[] result = null;
		
		// Super Class
		Field[] superField = data.getClass().getSuperclass().getDeclaredFields();
		// this Class
		Field[] fields = data.getClass().getDeclaredFields();
		
		List<Field> fieldList = new ArrayList<>();
		
		fieldList.addAll(Arrays.asList(superField));
		fieldList.addAll(Arrays.asList(fields));
		
		result = fieldList.stream()
				          .toArray(Field[]::new);
		
		return result;
	}
	
	public static Object getCommonHeader(StandardTelegram standardTelegram, byte[] headerCd) {
		Object headerSchema = null;
		
		DataSetCode dataSetCode = DataSetCode.of(new String(headerCd));
		
		switch (dataSetCode) {
		case AMGC_COPT :
			standardTelegram.setSttlAmgcCopt(new SttlAmgcCopt());
			headerSchema = standardTelegram.getSttlAmgcCopt();
			break;
		case NFCH_COPT :
			standardTelegram.setSttlNfchCopt(new SttlNfchCopt());
			headerSchema = standardTelegram.getSttlNfchCopt();
			break;
		case SVAT_COPT :
			standardTelegram.setSttlSvatCopt(new SttlSvatCopt());
			headerSchema = standardTelegram.getSttlSvatCopt();
			break;
		case MSG_COPT :
			standardTelegram.setSttlMsgCopt(new SttlMsgCopt());
			headerSchema = standardTelegram.getSttlMsgCopt();
			break;
		case OPAT_COPT :
			standardTelegram.setSttlOpatCopt(new SttlOpatCopt());
			headerSchema = standardTelegram.getSttlOpatCopt();
			break;
		case OTFX_COPT :
			standardTelegram.setSttlOtfxCopt(new SttlOtfxCopt());
			headerSchema = standardTelegram.getSttlOtfxCopt();
			break;
		case USER_DATA :
			standardTelegram.setUserData(new UserData());
			headerSchema = standardTelegram.getUserData();
			break;
		case END_SIGNAL :
			headerSchema = "@@";
			break;
			default :
				headerSchema = null;
				break;
		}
		
		return headerSchema;
	}
	
	/**
	 * AS-IS 전문과 TO-BE 전문 비교 시 제외 할 리스트(EAI FLAT전문 변환 시, 제외 해야 할 항목 지정)
	 * @return
	 */
	public static List<String> excludeFieldList() {
		return Arrays.asList(excludeFieldEai);
	}
	
	/**
	 * AS-IS 전문과 TO-BE 전문 비교 시 추가 할 데이터 추출
	 * @param fieldName
	 * @return
	 */
	public static String includeFieldList(String fieldName) {
		return includeFieldEai.get(fieldName);
	}
	
	/**
	 * 배열을 바이트 배열로 변환
	 * @param data
	 * @return
	 */
	public static byte[] convertArrayToByteArray(List<byte[]> data) {
		byte[] result = null;
		
		// 최종 결과 길이
		Integer length = data.stream()
				             .mapToInt(p -> p.length)
				             .sum();
		
		// 버퍼 할당
		ByteBuffer buffer = ByteBuffer.allocate(length);
		
		data.forEach(byteArray -> {
			buffer.put(byteArray);
		});
		
		result = buffer.array();
		
		return result;
	}
	
	public static Integer getByteArrayLength(Collection<byte[]> collection, Integer dist) {
		Integer length = collection.stream()
				             .mapToInt(p -> p.length)
				             .sum();
		
		length = length - dist;
		
		return length;
	}
	
	/**
	 * 가변 필드 길이 정보 추출
	 * @param data
	 * @param field
	 * @return
	 */
	public static Integer getFieldArrayLength(Object data, Field field) {
		
		Integer result = null;
		
		try {
			
			SttlFieldArray sttlFieldArray = field.getAnnotation(SttlFieldArray.class);
			
			String arrayLengthName = sttlFieldArray.lengthField();
			
			Field lengthField = data.getClass().getDeclaredField(arrayLengthName);
			
			lengthField.setAccessible(true);
			
			result = (Integer) lengthField.get(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
