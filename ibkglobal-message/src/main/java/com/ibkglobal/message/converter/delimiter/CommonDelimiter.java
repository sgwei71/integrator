package com.ibkglobal.message.converter.delimiter;

import java.io.File;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.ibkglobal.common.convert.Converter;

import lombok.Getter;

@SuppressWarnings("unchecked")
public class CommonDelimiter {
	
	@Getter
	private static List<DelimiterHeader> headerDelimiter;
	
	@Getter
	private static String ENDECODING_TYPE = "UTF-8";
	
	static {
		try {
			File file = new File(ResourceUtils.getURL("classpath:delimiterHeader.json").getFile());
			headerDelimiter = Converter.mapper.readValue(file, List.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
