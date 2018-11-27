package com.ibkglobal.integrator.tc;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.service.ConverterService;

@Component
public class ConverterParsingTest {
	
	@Autowired
	ConverterService converterService;
	
	/**
	 * JSON -> 표준전문
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public StandardTelegram jsonToTelegram(String data) throws Exception {
		
		StandardTelegram standardTelegram = converterService.jsonToObject(data, StandardTelegram.class);
		
		return standardTelegram;
	}
	
	/**
	 * 표준전문 -> JSON
	 * @param standardTelegram
	 * @return
	 * @throws Exception
	 */
	public String telegramToJson(StandardTelegram standardTelegram) throws Exception {		
		return converterService.objectToJson(standardTelegram);
	}
	
	/**
	 * 기관 플랫전문 -> USERDATA
	 * @param data
	 * @param headerSchemaList
	 * @param bodySchemaList
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Object> flatToUserData(byte[] data, List<Tlgr> headerSchemaList, List<Tlgr> bodySchemaList) throws Exception {
		
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// 헤더부 + 개별부 결합
		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		
		// 헤더부
		LinkedHashMap<String, Object> headerData = new LinkedHashMap<>();
		headerData = converterService.byteArrayToMap(buffer, headerSchemaList);
		
		// 개별부
		LinkedHashMap<String, Object> bodyData = new LinkedHashMap<>();
		bodyData = converterService.byteArrayToMap(buffer, bodySchemaList);
		
		result.putAll(headerData);
		result.putAll(bodyData);
		
		return result;
	}
	
	/**
	 * USERDATA -> 기관 플랫전문
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public byte[] userDataToFlat(LinkedHashMap<String, Object> data, List<Tlgr> headerSchemaList, List<Tlgr> bodySchemaList) throws Exception {
		
		List<byte[]> flatList = new LinkedList<>();
		
		converterService.mapToFlat(flatList, data, headerSchemaList);
		converterService.mapToFlat(flatList, data, bodySchemaList);
		
		// 최종 결과 길이
		Integer length = flatList.stream().mapToInt(p -> p.length).sum();

		// 최종 결과 리턴
		ByteBuffer buffer = ByteBuffer.allocate(length);

		flatList.forEach(flat -> {
			buffer.put(flat);
		});
		
		return buffer.array();
	}
	
	/**
	 * EAI 플랫전문 -> 표준전문 (전문 형식이 비슷하지만 조금씩 틀려서 includeField, excludeField 처리)
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public StandardTelegram eaiFlatToTelegram(byte[] data) throws Exception {
		
		// 유저 데이터는 일단 NULL로
		StandardTelegram standardTelegram = converterService.flatToTelegram(data, null);
		
		return standardTelegram;
	}
	
	/**
	 * 표준전문 -> EAI 플랫전문 (전문 형식이 비슷하지만 조금씩 틀려서 includeField, excludeField 처리)
	 * @param standardTelegram
	 * @return
	 * @throws Exception
	 */
	public byte[] telegramToEaiFlat(StandardTelegram standardTelegram) throws Exception {
		
		// 유저 데이터는 일단 NULL로
		byte[] result = converterService.telegramToFlat(standardTelegram, null);
		
		return result;
	}
	
	/**
	 * 표준전문 -> ISO (현재 협의 진행중)
	 * @throws Exception
	 */
	public void telegramToIso() throws Exception {
		// TelegramToIso 클래스에 담을 예정
	}
	
	/**
	 * ISO -> 표준전문 (현재 협의 진행중)
	 * @throws Exception
	 */
	public void isoToTelegram() throws Exception {
		// IsoToTelegram 클래스에 담을 예정
	}
}
