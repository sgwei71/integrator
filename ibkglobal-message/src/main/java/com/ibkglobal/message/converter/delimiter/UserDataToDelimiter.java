package com.ibkglobal.message.converter.delimiter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibk.ibkglobal.data.io.model.Tlgr;

public class UserDataToDelimiter {

	/**
	 * headerData -> delimiter HeaderData(Schema O)
	 * @param userData
	 * @param headerList
	 * @return
	 */
	public static String headerDataToDelimiter(LinkedHashMap<String, Object> userData, List<Tlgr> headerList) {

		String result = "";

		for (Tlgr header : headerList) {
			result += (String) userData.get(header.getEnsnFldNm());
		}

		return result;
	}
	
	/**
	 * headerData -> delimiter HeaderData(Schema X)
	 * @param headerData
	 * @return
	 */
	public static String headerDataToDelimiter(LinkedHashMap<String, Object> headerData) {

		String result = "";

		for (Map.Entry<String, Object> header : headerData.entrySet()) {
			result += (String) header.getValue();
		}

		return result;
	}

	/**
	 * bodyData -> delimiter BodyData(Schema O)
	 * @param delimiterValue
	 * @param userData
	 * @param bodyList
	 * @return
	 */
	public static String bodyDataToDelimiter(String delimiterValue, LinkedHashMap<String, Object> userData, List<Tlgr> bodyList) {

		String result = "";
		
		int bodyCount = 1;
		int bodySize  = bodyList.size();
		
		// 바디 변환
		for (Tlgr body : bodyList) {
			result += (String) userData.get(body.getEnsnFldNm());
			if (bodyCount < bodySize) {
				result += delimiterValue;
			}
			
			bodyCount++;
		}

		return result;
	}
	
	/**
	 * bodyData -> delimiter BodyData(Schema X)
	 * @param delimiterValue
	 * @param bodyData
	 * @return
	 */
	public static String bodyDataToDelimiter(String delimiterValue, LinkedHashMap<String, Object> bodyData) {
		
		String result = "";
		
		int bodyCount = 1;
		int bodySize  = bodyData.size();
		
		for (Map.Entry<String, Object> body : bodyData.entrySet()) {
			result += (String) body.getValue();
			if (bodyCount < bodySize) {
				result += delimiterValue;
			}
			
			bodyCount++;
		}
		
		return result;
	}

	/**
	 * 스키마 있을 경우
	 * @param delimiterValue
	 * @param userData
	 * @param headerList
	 * @param bodyList
	 * @return
	 */
	public static byte[] userDataToDelimiter(String delimiterValue, LinkedHashMap<String, Object> userData, List<Tlgr> headerList, List<Tlgr> bodyList) throws Exception {
		
		String result = "";
		
		String headerResult = headerDataToDelimiter(userData, headerList);
		String bodyResult   = bodyDataToDelimiter(delimiterValue, userData, bodyList);
		
		result = headerResult + bodyResult;

		return result.getBytes("UTF-8");
	}
	
	/**
	 * 스키마 없을 경우
	 * @param delimiterValue
	 * @param headerData
	 * @param bodyData
	 * @return
	 */
	public static byte[] userDataToDelimiter(String delimiterValue, LinkedHashMap<String, Object> headerData, LinkedHashMap<String, Object> bodyData) throws Exception {
		
		String result = "";
		
		String headerResult = headerDataToDelimiter(headerData);
		String bodyResult   = bodyDataToDelimiter(delimiterValue, bodyData);
		
		result = headerResult + bodyResult + "@EOF";
		
		return result.getBytes("UTF-8");
	}
}
