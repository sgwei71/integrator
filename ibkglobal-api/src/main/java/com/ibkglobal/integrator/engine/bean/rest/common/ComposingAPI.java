package com.ibkglobal.integrator.engine.bean.rest.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibk.ibkglobal.data.io.IoCacheAPI;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.integrator.config.ConstantCode;
import com.ibkglobal.integrator.engine.builder.model.endpoint.EndpointType;
import com.ibkglobal.integrator.exception.ErrorType;
import com.ibkglobal.integrator.exception.IBKExceptionAPI;
import com.ibkglobal.integrator.exception.IBKExceptionMCA;
import com.ibkglobal.integrator.util.IBKMessageUtil;
import com.ibkglobal.message.IBKMessage;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.common.normal.UserData;
import com.ibkglobal.message.converter.service.ConverterServiceAPI;
import com.ibkglobal.message.struct.resource.ResourceAPI;

public class ComposingAPI {

	@Autowired
	ConverterServiceAPI converterService;
	@Autowired
	ResourceAPI resourceAPI;
	
	private Logger logger = LoggerFactory.getLogger(ComposingAPI.class);
	
	public void composing(Exchange exchange) throws Exception {
		logger.info("::::::::::::::::::::::::::::::::ComposingAPI.composing:::::::::::::::::::::::::::::::::::::::::::::");
		try {
			Message message = exchange.getIn();
			IBKMessage ibkMessage = message.getBody(IBKMessage.class);

			EndpointType type = message.getHeader(ConstantCode.COMPOSING_HEADER, EndpointType.class);

			// MCA에서 최종 전송
			IBKMessageUtil.replyMessageDefaultSet(ibkMessage.getStandardTelegram(), ConstantCode.MCA_CODE);
			logger.info("IBKMessage --> [{}]",ibkMessage);
			if (type.equals(EndpointType.HTTP) || type.equals(EndpointType.CAMEL_HTTP) || type.equals(EndpointType.CAMEL_HTTP4)) {
				//MCA전송부분 
				byte[] flatData = dataToFlat(ibkMessage);
				message.setBody(flatData);
			} else if (type.equals(EndpointType.TCP)) {
				message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()).getBytes());
			} else if(type.equals(EndpointType.REST)) {
				message.setBody(converterService.objectToJson(ibkMessage.getStandardTelegram()).getBytes());
			}

			// 현재 큐일 경우에는 IBKMessage 그대로
		} catch (Exception e) {
			e.printStackTrace();
			throw new IBKExceptionAPI(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}

	/**
	 * Error Composing
	 * 
	 * @param exchange
	 */
	public void errorComposing(Exchange exchange) throws Exception {
		logger.info("::::::::::::::::::::::::::::::::ComposingAPI.errorComposing:::::::::::::::::::::::::::::::::::::::::::::");
		try {
			Message message = exchange.getIn();

			EndpointType type = message.getHeader(ConstantCode.COMPOSING_HEADER, EndpointType.class);
			StandardTelegram standardTelegram = message.getHeader(ConstantCode.IBK_NORMAL_HEADER,
					StandardTelegram.class);

			if (standardTelegram != null) {
				if (type != null) {
					if (type.equals(EndpointType.HTTP)) {
						message.setBody(converterService.objectToJson(standardTelegram));
					} else if (type.equals(EndpointType.TCP)) {
						message.setBody(converterService.objectToJson(standardTelegram).getBytes());
					}
				}
			}

		} catch (Exception e) {
			throw new IBKExceptionAPI(ErrorType.COMPOSING, e.getMessage(), e);
		}
	}
	public void composingFromHost(Exchange exchange)  {
		
		logger.info("::::::::::::::::::::::::::::::::ComposingAPI.composingFromHost:::::::::::::::::::::::::::::::::::::::::::::");
		Message message = exchange.getIn();
		IBKMessage ibkMessage = message.getBody(IBKMessage.class);
	
		//message.setHeader(ConstantCode.IBK_NORMAL_HEADER, standardTelegram);
		
		
		
		StandardTelegram telegram = message.getHeader(ConstantCode.IBK_NORMAL_HEADER,StandardTelegram.class);
				//.getStandardTelegram();
		logger.info("tele="+telegram);
		byte[] result;
		try {
			result = converterService.objectToJson(telegram.getUserData().getData()).getBytes();
			logger.info("result="+new String(result));
			
			message.setBody(result);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public byte[] dataToFlat(IBKMessage ibkMessage) {
		logger.info("ComposingAPI[dataToFlat]");

		UserData userData = ibkMessage.getStandardTelegram().getUserData();
		
		List<byte[]> flatList = new LinkedList<>();

		//Object result = null;
		byte[] resultFlat = null;
		try {
			//표준전문만 flat으로 변경 
			resultFlat = converterService.telegramToFlatAPI(ibkMessage.getStandardTelegram(), getStructList("OUT", ibkMessage.getInterfaceId()));
			logger.info("telegramToFlatAPI dataToFlat="+new String(resultFlat,"MS949"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//userData 변환 수행 
		//	logger.info("ComposingAPI[dataToFlat]+"+ibkMessage.getInterfaceId());

		//		IoCacheMCA entity = resourceMCA.getSourceIo(ibkMessage.getInterfaceId(), "IN") ;

		//MCA를 거치고 MCA용 Interface이므로 항상 Source임
		IoCacheAPI body = resourceAPI.getSourceIo(ibkMessage.getInterfaceId(),"IN");
				//.getTargetBodyEntity(ibkMessage.getInterfaceId());
		logger.info("ComposingAPI[dataToFlat] - IoCacheAPI [{}]",body);

		converterService.mapToFlat(flatList, userData.getData(), body.getInBound().getFieldList());
		logger.info("ComposingAPI[flatList]:"+flatList.size());

		// 최종 결과 길이
//		for(int i=0;i<flatList.size();i++) {
//			System.out.println(i+"."+flatList.get(i).length);
//		}
		Integer length = flatList.stream()
                                 .mapToInt(p -> p.length)
                                 .sum();
		logger.info("ComposingAPI[length]:"+length);
	
		// 최종 결과 리턴
		ByteBuffer buffer = ByteBuffer.allocate(length);
		
		flatList.forEach(flat -> {
			buffer.put(flat);
		});
		try {
			logger.info("ComposingAPI[buffer]:"+new String(buffer.array(),"MS949"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] resultData=buffer.array();
		
		//List<byte[]> result = new ArrayList<>();
		
		byte[] lastResult = null;
		try {
	//		logger.info("=="+new String(lengthCheck("INTEGER", resultData.length, 6, 0)));
			byte[] userLength = lengthCheck("INTEGER", resultData.length, 6, 0);
			lastResult = concatenateByteArrays(userLength,resultData);
			lastResult = concatenateByteArrays("IO".getBytes("MS949"),lastResult);
			lastResult = concatenateByteArrays(resultFlat,lastResult);
			lastResult = concatenateByteArrays(lastResult,"@@".getBytes("MS949"));
			System.arraycopy(lengthCheck("INTEGER", lastResult.length - 6, 6, 0), 0, lastResult, 0, 6);

			logger.info("[Flat 변환 last]="+new String(lastResult,"MS949"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//	logger.info("[last]="+lastResult);
		return lastResult;
	}
	byte[] concatenateByteArrays(byte[] a, byte[] b) {
	    byte[] result = new byte[a.length + b.length]; 
	    System.arraycopy(a, 0, result, 0, a.length); 
	    System.arraycopy(b, 0, result, a.length, b.length); 
	    return result;
	} 
	
	
	public List<Tlgr> getStructList(String parsing, String interfaceId) {
		
		IoCacheAPI ioCacheAPI = null;
		List<Tlgr> structList = null;
		
		switch (parsing) {
		case "IN" :
			ioCacheAPI = resourceAPI.getSourceIo(interfaceId, parsing);
			structList = ioCacheAPI.getInBound().getFieldList();
			break;
		case "OUT" :
			ioCacheAPI = resourceAPI.getSourceIo(interfaceId, parsing);
			structList = ioCacheAPI.getOutBound().getFieldList();
			break;
			default :
				break;
		}
		
		return structList;
	}
	/**
	 * 필드 길이 체크
	 * @param type
	 * @param value
	 * @param defaultLength
	 * @param scale
	 * @return
	 */
	public static byte[] lengthCheck(String type, Object value, int defaultLength, int scale) throws Exception {

		ByteBuffer result = ByteBuffer.allocate(defaultLength);
		
		String data = "";
		
		switch (type) {
		case "STRING" :
		case "DATASETCODE" :
			data = String.format("%1$-" + defaultLength + "s", value == null ? "" : value);
			break;
		case "INTEGER" :
		case "NUMERIC" :
		case "LONG" :
			String strValueIn = (value != null ? String.valueOf(value) : "0");
			data = String.format("%0" + defaultLength + "d", new Integer(strValueIn));
			break;
		case "BIGDECIMAL" :
		case "DOUBLE" :
		case "FLOAT" :
			String strValueFl = (value != null ? String.valueOf(value) : "0");
			if (scale == 0 && strValueFl.indexOf(".") > -1) {
				scale = strValueFl.substring(strValueFl.indexOf(".") + 1).length();
			}
			
			data = String.format("%0" + defaultLength + "." + String.valueOf(scale) + "f", new BigDecimal(strValueFl));
			break;
			
			default :
				break;
		}
		
		result.put(data.getBytes("MS949"), 0, defaultLength);
		
		return result.array();
	}
	
}