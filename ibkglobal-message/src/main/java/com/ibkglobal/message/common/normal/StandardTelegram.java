package com.ibkglobal.message.common.normal;

import java.io.Serializable;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibkglobal.message.common.normal.copt.SttlAmgcCopt;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt;
import com.ibkglobal.message.common.normal.copt.SttlNfchCopt;
import com.ibkglobal.message.common.normal.copt.SttlOpatCopt;
import com.ibkglobal.message.common.normal.copt.SttlOtfxCopt;
import com.ibkglobal.message.common.normal.copt.SttlSvatCopt;
import com.ibkglobal.message.common.normal.copt.SttlSysCopt;
import com.ibkglobal.message.common.normal.copt.SttlTrnCopt;

import lombok.Data;

/**
 * IBK Kolumbus 시스템 표준 전문 Model
 * 
 * @author Lee Hyungjoo
 * @date 2018. 5. 29.
 *
 */
@Data
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class StandardTelegram implements Serializable {

	/**
	 * 공통부
	 */
	@Valid
	private SttlSysCopt			sttlSysCopt;								// 시스템 공통부
	@Valid
	private SttlTrnCopt			sttlTrnCopt;								// 거래 공통부
	private SttlAmgcCopt		sttlAmgcCopt;								// 대면채널 공통부
	private SttlNfchCopt		sttlNfchCopt;								// 비대면채널 공통부
	private SttlSvatCopt		sttlSvatCopt;								// 책임자승인 공통부
	private SttlMsgCopt			sttlMsgCopt;								// 메시지 공통부
	private SttlOpatCopt		sttlOpatCopt;								// 조작자승인 공통부
	private SttlOtfxCopt		sttlOtfxCopt;								// 출력양식 공통부

	/**
	 * 개별부
	 */
	private UserData userData;

	/**
	 * 종료부
	 */
	private String endSignal = "@@";

}
