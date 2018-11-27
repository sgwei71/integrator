package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;

import com.ibk.ibkglobal.data.com.ComCode;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Internal implements Serializable {

	private String serviceId;   // 서비스 ID
	private ComCode synAsyn;    // 동기/비동기 코드
}
