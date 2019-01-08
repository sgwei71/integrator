package com.ibkglobal.common.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.ALWAYS)
public class LogError {
	
	private String           seq;              // 거래구분(0 오류)
	private String           transTime;        // 거래시간
	private String           serverName;       // 서버명
	private String           errorCd;          // 에러코드
	private String           errorContent;     // 에러내용
	private Object           msg;              // 전문 데이터
	
}