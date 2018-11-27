package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;

import com.ibk.ibkglobal.data.com.ComCode;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class IoOnline implements Serializable {

	private String	serviceInId;	// Input 서비스 ID
	private String	serviceInOutId;	// Output 서비스 ID

	private ComCode	mtvCd;	// 동기 / 비동기 여부
	private ComCode	tcslInfoTrknCd;		// 비즈허브 접촉정보거래종류
//	private ComCode	inndTrnCd;	// Input 거래 코드 (사용안함)
//	private ComCode	otbnTrnCd;	// Output 거래 코드 (사용안함)

//	private Internal	internal;
	private IoExternal	external;
}
