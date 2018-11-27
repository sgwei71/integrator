package com.ibkglobal.integrator.engine.bean.fep.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionFEP;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceFEP;

public class MappingFEP {
	
	@Autowired
	ResourceFEP resourceFEP;
	
	@Autowired
	ConverterService converterService;
	
	/**
	 * Do Mapping
	 * @param exchange
	 * @throws IBKExceptionFEP
	 */
	public void doMapping(Exchange exchange) throws IBKExceptionFEP {
		try {
			Message in = exchange.getIn();
			IBKMessage ibkMessage = in.getBody(IBKMessage.class);
			
			fepMapping(ibkMessage);
			
			in.setBody(ibkMessage);
		} catch (Exception e) {
			throw new IBKExceptionFEP(ErrorType.MAPPING, e.getMessage(), e);
		}
		
	}
	
	/**
	 * FEP Mapping
	 * @param ibkMessage
	 * @throws IBKExceptionFEP
	 */
	private void fepMapping(IBKMessage ibkMessage) throws IBKExceptionFEP {
		try {
			// 매핑 여부 확인
			if ("Y".equals(resourceFEP.getInterface(ibkMessage.getInterfaceId()).getCommon().getAttribute().getMpngYn().getCode())) {
				for (IoCacheFEP entity : resourceFEP.getTargetHeaderEntity(ibkMessage.getInterfaceId())) {
					ibkMessage.setHeader(
							converterService.mapping(
									resourceFEP.getMappingList(ibkMessage.getInterfaceId()),
									ibkMessage.getHeader(), 
									entity.getData().getFieldList(), "IN", true)
							);
				}
				
				ibkMessage.setBody(converterService.mapping(
						resourceFEP.getMappingList(ibkMessage.getInterfaceId())
						, ibkMessage.getBody()
						, resourceFEP.getTargetBodyEntity(ibkMessage.getInterfaceId()).getData().getFieldList(), "IN", true));	
			}
		} catch (Exception e) {
			throw new IBKExceptionFEP(ErrorType.MAPPING, e.getMessage(), e);
		}
	}
}
