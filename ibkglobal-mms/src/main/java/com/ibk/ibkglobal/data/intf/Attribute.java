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
public class Attribute implements Serializable {

  private ComCode infra;       // 인터페이스기본.연계구분
  private ComCode process;     // 인터페이스기본.인터페이스처리유형
  private ComCode mpngYn;      // 인터페이스기본.매핑여부
  private ComCode ocrnCylCd;  // 인터페이스기본.발생주기
  private Online online;       // 인터페이스기본.온라인
  
}
