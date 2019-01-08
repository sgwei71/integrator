package com.ibkglobal.integrator.engine.bean.log;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.log.LogAPI;
import com.ibkglobal.integrator.config.ConstantCodeAPI;
import com.ibkglobal.integrator.exception.FaultMessage;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.log.LogManager;
import com.ibkglobal.integrator.log.LogType;
import com.ibkglobal.integrator.util.CommonUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;


public class LoggerBean {
	
	@Autowired
	ConverterService converterService;
	
	@Autowired
	Environment env;
	
	private static Logger log = LoggerFactory.getLogger(LoggerBean.class);
	
	public static void loggingError(Exchange exchange) {
		log.info(":::::::::::::::::loggingError:::::::::::::::::::::::::::::::::");
		exchange.getIn().setHeader(ConstantCodeAPI.STATUS,"E");
//		Message message = exchange.getIn();
//
//		// 에러 로그 파일 생성
//		Logger logger   = LogManager.getLogger(LogType.EXCEPTION);
//		// 로깅
//		logger.error(createLogFormat(message));
		logging(exchange);
	}
	
	public static void logging(Exchange exchange) {
		log.info(":::::::::::::::::logging:::::::::::::::::::::::::::::::::");
		Message message = exchange.getIn();
		
		message.setHeader(ConstantCodeAPI.STATUS,"S");
		
		String logName  = message.getHeader(ConstantCodeAPI.LOGGER_KEY, String.class);
		
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
	 * @throws JsonProcessingException 
	 * @throws IBKExceptionMCA
	 */
	protected static String createLogFormat(Message message) {
		
		LogAPI logModel = new LogAPI();
		
		// base
		logModel.setSeq(CommonUtil.nullCheck(message.getHeader(ConstantCodeAPI.SEQ, String.class)));
		logModel.setStatus(CommonUtil.nullCheck(message.getHeader(ConstantCodeAPI.STATUS, String.class)));
		logModel.setSysCd(CommonUtil.nullCheck(message.getHeader(ConstantCodeAPI.SYS_CODE, String.class)));
		logModel.setBizCd(CommonUtil.nullCheck(message.getHeader(ConstantCodeAPI.BIZ_CODE, String.class)));
		logModel.setTransTime(CommonUtil.nullCheck(message.getHeader(ConstantCodeAPI.TRANS_TIME, String.class)));
		logModel.setOriRecvTime(CommonUtil.nullCheck(message.getHeader(ConstantCodeAPI.ADAPTER_RECV_TIME, String.class)));
		
		logModel.setWorkFlow(getWorkFlow(logModel.getSeq()));
		
		logModel.setSttlYn("N");
		
	
		if (message.getBody() instanceof IBKMessage) {
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);
			
			StandardTelegram standardTelegram = ibkMessage.getStandardTelegram();
			
			setDefaultLog(logModel, standardTelegram);
		} else if (message.getBody() instanceof String) {
			logModel.setMsg(convertStandardTelegram(logModel, message.getBody(String.class)));
		} else if (message.getBody() instanceof byte[]) {
			logModel.setMsg(convertStandardTelegram(logModel, new String(message.getBody(byte[].class))));
		} else {
			logModel.setMsg(convertStandardTelegram(logModel, message.getBody(String.class)));
		}
		String writeValueAsString =null;
		try {
			writeValueAsString = Converter.mapper.writeValueAsString(logModel);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			writeValueAsString= logModel.toString();
		}
		return writeValueAsString;
	}
	
	/**
	 * 표준전문 기본 셋
	 * @param logMca
	 * @param standardTelegram
	 */
	protected static void setDefaultLog(LogAPI logModel, StandardTelegram standardTelegram) {
		
		if (standardTelegram != null) {
			
			logModel.setMsg(standardTelegram);
			logModel.setSttlYn("Y");
			
			if (standardTelegram.getSttlSysCopt() != null) {
				
				// 전행표준전문작성년월일
				String whbnSttlWrtnYms   = standardTelegram.getSttlSysCopt().getWhbnSttlWrtnYmd();
				// 전행표준전문생성시스템명
				String whbnSttlCretSysNm = standardTelegram.getSttlSysCopt().getWhbnSttlCretSysNm();
				// 전행표준전문일련번호
				String whbnSttlSrn       = standardTelegram.getSttlSysCopt().getWhbnSttlSrn();
				
				logModel.setIntfId(standardTelegram.getSttlSysCopt().getSttlIntfId());
				logModel.setGlobalId(whbnSttlWrtnYms + whbnSttlCretSysNm + whbnSttlSrn);
				logModel.setRqstRspnDcd(standardTelegram.getSttlSysCopt().getRqstRspnDcd());
				logModel.setRspnPcrsDcd(standardTelegram.getSttlSysCopt().getRspnPcrsDcd());
				logModel.setOtptTmgtDcd(standardTelegram.getSttlSysCopt().getOtptTmgtDcd());
			}
		}
	}
	
	/**
	 * 표준 전문 변환 안되면 기존 데이터로 리턴
	 * @param logMca
	 * @param data
	 * @return
	 */
	protected static Object convertStandardTelegram(LogAPI logModel, String data) {
		
		try {
			StandardTelegram standardTelegram = Converter.mapper.readValue(data, StandardTelegram.class);
			
			setDefaultLog(logModel, standardTelegram);
			
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
