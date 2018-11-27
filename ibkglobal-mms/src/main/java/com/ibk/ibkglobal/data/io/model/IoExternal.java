package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;

import com.ibk.ibkglobal.data.com.ComCode;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class IoExternal implements Serializable {

	private ComCode	otisCd;	// 대외 기관 구분 코드
	private ComCode	extBswr;	// 대외 업무 코드
	private String	extTlgrIttcdCon;	// 대외 전문 종별 코드
	private String	extTrnDcdCon;		// 대외 거래 구분 코드
	private String	extEtcDcdCon;	// 대외 기타 구분 코드

	private ComCode inaoCd; // 대내외 구분 코드 : 1 대내, 2 대외

	private String	rpcdCon;		// 대외 응답 코드(Header에 값)
	private String	trcKeyCon;		// 고유 TraceKey
	private String	excnCdCon;	// Work 코드(실행코드 내용)
	private String	errCdCon;		// Error 코드(에러코드 내용)

	private String anenVlCon; // Encoding(인코딩값 내용)
}
