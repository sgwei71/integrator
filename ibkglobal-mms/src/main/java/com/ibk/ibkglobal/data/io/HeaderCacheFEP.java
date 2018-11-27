package com.ibk.ibkglobal.data.io;

import java.io.Serializable;

import com.ibk.ibkglobal.data.io.model.Header;
import com.ibk.ibkglobal.data.io.model.IoCommon;
import com.ibk.ibkglobal.data.io.model.ProcessType;
import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderCacheFEP implements IbkEhcache, Serializable {

	private String utInopId;       // 단위입출력 ID
	private String utInopIdnNm;    // 단위입출력식별자명   
	private String utInopVrsnVl;   // 단위입출력버전값
	private String utInopNm;       // 단위입출력명
	
	private IoCommon		common;
	private ProcessType	processType;
	
	private Header data;
}
