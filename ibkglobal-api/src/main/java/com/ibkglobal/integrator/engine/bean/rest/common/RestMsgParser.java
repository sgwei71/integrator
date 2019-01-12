package com.ibkglobal.integrator.engine.bean.rest.common;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Bytes;
import com.ibk.ibkglobal.data.io.IoCacheAPI;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionAPI;
import com.ibkglobal.integrator.util.RecoveryUtilAPI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt.MsgErrorInfo;
import com.ibkglobal.message.common.normal.copt.SttlMsgCopt.PnmgInfo;
import com.ibkglobal.message.converter.service.ConverterServiceAPI;
import com.ibkglobal.message.struct.resource.ResourceAPI;

import lombok.Getter;

@Component
public class RestMsgParser {

	@Autowired 
	@Getter
	ResourceAPI	resourceAPI;
	
	@Autowired
	@Getter
	ConverterServiceAPI converterService;
	
	private Logger logger = LoggerFactory.getLogger(RestMsgParser.class);
	
	/**json -> IBKMessage
	 * @param exchange
	 * @throws IBKExceptionAPI
	 */
	public void encode(Exchange exchange) throws IBKExceptionAPI {
		
		Message message = null;
		IBKMessage ibkMessage = null;	

		try {
			message = exchange.getIn();
			ibkMessage = convert(message);
			message.setBody(ibkMessage);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	// input (JSON -> IBKMessage)
	public IBKMessage convert(Message message) throws IBKExceptionAPI {
		
		IBKMessage ibkMessage = jsonParsing(message);
		logger.info("json -> IBKMessage : ",ibkMessage);
		return ibkMessage;
	}
	//기본헤더 셋팅 + userData
	public IBKMessage jsonParsing(Message message) throws IBKExceptionAPI {
		// JSON 파싱해서 표준 전문 만들고 IBKMessage 생성
		StandardTelegram standardTelegram = null;
		
		//일단 기본값 셋팅
		String ifid = message.getHeader(ConstantCode.IBK_INTERFACE_ID,String.class);
		String serviceId = message.getHeader("SERVICE_ID",String.class);
		String sttlRqstFuncDsncId = message.getHeader("STTL_RQST_FUNC_DSNC_ID",String.class);
		
		if(ifid == null || ifid.trim().equals("")) throw new IBKExceptionAPI(ErrorType.VALID,"인터페이스 아이디가 없습니다.");
		if(serviceId == null || serviceId.trim().length() <12 ) throw new IBKExceptionAPI(ErrorType.VALID,"서비스아이디["+serviceId+"]가  없습니다.");
		if(sttlRqstFuncDsncId == null || sttlRqstFuncDsncId.trim().length() < 3) throw new IBKExceptionAPI(ErrorType.VALID,"기능 구분 아이디["+sttlRqstFuncDsncId+"]  없습니다.");
		
		String reqSvcID = serviceId;
		String resSvcID = "";
		String resSvcIDErr = "";
		String errorCode = "";
		String normalTxCode = serviceId+sttlRqstFuncDsncId;
		standardTelegram = RecoveryUtilAPI.makeRequestNormalHeaderMCA(ifid,reqSvcID,resSvcID, resSvcIDErr,errorCode,normalTxCode);
		//여기서 텔레그램 정보 기본 값 셋팅 하도록 함
		updateStandardTelegram(standardTelegram);
		logger.info("new standardTelegram without userData:{}",standardTelegram);
		
		//개별부 셋팅
		try {
			String messageData = null;
			if(message.getBody() instanceof byte[]) {
				messageData = new String(message.getBody(byte[].class));
			}else {
				messageData = message.getBody(String.class);
			}
			//covert json to map
			LinkedHashMap<String, Object> map = converterService.jsonToMap(messageData);
			UserData userData = new UserData();
			userData.setData(map);
			standardTelegram.setUserData(userData);
			
			logger.info("new standardTelegram with userData:{}",standardTelegram);
		} catch (Exception e) {
			throw new IBKExceptionAPI(ErrorType.PARSING,e);
		}
		IBKMessage ibkMessage = new IBKMessage();
		ibkMessage.setBaseData(message.getBody());
		ibkMessage.setInterfaceId(ifid);
		ibkMessage.setStandardTelegram(standardTelegram);
		
		logger.info("IBKMessage --> {}",ibkMessage);
		return ibkMessage;
	}
	private void updateStandardTelegram(StandardTelegram standardTelegram) {
		// TODO 전문 값 셋팅
	}
	public void parsingFromHost(Exchange exchange) throws Exception {
		logger.info(":::::::::::::::::::::::::::RestMsgParser.parsingFromHost:::::::::::::::::::::::::::::::::::::::::::");
		Message message = exchange.getIn();
		byte[] messageData = message.getBody(byte[].class);
		String ifid = message.getHeader(ConstantCode.IBK_INTERFACE_ID,String.class);
		logger.info("[parsing before]:"+new String(messageData,"MS949"));
		logger.info("[Interface ID {}]:", ifid);
			

				
		StandardTelegram standardTelegram =converterService.flatToTelegramAPI(messageData, getStructList("OUT", messageData));			
		//check 정상 거래 여부
		//1. 전문 정상 여부  Check
		//1.1 에러이면 개별부 없음
		String rspnPcrsDcd = standardTelegram.getSttlSysCopt().getRspnPcrsDcd();
		System.out.println("========================================>"+rspnPcrsDcd);
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
		logger.info("[parsing after- flatToTelegram]:"+standardTelegram);
		IBKMessage ibkMessage = new IBKMessage();
		ibkMessage.setStandardTelegram(standardTelegram);
		message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
		// 최종 셋
		message.setBody(ibkMessage);
		logger.info("[End parsing]:"+ibkMessage);
		
	}
//	public IBKMessage convertFromHost(Message message) {
//		logger.info("[PHJ]ParsingAPI convertFromHost");
//		IBKMessage ibkMessage = new IBKMessage();
//		
//		// 원본 데이터 Set
//		ibkMessage.setBaseData(message.getBody());
//		
//		StandardTelegram standardTelegram = null;
//		
//		String parsing = message.getHeader(ConstantCode.EAI_PARSING, String.class);
//		
//		try {			
//			if (message.getBody() instanceof byte[]) {
//				// Byte로 들어올 경우
//				// 1. JSON인지 Flat인지 확인
//				// 2. 형식에 맞게 변환
//				byte[] byteData = message.getBody(byte[].class);
//				
//				String messageData = new String(byteData);
//				logger.info("[PHJ]ParsingAPI convertFromHost2");
//					
//				// 2018.09.06  임시 주석
//				logger.info(getStructList("IN", byteData));
//				standardTelegram = converterService.flatToTelegram(byteData, getStructList("IN", byteData));			
//				logger.info("[PHJ]ParsingAPI convertFromHost2");
//				
//			} else {
//				// JSON으로 들어올 경우(JSON으로 바로 변환)
//				standardTelegram = converterService.jsonToObject(message.getBody(String.class), StandardTelegram.class);
//			}
//			
//			message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
//			ibkMessage.setStandardTelegram(standardTelegram);
//		} catch (Exception e) {
//			try {
//				throw new IBKExceptionAPI(ErrorType.PARSING, "Parsing error : " + e.getMessage());
//			} catch (IBKExceptionAPI e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		
//		// 인터페이스 변경(기존 프로토콜로 보내는지, TO-BE로만 운영하는지, Composing 시 Flat으로 해야하는지)
//	//	setInterface(parsing, message, ibkMessage);
//		
//		return ibkMessage;
//	
//	}
	public List<Tlgr> getStructList(String parsing, byte[] byteData) {
		
		IoCacheAPI ioCacheAPI = null;
		List<Tlgr> structList = null;
		
		// 인터페이스 아이디 추출
		byte[] intf = new byte[12];		
		System.arraycopy(byteData, 198, intf, 0, 12);		
		String interfaceId = new String(intf);
		logger.info("interfaceId --> {}",interfaceId);
		switch (parsing) {
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
		}
		
		return structList;
	}	
}
