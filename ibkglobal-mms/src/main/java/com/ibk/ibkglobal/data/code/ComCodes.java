package com.ibk.ibkglobal.data.code;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCodes {

  private String                    COM_CD;
  private String                    COM_CD_NM;
  private Map<String, List<String>> COM_INST_CD;

  public static void main(String args[]) {
	  System.out.println("afafafaf");
  }
  
}
