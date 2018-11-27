package com.ibkglobal.common.validator.model;

import java.util.List;

import lombok.Data;

@Data
public class ValidResult {

  private boolean      result;   // Valid 결과
  private List<String> errorList;// 에러 목록
}
