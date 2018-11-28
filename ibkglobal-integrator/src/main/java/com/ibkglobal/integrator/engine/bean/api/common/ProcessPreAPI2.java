package com.ibkglobal.integrator.engine.bean.api.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibkglobal.common.validator.ValidatorService;
import com.ibkglobal.common.validator.model.ValidResult;
import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.exception.CommonException;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.log.LogManager;
import com.ibkglobal.integrator.log.LogType;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.InfraType;
import com.ibkglobal.message.common.normal.StandardTelegram;

import ch.qos.logback.classic.Logger;

public class ProcessPreAPI2 {
	
	@Autowired
	ParsingAPI parsingAPI;
	
	@Autowired
	ValidatorService validatorService;
	
	@Autowired
	CamelConfig camelConfig;
	
	public void preProcess(Exchange exchange) throws Exception {
		// 헤더 초기화
		Logger logger = LogManager.getLogger(LogType.DYNAMIC);
		
		logger.info("[PHJ]Preprocess");

		initSetHeader(exchange);
		logger.info("[PHJ2]Preprocess");

		// 파싱 및 초기화
		init(exchange);
		logger.info("[PHJ3]Preprocess");

	}

	protected void initSetHeader(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		
	    message.setHeader(ConstantCode.SEQ, "2");
	    message.setHeader(ConstantCode.IBK_NORMAL_MESSAGE_YN, "Y");
	    message.setHeader(ConstantCode.INFRA_TYPE, InfraType.MCA);
	}
	
	protected void init(Exchange exchange) throws Exception {
		// 수신 전문 파싱
		parsingAPI.parsing(exchange);
		
		// ValidCheck
		IBKMessage ibkMessage = exchange.getIn().getBody(IBKMessage.class);
	    ValidResult validResult = validatorService.validationSttl(ibkMessage.getStandardTelegram());
	    System.out.println("[PHJ11.1]"+validResult.isResult());  
		   
//	    if (!validResult.isResult()) {
//	    	String errorContent = "Valid Error";
//			if (validResult.getErrorList().size() > 0) {
//				errorContent = validResult.getErrorList().get(0);
//			}
//			throw new IBKExceptionMCA(ErrorType.VALID, errorContent);
//	    }
	    
	    
	    
	    System.out.println("[PHJ11.1]");  
	    if (ibkMessage.getInterfaceId().equals("ITRO00000111")) {
	    	try {
	    		// 인터페이스 : OBSO00025923
	    		String data = "001451N0KO02420180807dOBS0001074001800086120000000020180807dOBS0001074001800086120000134.100.100.100                        047D7BBF13B6D20180807191001000N000OBSS003398320180807191001000S     OBSOBSOBSO00025923SN020180807191001000CBKARR186109CBKARR186109                                                                                                             OBSOBSOBS                                                                              OLTOBSCBKARR186109800662                10                                                                                                                                                                                                                MC000124                                                                 0711020130                                                 NC000597                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     IO0000100032940538@@";
	        		
	    		exchange.getIn().setBody(data.getBytes("MS949"));
	    		
	    		camelConfig.getProducerTemplate().send("netty4:tcp://172.18.190.83:12501?requestTimeout=15000&disconnect=true&textline=true", exchange);
	    			    		
	    		//camelConfig.getProducerTemplate().sendBody("netty4:tcp://172.18.190.83:12501?requestTimeout=15000&disconnect=true", data.getBytes("MS949"));
	    		//camelConfig.getProducerTemplate().sendBody("netty4:tcp://10.104.162.77:11212?requestTimeout=15000&disconnect=true", data.getBytes("MS949"));
	    		
	    		exchange.getOut().setFault(true);
				exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
	    	} catch (Exception e) {
	    		throw new IBKExceptionMCA(ErrorType.ETC, "통신 오류 : " + e.getMessage());
	    	}
	    }
	    System.out.println("[PHJ12]");
	}
}
