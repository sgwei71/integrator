package com.ibkglobal.integrator.engine.bean.api.log;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.log.LogManager;
import com.ibkglobal.integrator.log.LogType;
import com.ibkglobal.integrator.log.model.LogEAI;
import com.ibkglobal.integrator.util.PackedNumericUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;

import ch.qos.logback.classic.Logger;

public class LoggingAPI {

	@Autowired
	ConverterService converterService;
	
	public void logging(Exchange exchange) throws Exception {
		
		Message message = exchange.getIn();
		
		String seq      = message.getHeader(ConstantCode.SEQ, String.class);
		String logName  = message.getHeader(ConstantCode.LOGGER_KEY, String.class);
		
		String logData  = createLogData(exchange);
		
		LogManager.putMdc(logName);
		Logger logger = LogManager.getLogger(LogType.DYNAMIC);
		
		//logger.info("[" + seq + "]"+ seqString(seq) + logData);
	}
	
	public String createLogData(Exchange exchange) throws Exception {

		Message message = exchange.getIn();

		String seq = message.getHeader(ConstantCode.SEQ, String.class);
		String bizCd = message.getHeader(ConstantCode.BIZ_CODE, String.class);

		LogEAI logEai = new LogEAI();
		logEai.setSeq(seq);
		logEai.setStandardTelegram(parsingLog(message));

		String globalId = logEai.getStandardTelegram().getSttlSysCopt().getWhbnSttlWrtnYmd()
				+ logEai.getStandardTelegram().getSttlSysCopt().getWhbnSttlCretSysNm()
				+ logEai.getStandardTelegram().getSttlSysCopt().getWhbnSttlSrn()
				+ PackedNumericUtil.pack(logEai.getStandardTelegram().getSttlSysCopt().getWhbnSttlPgrsDsncNo())
				+ PackedNumericUtil.pack(logEai.getStandardTelegram().getSttlSysCopt().getWhbnSttlPgrsNo());

		logEai.setGlobalId(globalId);

		String result = converterService.objectToJson(logEai);

		return result;
	}
	
	public StandardTelegram parsingLog(Message data) throws Exception {

		StandardTelegram standardTelegram = new StandardTelegram();

		String message = null;

		if (data.getBody() instanceof byte[]) {
			message = new String(data.getBody(byte[].class));

			standardTelegram = converterService.jsonToObject((String) message, StandardTelegram.class);
		} else if (data.getBody() instanceof IBKMessage) {
			standardTelegram = data.getBody(IBKMessage.class).getStandardTelegram();
		} else {
			message = data.getBody(String.class);

			standardTelegram = converterService.jsonToObject((String) message, StandardTelegram.class);
		}

		return standardTelegram;
	}
	
	public String seqString(String seq) {

		String result = null;

		switch (seq) {
		case "0":
			result = " [ERROR] ";
			break;
		case "1":
			result = " [FROM IN ADAPTER] ";
			break;
		case "2":
			result = " [PreProcess] ";
			break;
		case "3":
			result = " [TO IN ADAPTER] ";
			break;
		case "4":
			result = " [TO OUT ADAPTER] ";
			break;
		case "5":
			result = " [AfterProcess] ";
			break;
		case "6":
			result = " [FROM OUT ADAPTER] ";
			break;
		default:
			result = " [DEFAULT] ";
			break;
		}

		return result;
	}
}
