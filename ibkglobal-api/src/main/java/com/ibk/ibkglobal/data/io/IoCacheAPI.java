package com.ibk.ibkglobal.data.io;

import java.io.Serializable;

import com.ibk.ibkglobal.data.io.model.IoCommon;
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
public class IoCacheAPI implements IbkEhcache, Serializable {

	private String inopId; // 입출력 ID

	private IoCommon		common;
	private ProcessType	processType;

	private Io	inBound;	// 인바운드
	private Io	outBound;	// 아웃바운드
	
}
