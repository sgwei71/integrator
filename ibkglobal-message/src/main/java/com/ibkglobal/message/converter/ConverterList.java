package com.ibkglobal.message.converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.ibk.ibkglobal.data.io.model.Tlgr;

@Deprecated
public class ConverterList {

	@SuppressWarnings("unchecked")
	public static List<Tlgr> transform(LinkedHashMap<String, Object> data, List<Tlgr> fieldList) {

		Collections.sort(fieldList, new ConverterAscending());
		fieldList.forEach(field -> {			
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				transform((LinkedHashMap<String, Object>)data.get(field.getEnsnFldNm()), field.getFieldList());
			}
			else {				
//				field.setValue(data.get(field.getName()).toString());
				field.setDfvlCon(String.valueOf(data.get(field.getEnsnFldNm())));
			}
		});
		
		return fieldList;
	}
}
