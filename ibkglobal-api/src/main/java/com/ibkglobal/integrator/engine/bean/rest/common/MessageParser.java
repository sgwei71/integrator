/*
 * Copyright 2019. IBK(INDUSTRIAL BANK OF KOREA) All rights reserved.
 */

/**
 * 
 */
package com.ibkglobal.integrator.engine.bean.rest.common;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibk.ibkglobal.data.io.IoCacheAPI;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.ConstantCodeAPI;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionAPI;
import com.ibkglobal.integrator.exception.InvalidFunctionCodeException;
import com.ibkglobal.integrator.exception.InvalidServiceIdException;
import com.ibkglobal.integrator.exception.NotFoundInterfaceIdException;
import com.ibkglobal.integrator.util.RecoveryUtilAPI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt.MsgErrorInfo;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt.PnmgInfo;
import com.ibkglobal.message.converter.service.ConverterServiceAPI;

/**
 * @since 2019. 1. 9.
 * @author d25856
 * <PRE>
 * 2019. 1. 9. d25856 : 최초작성
 * </PRE>
 */
public class MessageParser {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageParser.class);
	/**
	 * @Author : d25856
	 * @Date   : 2019. 1. 9.
	 * @Method Name : encode
	 * @Return : void
	 */
	public void decode(Exchange exchange) throws Exception{
		
		Message message = exchange.getIn();
		String parsingType = message.getHeader(ConstantCodeAPI.FROM_PARSING_TYPE,String.class);
		
		switch (parsingType) {
		case "JSON":
			message.setBody(jsonMessageParsing(message,"IN"));
			break;
		case "FLAT":
			
			break;
		default:
			break;
		}
		
	}
	/**
	 * @Author : d25856
	 * @Date   : 2019. 1. 9.
	 * @Method Name : jsonMessageParsing
	 * @Return : IBKMessage
	 */
	@SuppressWarnings("unchecked")
	public IBKMessage jsonMessageParsing(Message message, String inOut) throws Exception {
		// JSON 파싱해서 표준 전문 만들고 IBKMessage 생성
		StandardTelegram standardTelegram = null;
		
		//일단 기본값 셋팅
		String ifid = message.getHeader(ConstantCode.IBK_INTERFACE_ID,String.class);
		String serviceId = message.getHeader("SERVICE_ID",String.class);
		String sttlRqstFuncDsncId = message.getHeader("STTL_RQST_FUNC_DSNC_ID",String.class);
		
		if(ifid == null || ifid.trim().equals("")) throw new NotFoundInterfaceIdException();
		if(serviceId == null || serviceId.trim().length() <12 ) throw new InvalidServiceIdException();
		if(sttlRqstFuncDsncId == null || sttlRqstFuncDsncId.trim().length() < 3) throw new InvalidFunctionCodeException();
		
		String reqSvcID = serviceId;
		String resSvcID = "";
		String resSvcIDErr = "";
		String errorCode = "";
		String normalTxCode = serviceId+sttlRqstFuncDsncId;
		standardTelegram = RecoveryUtilAPI.makeRequestNormalHeaderMCA(ifid,reqSvcID,resSvcID, resSvcIDErr,errorCode,normalTxCode);
		//여기서 텔레그램 정보 기본 값 셋팅 하도록 함
		updateStandardTelegram(standardTelegram);
		logger.info("[IBK INTEGRATOR]___ standardTelegram without userData:{}",standardTelegram);
		
		//개별부 셋팅
		try {
			String messageData = null;
			if(message.getBody() instanceof byte[]) {
				messageData = new String(message.getBody(byte[].class));
			}else {
				messageData = message.getBody(String.class);
			}
			//covert json to map
			LinkedHashMap<String, Object> map = Converter.mapper.readValue(messageData, LinkedHashMap.class);
			UserData userData = new UserData();
			userData.setData(map);
			standardTelegram.setUserData(userData);
			
			logger.info("[IBK INTEGRATOR]___ new standardTelegram with userData:{}",standardTelegram);
		} catch (Exception e) {
			throw new IBKExceptionAPI(ErrorType.PARSING,e);
		}
		IBKMessage ibkMessage = new IBKMessage();
		ibkMessage.setBaseData(message.getBody());
		ibkMessage.setInterfaceId(ifid);
		ibkMessage.setStandardTelegram(standardTelegram);
		
		logger.info("[IBK INTEGRATOR]___ IBKMessage --> {}",ibkMessage);
		return ibkMessage;
	}
	/**
	 * @Author : d25856
	 * @Date   : 2019. 1. 9.
	 * @Method Name : updateStandardTelegram
	 * @Return : void
	 */
	private void updateStandardTelegram(StandardTelegram standardTelegram) {
		// TODO Auto-generated method stub
		
	}
	
	public void flatMessageParsing(Exchange exchange, String inOut) throws Exception {
		logger.info("[IBK INTEGRATOR]___ flatMessageParsing:::::::::::::::::::::::::::::::::::::::::::");
		Message message = exchange.getIn();
		byte[] messageData = message.getBody(byte[].class);
		String ifid = message.getHeader(ConstantCode.IBK_INTERFACE_ID,String.class);
		logger.info("[parsing before]:"+new String(messageData,"MS949"));
		logger.info("[Interface ID {}]:", ifid);
			

				
		StandardTelegram standardTelegram =ConverterServiceAPI.flatToTelegramAPI(messageData, getStructList(inOut, messageData));			
		//check 정상 거래 여부
		//1. 전문 정상 여부  Check
		//1.1 에러이면 개별부 없음
		String rspnPcrsDcd = standardTelegram.getSttlSysCopt().getRspnPcrsDcd();
		logger.info("[IBK INTEGRATOR]___ rspnPcrsDcd [{}]",rspnPcrsDcd);
		if(!rspnPcrsDcd.equals("0")) {
				IBKExceptionAPI ibkExceptionAPI = new IBKExceptionAPI(ErrorType.VALID);
				List<PnmgInfo> pnmgInfos = standardTelegram.getSttlMsgCopt().getPnmgInfo();
				
				String pnmgCd = pnmgInfos.get(0).getPnmgCd();
				String pnmgCon = pnmgInfos.get(0).getPnmgCon();
				logger.info("ERROR ----> ["+pnmgCd+"]"+pnmgCon);
				List<MsgErrorInfo> msgErrorInfos = standardTelegram.getSttlMsgCopt().getMsgErrorInfo();
				System.out.println(msgErrorInfos.size());
				
				IBKExceptionAPI exceptionAPI = new IBKExceptionAPI(ErrorType.VALID,"["+pnmgCd+"]"+pnmgCon+msgErrorInfos.get(0).getMsgErifCon());
				throw exceptionAPI;
		}
		IBKMessage ibkMessage = new IBKMessage();
		ibkMessage.setStandardTelegram(standardTelegram);
//		message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
		// 최종 셋
		message.setBody(ibkMessage);
		logger.info("[IBK INTEGRATOR]___ IBK Message -> "+ibkMessage);
		
	}
	public List<Tlgr> getStructList(String parsing, byte[] byteData) {
		
		IoCacheAPI ioCacheAPI = null;
		List<Tlgr> structList = null;
		
		// 인터페이스 아이디 추출
		byte[] intf = new byte[12];		
		System.arraycopy(byteData, 198, intf, 0, 12);		
		String interfaceId = new String(intf);
		logger.info("interfaceId --> {}",interfaceId);
/*		switch (parsing) {
		case "IN" :
		//	ioCacheMCA = resourceEAI.getSourceIo(interfaceId, parsing);
			
			ioCacheAPI = resourceAPI.getSourceIo(interfaceId, "IN");

			structList = ioCacheAPI.getInBound().getFieldList();
			break;
		case "OUT" :
			ioCacheAPI = resourceAPI.getSourceIo(interfaceId, "OUT");

			structList = ioCacheAPI.getOutBound().getFieldList();

			break;
			default :
				break;
		}*/
		
		return structList;
	}	
}
