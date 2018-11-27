package com.ibk.ibkglobal.data.com;

import java.io.Serializable;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCode implements Serializable {

  private String code;
  private String name;
  
}
