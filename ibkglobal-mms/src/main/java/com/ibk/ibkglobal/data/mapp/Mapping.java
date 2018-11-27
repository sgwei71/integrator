package com.ibk.ibkglobal.data.mapp;

import java.io.Serializable;
import java.util.List;

import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mapping implements IbkEhcache, Serializable {

  private String intfId;                          // 인터페이스 ID
  private String intfIdnNm;                       // 인터페이스 식별자명
  private String inndOtbnCd;                      // 인바운드아웃바운드 코드
  private String sorcInopId;                      // 소스 IO ID
  private String sorcInopNm;                   // 소스 IO 식별자명
  private String tgtInopId;                      // 타겟 IO ID
  private String tgtInopNm;                    // 타겟 IO 식별자명
  
  private List<TlgrMapping> mappingList;          // 매핑 리스트
}
