package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;

import com.ibk.ibkglobal.data.com.ComCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IoCommon implements Serializable {

  private String inopDescCon;       //입출력기본.입출력설명내용
  private ComCode system;           //입출력기본.시스템코드
  private ComCode bswr;             //입출력기본.업무코드
  private String wrtnTs;            //입출력기본.작성일시
  private String mkrEmpNo;            //입출력기본.작성자직원번호
  
}
