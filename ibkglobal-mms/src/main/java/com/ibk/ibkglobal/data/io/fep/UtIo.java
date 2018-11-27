package com.ibk.ibkglobal.data.io.fep;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtIo implements Serializable {

  private String utInopId;          // 단위입출력ID
  private String utInopIdnNm;       // 단위입출력식별자명
  private String utInopDescCon;     // 단위입출력설명내용
  
  private List<Tlgr> fieldList;     // 전문 리스트
}
