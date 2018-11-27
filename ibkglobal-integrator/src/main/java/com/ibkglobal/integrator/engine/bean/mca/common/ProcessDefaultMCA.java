package com.ibkglobal.integrator.engine.bean.mca.common;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.io.model.Io;
import com.ibkglobal.integrator.config.TelegramConstants;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.converter.service.ConverterService;
import com.ibkglobal.message.struct.resource.ResourceMCA;

public abstract class ProcessDefaultMCA {

	@Autowired
	ResourceMCA resourceMCA;

	@Autowired
	ConverterService converterService;

	public void bodyParsing(IBKMessage ibkMessage) throws IBKExceptionMCA {

		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		LinkedHashMap<String, Object> body = new LinkedHashMap<>();
		Io io = null;

		String mpngYn = null;

		try {
			// bypass 여부(Y / N)
			mpngYn = resourceMCA.getInterface(ibkMessage.getInterfaceId()).getCommon().getAttribute().getMpngYn()
					.getCode();

			if ("Y".contains(mpngYn)) {
				if (ibkMessage.getStandardTelegram().getUserData() != null
						&& ibkMessage.getStandardTelegram().getUserData().getData() != null) {
					data = ibkMessage.getStandardTelegram().getUserData().getData();
				}

				// 'S'=요청 , 'R'=응답, 'K'=ACK
				String rqstRspnDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getRqstRspnDcd();
				if ("S".equals(rqstRspnDcd)) {
					io = resourceMCA.getSourceIo(ibkMessage.getInterfaceId(), "IN").getInBound();
				} else if ("R".equals(rqstRspnDcd)) {
					io = resourceMCA.getTargetIo(ibkMessage.getInterfaceId(), "OUT").getOutBound();
				}
			}

			if (io != null) {
				String inptTmgtDcd = ibkMessage.getStandardTelegram().getSttlSysCopt().getInptTmgtDcd();
				body.putAll(converterService.listToMap(data, io.getFieldList(), isNullable(inptTmgtDcd)));
				ibkMessage.setBody(body);
			}
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.MESSAGE, "Message Search Error : " + ibkMessage.getInterfaceId());
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
}
