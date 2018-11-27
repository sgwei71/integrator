package com.ibk.ibkglobal.data.mapp;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TlgrOrder implements Serializable{

  private String fldNm;
  private String fldPara;
  private String lvlNo;
}
