package com.ibkglobal.message.converter.delimiter;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.model.Tlgr;

public class DelimiterToUserData {

	/**
	 * delimiter 전문 byte -> String 변환 후 헤더 파싱
	 * @param data
	 * @param headerList
	 * @return
	 */
	public static LinkedHashMap<String, Object> delimiterToHeaderData(byte[] data, List<Tlgr> headerList) throws Exception {

		String delimiterData = new String(data, "UTF-8");

		Integer headerCount = getIoCount(headerList);

		// 헤더 파싱 시작
		LinkedHashMap<String, Object> headerData = new LinkedHashMap<>();
		String delimiterHeader = delimiterData.substring(0, headerCount);

		int headerSeq = 0;
		for (Tlgr header : headerList) {
			int fieldLength = Integer.parseInt(header.getAllDataLenCon());

			headerData.put(header.getEnsnFldNm(), delimiterHeader.substring(headerSeq, headerSeq + fieldLength));
			headerSeq = headerSeq + fieldLength;
		}

		return headerData;
	}

	/**
	 * delimiter 전문 byte -> String 변환 후 바디 파싱
	 * @param delimiterValue
	 * @param data
	 * @param headerList
	 * @param bodyList
	 * @return
	 */
	public static LinkedHashMap<String, Object> delimiterToBodyData(String delimiterValue, byte[] data, List<Tlgr> headerList, List<Tlgr> bodyList) throws Exception {

		String delimiterData = new String(data, "UTF-8");
		
		// @EOF 종료부 제거
		delimiterData = delimiterData.replaceAll("@EOF", "");

		Integer headerCount = getIoCount(headerList);

		// 바디 파싱 시작
		LinkedHashMap<String, Object> bodyData = new LinkedHashMap<>();
		String[] delimiterBodyList = StringUtils.delimitedListToStringArray(delimiterData.substring(headerCount), delimiterValue);

		int bodySeq = 0;
		for (Tlgr body : bodyList) {
			bodyData.put(body.getEnsnFldNm(), delimiterBodyList[bodySeq]);
			bodySeq++;
		}
		
		return bodyData;
	}

	public static Integer getIoCount(List<Tlgr> fieldList) {
		
		Integer result = null;

		result = fieldList.stream().mapToInt(p -> Integer.parseInt(p.getAllDataLenCon())).sum();

		return result;
	}
}
