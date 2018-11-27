/**
 * 
 */
package com.ibkglobal.message.common.normal;

import com.ibkglobal.common.validator.sttl.SttlField;

import lombok.Data;

/**
 * 가변 길이 공통부
 * 
 * @author Lee Hyungjoo
 * @date 2018. 5. 29.
 *
 */
@Data
public class AbstractFlexibleDataSet {

	@SttlField(fieldName = "DTST_DCD", length = 2, defaultValue = "")
	private DataSetCode dtstDcd; // 데이터셋 구분코드

	@SttlField(fieldName = "DTST_LEN", length = 6, defaultValue = "")
	private Integer dtstLen; // 데이터셋 길이

}
