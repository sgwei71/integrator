package com.ibkglobal.integrator.engine.bean.api.common;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.io.model.Io;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.TelegramConstants;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.RecoveryUtil;
import com.ibkglobal.integrator.util.RecoveryUtilAPI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.converter.service.ConverterServiceAPI;
import com.ibkglobal.message.struct.resource.ResourceMCA;

@Component
public class ParsingAPI {

	@Autowired
	ResourceMCA resourceMCA;

	
	@Autowired
	ConverterServiceAPI converterService;
	
	public void parsing(Exchange exchange) throws Exception {

		Message message = exchange.getIn();
		
		// IBK 메시지 셋
		IBKMessage ibkMessage = convert(message);

		// 최종 셋
		message.setBody(ibkMessage);
	}
	
	// input (json -> IBKMessage)
	public IBKMessage convert(Message message) throws Exception {		
		byte[] messageData = null;
		
		messageData = message.getBody(byte[].class);

		ByteBuffer messageBuffer = ByteBuffer.wrap(messageData);
		// 전문 파싱
		IBKMessage ibkMessage = jsonParsing(message);
		
		System.out.println("IBKMessage="+ibkMessage);	
		
		return ibkMessage;
	}
	//기본 헤더 셋팅 + userData 
	public IBKMessage jsonParsing(Message message) throws Exception {
		//Json 파싱해서 표준전문 만들고 IBKMessage     
		StandardTelegram standardTelegram = null;
	
		//일단 기본값 셋팅makeRequestNormalHeader   
		String ifid = message.getHeader(ConstantCode.IBK_INTERFACE_ID,String.class);
		System.out.println("jsonParsing:ifid"+ifid);
		
		//불러와야 하는데 일단 기본값 셋팅 한번 수행 
		String reqSvcID = "CBKCOM915000";
		String resSvcID = "";
		String resSvcIDErr = "";
		String errorCode = "";
		String normalTxCode = "CBKCOM915000";
		
		//==================================
		standardTelegram = RecoveryUtilAPI.makeRequestNormalHeaderMCA(ifid,reqSvcID,resSvcID, resSvcIDErr,errorCode,normalTxCode);

		System.out.println("[PHJ]jsonParsing[default StandardTelegram]:"+standardTelegram);
		
		try {
			// 파싱 실행
			String messageData = null;
			if (message.getBody() instanceof byte[]) {
				messageData = new String(message.getBody(byte[].class));
			} else {
				messageData = message.getBody(String.class);
			}

			//UserData Setting
			LinkedHashMap<String, Object> map = converterService.jsonToMap(message.getBody(String.class));
			UserData userData = new UserData();
			userData.setData(map);
			standardTelegram.setUserData(userData);
			System.out.println("[PHJ]jsonParsing[UserData Setting After]:"+standardTelegram);
			message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.PARSING, "Parsing error : " + e.getMessage());
		}
		
		// 2018.10.02 임시 하드코딩 (ITRO00000035) 비드때문
		if (StringUtils.isEmpty(ifid)) {
			throw new IBKExceptionMCA(ErrorType.MESSAGE, "Can't find interface : Parsing.stringParsing : " + ifid);
		}
		
		IBKMessage ibkMessage = new IBKMessage();
		
		// 원본 데이터 Set
		ibkMessage.setBaseData(message.getBody());
		ibkMessage.setInterfaceId(ifid);
		ibkMessage.setStandardTelegram(standardTelegram);
		ibkMessage.setInterfaceId(ifid);

		InfraType infraType = message.getHeader(ConstantCode.INFRA_TYPE, InfraType.class);
		
		//IO 기준으로 메세지 매핑 
		bodyParsing(ibkMessage);
		
		System.out.println("Test:ibkMessage"+ibkMessage);
		return ibkMessage;
	}

	private void bodyParsing(IBKMessage ibkMessage) throws Exception {

		LinkedHashMap<String, Object> data = ibkMessage.getStandardTelegram().getUserData().getData();
		LinkedHashMap<String, Object> body = new LinkedHashMap<>();
		Io io = null;

		String mpngYn = null;

		try {
			// bypass 여부(Y / N)
			System.out.println("[PHJ]bodyParsing:"+data);
			mpngYn = "Y";
			String rqstRspnDcd="S";
			//mpngYn = resourceMCA.getInterface(ibkMessage.getInterfaceId()).getCommon().getAttribute().getMpngYn().getCode();
			
			if ("Y".contains(mpngYn)) {
				// 'S'=요청 , 'R'=응답, 'K'=ACK
				//String rqstRspnDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getRqstRspnDcd();
				if ("S".equals(rqstRspnDcd)) {
					System.out.println("[PHJ]bodyParsing2:"+ ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd());
					io = resourceMCA.getSourceIo("ITRO00000035", "IN").getInBound();

//					io = resourceMCA.getSourceIo(ibkMessage.getInterfaceId(), "IN").getInBound();
				} else if ("R".equals(rqstRspnDcd)) {
					io = resourceMCA.getTargetIo(ibkMessage.getInterfaceId(), "OUT").getOutBound();
				}
			}
			System.out.println("[PHJ]bodyParsing2.2:"+ ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd());

			if (io != null) {
				System.out.println("[PHJ]bodyParsing3:"+ ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd());
				
				String inptTmgtDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd();
				
				System.out.println("[PHJ]bodyParsing4body:"+ data);
				
				body.putAll(converterService.listToMap(data, io.getFieldList(), isNullable(inptTmgtDcd)));
				System.out.println("[PHJ]bodyParsing4body:"+ body);
				
				ibkMessage.setBody(body);
			}
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.MESSAGE, "Message Search Error : " + ibkMessage.getInterfaceId());
		}
	}
	public void parsingFromHost(Exchange exchange ) throws Exception {
		Message message = exchange.getIn();
		byte[] messageData = message.getBody(byte[].class);
		
		System.out.println("[parsing before]:"+new String(messageData));
		StandardTelegram standardTelegram = //converterService.flatToTelegram(messageData, null);
		converterService.flatToTelegramAPI(messageData, getStructList("OUT", messageData));			
			
		System.out.println("[parsing after- flatToTelegram]:"+standardTelegram);
		IBKMessage ibkMessage = new IBKMessage();
		ibkMessage.setStandardTelegram(standardTelegram);
		message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
		// 최종 셋
		message.setBody(ibkMessage);
		System.out.println("[End parsing]:"+ibkMessage);
			
	}
	public IBKMessage convertFromHost(Message message) {
		System.out.println("[PHJ]ParsingAPI convertFromHost");
		IBKMessage ibkMessage = new IBKMessage();
		
		// 원본 데이터 Set
		ibkMessage.setBaseData(message.getBody());
		
		StandardTelegram standardTelegram = null;
		
		String parsing = message.getHeader(ConstantCode.EAI_PARSING, String.class);
		
		try {			
			if (message.getBody() instanceof byte[]) {
				// Byte로 들어올 경우
				// 1. JSON인지 Flat인지 확인
				// 2. 형식에 맞게 변환
				byte[] byteData = message.getBody(byte[].class);
				
				String messageData = new String(byteData);
				System.out.println("[PHJ]ParsingAPI convertFromHost2");
					
				// 2018.09.06  임시 주석
				System.out.println(getStructList("IN", byteData));
				standardTelegram = converterService.flatToTelegram(byteData, getStructList("IN", byteData));			
				System.out.println("[PHJ]ParsingAPI convertFromHost2");
				
			} else {
				// JSON으로 들어올 경우(JSON으로 바로 변환)
				standardTelegram = converterService.jsonToObject(message.getBody(String.class), StandardTelegram.class);
			}
			
			message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
			ibkMessage.setStandardTelegram(standardTelegram);
		} catch (Exception e) {
			try {
				throw new IBKExceptionEAI(ErrorType.PARSING, "Parsing error : " + e.getMessage());
			} catch (IBKExceptionEAI e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		// 인터페이스 변경(기존 프로토콜로 보내는지, TO-BE로만 운영하는지, Composing 시 Flat으로 해야하는지)
	//	setInterface(parsing, message, ibkMessage);
		
		return ibkMessage;
	
	}
	
	
	public List<Tlgr> getStructList(String parsing, byte[] byteData) {
		
		IoCacheMCA ioCacheMCA = null;
		List<Tlgr> structList = null;
		
		// 인터페이스 아이디 추출
		byte[] intf = new byte[12];		
		//System.arraycopy(byteData, 198, intf, 0, 12);		
		//String interfaceId = resourceEAI.getEaiKey(new String(intf));
		
		switch (parsing) {
		case "IN" :
		//	ioCacheMCA = resourceEAI.getSourceIo(interfaceId, parsing);
			ioCacheMCA = resourceMCA.getSourceIo("ITRO00000035", "IN");

			structList = ioCacheMCA.getInBound().getFieldList();
			break;
		case "OUT" :
			ioCacheMCA = resourceMCA.getSourceIo("ITRO00000035", "OUT");

			structList = ioCacheMCA.getInBound().getFieldList();

			break;
			default :
				break;
		}
		
		return structList;
	}
	/**
	 * Is null able
	 * 
	 * @param code
	 * @return
	 */
	private boolean isNullable(String code) {
		boolean nullable = true;
		TelegramConstants.InptTmgtDcd inptTmgtDcd = TelegramConstants.InptTmgtDcd.of(code);

		switch (inptTmgtDcd) {
		case MASS_OUT_CONTINUE_REQUEST:
		case MASS_OUT_STOP_REQUEST:
			nullable = false;
			break;
		default:
			nullable = true;
			break;
		}
		return nullable;
	}
}
