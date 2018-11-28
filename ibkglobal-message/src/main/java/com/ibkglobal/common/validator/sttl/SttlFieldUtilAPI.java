package com.ibkglobal.common.validator.sttl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.common.normal.copt.SttlAmgcCopt;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt;
import com.ibkglobal.message.common.normal.copt.SttlNfchCopt;
import com.ibkglobal.message.common.normal.copt.SttlOpatCopt;
import com.ibkglobal.message.common.normal.copt.SttlOtfxCopt;
import com.ibkglobal.message.common.normal.copt.SttlSvatCopt;
import com.ibkglobal.message.common.normal.copt.SttlSysCopt;
import com.ibkglobal.message.common.normal.copt.SttlTrnCopt;

public class SttlFieldUtilAPI extends SttlFieldUtil{
	
	
	public static StandardTelegram defaultMCA() {
		
		StandardTelegram standardTelegram = defaultInit();
		
		List<Object> sttlList = new ArrayList<>();
		
		sttlList.add(standardTelegram.getSttlSysCopt());
		sttlList.add(standardTelegram.getSttlTrnCopt());
		sttlList.add(standardTelegram.getSttlAmgcCopt());
		sttlList.add(standardTelegram.getSttlNfchCopt());
		sttlList.forEach(data -> {
			Field[] fields = data.getClass().getDeclaredFields();
			
			for (Field field : fields) {				
				SttlField sttlField = field.getAnnotation(SttlField.class);
				
				field.setAccessible(true);
				
				Object defaultValue = null;
				
				// 데이터 변환
				try {
					if (sttlField != null && !sttlField.defaultValue().equals("")) {
						if (field.getType().equals(String.class)) {
							defaultValue = sttlField.defaultValue();
						} else if (field.getType().equals(Integer.class)) {
							defaultValue = Integer.parseInt(sttlField.defaultValue());
						} else if (field.getType().equals(BigDecimal.class)) {
							
						}
					}
				} catch (Exception e) {
					System.out.println("필드 변환 오류");
				}
				
				// 필드 변환
				try {
					field.set(data, defaultValue);
				} catch (Exception e) {
					System.out.println("필드 Set 오류");
				}
			}
		});		
		
		return standardTelegram;
	}
	
}
