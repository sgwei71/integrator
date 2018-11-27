package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;
import java.util.List;

import com.ibk.ibkglobal.data.com.ComCode;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Header implements Serializable {

	private String	trcKeyCon;           // TRACEKEY내용
	private String  extTrnDcdCon;        // 대외거래구분코드내용
	private String  extTlgrIttcdCon;       // 대외전문종별코드내용
	private String	msgKey;              // 대외거래구분코드_TRACEKEY값

	private ComCode	extBswr;             // 대외업무코드
	private String  workCode;            // idn(0,11)+전문종별코드+'_'+대외 거래구분코드
	private String	errCdCon;             // 에러코드내용
	
//	private String	msgLen;
//	private String	logKey;           
//	private String	routeKey;

	private List<IoElement> sequence;
}
