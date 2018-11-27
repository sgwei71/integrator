package com.ibk.ibkglobal.data.io.fep;

import java.io.Serializable;

import com.ibk.ibkglobal.data.com.ComCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Online implements Serializable {

  private String inndUtInopId;          // 입출력기본이력.인바운드단위입출력ID
  private String otbnUtInopId;          // 입출력기본이력.아웃바운드단위입출력ID
  private ComCode mtvCd;                // 입출력기본이력.동기코드
  private External external;            // 대외
}

