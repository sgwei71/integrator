package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IoElement implements Serializable {

	private Integer	seq;		// 순서
	private String	name;		// header | entity
	private String	elementCd;	// HEADER | ENTITY
	private String	type;		// CDPA_COMM_ECommonHeader | CDPA_COMM_E0200_670000_ENT
}
