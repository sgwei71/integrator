package com.ibkglobal.message.converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibk.ibkglobal.data.mapp.TlgrOrder;

public class ConverterMapping {

	/**
	 * mapping inbound <-> outbound
	 * 
	 * @param List<Mapping>
	 *            mappingLis
	 * @param Map<String,
	 *            Object> sourceData
	 * @param List<Field>
	 *            targetList
	 * @param String
	 *            inOut
	 * @param boolean
	 *            nullable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> transform(List<TlgrMapping> mappingList, Map<String, Object> sourceData,
			List<Tlgr> targetList, String inOut, boolean nullable) {

		Collections.sort(targetList, new ConverterAscending());
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();

		for (Tlgr targetField : targetList) {
			// if ("ARRAY".equals(targetField.getDataTypeNm()) && targetField.getFieldList()
			// != null) {
			if ("ARRAY".equals(targetField.getDataTypeNm())) {
				LinkedList<?> mapList = null;
				if (sourceData.get(targetField.getEnsnFldNm()) != null) {
					mapList = ((List<Map<String, Object>>) sourceData.get(targetField.getEnsnFldNm())).stream()
							.map(data -> transform(mappingList, data, targetField.getFieldList(), inOut, nullable))
							.collect(Collectors.toCollection(LinkedList::new));
				}
				if (!nullable && (mapList == null || mapList.size() == 0 || mapList.get(0) == null))
					continue;

				map.put(targetField.getEnsnFldNm(), mapList);
			} else if ("GROUP".equals(targetField.getDataTypeNm())) {
				if (sourceData.get(targetField.getEnsnFldNm()) != null) {
					LinkedHashMap<String, Object> groupMap = new LinkedHashMap<>();					
					LinkedHashMap<String, Object> sourceGroupData = (LinkedHashMap<String, Object>) sourceData.get(targetField.getEnsnFldNm());
					
					targetField.getFieldList().forEach(p -> {						
						if (sourceGroupData.get(p.getEnsnFldNm()) != null) {
							groupMap.put(p.getEnsnFldNm(), sourceGroupData.get(p.getEnsnFldNm()));
						}
					});

					if (!nullable && (groupMap == null || groupMap.size() == 0))
						continue;

					map.put(targetField.getEnsnFldNm(), groupMap);
				}
			} else {
				Object value = transField(mappingList, sourceData, targetField, inOut);

				if (!nullable && value == null || value == null)
					continue;

				map.put(targetField.getEnsnFldNm(), value);
			}
		}

		return map;
	}

	/**
	 * 타겟필드로 정보를 가공해서 매핑한다.
	 * 
	 * @param List<Mapping>
	 *            mappingList
	 * @param Map<String,
	 *            Object> sourceData
	 * @param Field
	 *            targetField
	 * @param String
	 *            inOut
	 * @return
	 */
	public static Object transField(List<TlgrMapping> mappingList, Map<String, Object> sourceData, Tlgr targetField,
			String inOut) {
		Object result = null;

		TlgrMapping mapping = null;

		if ("IN".equals(inOut)) {
			try {
				mapping = mappingList.stream()
						.filter(v -> v.getTargetNm().get(0).getFldNm().equals(targetField.getEnsnFldNm())
								&& v.getTargetNm().get(0).getLvlNo().equals(targetField.getLvlNo()))
						.findFirst().get();
			} catch (Exception e) {
				return null;
			}
		} else {
			try {
				mapping = mappingList.stream()
						.filter(v -> v.getSourceNm().get(0).getFldNm().equals(targetField.getEnsnFldNm())
								&& v.getSourceNm().get(0).getLvlNo().equals(targetField.getLvlNo()))
						.findFirst().get();
			} catch (Exception e) {
				return null;
			}
		}

		switch (mapping.getFnctNm().toUpperCase()) {
		case "MOVE":
			result = sourceData.get(("IN".equals(inOut)) ? mapping.getSourceNm().get(0).getFldNm()
					: mapping.getTargetNm().get(0).getFldNm());
			break;

		case "ASSIGN":
			result = mapping.getUserParaCon();
			break;

		case "CONCAT":
			List<TlgrOrder> tlgrOrderList = ("IN".equals(inOut)) ? mapping.getSourceNm() : mapping.getTargetNm();

			if (tlgrOrderList != null && tlgrOrderList.size() > 0) {
				List<Object> transList = tlgrOrderList.stream().map(m -> sourceData.get(m.getFldNm()))
						.collect(Collectors.toList());

				result = transConcatValue(transList, targetField);
			}
			break;

		case "REMOVE":
			if (mapping.getUserParaCon() != null && (mapping.getUserParaCon().length() > 0)) {
				String fieldName = mapping.getSourceNm().get(0).getFldNm();

				result = String.valueOf(sourceData.get(fieldName)).replace(mapping.getUserParaCon(), "");
			}
			break;

		case "SUBSTRING":
			if (mapping.getUserParaCon() != null && (mapping.getUserParaCon()).length() > 0) {
				String[] paraConValue = mapping.getUserParaCon().split(",");

				if (paraConValue.length >= 2) {
					String delimiter = paraConValue[0];
					String index = paraConValue[1];

					String fieldName = ("IN".equals(inOut)) ? mapping.getSourceNm().get(0).getFldNm()
							: mapping.getTargetNm().get(0).getFldNm();

					String srcFldValue = String.valueOf(sourceData.get(fieldName));
					int idx = srcFldValue.indexOf(delimiter);

					if (idx != -1) {
						String[] splitValue = srcFldValue.split(delimiter);
						if (splitValue.length > Integer.parseInt(index)) {
							srcFldValue = splitValue[Integer.parseInt(index)];
						}
					}
					result = srcFldValue;
				}
			}
			break;

		default:
			break;
		}

		return result;
	}

	/**
	 * 소스 필드 정보를 찾아서 타겟에 CONCAT 한다.
	 * 
	 * @param List<Object>
	 *            sourceFieldList
	 * @param Field
	 *            targetField
	 * @return
	 */
	public static String transConcatValue(List<Object> sourceFieldList, Tlgr targetField) {

		String result = "";

		switch (targetField.getDataTypeNm()) {
		case "STRING":
			result = sourceFieldList.stream().map(p -> String.valueOf(p)).collect(Collectors.joining());
			break;

		case "NUMERIC":
			Integer isumValue = sourceFieldList.stream().mapToInt(v -> (Integer) v).sum();

			result = isumValue.toString();
			break;

		case "DECIMAL":
			double dsumValue = sourceFieldList.stream().mapToDouble(v -> (Double) v).sum();

			result = String.valueOf(dsumValue);
			break;

		default:
			break;
		}

		return result;
	}
}
