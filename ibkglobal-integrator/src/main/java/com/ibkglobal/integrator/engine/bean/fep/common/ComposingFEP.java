package com.ibkglobal.integrator.engine.bean.fep.common;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.ParsingType;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceFEP;

public class ComposingFEP {
	
	@Autowired
	ConverterService converterService;
	
	@Autowired
	ResourceFEP resourceFEP;
	
	public void composing(Exchange exchange) throws IBKExceptionFEP {
		
		try {			
			Message message = exchange.getIn();
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);
			
			ParsingType type = message.getHeader(ConstantCode.PARSING_TYPE, ParsingType.class);
			
			switch (type) {
			case JSON :
				message.setHeader("Content-Type", "application/json;charset=UTF-8");
				message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()));
				break;
			case JSON_FLAT :
				message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()).getBytes());
				break;
			case FLAT :
				message.setBody(userDataToFlat(ibkMessage));
				break;
			case ISO :
				message.setBody(converterService.userDataToIso(ibkMessage.getStandardTelegram().getUserData().getData()));
				break;
			case DELIMITER :
				message.setBody(converterService.userDataToDelimiter("|", ibkMessage.getHeader(), ibkMessage.getBody()));
				break;
			default :
				break;
			}
		} catch (Exception e) {
			throw new IBKExceptionFEP(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}
	
	public byte[] userDataToFlat(IBKMessage ibkMessage) {
		
		UserData userData = ibkMessage.getStandardTelegram().getUserData();
		
		List<byte[]> flatList = new LinkedList<>();
		
		// 헤더 정보
		LinkedList<IoCacheFEP> headerList = resourceFEP.getTargetHeaderEntity(ibkMessage.getInterfaceId());
		
		headerList.forEach(header -> {
			converterService.mapToFlat(flatList, userData.getData(), header.getData().getFieldList());
		});
		
		// 바디 정보
		IoCacheFEP body = resourceFEP.getTargetBodyEntity(ibkMessage.getInterfaceId());
		
		converterService.mapToFlat(flatList, userData.getData(), body.getData().getFieldList());
		
		// 최종 결과 길이
		Integer length = flatList.stream()
                                 .mapToInt(p -> p.length)
                                 .sum();
		
		// 최종 결과 리턴
		ByteBuffer buffer = ByteBuffer.allocate(length);
		
		flatList.forEach(flat -> {
			buffer.put(flat);
		});
		
		return buffer.array();
	}
}
