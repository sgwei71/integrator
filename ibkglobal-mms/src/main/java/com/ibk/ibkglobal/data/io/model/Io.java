package com.ibk.ibkglobal.data.io.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Io implements Serializable {

	private String	utInopId;		// 단위입출력 ID
	private String	utInopNm;		// 단위입출력명
	private String	utInopDescCon;	// 단위입출력설명내용
	private String  utInopIdnNm;     // 단위입출력 식별자명
	private List<Tlgr> fieldList; // 전문 리스트

}
