package com.ibkglobal.message.converter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ibk.ibkglobal.data.io.model.Tlgr;

public class ConverterMap {
	
	/**
	 * transform
	 * @param data
	 * @param fieldList
	 * @param nullable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> transform(Map<String, Object> data, List<Tlgr> fieldList, boolean nullable) {
		
		Collections.sort(fieldList, new ConverterAscending());
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		
		for (Tlgr field : fieldList) {		
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				LinkedList<?> mapList = null;
				if (data.get(field.getEnsnFldNm()) != null) {
					mapList = ((List<Map<String, Object>>) data.get(field.getEnsnFldNm())).stream()
									.map(m -> transform(m, field.getFieldList(), nullable))
									.collect(Collectors.toCollection(LinkedList::new));
				}				
				if (!nullable && (mapList == null || mapList.size() == 0 || mapList.get(0) == null)) continue;
				
				map.put(field.getEnsnFldNm(), mapList);
			} else if ("GROUP".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				LinkedHashMap<String, Object> groupMap = new LinkedHashMap<>();
				if (data.get(field.getEnsnFldNm()) != null) {					
					
					LinkedHashMap<String, Object> compareData = (LinkedHashMap<String, Object>)data.get(field.getEnsnFldNm());
					
					field.getFieldList().forEach(p -> {	
						if (compareData.get(p.getEnsnFldNm()) != null) {
							groupMap.put(p.getEnsnFldNm(), compareData.get(p.getEnsnFldNm()));
						}
					});
					
					if (!nullable && (groupMap == null || groupMap.size() == 0)) continue;
					
					map.put(field.getEnsnFldNm(), groupMap);
				}
			} else {
				Object value = data.get(field.getEnsnFldNm());
				if (!nullable && value == null) continue;
				
				map.put(field.getEnsnFldNm(), (value != null) ? value : field.getDfvlCon());
			}
		}
		return map;
	}
	
	
	/**
	 * @param fieldList
	 * @return
	 */
	public static LinkedHashMap<String, Object> transform(List<Tlgr> fieldList) {
		
		Collections.sort(fieldList, new ConverterAscending());
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		
		fieldList.forEach(field -> {
			if ("ARRAY".equals(field.getDataTypeNm()) && field.getFieldList() != null) {
				List<LinkedHashMap<String, Object>> arrayList = new ArrayList<>();
				arrayList.add(transform(field.getFieldList()));				
				map.put(field.getEnsnFldNm(), arrayList);
			}
			else {
//				map.put(field.getName(), getField(field));
				map.put(field.getEnsnFldNm(), field.getDfvlCon());
			}
		});
		
		return map;		
	}
	
	/**
	 * Field 정보 맵정보로 변환 시 길이 체크
	 * 
	 * @param field
	 * @return
	 */
	public static String getField(Tlgr field) {
		
		String value = String.valueOf(field.getDfvlCon());
		
		byte[] byteValue = value.getBytes();
		
		Integer length = Integer.parseInt(field.getAllDataLenCon());
		
		if (byteValue.length != length)
		{			
			byte[] newValue = new byte[length];
			
			if (byteValue.length > length) {
				ByteBuffer buffer = ByteBuffer.wrap(byteValue);
				buffer.get(newValue);
				value = new String(newValue);
			}
			else if (byteValue.length < length) {
				ByteBuffer buffer = ByteBuffer.allocate(length);
				buffer.put(byteValue);
				
				for (int n = buffer.position(); n < buffer.limit(); n++) {
					buffer.put((byte) 0);
				}
				
				buffer.position(0);
				buffer.get(newValue);
				value = new String(newValue);
			}
		}
		
		return value;
	}
}
