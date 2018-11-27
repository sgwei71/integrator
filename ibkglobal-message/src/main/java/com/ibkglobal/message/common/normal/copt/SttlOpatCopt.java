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
 * 표준 전문 :: 조작자 승인 공통부 Model
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
public class SttlOpatCopt extends AbstractFlexibleDataSet implements Serializable {

	@SttlField(fieldName = "OPAT_PTRN_DCD", length = 1, defaultValue = "")
	private String opatPtrnDcd; // 조작자승인유형구분코드

	@SttlField(fieldName = "OPAT_RCD_ITRN_NBT", length = 2, defaultValue = "")
	private Integer opatRcdItrnNbt; // 조작자승인사유코드반복횟수

	@Valid
	@SttlFieldArray(lengthField = "opatRcdItrnNbt")
	private List<OpatRsnInfo> opatRsnInfo; // 조작자승인사유 반복부

	@SttlField(fieldName = "OPAT_MSG_ITRN_NBT", length = 2, defaultValue = "")
	private Integer opatMsgItrnNbt; // 조작자승인메시지반복횟수

	@Valid
	@SttlFieldArray(lengthField = "opatMsgItrnNbt")
	private List<OpatMsgInfo> opatMsgInfo; // 조작자승인메시지 반복부

	@SttlField(fieldName = "OPAT_SB_MSG_ITRN_NBT", length = 2, defaultValue = "")
	private Integer opatSbMsgItrnNbt; // 조작자승인부메시지반복횟수

	@Valid
	@SttlFieldArray(lengthField = "opatSbMsgItrnNbt")
	private List<OpatSbMsgInfo> opatSbMsgInfo; // 조작자승인부메시지 반복부

	/**
	 * 조작자승인사유정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class OpatRsnInfo {

		@SttlField(fieldName = "OPTO_PAPV_YN", length = 1, defaultValue = "")
		private String optoPapvYn; // 조작자기승인여부

		@SttlField(fieldName = "OPAT_RCD", length = 12, defaultValue = "")
		private String opatRcd; // 조작자승인사유코드
	}

	/**
	 * 조작자승인메시지정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class OpatMsgInfo {

		@SttlField(fieldName = "OPAT_RCD_INKY_VL", length = 2, defaultValue = "")
		private Integer opatRcdInkyVl; // 조작자승인사유코드인덱스KEY값

		@SttlField(fieldName = "OPAT_MSG_CON", length = 200, defaultValue = "")
		private String opatMsgCon; // 조작자승인메시지내용
	}

	/**
	 * 조작자승인부메시지정보
	 * 
	 * @author Lee Hyungjoo
	 * @date 2018. 5. 29.
	 *
	 */
	@Data
	public static class OpatSbMsgInfo {

		@SttlField(fieldName = "OPAT_SB_RCD_INKY_VL", length = 2, defaultValue = "")
		private Integer opatSbRcdInkyVl; // 조작자승인부사유코드인덱스KEY값

		@SttlField(fieldName = "OPAT_SB_MSG_CON", length = 200, defaultValue = "")
		private String opatSbMsgCon; // 조작자승인부메시지내용
	}

}
