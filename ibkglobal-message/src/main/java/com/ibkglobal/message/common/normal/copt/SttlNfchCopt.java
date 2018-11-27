package com.ibkglobal.message.common.normal.copt;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibkglobal.common.validator.sttl.SttlField;
import com.ibkglobal.message.common.normal.AbstractFlexibleDataSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 표준 전문 :: 비대면채널 공통부 Model
 * 
 * @author Lee Hyungjoo
 * @date 2018. 5. 29.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SttlNfchCopt extends AbstractFlexibleDataSet implements Serializable {

	@SttlField(fieldName = "USR_IDNT_NO", length = 13, defaultValue = "")
	private String usrIdntNo; // 이용자식별번호

	@SttlField(fieldName = "LRRN_USR_NO", length = 13, defaultValue = "")
	private String lrrnUsrNo; // 하위이용자번호

	@SttlField(fieldName = "EBK_CUS_ID", length = 20, defaultValue = "")
	private String ebkCusId; // 전자금융고객ID

	@SttlField(fieldName = "USR_CCTN_MCTL_IP", length = 39, defaultValue = "")
	private String usrCctnMctlIp; // 이용자접속기기IP

	@SttlField(fieldName = "USR_CCTN_MAC_VL", length = 17, defaultValue = "")
	private String usrCctnMacVl; // 이용자접속MAC값

	@SttlField(fieldName = "USR_CCTN_TPN", length = 20, defaultValue = "")
	private String usrCctnTpn; // 이용자접속전화번호

	@SttlField(fieldName = "USR_CCTN_MCTL_ID", length = 20, defaultValue = "")
	private String usrCctnMctlId; // 이용자접속기기ID

	@SttlField(fieldName = "USR_ACNT_CDN", length = 16, defaultValue = "")
	private String usrAcntCdn; // 이용자계좌카드번호

	@SttlField(fieldName = "ATCT_CQRCG_NO", length = 25, defaultValue = "")
	private String atctCqrcgNo; // 공인인증서고유식별번호

	@SttlField(fieldName = "PWD_VRFC_DCD", length = 1, defaultValue = "")
	private String pwdVrfcDcd; // 비밀번호검증구분코드

	@SttlField(fieldName = "PWD_ENCP_YN", length = 1, defaultValue = "")
	private String pwdEncpYn; // 비밀번호암호화여부

	@SttlField(fieldName = "SECU_MDIA_DCD", length = 1, defaultValue = "")
	private String secuMdiaDcd; // 보안매체구분코드

	@SttlField(fieldName = "SECU_MDIA_NO", length = 12, defaultValue = "")
	private String secuMdiaNo; // 보안매체번호

	@SttlField(fieldName = "SCCD_SQN1", length = 3, defaultValue = "")
	private String sccdSqn1; // 보안카드순번1

	@SttlField(fieldName = "SCCD_PWD1", length = 4, defaultValue = "")
	private String sccdPwd1; // 보안카드비밀번호1

	@SttlField(fieldName = "SCCD_SQN2", length = 3, defaultValue = "")
	private String sccdSqn2; // 보안카드순번2

	@SttlField(fieldName = "SCCD_PWD2", length = 4, defaultValue = "")
	private String sccdPwd2; // 보안카드비밀번호2

	@SttlField(fieldName = "TRAN_PWD", length = 128, defaultValue = "")
	private String tranPwd; // 이체비밀번호

	@SttlField(fieldName = "ACNT_PWD", length = 128, defaultValue = "")
	private String acntPwd; // 계좌비밀번호

	@SttlField(fieldName = "OTP_VRFC_MSGCD", length = 12, defaultValue = "")
	private String otpVrfcMsgcd; // OTP검증메시지코드

	@SttlField(fieldName = "BRCD", length = 4, defaultValue = "")
	private String brcd; // 부점코드

	@SttlField(fieldName = "TLN", length = 8, defaultValue = "")
	private String tln; // 텔러번호

	@SttlField(fieldName = "SRVR_OTPT_RQST_NBI", length = 5, defaultValue = "")
	private Integer srvrOtptRqstNbi; // 서버출력요청건수

	@SttlField(fieldName = "TLGR_MSGCD", length = 7, defaultValue = "")
	private String tlgrMsgcd; // 전문메시지코드

	@SttlField(fieldName = "ENTP_DCD", length = 5, defaultValue = "")
	private String entpDcd; // 업체구분코드

	@SttlField(fieldName = "SRVR_DATA_DLPR_CON", length = 100, defaultValue = "")
	private String srvrDataDlprCon; // 서버데이터전달부내용

}
