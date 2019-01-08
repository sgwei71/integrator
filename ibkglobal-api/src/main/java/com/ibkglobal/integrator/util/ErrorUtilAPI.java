package com.ibkglobal.integrator.util;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.ConstantCodeAPI;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.FaultMessage;
import com.ibkglobal.integrator.exception.IBKExceptionAPI;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
public class ErrorUtilAPI {

	private static Logger logger = LoggerFactory.getLogger(ErrorUtilAPI.class);
	
	/**
	 * 일반 오류 시
	 * 
	 * @param exchange
	 * @throws JsonProcessingException 
	 */
	public static void setErrorMessage(Exchange exchange) {
		Message message = exchange.getIn();

		String errorSpot = message.getHeader(ConstantCode.ERR_SPOT, String.class);
		String errorCode = message.getHeader(ConstantCode.ERR_CODE, String.class);
		Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		throwable.printStackTrace();
		FaultMessage faultMsg = new FaultMessage();
		if(throwable instanceof java.net.ConnectException) {
			System.out.println("ReadTimeoutException--->"+throwable.getClass().getName());
		}else if(throwable instanceof io.netty.handler.timeout.ReadTimeoutException) {
			System.out.println("ReadTimeoutException--->"+throwable.getClass().getName());
		}
		
		faultMsg.setOriErcd("ORI_ERCD");
		faultMsg.setMsgCd("MSG_CD");
		faultMsg.setMnmsgCntn("MNMSG_CNTN");
		faultMsg.setErrLocatCntn("ERR_LOCAT_CNTN");
		faultMsg.setAndMsgCd("AND_MSG_CD");
		faultMsg.setAndMsgCntn("AND_MSG_CNTN");
		
		String errorMsg;
		
		try {
			errorMsg = Converter.mapper.writeValueAsString(faultMsg);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorMsg = faultMsg.toString();
		}
		logger.info("errorMsg -> [{}]",errorMsg);
		logger.info("errorspot -> [{}]",errorSpot);
//		exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, Builder.constant(400));
//		exchange.getOut().setFault(false);
		exchange.getIn().setBody(errorMsg.getBytes());
	}

	/**
	 * 헬스체크 오류 시
	 * 
	 * @param exchange
	 */
	public static void setErrorMessageHealth(Exchange exchange) {

	}

	/**
	 * Exchange 에러코드 셋
	 * 
	 * @param exchange
	 * @param throwable
	 */
	public static void setErrorCode(Exchange exchange, Throwable throwable) {
		Message message = exchange.getIn();

		String bizCode = message.getHeader(ConstantCode.BIZ_CODE, String.class);

		message.setHeader(ConstantCode.ERR_SPOT, createErrorSpot(throwable));
		message.setHeader(ConstantCode.ERR_CODE, createErrorCode(bizCode, throwable));
		message.setHeader(ConstantCode.ERR_MSG, createErrorMsg(throwable));
	}

	public static String createErrorSpot(Throwable throwable) {
		String sysCode = "SYS";

		if (throwable instanceof IBKExceptionMCA) {
			sysCode = ConstantCode.MCA_CODE;
		} else if (throwable instanceof IBKExceptionFEP) {
			sysCode = ConstantCode.FEP_CODE;
		} else if (throwable instanceof IBKExceptionEAI) {
			sysCode = ConstantCode.EAI_CODE;
		}else if (throwable instanceof IBKExceptionAPI) {
			sysCode = ConstantCodeAPI.API_CODE;
		}
		return sysCode;
	}

	/**
	 * 에러 코드 생성(12자리)
	 * 
	 * @param bizCode
	 * @param throwable
	 * @return
	 */
	public static String createErrorCode(String bizCode, Throwable throwable) {
		String sysCode = "SYS";

		// 시스템 구분
		if (throwable instanceof IBKExceptionMCA) {
			sysCode = ConstantCode.MCA_CODE;
		} else if (throwable instanceof IBKExceptionFEP) {
			sysCode = ConstantCode.FEP_CODE;
		} else if (throwable instanceof IBKExceptionAPI) {
			sysCode = ConstantCodeAPI.API_CODE;
		}
//		// 업무 구분
//		if (bizCode == null || bizCode.length() != 3) {
//			bizCode = sysCode;
//		}
		bizCode = "COM";

		// 일련번호
		String code = "99999";

		if (throwable instanceof CommonException) {
			ErrorType errorType = ((CommonException) throwable).getErrorType();
			if (errorType != null) {
				code = errorType.getErrorCode();
			}
		}

		return ConstantCode.ERROR_HEAD + sysCode + bizCode + code;
	}

	/**
	 * 에러 메시지 생성
	 * 
	 * @param throwable
	 * @return
	 */
	public static String createErrorMsg(Throwable throwable) {
		if (throwable == null) {
			return "error content null";
		}

		String errorMsg = throwable.getMessage();

		if (StringUtils.isEmpty(errorMsg)) {
			errorMsg = throwable.getLocalizedMessage();
		}

		return errorMsg;
	}
}
