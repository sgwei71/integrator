package com.ibkglobal.common.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.ALWAYS)
public class LogFEP extends LogData {
	
	private String           seq;              // 거래구분(1, 2, 3, 4...)
	private String           bizCd;            // 업무코드
	private String           orgCd;            // 기관코드
	private String           msgCd;            // 전문종별코드
	private String           txCd;             // 거래구분코드
	private String           etcCd;            // 기타구분코드
	private String           trxNo;            // 전문거래고유번호	
	private String           adtUid;           // 고유 TRASACTION CODE
	private String           respCd;           // 응답코드	
	private String           workFlow;         // 업무 흐름
	private String           workCd;           // 거래구분(변환 된 헤더 아이디 넣는거 같음)	
	private String           transTime;        // 거래시간
	private String           oriRecvTime;      // 어댑터에서 처음 수신받은 시간
	private String           msgState;         // 전문상태(S - 정상, E - 에러)
	private String           errorContent;     // 에러내용	
	private String           serverName;       // 현재 로그남기는 서버 명명
	private String           tmsgSubkey;       // 조회용 SUBKEY ( RFFB - 업체번호)	
	
	private String           htdsp;            // 당타발구분(1 - 당발, 2 - 타발)
	private String           srflag;           // 송수신구분(H - 송신, E - 수신)
	
	// 표준전문이 있을 때만 값을 넣음
	private String           sttlYn;           // 표준전문여부
	private String           extTrnUnqId;      // 거래고유번호(bizkey)
	private String           globalId;         // 글로벌 ID
	private String           intfId;           // 인터페이스 ID
	private String           rspnPcrsDcd;      // 응답처리결과구분코드
	private String           otptTmgtDcd;      // 출력전문유형구분코드
	
	// 전문
	private Object           msg;              // 전문 데이터
}