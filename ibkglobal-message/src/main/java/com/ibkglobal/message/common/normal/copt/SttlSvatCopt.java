package com.ibkglobal.message.common.normal.copt;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibkglobal.common.validator.sttl.SttlField;
import com.ibkglobal.common.validator.sttl.SttlFieldArray;
import com.ibkglobal.message.common.normal.AbstractFlexibleDataSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 표준 전문 :: 책임자승인 공통부 Model
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
public class SttlSvatCopt extends AbstractFlexibleDataSet implements Serializable {

	@SttlField(fieldName = "TMS1_ATHZ_EMN", length = 6, defaultValue = "")
	private String tms1AthzEmn; // 1차승인직원번호

	@SttlField(fieldName = "TMS2_ATHZ_EMN", length = 6, defaultValue = "")
	private String tms2AthzEmn; // 2차승인직원번호

	@SttlField(fieldName = "TMS1_ATHZ_SPRS_CLWR_AWL_CD", length = 6, defaultValue = "")
	private String tms1AthzSprsClwrAwlCd; // 1차승인책임자사무분장코드

	@SttlField(fieldName = "TMS2_ATHZ_SPRS_CLWR_AWL_CD", length = 6, defaultValue = "")
	private String tms2AthzSprsClwrAwlCd; // 2차승인책임자사무분장코드

	@SttlField(fieldName = "TMS1_MBAT_YN", length = 1, defaultValue = "")
	private String tms1MbatYn; // 1차모바일승인여부

	@SttlField(fieldName = "TMS2_MBAT_YN", length = 1, defaultValue = "")
	private String tms2MbatYn; // 2차모바일승인여부

	@SttlField(fieldName = "MBAT_SRN", length = 3, defaultValue = "")
	private Integer mbatSrn; // 모바일승인일련번호

	@SttlField(fieldName = "SVAT_RCD_ITRN_NBT", length = 2, defaultValue = "")
	private Integer svatRcdItrnNbt; // 책임자승인사유코드반복횟수

	@Valid
	@SttlFieldArray(lengthField = "svatRcdItrnNbt")
	private List<SvatRsnInfo> svatRsnInfo; // 책임자승인사유 반복부

	@SttlField(fieldName = "SVAT_MSG_ITRN_NBT", length = 2, defaultValue = "")
	private Integer svatMsgItrnNbt; // 책임자승인메시지반복횟수

	@Valid
	@SttlFieldArray(lengthField = "svatMsgItrnNbt")
	private List<SvatMsgInfo> svatMsgInfo; // 책임자승인주메시지 반복부

	@SttlField(fieldName = "SVAT_SB_MSG_ITRN_NBT", length = 2, defaultValue = "")
	private Integer svatSbMsgItrnNbt; // 책임자승인부메시지반복횟수

	@Valid
	@SttlFieldArray(lengthField = "svatSbMsgItrnNbt")
	private List<SvatSbMsgInfo> svatSbMsgInfo; // 책임자승인부메시지 반복부

	@SttlField(fieldName = "CCSP_INFO_ITRN_NBT", length = 3, defaultValue = "")
	private Integer ccspInfoItrnNbt; // 접속책임자정보반복횟수

	@Valid
	@SttlFieldArray(lengthField = "ccspInfoItrnNbt")
	private List<BrwtCcspInfo> brwtCcspInfo; // 부점내접속책임자 반복부

	@SttlField(fieldName = "MBAT_LNK_DATA_ITRN_NBT", length = 2, defaultValue = "")
	private Integer mbatLnkDataItrnNbt; // 모바일승인연계데이터반복횟수

	@Valid
	@SttlFieldArray(lengthField = "mbatLnkDataItrnNbt")
	private List<MbatLnkDataInfo> mbatLnkDataInfo; // 모바일승인연계데이터 반복부

	/**
	 * 책임자승인사유정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class SvatRsnInfo {

		@SttlField(fieldName = "SVAT_RCD", length = 6, defaultValue = "")
		private String svatRcd; // 책임자승인사유코드
		
		@SttlField(fieldName = "SVAT_PTRN_ID", length = 7, defaultValue = "")
		private String svatPtrnId; // 책임자승인사유유형ID
	}

	/**
	 * 책임자승인주메시지정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class SvatMsgInfo {

		@SttlField(fieldName = "SVAT_RCD_INKY_VL", length = 2, defaultValue = "")
		private Integer svatRcdInkyVl; // 책임자승인사유코드인덱스KEY값

		@SttlField(fieldName = "SVAT_PRCP_MSG_CON", length = 200, defaultValue = "")
		private String svatPrcpMsgCon; // 책임자승인주메시지내용
	}

	/**
	 * 책임자승인부메시지정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class SvatSbMsgInfo {

		@SttlField(fieldName = "SVAT_SB_RCD_INKY_VL", length = 2, defaultValue = "")
		private Integer svatSbRcdInkyVl; // 책임자승인부사유코드인덱스KEY값

		@SttlField(fieldName = "SVAT_SB_MSG_CON", length = 200, defaultValue = "")
		private String svatSbMsgCon; // 책임자승인부메시지내용
	}

	/**
	 * 부점내접속책임자정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class BrwtCcspInfo {

		@SttlField(fieldName = "CCSP_EMN", length = 6, defaultValue = "")
		private String ccspEmn; // 접속책임자직원번호

		@SttlField(fieldName = "CCSP_NM", length = 20, defaultValue = "")
		private String ccspNm; // 접속책임자명

		@SttlField(fieldName = "CCSP_CLWR_AWL_CD", length = 6, defaultValue = "")
		private String ccspClwrAwlCd; // 접속책임자사무분장코드

		@SttlField(fieldName = "CCSP_TRMN_IP_ADR", length = 39, defaultValue = "")
		private String ccspTrmnIpAdr; // 접속책임자단말IP
	}

	/**
	 * 모바일승인연계데이터정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class MbatLnkDataInfo {

		@SttlField(fieldName = "MBAT_LNK_FLD_NM", length = 30, defaultValue = "")
		private String mbatLnkFldNm; // 모바일승인연계필드명

		@SttlField(fieldName = "MBAT_LNK_FLD_VL", length = 100, defaultValue = "")
		private String mbatLnkFldVl; // 모바일승인연계필드값
	}

}
