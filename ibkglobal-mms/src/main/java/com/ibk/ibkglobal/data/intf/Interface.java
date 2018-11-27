package com.ibk.ibkglobal.data.intf;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Interface implements IbkEhcache, Serializable{
 
  private String intfId;               // 인터페이스ID
  private BigDecimal intfVrsnVl;       // 인터페이스버전값 
  private String intfIdnNm;            // 인터페이스식별자명
  private String intfNm;               // 인터페이스명
  
  private Common common;               // 인터페이스 공통부분
  private InterfaceType interfaceType; // 인터페이스 타입
  
  private String etcCon;               // 기타내용
  
}
