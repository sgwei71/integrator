package com.ibkglobal.integrator.engine.bean.mca.work;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ibkglobal.integrator.config.CamelConfig;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.config.EndpointCode;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.RuleCommonUtil;
import com.ibkglobal.message.IBKMessage;

public class MCABidBean {
	
	@Autowired
	CamelConfig camelConfig;
	
	public void execute(Exchange exchange) throws IBKExceptionMCA {
		
		try {
			Message message = exchange.getIn();
			
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);
			
			// 원본 데이터 삭제
			ibkMessage.setBaseData(null);
			
			String bizCode = message.getHeader(ConstantCode.BIZ_CODE, String.class);
			String sysCode = message.getHeader(ConstantCode.SYS_CODE, String.class);
			
			// 임시 하드코딩
			String bidQueueName = getBidQueueName(bizCode, sysCode);
			
			Exchange bidExchange = exchange.copy();
			
			// 비동기 전송
			camelConfig.getProducerTemplate().asyncSend(bidQueueName, bidExchange);
			
			// Ack 응답
			setBidAck(message);
		} catch (Exception e) {
			throw new IBKExceptionMCA(ErrorType.MCA_BID, "비드 큐 생성, 전송 오류");
		}
	}
	
	/**
	 * 비드 큐 명칭 지정
	 * @param bizCode
	 * @param sysCode
	 * @return
	 * @throws IBKExceptionMCA
	 */
	protected String getBidQueueName(String bizCode, String sysCode) throws IBKExceptionMCA {
		
		String qName = "";
		
		if (StringUtils.isEmpty(bizCode) || StringUtils.isEmpty(sysCode)) {
			throw new IBKExceptionMCA(ErrorType.MCA_BID, "비드 업무코드 및 시스템코드 생성오류 - " + "[bizCode] : " + bizCode + " [SysCode] : " + sysCode);
		}
		
		// ex) M.BID0.GCB0.KR00.Q
		qName = EndpointCode.QUEUE + "M.BID0." + RuleCommonUtil.checkFixName(sysCode) + "." + RuleCommonUtil.checkFixName(bizCode) + ".Q";
		
		return qName;
	}
	
	/**
	 * Ack 메시지 지정
	 * @param message
	 */
	protected void setBidAck(Message message) {
		
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
		
		ibkMessage.getStandardTelegram().getSttlSysCopt().setRqstRspnDcd("K");
	}
}
