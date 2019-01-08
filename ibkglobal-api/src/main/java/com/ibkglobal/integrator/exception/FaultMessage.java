package com.ibkglobal.integrator.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class FaultMessage {
	@JsonProperty("ORI_ERCD")
	String oriErcd;	//원오류코드(전문)
	@JsonProperty("MSG_CD")
	String msgCd;		//메시지코드(Kolumbus)
	@JsonProperty("MNMSG_CNTN")
	String mnmsgCntn;	//주메시지 내용(Kolumbus 또는 전문)
	@JsonProperty("ERR_LOCAT_CNTN")
	String errLocatCntn;	//오류위치(전문)
	@JsonProperty("AND_MSG_CD")
	String andMsgCd;		//부가메시지코드(전문)
	@JsonProperty("AND_MSG_CNTN")
	String andMsgCntn;	//부가세시지내용(전문)

}
