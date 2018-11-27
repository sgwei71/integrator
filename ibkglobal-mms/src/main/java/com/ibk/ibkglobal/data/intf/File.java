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
public class File implements Serializable {

  private String  path;           // 파일경로
  private String  rule;           // 파일명 규칙내용
  private ComCode flprMthd;       // 파일처리방식
  private String  rsltFileTrnfYn; // 결과파일전달여부
  private String  rsltFpnmCon;    // 결과파일경로명
  private String  rsltFlnmCon;    // 결과파일명내용

}
