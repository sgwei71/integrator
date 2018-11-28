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

public class ConverterByteAPI extends ConverterByte{
	
	public static LinkedHashMap<String, Object> byteArrayToMap(ByteBuffer buffer, List<Tlgr> fieldList) {
		System.out.println("======"+fieldList);
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
				System.out.println("byteArrayToMap"+field.getEnsnFldNm()+":"+new String(bytes));
				result.put(field.getEnsnFldNm(), new String(bytes));
			}
		});
		
		return result;
	}
}
