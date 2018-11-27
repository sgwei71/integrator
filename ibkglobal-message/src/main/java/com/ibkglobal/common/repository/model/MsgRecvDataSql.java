package com.ibkglobal.common.repository.model;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class MsgRecvDataSql implements Serializable {
	
	private String srlNo;
	private String bswrCd;
	private String inttCd;
	private String tlgrClscCd;
	private String trnDcd;
	private String etcCd;
	private String hgrnTlgrClscCd;
	private String hgrnTrnDcd;
	private String hgrnEtcCd;
	private String mpngFldIdxNo;
	private String mpngFldNmCon;
	private String rcdaCon;
	private String useYn;
	private String tlgrDesc;
	private String lsedId;
	private String lsamTs;
	private String fnctAplyFldCon;
}
