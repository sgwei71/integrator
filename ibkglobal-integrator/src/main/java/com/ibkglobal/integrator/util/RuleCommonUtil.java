package com.ibkglobal.integrator.util;

import org.springframework.util.StringUtils;

public class RuleCommonUtil {
	
	final static int ruleLength = 4;
	
	/**
	 * 업무, 기관 등에 대한 고정 길이
	 * @param data
	 * @return
	 */
	public static String checkFixName(String data) {
		
		if (StringUtils.isEmpty(data) || (data.length() > ruleLength)) {
		} else {
			int value = ruleLength - data.length();
			
			for (int n = 0; n < value; n++) {
				data += "0";
			}
		}
		
		return data;
	}
}
