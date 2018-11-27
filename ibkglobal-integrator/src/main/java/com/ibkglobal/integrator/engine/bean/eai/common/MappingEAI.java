package com.ibkglobal.integrator.engine.bean.eai.common;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.integrator.config.TelegramConstants;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionEAI;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceEAI;

public class MappingEAI {
	
	@Autowired
	ResourceEAI resourceEAI;
	
	@Autowired
	ConverterService converterService;
	
	public void mapping(Exchange exchange) throws IBKExceptionEAI {
		
		Message message = exchange.getIn();
		IBKMessage ibkMessage = null;
		
		try {
			ibkMessage = message.getBody(IBKMessage.class);
			
			String interfaceId  = ibkMessage.getInterfaceId();	
			Interface intf      = resourceEAI.getInterface(interfaceId);
			
			String mappingCheck = intf.getCommon().getAttribute().getMpngYn().getCode();
			
			// 매핑 여부 확인
			if ("Y".equals(mappingCheck)) {
				List<TlgrMapping> mappingList = null;
				List<Tlgr>        targetList  = null;
				
				// 개별부 데이터가 있을 때 개별부 매핑 실행
				if (ibkMessage.getStandardTelegram().getUserData() != null  && 
					ibkMessage.getStandardTelegram().getUserData().getData() != null) {
					
					// 시스템 공통부 입력전문유형구분코드
					String inptTmgtDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd();
					
					// 'S'=요청 , 'R'=응답, 'K'=ACK
					String rqstRspnDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getRqstRspnDcd();
					
					switch (rqstRspnDcd) {
					case "S" :
						mappingList = resourceEAI.getMappingList(ibkMessage.getInterfaceId(), "IN");
						targetList  = resourceEAI.getTargetIo(ibkMessage.getInterfaceId(), "OUT").getInBound().getFieldList();
						
						converterService.mapping(mappingList, ibkMessage.getBody(), targetList, "IN", isNullable(inptTmgtDcd));
						break;
					case "R" :
						mappingList = resourceEAI.getMappingList(ibkMessage.getInterfaceId(), "OUT");
						targetList  = resourceEAI.getSourceIo(ibkMessage.getInterfaceId(), "IN").getOutBound().getFieldList();
						
						converterService.mapping(mappingList, ibkMessage.getBody(), targetList, "OUT", isNullable(inptTmgtDcd));
						break;
						default :
							break;
					}
				}
			}
		} catch (Exception e) {
			throw new IBKExceptionEAI(ErrorType.MAPPING, "Mapping Error : " + ibkMessage.getInterfaceId(), e);
		}
	}
	
	/**
	 * Is null able
	 * 
	 * @param code
	 * @return
	 */
	private boolean isNullable(String code) throws IBKExceptionMCA {
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
