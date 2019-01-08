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
 * 표준 전문 :: 메시지 공통부 Model
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
public class SttlMsgCopt extends AbstractFlexibleDataSet implements Serializable {

	@SttlField(fieldName = "MSG_RPSN_DCD", length = 1, defaultValue = "")
	private String msgRpsnDcd; // 메시지표시구분코드

	@SttlField(fieldName = "EROC_FLD_NM", length = 30, defaultValue = "")
	private String erocFldNm; // 오류발생필드명

	@SttlField(fieldName = "PNMG_ITRN_NBT", length = 2, defaultValue = "")
	private Integer pnmgItrnNbt; // 주메시지반복횟수

	@Valid
	@SttlFieldArray(lengthField = "pnmgItrnNbt")
	private List<PnmgInfo> pnmgInfo; // 주메시지 반복부

	@SttlField(fieldName = "MSG_ERIF_ITRN_NBT", length = 2, defaultValue = "")
	private Integer msgErifItrnNbt; // 메시지오류정보반복횟수

	@Valid
	@SttlFieldArray(lengthField = "msgErifItrnNbt")
	private List<MsgErrorInfo> msgErrorInfo; // 메시지오류 반복부

	@SttlField(fieldName = "RPLC_LTRS_ITRN_NBT", length = 2, defaultValue = "")
	private Integer rplcLtrsItrnNbt; // 치환문자반복횟수

	@Valid
	@SttlFieldArray(lengthField = "rplcLtrsItrnNbt")
	private List<RplcLtrsInfo> rplcLtrsInfo; // 치환문자 반복부

	@SttlField(fieldName = "SBMG_ITRN_NBT", length = 2, defaultValue = "")
	private Integer sbmgItrnNbt; // 부메시지반복횟수

	@Valid
	@SttlFieldArray(lengthField = "sbmgItrnNbt")
	private List<SbmgInfo> sbmgInfo; // 부메시지 반복부

	@SttlField(fieldName = "CLOT_SCRE_INFO_ITRN_NBT", length = 2, defaultValue = "")
	private Integer clotScreInfoItrnNbt; // 호출화면정보반복횟수

	@Valid
	@SttlFieldArray(lengthField = "clotScreInfoItrnNbt")
	private List<ClotScreInfo> clotScreInfo; // 호출화면정보 반복부

	@SttlField(fieldName = "CLOT_SCRE_FLD_ITRN_NBT", length = 2, defaultValue = "")
	private Integer clotScreFldItrnNbt; // 호출화면필드반복횟수

	@Valid
	@SttlFieldArray(lengthField = "clotScreFldItrnNbt")
	private List<ClotScreFldInfo> clotScreFldInfo; // 호출화면필드 반복부

	/**
	 * 주메시지 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class PnmgInfo {

		@SttlField(fieldName = "PNMG_CD", length = 12, defaultValue = "")
		private String pnmgCd; // 주메시지코드

		@SttlField(fieldName = "PNMG_DSNC_PGRS_NO", length = 2, defaultValue = "")
		private Integer pnmgDsncPgrsNo; // 주메시지구분진행번호

		@SttlField(fieldName = "PNMG_CON", length = 200, defaultValue = "")
		private String pnmgCon; // 주메시지내용
	}

	/**
	 * 메시지 오류 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class MsgErrorInfo {

		@SttlField(fieldName = "PNMG_INKY_VL", length = 2, defaultValue = "")
		private Integer pnmgInkyVl; // 주메시지인덱스KEY값

		@SttlField(fieldName = "MSG_ERIF_CON", length = 400, defaultValue = "")
		private String msgErifCon; // 메시지오류정보내용
	}

	/**
	 * 치환문자 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class RplcLtrsInfo {

		@SttlField(fieldName = "PNMG_INKY_VL", length = 2, defaultValue = "")
		private Integer pnmgInkyVl; // 주메시지인덱스KEY값

		@SttlField(fieldName = "FRTM_RPLC_LTRS_CON", length = 30, defaultValue = "")
		private String frtmRplcLtrsCon; // 첫번째치환문자내용

		@SttlField(fieldName = "SCTM_RPLC_LTRS_CON", length = 30, defaultValue = "")
		private String sctmRplcLtrsCon; // 두번째치환문자내용

		@SttlField(fieldName = "THTM_RPLC_LTRS_CON", length = 30, defaultValue = "")
		private String thtmRplcLtrsCon; // 세번째치환문자내용
	}

	/**
	 * 부메시지 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class SbmgInfo {

		@SttlField(fieldName = "PNMG_INKY_VL", length = 2, defaultValue = "")
		private Integer pnmgInkyVl; // 주메시지인덱스KEY값

		@SttlField(fieldName = "SBMG_CON", length = 200, defaultValue = "")
		private String sbmgCon; // 부메시지내용
	}

	/**
	 * 호출화면 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class ClotScreInfo {

		@SttlField(fieldName = "PNMG_INKY_VL", length = 2, defaultValue = "")
		private Integer pnmgInkyVl; // 주메시지인덱스KEY값

		@SttlField(fieldName = "CLOT_SCRE_NM", length = 100, defaultValue = "")
		private String clotScreNm; // 호출화면명

		@SttlField(fieldName = "CLOT_SCRE_ADR", length = 200, defaultValue = "")
		private String clotScreAdr; // 호출화면주소
	}

	/**
	 * 호출화면 필드 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class ClotScreFldInfo {

		@SttlField(fieldName = "PNMG_INKY_VL", length = 2, defaultValue = "")
		private Integer pnmgInkyVl; // 주메시지인덱스KEY값

		@SttlField(fieldName = "CLOT_SCRE_FLD_NM", length = 30, defaultValue = "")
		private String clotScreFldNm; // 호출화면필드명

		@SttlField(fieldName = "CLOT_SCRE_FLD_VL", length = 100, defaultValue = "")
		private String clotScreFldVl; // 호출화면필드값
	}

}
