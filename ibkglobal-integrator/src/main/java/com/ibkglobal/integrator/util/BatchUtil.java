package com.ibkglobal.integrator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class BatchUtil {
	
	/**
	 * Batch 파일명 변경
	 * @param data
	 * @return
	 */
	public static String batchConvert(String data) {
		
		if (!StringUtils.isEmpty(data)) {			
			data = batchParamTransform("#yyyyMMdd#", data);
			data = batchParamTransform("#MMdd#", data);
			data = batchParamTransform("#dd#", data);
			data = batchParamTransform("#MM#", data);
		}
		
		return data;
	}
	
	/**
	 * Batch 파일명 변환
	 * @param format
	 * @param data
	 * @return
	 */
	public static String batchParamTransform(String format, String data) {
		switch (format) {
		case "#yyyyMMdd#" :
			SimpleDateFormat sdfymd = new SimpleDateFormat("yyyyMMdd");
			data = data.replaceAll(format, sdfymd.format(new Date()));
			break;
		case "#MMdd#" :
			SimpleDateFormat sdfmd = new SimpleDateFormat("MMdd");
			data = data.replaceAll(format, sdfmd.format(new Date()));
			break;
		case "#dd#" :
			SimpleDateFormat sdfd = new SimpleDateFormat("dd");
			data = data.replaceAll(format, sdfd.format(new Date()));
			break;
		case "#MM#" :
			SimpleDateFormat sdfm = new SimpleDateFormat("MM");
			data = data.replaceAll(format, sdfm.format(new Date()));
			break;
		default :
			break;
		}
		
		return data;
	}
}
