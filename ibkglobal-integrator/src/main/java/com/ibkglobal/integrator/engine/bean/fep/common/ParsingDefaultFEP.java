package com.ibkglobal.integrator.engine.bean.fep.common;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibk.ibkglobal.data.io.model.Header;
import com.ibk.ibkglobal.data.io.model.Io;
import com.ibk.ibkglobal.data.io.model.IoElement;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.TelegramConstants;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceFEP;

public abstract class ParsingDefaultFEP {

	@Autowired
	ResourceFEP resourceFEP;

	@Autowired
	ConverterService converterService;

	public abstract void parsing(Exchange exchange) throws IBKExceptionFEP;

	/**
	 * Json일 경우 헤더 파싱
	 * 
	 * @param ibkMessage
	 * @return
	 */
	public LinkedHashMap<String, Object> jsonHeaderParsing(IBKMessage ibkMessage) {

		LinkedHashMap<String, Object> userData = ibkMessage.getStandardTelegram().getUserData().getData();
		LinkedHashMap<String, Object> headerData = new LinkedHashMap<>();

		String inptTmgtDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd();
		boolean nullable = isNullable(inptTmgtDcd);

		for (IoCacheFEP entity : resourceFEP.getSourceHeaderEntity(ibkMessage.getInterfaceId())) {
			headerData.putAll(converterService.listToMap(userData, entity.getData().getFieldList(), nullable));
		}

		return headerData;
	}

	/**
	 * Json일 경우 바디 파싱
	 * 
	 * @param ibkMessage
	 * @return
	 */
	public LinkedHashMap<String, Object> jsonBodyParsing(IBKMessage ibkMessage) {

		LinkedHashMap<String, Object> data = ibkMessage.getStandardTelegram().getUserData().getData();
		LinkedHashMap<String, Object> bodyData = new LinkedHashMap<>();
		Io io = null;

		String mpngYn = null;

		// bypass 여부(Y / N)
		mpngYn = resourceFEP.getInterface(ibkMessage.getInterfaceId()).getCommon().getAttribute().getMpngYn().getCode();

		if ("Y".contains(mpngYn)) {
			io = resourceFEP.getSourceBodyEntity(ibkMessage.getInterfaceId()).getData();
		}

		if (io != null) {
			String inptTmgtDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd();
			bodyData.putAll(converterService.listToMap(data, io.getFieldList(), isNullable(inptTmgtDcd)));
		}

		return bodyData;
	}

	public Header getHeader(Message message) throws IBKExceptionFEP {

		String headerID = message.getHeader(ConstantCode.HEADER_ID, String.class);

		Header header = resourceFEP.getHeaderFEP().get(headerID).getData();
		if (header == null) {
			throw new IBKExceptionFEP(ErrorType.MESSAGE, "Can't find header [" + headerID + "]");
		}

		return header;
	}

	public List<Tlgr> getHeaderList(Header header) {

		List<Tlgr> headerIoList = new LinkedList<>();

		for (IoElement element : header.getSequence()) {
			IoCacheFEP io = resourceFEP.getIoFEP().get(element.getType());
			headerIoList.addAll(io.getData().getFieldList());
		}

		return headerIoList;
	}

	public String getInterfaceId(Message message, Header header, LinkedHashMap<String, Object> headerData) throws IBKExceptionFEP {
		
		// 1:N 관계의 인터페이스를 업무코드로 찾는다.
		String workCode = resourceFEP.getWorkCode(header, headerData); // result ex) workCode = "KFKF_KFTC_E201809_200";
		
		String interfaceId = resourceFEP.getInterfaceID(workCode);
		
		message.setHeader(ConstantCode.WORK_CODE, workCode);
		
		if (StringUtils.isEmpty(interfaceId)) {
			throw new IBKExceptionFEP(ErrorType.MESSAGE, "Can't find interface : workcode=[" + workCode + "], header=[" + header + "]");
		}
		
		return interfaceId;
	}

	/**
	 * Is null able
	 * 
	 * @param code
	 * @return
	 */
	public boolean isNullable(String code) {
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
