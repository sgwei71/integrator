package com.ibkglobal.message.common.normal.copt;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibkglobal.common.validator.sttl.Reply;
import com.ibkglobal.common.validator.sttl.Request;
import com.ibkglobal.common.validator.sttl.SttlField;

import lombok.Data;

/**
 * 표준 전문 :: 거래 공통부 Model
 * 
 * @author Lee Hyungjoo
 * @date 2018. 5. 29.
 *
 */
@Data
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SttlTrnCopt implements Serializable {

//	@SttlField(fieldName = "NTCD", length = 2, groups = {Request.class, Reply.class}, defaultValue = "")
//	private String ntcd; // 국가코드
//
//	@SttlField(fieldName = "BNCD", length = 2, groups = {Request.class, Reply.class}, defaultValue = "")
//	private String bncd; // 은행코드

	@SttlField(fieldName = "TRN_CHNL_DCD", length = 3, groups = {Request.class, Reply.class}, defaultValue = "OLT")
	private String trnChnlDcd; // 거래채널구분코드

	@SttlField(fieldName = "TRRQ_BSWR_DCD", length = 3, groups = {Request.class, Reply.class}, defaultValue = "")
	private String trrqBswrDcd; // 거래요청업무구분코드

	@SttlField(fieldName = "STTL_XCD", length = 15, groups = {Request.class, Reply.class}, defaultValue = "")
	private String sttlXcd; // 표준전문거래코드

	@SttlField(fieldName = "STTL_RQST_FUNC_DSNC_ID", length = 3, groups = {Request.class, Reply.class}, defaultValue = "")
	private String sttlRqstFuncDsncId; // 표준전문요청기능구분ID

	@SttlField(fieldName = "STTL_RSPN_FUNC_DSNC_ID", length = 3, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "")
	private String sttlRspnFuncDsncId; // 표준전문응답기능구분ID

	@SttlField(fieldName = "STTL_EROR_FUNC_DSNC_ID", length = 3, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "")
	private String sttlErorFuncDsncId; // 표준전문오류기능구분ID

	@SttlField(fieldName = "INPT_SCRE_NO", length = 9, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "")
	private String inptScreNo; // 입력화면번호

	@SttlField(fieldName = "AHD_IQTR_YN", length = 1, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "")
	private String ahdIqtrYn; // 선조회거래여부

	@SttlField(fieldName = "CNCL_DCD", length = 1, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "1")
	private String cnclDcd; // 취소구분코드

	@SttlField(fieldName = "CTNT_TRN_DCD", length = 1, groups = {Request.class, Reply.class}, defaultValue = "0")
	private Integer ctntTrnDcd; // 연속거래구분코드

	@SttlField(fieldName = "INPT_TRN_SRN", length = 10, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "0000000000")
	private String inptTrnSrn; // 입력거래일련번호

	@SttlField(fieldName = "BLNG_FNCM_CD", length = 3, groups = {Request.class, Reply.class}, defaultValue = "")
	private String blngFncmCd; // 소속금융회사코드

	//2018.11.27 추가 
	@SttlField(fieldName = "TCSL_INFO_TRN_KCD", length = 3, groups = {Request.class, Reply.class}, defaultValue = "0")
	private Integer tcslInfoTrnKcd; // 접촉정보거래종류코드

	@SttlField(fieldName = "EXT_TRN_BSWR_DCD", length = 4, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "")
	private String extTrnBswrDcd; // 대외거래업무구분코드

	@SttlField(fieldName = "EXT_TRN_INTT_DCD", length = 4, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "1")
	private String extTrnInttDcd; // 대외거래기관구분코드

	@SttlField(fieldName = "EXT_TRN_UNQ_ID", length = 48, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "1")
	private String extTrnUnqId; // 대외거래고유ID

	@SttlField(fieldName = "EXT_LNK_INTF_ID", length = 51, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "1")
	private String extLnkIntfId; // 대외연계인터페이스ID

	@SttlField(fieldName = "EXT_TRN_SSN_ID", length = 30, groups = {Request.class, Reply.class}, valueCheck = false, defaultValue = "1")
	private String extTrnSsnId; // 대외거래세션ID

	@SttlField(fieldName = "EXT_TRN_RUTN_ID", length = 5, defaultValue = "1")
	private String extTrnRutnId; // 대외거래라우팅ID

	@SttlField(fieldName = "TRN_COPT_FLOP", length = 50, defaultValue = "1")
	private String trnCoptFlop; // 거래공통부예비필드
}
