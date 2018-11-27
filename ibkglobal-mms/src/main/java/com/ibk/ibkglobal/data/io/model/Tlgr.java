package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tlgr implements Serializable {

  private Integer tlgrSqc;           // 전문순서
  private String  lvlNo;             // 레벨번호
  private String  fldNm;             // 필드명
  private String  ensnFldNm;         // 영문필드명
  private String  tlgrDescCon;       // 전문설명내용
  private String  dataTypeNm;        // 데이터타입
  private String  allDataLenCon;     // 전체데이터길이내용
  private String  inptMndrYn;        // 필수입력여부
  private String  vrfcYn;            // 검증여부
  private Integer dcfrDataLen;       // 소수데이터길이
  private String  dfvlCon;           // 기본값내용
  private String  vldtInspYn;        // 유효성검사여부
  private String  encpYn;            // 암호화여부
  private String  trcKeyUseYn;       // TRACEKEY사용여부
  private String  msgKeyUseYn;       // 메시지KEY사용여부
  private String  extTlgrIttcdUseYn; // 대외전문종별코드사용여부
  private String  trnDcdUseYn;       // 거래구분코드사용여부
  private String  extEtcDcdUseYn;    // 대외기타구분코드사용여부
  private String  rpcdUseYn;         // 응답코드사용여부
  private String  vrbnYn;            // 가변여부
  private String  rvsnLenCon;        // 보정길이내용
  private String  mskgUseYn;         // 마스킹사용여부
  private String  logKeyUseYn;       // LOGKEY사용여부
  private String  cmpbCon;           // 정합성내용
  private String  ebccdDIcCdUseYn;   // EBCDIC코드사용여부
  private String  dbCsUseYn;         // DBCS사용여부

  private List<Tlgr> fieldList; // Array 일때 하위값

}
