package com.ibk.ibkglobal.data.io.fep;

import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IoExtCache implements IbkEhcache {

  private String inopId;
  private IoExt  io;
}
