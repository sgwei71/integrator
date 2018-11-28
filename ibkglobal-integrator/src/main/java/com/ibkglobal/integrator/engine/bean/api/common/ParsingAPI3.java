package com.ibkglobal.integrator.engine.bean.api.common;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.integrator.util.RecoveryUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceEAI;

@Component
public class ParsingAPI3 {
	
	@Autowired
	ConverterService converterService;
	
	@Autowired
	ResourceEAI resourceEAI;
	
	@Autowired
	ObjectMapper mapper;
	
	public void parsing(Exchange exchange) throws Exception {
		
		Message message = exchange.getIn();
		
		// IBK 메시지 셋
		IBKMessage ibkMessage = convert(message);
		
		// 최종 셋
		message.setBody(ibkMessage);
	}
	
	public IBKMessage convert(Message message) throws Exception {
		
		IBKMessage ibkMessage = new IBKMessage();
		
		// 원본 데이터 Set
		ibkMessage.setBaseData(message.getBody());
		
		StandardTelegram standardTelegram = null;
		standardTelegram = RecoveryUtil.makeRequestNormalHeader("ARSN", "KFTC", "ARSN_KFTC_0200",
				"FEPO1234", true, null, null, null, null, null, null);
		System.out.println("[PHJ]"+standardTelegram);
		String parsing = message.getHeader(ConstantCode.EAI_PARSING, String.class);
		
		try {			
			if (message.getBody() instanceof byte[]) {
				// Byte로 들어올 경우
				// 1. JSON인지 Flat인지 확인
				// 2. 형식에 맞게 변환
				byte[] byteData = message.getBody(byte[].class);
				
				String messageData = new String(byteData);
				
				// 2018.09.06  임시 주석
//				standardTelegram = JsonCheck(messageData) ? converterService.jsonToObject(messageData, StandardTelegram.class) 
//						                                  : converterService.flatToTelegram(byteData, getStructList(parsing, byteData));			
			} else {
				// JSON으로 들어올 경우(JSON으로 바로 변환)
				System.out.println("Test2"+message.getBody(String.class));
			//	Map<String, Object>
				//LinkedHashMap<String, Object> jsonToMap(String jsonStr)
				LinkedHashMap<String, Object> map = converterService.jsonToMap(message.getBody(String.class));
				//System.out.println("map"+map);
				//standardTelegram.setUserData(userData);
				UserData userData = new UserData();
				userData.setData(map);
				standardTelegram.setUserData(userData);
				//standardTelegram = converterService.jsonToObject(message.getBody(String.class), StandardTelegram.class);
				byte[] result = converterService.telegramToFlat(standardTelegram, null);
				//converterService.tel
			//	byte[] eaiFlat = converterParsingTest.telegramToEaiFlat(eaiStandardTeleram);
				
				// 결과
					
				System.out.println("표준전문 -> 대내 EAI 플랫전문 변환 결과 : " + new String(result, "MS949"));
			
			}
			System.out.println("Test");
			message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
			
			ibkMessage.setStandardTelegram(standardTelegram);
			System.out.println("[PHJ]"+standardTelegram);

		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.PARSING, "Parsing error : " + e.getMessage());
		}
		
		// 인터페이스 변경(기존 프로토콜로 보내는지, TO-BE로만 운영하는지, Composing 시 Flat으로 해야하는지)
	//	System.out.println("setInterface"+parsing);
		System.out.println("setInterface"+message);
		System.out.println("setInterface"+ibkMessage);
		
		setInterface("IN", message, ibkMessage);
		
		return ibkMessage;
	}
	
	/**
	 * 기존 EAI 시스템과의 연계 시 정보 변경
	 * @param parsing
	 * @param message
	 * @param ibkMessage
	 * @throws Exception
	 */
	public void setInterface(String parsing, Message message, IBKMessage ibkMessage) throws Exception {
		System.out.println("1setInterface"+ibkMessage.getStandardTelegram());
		System.out.println("1setInterface"+ibkMessage.getStandardTelegram().getSttlSysCopt());
		
		String interfaceId = "FEPO1234";
				
				//ibkMessage.getStandardTelegram().getSttlSysCopt().getSttlIntfId();
		//System.out.printnln
		// 기본 일 때
		ibkMessage.setInterfaceId(interfaceId);
		
		if (StringUtils.isEmpty(interfaceId)) {
			throw new IBKExceptionEAI(ErrorType.MESSAGE, "Can't find interface : Parsing.stringParsing : " + interfaceId);
		}
		
		// 인터페이스 정보 추출
		Interface intf = null;
		
		try {
			intf = resourceEAI.getInterface(interfaceId);
		} catch (Exception ex) {
			throw new IBKExceptionEAI(ErrorType.MESSAGE, "Message Search Error : " + ibkMessage.getInterfaceId());
		}
		
		// 국내로 보내는 전문인지 확인
		String domesticKey = intf.getInterfaceType().getSource().getProcessType().getOnline().getInternal().getMmsIntfId();		
		String eaiKey      = resourceEAI.getEaiKey(domesticKey);
		
		// EAI 전문에 포함 될 때
		switch (parsing) {
		case "IN" :
			if (!StringUtils.isEmpty(eaiKey)) {
				// 전문의 인터페이스 변경(국내 EAI 인터페이스로 변경)
				ibkMessage.getStandardTelegram().getSttlSysCopt().setSttlIntfId(domesticKey);
				
				// EAI로 보낼 때는 컴포징 선언 필요
				message.setHeader(ConstantCode.EAI_COMPOSING, "Y");
			}
			break;
		case "OUT" :
			if (!StringUtils.isEmpty(eaiKey)) {
				// 전문의 인터페이스 변경(SMR 인터페이스로 변경)
				ibkMessage.setInterfaceId(eaiKey);
				ibkMessage.getStandardTelegram().getSttlSysCopt().setSttlIntfId(eaiKey);
			}
			break;
			default :
				break;
		}		
	}
	
	public List<Tlgr> getStructList(String parsing, byte[] byteData) {
		
		IoCacheMCA ioCacheMCA = null;
		List<Tlgr> structList = null;
		
		// 인터페이스 아이디 추출
		byte[] intf = new byte[12];		
		System.arraycopy(byteData, 198, intf, 0, 12);		
		String interfaceId = resourceEAI.getEaiKey(new String(intf));
		
		switch (parsing) {
		case "IN" :
			ioCacheMCA = resourceEAI.getSourceIo(interfaceId, parsing);
			structList = ioCacheMCA.getInBound().getFieldList();
			break;
		case "OUT" :
			ioCacheMCA = resourceEAI.getTargetIo(interfaceId, parsing);
			structList = ioCacheMCA.getOutBound().getFieldList();
			break;
			default :
				break;
		}
		
		return structList;
	}
	
	public boolean JsonCheck(String messageData) {	
		
		boolean check = true;
		
		try {
			if (!(messageData.charAt(0) == '{' 
					&& messageData.charAt(messageData.length() - 1) == '}')) {
				check = false;
			}
		} catch (Exception e) {
			check = false;
		}
		
		return check;
	}
}
