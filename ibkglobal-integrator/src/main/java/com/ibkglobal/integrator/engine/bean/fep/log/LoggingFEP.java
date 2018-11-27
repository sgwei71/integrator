package com.ibkglobal.integrator.engine.bean.fep.log;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.log.LogFEP;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.log.LogManager;
import com.ibkglobal.integrator.log.LogType;
import com.ibkglobal.integrator.util.CommonUtil;
import com.ibkglobal.message.IBKMessage;

import ch.qos.logback.classic.Logger;

public class LoggingFEP {
	
	public static void logging(Exchange exchange) throws Exception {
		
		Message message = exchange.getIn();
		
		String logName  = message.getHeader(ConstantCode.LOGGER_KEY, String.class);
		
		// 다이나믹 로그 파일 생성
		LogManager.putMdc(logName);
		Logger logger   = LogManager.getLogger(LogType.DYNAMIC);
		
		// 로깅
		logger.info(createLogFormat(message));
	}
	
	protected static String createLogFormat(Message message) throws Exception {
		
		LogFEP logFep = new LogFEP();
		
		// base
		logFep.setSttlYn("N");
		logFep.setExtTrnUnqId("");
		logFep.setGlobalId("");
		logFep.setIntfId("");
		logFep.setRspnPcrsDcd("");
		logFep.setOtptTmgtDcd("");
		
		// 메시지 데이터
		if (message.getBody() instanceof IBKMessage) {			
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);
			
			logFep.setIntfId(ibkMessage.getInterfaceId());
			
			if (ibkMessage.getStandardTelegram() != null) {
				
				logFep.setMsg(ibkMessage.getStandardTelegram());
				logFep.setSttlYn("Y");
				
				// 시스템 공통부
				if (ibkMessage.getStandardTelegram().getSttlSysCopt() != null) {
					
					// 전행표준전문작성년월일
					String whbnSttlWrtnYms   = ibkMessage.getStandardTelegram().getSttlSysCopt().getWhbnSttlWrtnYmd();
					// 전행표준전문생성시스템명
					String whbnSttlCretSysNm = ibkMessage.getStandardTelegram().getSttlSysCopt().getWhbnSttlCretSysNm();
					// 전행표준전문일련번호
					String whbnSttlSrn       = ibkMessage.getStandardTelegram().getSttlSysCopt().getWhbnSttlSrn();
							
					logFep.setGlobalId(whbnSttlWrtnYms + whbnSttlCretSysNm + whbnSttlSrn);
					logFep.setRspnPcrsDcd(ibkMessage.getStandardTelegram().getSttlSysCopt().getRspnPcrsDcd());
					logFep.setOtptTmgtDcd(ibkMessage.getStandardTelegram().getSttlSysCopt().getOtptTmgtDcd());
				}
				
				// 거래 공통부
				if (ibkMessage.getStandardTelegram().getSttlTrnCopt() != null) {
					logFep.setExtTrnUnqId(ibkMessage.getStandardTelegram().getSttlTrnCopt().getExtTrnUnqId());
				}
			} else if (ibkMessage.getHeader() != null || ibkMessage.getBody() != null) {
				// 헤더부 + 개별부 결합
				LinkedHashMap<String, Object> data = new LinkedHashMap<>();
				
				data.putAll(ibkMessage.getHeader());
				data.putAll(ibkMessage.getBody());
				
				logFep.setMsg(data);
			}
		} else if (message.getBody() instanceof String) {
			logFep.setMsg(message.getBody(String.class));
		} else if (message.getBody() instanceof byte[]) {			
			logFep.setMsg(new String(message.getBody(byte[].class)));
		}
		
		logFep.setSeq(CommonUtil.nullCheck(message.getHeader(ConstantCode.SEQ, String.class)));
		logFep.setBizCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.BIZ_CODE, String.class)));
		logFep.setOrgCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.ORG_CODE, String.class)));
		logFep.setMsgCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.MSG_CODE, String.class)));
		logFep.setTxCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.TX_CODE, String.class)));
		logFep.setEtcCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.ETC_CODE, String.class)));
		logFep.setTrxNo(CommonUtil.nullCheck(message.getHeader(ConstantCode.TRXCQ_NO, String.class)));
		logFep.setAdtUid(CommonUtil.nullCheck(message.getHeader(ConstantCode.ADT_UID, String.class)));
		logFep.setRespCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.RESP_CODE, String.class)));
		logFep.setWorkFlow(CommonUtil.nullCheck(message.getHeader(ConstantCode.WORK_FLOW, String.class)));
		logFep.setWorkCd(CommonUtil.nullCheck(message.getHeader(ConstantCode.WORK_CODE, String.class)));
		logFep.setTransTime(CommonUtil.nullCheck(message.getHeader(ConstantCode.TRANS_TIME, String.class)));
		logFep.setOriRecvTime(CommonUtil.nullCheck(message.getHeader(ConstantCode.ADAPTER_RECV_TIME, String.class)));
		logFep.setMsgState(CommonUtil.nullCheck(message.getHeader(ConstantCode.ERR_CODE, String.class)));
		logFep.setErrorContent(CommonUtil.nullCheck(message.getHeader(ConstantCode.TMSG_ERROR, String.class)));
		logFep.setServerName(CommonUtil.nullCheck(message.getHeader(ConstantCode.SERVER_NAME, String.class)));
		logFep.setTmsgSubkey(CommonUtil.nullCheck(message.getHeader(ConstantCode.TMSG_SUBKEY, String.class)));
		logFep.setSrflag(CommonUtil.nullCheck(message.getHeader(ConstantCode.IBK_SRFLAG, String.class)));
		
		return Converter.mapper.writeValueAsString(logFep);
	}
}
