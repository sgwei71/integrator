package com.ibk.ibkglobal.data.mapp;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parameter implements Serializable {

  private String name;
  private String fldParaSqc;                // 필드 파라미터 순서
}
