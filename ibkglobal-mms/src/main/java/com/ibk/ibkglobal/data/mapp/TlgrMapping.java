package com.ibk.ibkglobal.data.mapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TlgrMapping implements Serializable {

  private String fnctNm;                // 함수코드 이름
  private List<TlgrOrder> sourceNm;              // 소스 이름
  private List<TlgrOrder> targetNm;              // 타겟 이름
  
  private String userParaCon;           // 사용자팔미터내용
  
  public TlgrMapping(TlgrMapping tlgrMapping) {
    super();
    this.fnctNm = tlgrMapping.getFnctNm();
    this.sourceNm = new ArrayList<TlgrOrder>(tlgrMapping.getSourceNm());
    this.targetNm = new ArrayList<TlgrOrder>(tlgrMapping.getTargetNm());
    this.userParaCon = tlgrMapping.getUserParaCon();
  }
  
}
