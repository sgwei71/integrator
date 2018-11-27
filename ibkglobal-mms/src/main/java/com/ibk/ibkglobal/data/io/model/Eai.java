package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Eai implements Serializable {

	private String	service;	// DB 서비스명
	private String	table;		// DB 테이블 명
}
