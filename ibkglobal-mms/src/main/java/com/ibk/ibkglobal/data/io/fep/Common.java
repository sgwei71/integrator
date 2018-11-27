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
public class Common implements Serializable {

  private String description;
  private ComCode system;
  private ComCode bswr;
  private String wrtnTs;
  private String mkrEmn;
  
}
