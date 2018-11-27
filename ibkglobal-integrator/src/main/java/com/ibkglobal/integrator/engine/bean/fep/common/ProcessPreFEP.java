package com.ibkglobal.integrator.engine.bean.fep.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.bean.fep.log.LoggingFEP;
import com.ibkglobal.integrator.engine.bean.fep.recovery.RecoveryJournalRepo;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.integrator.util.CommonUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;
import com.ibkglobal.message.struct.resource.ResourceFEP;

public class ProcessPreFEP {
	
	@Autowired
	RecoveryJournalRepo recoveryJournal;
	
	@Autowired
	ResourceFEP resourceFEP;
	
	public void preProcess(Exchange exchange) throws Exception {
		// 헤더 초기화
		initSetHeader(exchange);
		
		// 파싱 및 초기화
		init(exchange);
	}
	
	protected void initSetHeader(Exchange exchange) throws IBKExceptionFEP {
		Message message = exchange.getIn();
		
		message.setHeader(ConstantCode.SEQ, "2");
	    message.setHeader(ConstantCode.INFRA_TYPE, InfraType.FEP);
	}
	
	protected void init(Exchange exchange) throws Exception {
		// 기본 헤더 셋
		initDefaultHeader(exchange);
		
		// 복원로직
		recovery(exchange);
		
		// Logging
		LoggingFEP.logging(exchange);
	}
	
	protected void initDefaultHeader(Exchange exchange) throws IBKExceptionFEP {
		
		Message message = exchange.getIn();
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		String interfaceId = ibkMessage.getInterfaceId();
		if (StringUtils.isEmpty(interfaceId)) {
			throw new IBKExceptionFEP(ErrorType.MESSAGE, "Can't find interfaceId : IBKMessage=[" + ibkMessage +	"] Message=[" + message + "]");
		}
		
		message.setHeader(ConstantCode.IBK_INTERFACE_ID, interfaceId);
		Interface interface1 = resourceFEP.getInterface(interfaceId);
		
		message.setHeader(ConstantCode.HEADER_ID, resourceFEP.getSourceHeader(interfaceId).getUtInopIdnNm());
		
		try {
			String refIFID = interface1.getCommon().getAttribute().getOnline().getExternal().getHgrnIntfIdCon();
			if (!StringUtils.isEmpty(refIFID)) {
				message.setHeader(ConstantCode.IBK_INTERFACE_ID_REF, refIFID);
			}
		} catch (Exception e) {}
		
		// 당타발 : 1 - 당발, 2 - 타발
		message.setHeader(ConstantCode.IBK_HTDSP, interface1.getCommon().getAttribute().getOnline().getExternal().getHtdspCd().getCode());
		// 송수신 : H - 송신, E - 수신
		message.setHeader(ConstantCode.IBK_SRFLAG, interface1.getCommon().getAttribute().getOnline().getExternal().getSnrcCd());
		// 매핑 (bypass) 여부 : Y - 매핑함, N - BYPASS
		message.setHeader(ConstantCode.IBK_MAPPING_FLAG, interface1.getCommon().getAttribute().getMpngYn().getCode());
		
		// 전문추적번호 TRXCQ_NO 가 없을 경우만 
		String trxcqNo = message.getHeader(ConstantCode.TRXCQ_NO, String.class);
		if( StringUtils.isEmpty(trxcqNo) ) {
			trxcqNo = resourceFEP.getTraceKey(ibkMessage.getInterfaceId(), ibkMessage.getHeader());
			message.setHeader(ConstantCode.TRXCQ_NO, trxcqNo);
		}
		
		message.setHeader(ConstantCode.TRXCQ_NO_SRC, trxcqNo);
		String trxcq2No = message.getHeader(ConstantCode.TRXCQ2_NO, String.class);
		if( StringUtils.isEmpty(trxcq2No) ) {
			trxcq2No = resourceFEP.getMsgKey(ibkMessage.getInterfaceId(), ibkMessage.getHeader());
			message.setHeader(ConstantCode.TRXCQ2_NO, trxcq2No);
		}
		message.setHeader(ConstantCode.TRXCQ2_NO_SRC, trxcq2No);
		
		// 응답코드
		message.setHeader(ConstantCode.RESP_CODE, resourceFEP.getErrCode(ibkMessage.getInterfaceId(), ibkMessage.getHeader()));
		
		String[] msgCds = resourceFEP.getWorkCode(ibkMessage.getInterfaceId(), ibkMessage.getHeader()).split("\\_");
		if( msgCds.length > 2) {
			message.setHeader(ConstantCode.MSG_CODE, msgCds[2].substring(1)); //전문 종별번호
		}
		if( msgCds.length > 3) {
			String txCd = "";
			for (int i = 3; i < msgCds.length; i++) {
				if( i == msgCds.length-1) {
					txCd += msgCds[i];
				} else {
					txCd += msgCds[i] + "_";
				}
			}
			message.setHeader(ConstantCode.TX_CODE, txCd );   //전문거래코드
		}
		
		//adapter안통하고 queue로 바로 들어왔을 경우
		String adtUid = message.getHeader(ConstantCode.ADT_UID, String.class);
		if (StringUtils.isEmpty(adtUid)) {
			String trId= CommonUtil.getTrId(); //채번
			message.setHeader(ConstantCode.ADT_UID, trId);
		}
		
		// TMSG_SUBKEY
		String subKeyNm = message.getHeader(ConstantCode.TMSG_SUBKEY_NAME, String.class);
		if (!StringUtils.isEmpty(subKeyNm)) {
			message.setHeader(ConstantCode.TMSG_SUBKEY, ibkMessage.getValue(subKeyNm));
		} else {
			Object[] tmpObjs = resourceFEP.getLogKey(ibkMessage.getInterfaceId(), ibkMessage.getHeader());
			if (tmpObjs != null && tmpObjs.length > 0) {
				message.setHeader(ConstantCode.TMSG_SUBKEY, tmpObjs[0].toString());
			} else {
				message.removeHeader(ConstantCode.TMSG_SUBKEY);
			}
		}
	}
	
	protected void recovery(Exchange exchange) throws IBKExceptionFEP {
		Message message = exchange.getIn();
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		if (ibkMessage.getStandardTelegram() != null) {
			recoveryJournal.putRecoveryJournal(exchange);
		}
	}
}
