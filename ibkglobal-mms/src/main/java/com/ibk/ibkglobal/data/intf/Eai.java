package com.ibk.ibkglobal.data.intf;

import java.io.Serializable;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Eai implements Serializable {

  private String   ctifCd;
  private Batch    batch;
  private Deferred deferred;

}
