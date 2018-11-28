package com.ibkglobal.message.common.normal.copt;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibkglobal.common.validator.sttl.SttlField;
import com.ibkglobal.message.common.normal.AbstractFlexibleDataSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 표준 전문 :: 대면채널 공통부 Model
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
public class SttlAmgcCopt extends AbstractFlexibleDataSet implements Serializable {

	@SttlField(fieldName = "TRMN_INLT_BRCD", length = 4, defaultValue = "")
	private String trmnInltBrcd; // 단말설치부점코드

	@SttlField(fieldName = "TMN", length = 3, defaultValue = "")
	private String tmn; // 단말번호

	@SttlField(fieldName = "IDCR_SCAN_SRN", length = 2, defaultValue = "")
	private String idcrScanSrn; // 신분증스캔일련번호

	//2018.11.27
	@SttlField(fieldName = "INGN_SQN_MCTL_SRN", length = 4, defaultValue = "0")
	private Integer ingnSqnMctlSrn; // 지능형순번기기일련번호

	@SttlField(fieldName = "TLN", length = 8, defaultValue = "CHFEP000")
	private String tln; // 텔러번호

	@SttlField(fieldName = "ACIT_RNL_MODE_DCD", length = 1, defaultValue = "1")
	private String acitRnlModeDcd; // 계정갱신모드구분코드

	@SttlField(fieldName = "BACL_DCD", length = 1, defaultValue = "1")
	private String baclDcd; // 마감전후구분코드

	@SttlField(fieldName = "RCKN_YMD", length = 8, defaultValue = "")
	private String rcknYmd; // 기산년월일

	@SttlField(fieldName = "YNBK_DCD", length = 1, defaultValue = "2")
	private String ynbkDcd; // 유통무통구분코드

	@SttlField(fieldName = "CAAL_DCD", length = 1, defaultValue = "2")
	private String caalDcd; // 현금대체구분코드

	@SttlField(fieldName = "PRUS_SVC_ID", length = 12, defaultValue = "")
	private String prusSvcId; // 출력용서비스ID

	@SttlField(fieldName = "PRUS_INTF_ID", length = 12, defaultValue = "")
	private String prusIntfId; // 출력용인터페이스ID

	@SttlField(fieldName = "BNKB_SRN", length = 5, defaultValue = "0")
	private Integer bnkbSrn; // 통장일련번호

	@SttlField(fieldName = "SMAT_INQ_SRN", length = 2, defaultValue = "0")
	private Integer smatInqSrn; // 스마트조회일련번호

	@SttlField(fieldName = "IC_CHIP_MDIA_KIND_DCD", length = 1, defaultValue = "")
	private String icChipMdiaKindDcd; // IC칩매체종류구분코드

	@SttlField(fieldName = "BRCD", length = 4, defaultValue = "9301")
	private String brcd; // 부점코드

	@SttlField(fieldName = "OPTO_EMN", length = 6, defaultValue = "")
	private String optoEmn; // 조작자직원번호

	@SttlField(fieldName = "SVAT_DCD", length = 1, defaultValue = "")
	private String svatDcd; // 책임자승인구분코드

	@SttlField(fieldName = "OPAT_DCD", length = 1, defaultValue = "")
	private String opatDcd; // 조작자승인구분코드

	@SttlField(fieldName = "TRMG_ATHZ_DCD", length = 1, defaultValue = "")
	private String trmgAthzDcd; // 전달메시지승인구분코드

	@SttlField(fieldName = "WON_CASH_BAL", length = 15, defaultValue = "")
	private BigDecimal wonCashBal; // 원화현금잔액

	@SttlField(fieldName = "OBRC_AMT", length = 15, defaultValue = "")
	private BigDecimal obrcAmt; // 타점수납금액

	@SttlField(fieldName = "WON_ALTR_DFAM_AMT", length = 15, defaultValue = "")
	private BigDecimal wonAltrDfamAmt; // 원화대체차액금액

	@SttlField(fieldName = "FRCTF_DSCR_YN", length = 1, defaultValue = "")
	private String frctfDscrYn; // 외화대체불일치여부
}
