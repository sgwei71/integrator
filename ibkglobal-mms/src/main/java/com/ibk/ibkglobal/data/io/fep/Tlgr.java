package com.ibk.ibkglobal.data.io.fep;

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

  private String tlgrSqc;         // 전문순서
  private String lvlNo;           // 레벨번호
  private String fldNm;           // 필드명
  private String ensnFldNm;       // 영문필드명
  private String tlgrDescCon;     // 전문설명내용
  private String dataTypeNm;      // 데이터타입
  private String allDataLenCon;   // 전체데이터길이내용
  private String mdinYn;          // 필수입력여부
  private String vrfcYn;            // 검증여부
  
  private List<Tlgr> fieldList;   // Array 일때 하위값
}
