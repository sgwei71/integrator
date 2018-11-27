package com.ibk.ibkglobal.data.intf;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Method implements Serializable {

  private String content;
  private String code;
  private String name;

}
