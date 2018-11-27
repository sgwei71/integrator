package com.ibkglobal.integrator.engine.bean.mca.log;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.log.LogMCA;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.log.LogManager;
import com.ibkglobal.integrator.log.LogType;
import com.ibkglobal.integrator.util.CommonUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;

import ch.qos.logback.classic.Logger;

public class LoggingMCA {
	
	@Autowired
	ConverterService converterService;
	
	public static void loggingError(Exchange exchange) throws IBKExceptionMCA {
		
		Message message = exchange.getIn();
		
		// 에러 로그 파일 생성
		Logger logger   = LogManager.getLogger(LogType.EXCEPTION);
		
		// 로깅
		logger.error(createLogFormat(message));
	}
	
	public static void logging(Exchange exchange) throws IBKExceptionMCA {
		
		Message message = exchange.getIn();
		
		String logName  = message.getHeader(ConstantCode.LOGGER_KEY, String.class);
		
		// 다이나믹 로그 파일 생성
		LogManager.putMdc(logName);
		Logger logger   = LogManager.getLogger(LogType.DYNAMIC);
		
		// 로깅
		logger.info(createLogFormat(message));
	}
	
	/**
	 * MCA 로그 포멧
	 * @param message
	 * @return
	 * @throws IBKExceptionMCA
	 */
	protected static String createLogFormat(Message message) throws IBKExceptionMCA {
		
		LogMCA logMca = new LogMCA();
		
		// base
		logMca.setSeq(CommonUtil.nullCheck(message.getHeader(ConstantCode.SEQ, String.class)));
		logMca.setSysCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.SYS_CODE, String.class)));
		logMca.setBizCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.BIZ_CODE, String.class)));
		logMca.setTransTime(CommonUtil.nullCheck(message.getHeader(ConstantCode.TRANS_TIME, String.class)));
		logMca.setOriRecvTime(CommonUtil.nullCheck(message.getHeader(ConstantCode.ADAPTER_RECV_TIME, String.class)));
		
		logMca.setWorkFlow(getWorkFlow(logMca.getSeq()));
		
		logMca.setSttlYn("N");
		
		String session = message.getHeader("CamelNettyChannelHandlerContext") == null ? "" : message.getHeader("CamelNettyChannelHandlerContext").toString();
		logMca.setSession(session);
		
		if (message.getBody() instanceof IBKMessage) {
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);
			
			StandardTelegram standardTelegram = ibkMessage.getStandardTelegram();
			
			setDefaultLog(logMca, standardTelegram);
		} else if (message.getBody() instanceof String) {
			logMca.setMsg(convertStandardTelegram(logMca, message.getBody(String.class)));
		} else if (message.getBody() instanceof byte[]) {
			logMca.setMsg(convertStandardTelegram(logMca, new String(message.getBody(byte[].class))));
		} else {
			logMca.setMsg(convertStandardTelegram(logMca, message.getBody(String.class)));
		}
		
		String result = "";
		
		try {
			result = Converter.mapper.writeValueAsString(logMca);
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.LOG_ERROR, "로그 변환 오류");
		}
		
		return result;
	}
	
	/**
	 * 표준전문 기본 셋
	 * @param logMca
	 * @param standardTelegram
	 */
	protected static void setDefaultLog(LogMCA logMca, StandardTelegram standardTelegram) {
		
		if (standardTelegram != null) {
			
			logMca.setMsg(standardTelegram);
			logMca.setSttlYn("Y");
			
			if (standardTelegram.getSttlSysCopt() != null) {
				
				// 전행표준전문작성년월일
				String whbnSttlWrtnYms   = standardTelegram.getSttlSysCopt().getWhbnSttlWrtnYmd();
				// 전행표준전문생성시스템명
				String whbnSttlCretSysNm = standardTelegram.getSttlSysCopt().getWhbnSttlCretSysNm();
				// 전행표준전문일련번호
				String whbnSttlSrn       = standardTelegram.getSttlSysCopt().getWhbnSttlSrn();
				
				logMca.setIntfId(standardTelegram.getSttlSysCopt().getSttlIntfId());
				logMca.setGlobalId(whbnSttlWrtnYms + whbnSttlCretSysNm + whbnSttlSrn);
				logMca.setRqstRspnDcd(standardTelegram.getSttlSysCopt().getRqstRspnDcd());
				logMca.setRspnPcrsDcd(standardTelegram.getSttlSysCopt().getRspnPcrsDcd());
				logMca.setOtptTmgtDcd(standardTelegram.getSttlSysCopt().getOtptTmgtDcd());
			}
		}
	}
	
	/**
	 * 표준 전문 변환 안되면 기존 데이터로 리턴
	 * @param logMca
	 * @param data
	 * @return
	 */
	protected static Object convertStandardTelegram(LogMCA logMca, String data) {
		
		try {
			StandardTelegram standardTelegram = Converter.mapper.readValue(data, StandardTelegram.class);
			
			setDefaultLog(logMca, standardTelegram);
			
			return standardTelegram;
		} catch (Exception e) {
			return data;
		}
	}
	
	/**
	 * 업무 흐름
	 * @param seq
	 * @return
	 */
	protected static String getWorkFlow(String seq) {
		
		String result = null;
		
		switch (seq) {
		case "0" :
			result = "ERROR";
			break;
		case "1" :
			result = "FROM IN ADAPTER";
			break;
		case "2" :
			result = "PreProcess";
			break;
		case "3" :
			result = "TO IN ADAPTER";
			break;
		case "4" :
			result = "TO OUT ADAPTER";
			break;
		case "5" :
			result = "AfterProcess";
			break;
		case "6" :
			result = "FROM OUT ADAPTER";
			break;
		default:
			result = "DEFAULT";
			break;
		}
		
		return result;
	}
}
