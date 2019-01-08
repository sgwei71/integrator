package com.ibkglobal.common.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.ALWAYS)
public class RestLogModel{
	
	private String           seq;               // 거래구분(1, 2, 3, 4...)
	private String           sysCd;             // 시스템코드
	private String           bizCd;             // 업무코드
	private String           intfId;            // 인터페이스ID
	private String           transTime;         // 송수신시각
	private String           oriRecvTime;       // 어댑터에서 처음 수신받은 시간
	private String           globalId;          // 글로벌ID(34자리)
	private String           rqstRspnDcd;       // 요청응답구분코드
	private String           rspnPcrsDcd;       // 응답처리결과구분코드
	private String           otptTmgtDcd;       // 출력전문유형구분코드
	private String           workFlow;          // 업무 흐름
	private String           sttlYn;            // 표준전문여부
	private Object           msg;               // 전문 데이터
}