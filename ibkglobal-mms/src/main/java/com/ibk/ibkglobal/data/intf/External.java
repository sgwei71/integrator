package com.ibk.ibkglobal.data.intf;

import java.io.Serializable;

import com.ibk.ibkglobal.data.com.ComCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class External implements Serializable {

	  private ComCode otisCd;          // 인터페이스기본.대외기관코드내용
	  private ComCode extBswr;         // 인터페이스기본.대외업무코드
	  private ComCode htdspCd;         // SMR인터페이스대외온라인상세.당타발코드
	  private String  svcId;           // SMR인터페이스대외온라인상세.서비스ID
	  private String  extTlgrIttcdCon; // SMR인터페이스대외온라인상세.대외전문종별코드내용
	  private String  extTrnDcdCon;    // SMR인터페이스대외온라인상세.대외거래구분코드내용
	  private Method  extPrwaCd;       // SMR인터페이스대외온라인상세.대외처리방법코드
	  private ComCode safYn;           // SMR인터페이스대외온라인상세.SAF여부
	  private ComCode netrccHECKYn;    // SMR인터페이스대외온라인상세.NETCHECK여부
	  private ComCode sync;            // SMR인터페이스대외온라인상세.동기코드
	  private String  mopfVl;          // 인터페이스기본.타이머값
	  private String  hgrnIntfIdCon;   // SMR인터페이스대외온라인상세.상위인터페이스ID내용
	  private String  extEtcDcdCon;    // SMR인터페이스대외온라인상세.대외기타구분코드내용
	  private String  snrcCd;          // SMR인터페이스대외온라인상세.송수신코드
	  private String  onlTrnCd;        // SMR인터페이스대외온라인상세.온라인거래코드
  
}
