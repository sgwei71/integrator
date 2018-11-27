package com.ibkglobal.integrator.engine.bean.eai.common;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.model.Io;
import com.ibkglobal.integrator.config.TelegramConstants;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceEAI;

public abstract class ProcessDefaultEAI {

	@Autowired
	ResourceEAI resourceEAI;

	@Autowired
	ConverterService converterService;

	public void bodyParsing(IBKMessage ibkMessage) throws IBKExceptionEAI {

		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		LinkedHashMap<String, Object> body = new LinkedHashMap<>();
		Io io = null;

		String mpngYn = null;

		try {
			// bypass 여부(Y / N)
			mpngYn = resourceEAI.getInterface(ibkMessage.getInterfaceId()).getCommon().getAttribute().getMpngYn()
					.getCode();

			if ("Y".contains(mpngYn)) {
				if (ibkMessage.getStandardTelegram().getUserData() != null
						&& ibkMessage.getStandardTelegram().getUserData().getData() != null) {
					data = ibkMessage.getStandardTelegram().getUserData().getData();
				}
				
				// 'S'=요청 , 'R'=응답, 'K'=ACK
				String rqstRspnDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getRqstRspnDcd();
				if ("S".equals(rqstRspnDcd)) {
					io = resourceEAI.getSourceIo(ibkMessage.getInterfaceId(), "IN").getInBound();
				} else if ("R".equals(rqstRspnDcd)) {
					io = resourceEAI.getTargetIo(ibkMessage.getInterfaceId(), "OUT").getOutBound();
				}
			}

			if (io != null) {
				String inptTmgtDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd();
				body.putAll(converterService.listToMap(data, io.getFieldList(), isNullable(inptTmgtDcd)));
				ibkMessage.setBody(body);
			}
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.MESSAGE, "Message Search Error : " + ibkMessage.getInterfaceId());
		}
	}
	
	public void serviceSet(IBKMessage ibkMessage) throws IBKExceptionEAI {
		
		try {
			StandardTelegram standardTelegram = ibkMessage.getStandardTelegram();
			
			Interface intf = resourceEAI.getInterface(ibkMessage.getInterfaceId());
			
			standardTelegram.getSttlSysCopt().setRqstRcvSvcId(intf.getCommon().getAttribute().getOnline().getInternal().getSvcId());
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.MESSAGE, "Message Service Set Eror : " + ibkMessage.getInterfaceId());
		}
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

	/**
	 * 기존 EAI 시스템과의 연계 시 정보 변경
	 * @param parsing
	 * @param message
	 * @param ibkMessage
	 * @throws IBKExceptionEAI
	 */
	public void checkInterface(String parsing, IBKMessage ibkMessage) throws IBKExceptionEAI {

		String interfaceId = ibkMessage.getStandardTelegram().getSttlSysCopt().getSttlIntfId();

		// 인터페이스 정보 추출
		Interface intf = null;
		
		try {
			intf = resourceEAI.getInterface(interfaceId);
		} catch (Exception ex) {
			throw new IBKExceptionEAI(ErrorType.MESSAGE, "Message Search Error : " + ibkMessage.getInterfaceId());
		}
		
		// 국내로 보내는 전문인지 확인
		String domesticKey = intf.getInterfaceType().getSource().getProcessType().getOnline().getInternal().getMmsIntfId();
		String eaiKey = resourceEAI.getEaiKey(domesticKey);
		
		// EAI 전문에 포함 될 때
		switch (parsing) {
		case "IN":
			if (!StringUtils.isEmpty(eaiKey)) {
				// 전문의 인터페이스 변경(국내 EAI 인터페이스로 변경)
				ibkMessage.getStandardTelegram().getSttlSysCopt().setSttlIntfId(domesticKey);
			}
			break;
		case "OUT":
			if (!StringUtils.isEmpty(eaiKey)) {
				// 전문의 인터페이스 변경(SMR 인터페이스로 변경)
				ibkMessage.setInterfaceId(eaiKey);
				ibkMessage.getStandardTelegram().getSttlSysCopt().setSttlIntfId(eaiKey);
			}
			break;
		default:
			break;
		}
	}
}
