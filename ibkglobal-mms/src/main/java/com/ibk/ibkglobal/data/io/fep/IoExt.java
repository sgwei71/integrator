package com.ibk.ibkglobal.data.io.fep;

import lombok.NoArgsConstructor;

import java.io.Serializable;

import lombok.AllArgsConstructor;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IoExt implements Serializable {

  private String inopId;              // IO 아이디
  private Common common;              // IO 공통부분  
  private Process process;            // IO 프로세스
  
  
}
