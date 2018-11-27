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
public class Db implements Serializable {

  private String  dbSvcNm;  // DB 서비스이름
  private String  sql;      // DB SQL
  private ComCode dataExta; // DB적용방식
  private String  tableNm;  // 테이블 이름

}
