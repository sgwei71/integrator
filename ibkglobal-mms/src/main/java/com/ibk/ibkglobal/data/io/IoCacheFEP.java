package com.ibk.ibkglobal.data.io;

import com.ibk.ibkglobal.data.io.model.IoCommon;

import java.io.Serializable;

import com.ibk.ibkglobal.data.io.model.Io;
import com.ibk.ibkglobal.data.io.model.ProcessType;
import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IoCacheFEP implements IbkEhcache, Serializable {

	private String utInopId; // 단위입출력 ID
	private String utInopIdnNm;  // 단위입출력 식별자
	
	private IoCommon		common;
	private ProcessType	processType;

	private Io data;
}
