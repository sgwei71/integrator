package com.ibk.ibkglobal.data.intf;

import java.io.Serializable;

import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntfKey implements IbkEhcache,Serializable {

  private String intfId;
  
}
