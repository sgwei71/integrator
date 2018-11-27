package com.ibk.ibkglobal.data.com;

import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Com implements IbkEhcache {

  private String cmcd;                  // 공통코드.공통코드
  private String comDecd;               // 공통코드.공통상세코드
  private String comDecdNm;             // 공통코드.공통상세코드명
  private String comDecdDescCon;        // 공통코드.공통상세코드설명내용 
  private String comDecdSqc;            // 공통코드.공통상세코드순서
  private String comDecdUseYn;          // 공통코드.공통상세코드사용여부
  private String sysLsmdEmn;            // 공통코드.시스템최종변경직원번호
  private String sysLsmdTsp;            // 공통코드.시스템최종변경타임스탬프
}
