package com.ibk.ibkglobal.data.io.fep;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class External implements Serializable {
  
  private String system;                     // 입출력기본이력.대외기관코드
  private String work;                       // 입출력기본이력.대외업무코드
  private String extTlgrIttcdCon;            // 대외전문종별코드내용
  private String extTrnDcdCon;               // 대외구분거래코드
  private String inaoCd;                     // 대내외코드
  private String extEtcDcdCon;               // 대외기타구분코드
}
