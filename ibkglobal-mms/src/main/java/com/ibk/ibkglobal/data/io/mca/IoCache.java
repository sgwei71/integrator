package com.ibk.ibkglobal.data.io.mca;

import java.io.Serializable;

import com.ibk.ibkglobal.data.io.model.Io;
import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IoCache implements IbkEhcache, Serializable {

  private String inopId;              // 입출력 ID
  
  private Io inBound;                 // 인바운드 
  private Io outBound;                // 아웃바운드
}
