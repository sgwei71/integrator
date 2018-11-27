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
import com.ibkglobal.integrator.util.RecoveryUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.struct.resource.ResourceFEP;

public class ProcessAfterFEP {
	
	@Autowired
	ResourceFEP resourceFEP;
	
	@Autowired
	RecoveryJournalRepo recoveryJournal;
	
	String recoverMessage2ndFlag = "";
	
	public void afterProcess(Exchange exchange) throws Exception {
		// 헤더 초기화
		initSetHeader(exchange);
				
		// 파싱 및 초기화
		init(exchange);
		
		// logging
		LoggingFEP.logging(exchange);
	}
	
	protected void initSetHeader(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		
		message.setHeader(ConstantCode.SEQ, "3");
	    message.setHeader(ConstantCode.INFRA_TYPE, InfraType.FEP);
	}
	
	protected void init(Exchange exchange) throws Exception {
		// 복원(당발에서 먼저 시작했을 경우는 복원, 아닐 경우는 신규 표준전문 생성)
		recovery(exchange);
	}
	
	protected void recovery(Exchange exchange) throws Exception {
		
		Message message = exchange.getIn();
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		// 헤더 생성
		StandardTelegram ibkNormalHeader = null;
		
		String recoveryYn = message.getHeader(ConstantCode.IBK_NORMAL_MESSAGE_RECOVERY_YN, String.class);
		
		// 전문 복원이 필요한 경우
		if (!StringUtils.isEmpty(recoveryYn) && "Y".equalsIgnoreCase(recoveryYn)) {
			
			// 인터페이스 정보
			String ifid    = message.getHeader(ConstantCode.IBK_INTERFACE_ID, String.class);
			// 당발(수신)일 경우만 존재함 / 기관에서 들어온 메시지에 대한 정보를 기존에 보냈던 정보로 복원할 때 사용
			String ifidRef = message.getHeader(ConstantCode.IBK_INTERFACE_ID_REF, String.class);
			
			if (StringUtils.isEmpty(ifid)) {
				throw new IBKExceptionFEP(ErrorType.MESSAGE, "INTERFACE ID is not set : [" +	message.getHeader(ConstantCode.WORK_CODE, String.class) + "])");
			}
			
			Interface intf        = resourceFEP.getInterface(ifid);			
			String    bizCode     = message.getHeader(ConstantCode.BIZ_CODE, String.class);
			String    orgCode     = message.getHeader(ConstantCode.ORG_CODE, String.class);
			String    msgKeyValue = message.getHeader(ConstantCode.TRXCQ2_NO, String.class);
			
			// 당발(수신전문을 송신으로 복원할 때) - 전문복원
			if (!StringUtils.isEmpty(ifidRef)) {
				try {
					// 복원할 전문 헤더 Set
					recoveryJournal.getRecoveryJournal(exchange); // StandardTelegram을 헤더에 넣는다.
					//ibkNormalHeader = message.getHeader(ConstantCode.IBK_NORMAL_HEADER, StandardTelegram.class);
					ibkNormalHeader = message.getBody(IBKMessage.class).getStandardTelegram();
					
					String reqSvcID = null;
					String resSvcID = null;
					String resSvcIDErr = null;
					String normalTxCode = null;
					
					// 응답값 셋
					RecoveryUtil.makeResponseNormalHeader(ibkNormalHeader, ifid, reqSvcID, resSvcID, resSvcIDErr, 
							normalTxCode, bizCode, orgCode, msgKeyValue, ifidRef, 0); // 길이 부분 수정필요함 임시로 0임
					
				} catch (Exception e) {
					// 복원 실패 시 대응
					if (recoverMessage2ndFlag != null && recoverMessage2ndFlag.equalsIgnoreCase("Y")) {
						// 일반 타발 처럼 공통부 생성, 에러 코드에 4 (전문복원실패) 셋팅
						ibkNormalHeader = RecoveryUtil.makeIBKNormalHeaderFor2ndRecovery(intf, bizCode, orgCode, msgKeyValue, ifidRef);
					} else {
						if (!(e instanceof IBKExceptionFEP)) {
							throw e;
						} else {
							throw new IBKExceptionFEP (ErrorType.FEP_RECOVERY, "표준전문 공통부 복원 실패", e);
						}
					}
				}
			} 
			// 타발 - 공통부 생성
			else {
				ibkNormalHeader = RecoveryUtil.makeRequestNormalHeader(intf, bizCode, orgCode, msgKeyValue);
			}
		}
		// 복원이 필요 없는 경우
		else if (StringUtils.isEmpty(recoveryYn)) {			
			
			ibkNormalHeader = ibkMessage.getStandardTelegram();
			
			if (ibkNormalHeader != null) {
				// 타발
			} else {
				// 당발
				String ifid = null;
				String reqSvcID = null;
				String resSvcID = null;
				String resSvcIDErr = null;
				String normalTxCode = null;
				String bizCode = null;
				String orgCode = null;
				String msgKeyValue = null;
				String ifidRef = null;
				
				// 응답값 셋
				RecoveryUtil.makeResponseNormalHeader(ibkNormalHeader, ifid, reqSvcID, resSvcID, resSvcIDErr,
						normalTxCode, bizCode, orgCode, msgKeyValue, ifidRef, 0); // 길이 부분 수정필요함 임시로 0임
			}
		}
		
//		if (ibkNormalHeader != null) {
//			message.setHeader(ConstantCode.IBK_NORMAL_HEADER, ibkNormalHeader);
//			message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
//			message.setHeader(ConstantCode.IBK_BIZKEY, ibkNormalHeader.getSttlTrnCopt().getExtTrnUnqId());
//			message.setHeader(ConstantCode.IBK_GID_WRTN_YMD, ibkNormalHeader.getSttlSysCopt().getWhbnSttlWrtnYmd());
//			message.setHeader(ConstantCode.IBK_GID_CRET_SYS_NM, ibkNormalHeader.getSttlSysCopt().getWhbnSttlCretSysNm());
//			message.setHeader(ConstantCode.IBK_GID_SRN, ibkNormalHeader.getSttlSysCopt().getWhbnSttlSrn());
//			message.setHeader(ConstantCode.IBK_INTERFACE_ID, ibkNormalHeader.getSttlSysCopt().getSttlIntfId());
//			message.setHeader(ConstantCode.IBK_PCRS_DCD, ibkNormalHeader.getSttlSysCopt().getRspnPcrsDcd());
//			message.setHeader(ConstantCode.IBK_OTPT_TMGT_DCD, ibkNormalHeader.getSttlSysCopt().getOtptTmgtDcd());
//		}
		
		// 최종 데이터 셋(standardTelegram, 헤더부, 거래부 결합)
		// Composing전 최종 셋
		if (ibkNormalHeader != null) {
			
			// Default(인터페이스의 서비스 아이디를 채워서 보내준다)
			Interface intf  = resourceFEP.getInterface(ibkMessage.getInterfaceId());
			String svcId    = intf.getCommon().getAttribute().getOnline().getExternal().getSvcId();
			String onlTrnCd = intf.getCommon().getAttribute().getOnline().getExternal().getOnlTrnCd();			
			ibkNormalHeader.getSttlTrnCopt().setSttlXcd(svcId + onlTrnCd);
			ibkNormalHeader.getSttlTrnCopt().setSttlRqstFuncDsncId(onlTrnCd);
			
			message.setHeader(ConstantCode.IBK_NORMAL_HEADER, ibkNormalHeader);			
			message.setBody(RecoveryUtil.makeNormalMsg(ibkNormalHeader, ibkMessage));
		} 
	}
}
