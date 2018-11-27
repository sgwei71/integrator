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
public class InterfaceIo implements Serializable {

  private String  inopId;    // IO ID
  private String  inopIdnNm; // IO 식별자명
  private ComCode system;    // 시스템
  private ComCode bswr;      // 업무
  private String  mkrEmpNo;  // 작성자
  private String  inndSvcId; // 인바운드 서비스 ID
  private String  otbnSvcId; // 아웃바운드 서비스 ID
  private ComCode tlgrFmt;   // 전문포맷

  private EaiType processType; // eai
}
