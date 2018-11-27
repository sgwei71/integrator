package com.ibk.ibkglobal.data.intf;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Online implements Serializable {

  private Internal internal;
  private External external;
  
}
