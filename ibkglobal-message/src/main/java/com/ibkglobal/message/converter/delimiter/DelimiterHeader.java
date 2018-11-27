package com.ibkglobal.message.converter.delimiter;

import lombok.Data;

@Data
public class DelimiterHeader {
	
	private Integer seq;       // 필드 순번
	private String  name;      // 필드 명
	private String  fieldName; // 필드 스키마명
	private String  type;      // 필드 타입
	private Integer length;    // 필드 길이
	private String  desc;      // 필드 세부설명
	private String  etc;       // 필드 비고
}
