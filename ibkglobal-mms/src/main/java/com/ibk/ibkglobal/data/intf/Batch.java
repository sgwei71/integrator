package com.ibk.ibkglobal.data.intf;

import java.io.Serializable;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Batch implements Serializable {

  private Db   db2db;
  private File file2file;
}
