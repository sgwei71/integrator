/**
 * 
 */
package com.ibkglobal.message.common.normal;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonValue;
import com.ibkglobal.message.common.normal.copt.SttlAmgcCopt;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt;
import com.ibkglobal.message.common.normal.copt.SttlNfchCopt;
import com.ibkglobal.message.common.normal.copt.SttlOpatCopt;
import com.ibkglobal.message.common.normal.copt.SttlOtfxCopt;
import com.ibkglobal.message.common.normal.copt.SttlSvatCopt;
import com.ibkglobal.message.common.normal.copt.SttlSysCopt;
import com.ibkglobal.message.common.normal.copt.SttlTrnCopt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lee Hyungjoo
 * @date 2018. 5. 29.
 *
 */
@AllArgsConstructor
public enum DataSetCode {

  SYS_COPT   ("SC" , SttlSysCopt.class), // 시스템 공통부
  TRN_COPT   ("TC" , SttlTrnCopt.class), // 거래 공통부
  AMGC_COPT  ("MC", SttlAmgcCopt.class), // 대면채널공통부
  NFCH_COPT  ("NC", SttlNfchCopt.class), // 비대면채널공통부
  SVAT_COPT  ("SA", SttlSvatCopt.class), // 책임자승인공통부
  MSG_COPT   ("MS" , SttlMsgCopt.class), // 메시지공통부
  OPAT_COPT  ("OA", SttlOpatCopt.class), // 조작자승인공통부
  OTFX_COPT  ("OF", SttlOtfxCopt.class), // 출력양식공통부
  USER_DATA  ("IO", null), // UserData개별부
  END_SIGNAL ("@@", null), // END OF TELEGRAM
  NONE       ("  ", null);

  private @Getter(onMethod = @__({ @JsonValue })) String jsonValue;
  private @Getter Class<?>                               dataSetType;
  
  public static DataSetCode of(String code) {
	  try {		  
		  return Arrays.stream(values()).filter(v -> v.getJsonValue().equals(code)).findFirst().get();  
	  } catch (Exception e) {
		  return null;
	  }
  }
}
