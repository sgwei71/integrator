package com.ibk.ibkglobal.data.io;

import java.io.Serializable;

import com.ibk.ibkglobal.data.io.model.IoCommon;
import com.ibk.ibkglobal.data.io.model.Message;
import com.ibk.ibkglobal.data.io.model.ProcessType;
import com.ibk.ibkglobal.data.vo.IbkEhcache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCacheFEP implements IbkEhcache, Serializable {

	private String inopId;       // 입출력아이디
	private String inopIdnNm;    // 입출력 식별자명
	private String inopVrsnVl;   // 입출력 버전
	private String inopNm;         // 입출력 이름
	
	private IoCommon		common;      // 공통부분
	private ProcessType	processType;
	
	private Message data;
}
