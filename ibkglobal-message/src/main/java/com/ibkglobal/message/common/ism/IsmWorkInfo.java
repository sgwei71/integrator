package com.ibkglobal.message.common.ism;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class IsmWorkInfo implements Serializable {
	
	private String interfaceId;          // 배치 인터페이스ID
	private String sendFileName;         // 송신 파일명
	private String recvFileName;         // 수신 파일명
	private String queryParam;           // DB 쿼리 파라미터
	private String fileParam;            // 파일명 파라미터
	private IsmWorkResult ismWorkResult; // 배치 결과 데이터
	
	@Data
	@AllArgsConstructor
	public static class IsmWorkResult {
		
		private boolean result;
		private String  error;
	}
}
