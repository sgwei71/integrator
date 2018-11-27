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
 * 표준 전문 :: 출력 양식 공통부 Model
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
public class SttlOtfxCopt extends AbstractFlexibleDataSet implements Serializable {

	@SttlField(fieldName = "OTPT_SCRE_ITRN_NBT", length = 2, defaultValue = "")
	private Integer otptScreItrnNbt; // 출력화면반복횟수

	@Valid
	@SttlFieldArray(lengthField = "otptScreItrnNbt")
	private List<OtptScreInfo> otptScreInfo; // 출력화면 반복부

	@SttlField(fieldName = "FAX_OTPT_ITRN_NBT", length = 2, defaultValue = "")
	private Integer faxOtptItrnNbt; // 팩스출력반복횟수

	@Valid
	@SttlFieldArray(lengthField = "faxOtptItrnNbt")
	private List<FaxOtptInfo> FaxOtptInfo; // 팩스출력 반복부

	/**
	 * 출력화면 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class OtptScreInfo {

		@SttlField(fieldName = "OTPT_SCRE_ID_INKY_VL", length = 2, defaultValue = "")
		private Integer otptScreIdInkyVl; // 출력화면ID인덱스KEY값

		@SttlField(fieldName = "OTPT_SCRE_ID", length = 9, defaultValue = "")
		private String otptScreId; // 출력화면ID

		@SttlField(fieldName = "OTPT_PCSN_PTRN_DCD", length = 1, defaultValue = "")
		private String otptPcsnPtrnDcd; // 출력처리유형구분코드

		@SttlField(fieldName = "OTPT_PNTE_DCD", length = 1, defaultValue = "")
		private String otptPnteDcd; // 출력프린터구분코드

		@SttlField(fieldName = "EJCT_DCD", length = 1, defaultValue = "")
		private String ejctDcd; // 배출구분코드
		
		@SttlField(fieldName = "OTPT_RPT_KEY_VL", length = 100)
		private String otptRptKeyVl; // 메시지 키
	}

	/**
	 * 팩스출력 정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class FaxOtptInfo {

		@SttlField(fieldName = "FAX_OTPT_INKY_VL", length = 2, defaultValue = "")
		private Integer faxOtptInkyVl; // 팩스출력인덱스KEY값

		@SttlField(fieldName = "FAX_LLN", length = 4, defaultValue = "")
		private String faxLln; // 팩스지역번호

		@SttlField(fieldName = "FAX_TON", length = 4, defaultValue = "")
		private String faxTon; // 팩스국번호

		@SttlField(fieldName = "FXN_SRN", length = 4, defaultValue = "")
		private String fxnSrn; // 팩스번호일련번호

		@SttlField(fieldName = "FXTN_TS", length = 14, defaultValue = "")
		private String fxtnTs; // 팩스전송일시
	}
}
