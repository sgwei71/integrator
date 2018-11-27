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
public class Common implements Serializable {

  private String intfDescCon;          // 인터페이스기본.인터페이스설명내용
  private ComCode system;              // 인터페이스기본.시스템
  private ComCode bswr;                // 인터페이스기본.업무
  private String wrtnTs;               // 인터페이스기본.작성일시
  private String mkrEmpNo;               // 인터페이스기본.작성자직원번호
  private Attribute attribute;         // 인터페이스 속성

}
