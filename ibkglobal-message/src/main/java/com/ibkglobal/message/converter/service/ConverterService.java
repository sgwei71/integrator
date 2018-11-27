package com.ibkglobal.message.converter.service;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.message.common.normal.StandardTelegram;
import com.ibkglobal.message.converter.ConverterByte;
import com.ibkglobal.message.converter.ConverterList;
import com.ibkglobal.message.converter.ConverterMap;
import com.ibkglobal.message.converter.ConverterMapping;
import com.ibkglobal.message.converter.delimiter.DelimiterToUserData;
import com.ibkglobal.message.converter.delimiter.UserDataToDelimiter;
import com.ibkglobal.message.converter.iso.IsoToUserData;
import com.ibkglobal.message.converter.iso.UserDataToIso;
import com.ibkglobal.message.converter.telegram.FlatToTelegram;
import com.ibkglobal.message.converter.telegram.TelegramToFlat;

@Service
@SuppressWarnings("deprecation")
public class ConverterService {
	
	/**
	 * Convert : Object -> Json
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public <T> String objectToJson(T value) throws Exception {
		return Converter.mapper.writeValueAsString(value);
	}
	
	/**
	 * Convert : Json -> Object
	 * @param jsonString
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	public <T> T jsonToObject(String jsonString, Class<T> valueType) throws Exception {
		return Converter.mapper.readValue(jsonString, valueType);
	}
	
	/**
	 * Object -> Object<Map>
	 * @param fromObject
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	public <T> T objectToObject(Object fromObject, Class<T> valueType) throws Exception {
		return Converter.mapper.convertValue(fromObject, valueType);
	}
	
	/**
	 * Convert : Json -> Map
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> jsonToMap(String jsonStr) throws Exception {
		return Converter.mapper.readValue(jsonStr, LinkedHashMap.class);
	}
	
	/**
	 * Convert : Map -> String<Json>
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String mapToJson(Map<String, Object> data) throws Exception {
		return Converter.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
	}
	
	/**
	 * Convert : ByteArray -> Map
	 * @param data
	 * @param fieldList
	 * @return
	 */
	public LinkedHashMap<String, Object> byteArrayToMap(byte[] data, List<Tlgr> fieldList) {
		return ConverterByte.transform(data, fieldList);
	}
	
	/**
	 * Convert : ByteArray -> Map
	 * @param data
	 * @param fieldList
	 * @return
	 */
	public LinkedHashMap<String, Object> byteArrayToMap(ByteBuffer data, List<Tlgr> fieldList) {
		return ConverterByte.byteArrayToMap(data, fieldList);
	}
	
	/**
	 * Convert : Map<String, Value> -> List<Field>
	 * @param data
	 * @param fieldList
	 * @return
	 */
	@Deprecated
	public List<Tlgr> mapToList(LinkedHashMap<String, Object> data, List<Tlgr> fieldList) {
		return ConverterList.transform(data, fieldList);
	}
		
	/**
	 * Convert : source -> target
	 * @param mappingList
	 * @param sourceData
	 * @param targetList
	 * @param inOut
	 * @param nullable
	 * @return
	 */
	public LinkedHashMap<String, Object> mapping(List<TlgrMapping> mappingList, Map<String, Object> sourceData, List<Tlgr> targetList, String inOut, boolean nullable) {
		return ConverterMapping.transform(mappingList, sourceData, targetList, inOut, nullable);
	}
	
	/**
	 * Convert : List<Field> -> Map<String, Value>
	 * @param fieldList
	 * @return
	 */
	public LinkedHashMap<String, Object> listToMap(List<Tlgr> fieldList) {
		return ConverterMap.transform(fieldList);
	}
	
	/**
	 * Convert : Map의 정보를 Field List 기준으로 정렬  
	 * @param data
	 * @param fieldList
	 * @param nullable
	 * @return
	 */
	public LinkedHashMap<String, Object> listToMap(Map<String, Object> data, List<Tlgr> fieldList, boolean nullable) {
		return ConverterMap.transform(data, fieldList, nullable);
	}
	
	/**
	 * Convert : LinkedHashMap<String, Object> -> ByteArray
	 * @param buffer
	 * @param data
	 * @param fieldList
	 * @return
	 */
	public byte[] mapToByteArray(ByteBuffer buffer, LinkedHashMap<String, Object> data, List<Tlgr> fieldList) {
		return ConverterByte.mapToByteArray(buffer, data, fieldList).array();
	}
	
	/**
	 * Convert : 표준전문(Declared Fields) 기준으로 dataMap -> ByteArray
	 * @param classType
	 * @param dataMap
	 * @return
	 */
	@Deprecated
	public Map<String, Object> telegramToByteArray(Class<?> classType, LinkedHashMap<String, Object> dataMap) {
		return ConverterByte.telegramToByteArray(classType, dataMap);
	}
	
	/**
	 * Field String Format
	 * @param type
	 * @param value
	 * @param defaultLength
	 * @param scale
	 * @return
	 */
	public String fieldStringFormat(String type, Object value, int defaultLength, int scale) {
		return ConverterByte.fieldStringFormat(type, value, defaultLength, scale);
	}
	
	/** 신규 **/
	
	/**
	 * 대내 EAI Flat -> Telegram
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public StandardTelegram flatToTelegram(byte[] data, List<Tlgr> fieldList) throws Exception {
		return FlatToTelegram.flatToTelegram(data, fieldList);
	}
	
	/**
	 * Telegram -> 대내 EAI Flat
	 * @param standardTelegram
	 * @return
	 * @throws Exception 
	 */
	public byte[] telegramToFlat(StandardTelegram standardTelegram, List<Tlgr> fieldList) throws Exception {
		return TelegramToFlat.telegramToFlat(standardTelegram, fieldList);
	}
	
	/**
	 * Map -> Flat
	 * @param data
	 * @param fieldList
	 * @return
	 */
	public void mapToFlat(List<byte[]> flatList, LinkedHashMap<String, Object> data, List<Tlgr> fieldList) {
		ConverterByte.mapToFlat(flatList, data, fieldList);
	}
	
	/**
	 * iso -> headerData
	 * @param data
	 * @param headerList
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Object> isoToHeaderData(byte[] data, List<Tlgr> headerList) throws Exception {
		return IsoToUserData.isoToHeaderData(data, headerList);
	}
	
	/**
	 * iso -> bodyData
	 * @param data
	 * @param bodyList
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Object> isoToBodyData(byte[] data, List<Tlgr> bodyList) throws Exception {
		return IsoToUserData.isoToBodyData(data, bodyList);
	}
	
	/**
	 * delimiter -> headerData
	 * @param data
	 * @param headerList
	 * @return
	 */
	public LinkedHashMap<String, Object> delimiterToHeaderData(byte[] data, List<Tlgr> headerList) throws Exception {
		return DelimiterToUserData.delimiterToHeaderData(data, headerList);
	}
	
	/**
	 * delimiter -> bodyData
	 * @param delimiterValue
	 * @param data
	 * @param headerList
	 * @param bodyList
	 * @return
	 */
	public LinkedHashMap<String, Object> delimiterToBodyData(String delimiterValue, byte[] data, List<Tlgr> headerList, List<Tlgr> bodyList) throws Exception {
		return DelimiterToUserData.delimiterToBodyData(delimiterValue, data, headerList, bodyList);
	}
	
	/**
	 * userData -> Iso
	 * @param mtiValue
	 * @param userData
	 * @return
	 * @throws Exception
	 */
	public byte[] userDataToIso(LinkedHashMap<String, Object> userData) throws Exception {
		return UserDataToIso.userDataToIso(userData);
	}
	
	/**
	 * userData -> Delimiter (Schema O)
	 * @param delimiterValue
	 * @param userData
	 * @param headerList
	 * @param bodyList
	 * @return
	 */
	public byte[] userDataToDelimiter(String delimiterValue, LinkedHashMap<String, Object> userData, List<Tlgr> headerList, List<Tlgr> bodyList) throws Exception {
		return UserDataToDelimiter.userDataToDelimiter(delimiterValue, userData, headerList, bodyList);
	}
	
	/**
	 * userData -> Delimiter (Schema X)
	 * @param delimiterValue
	 * @param headerData
	 * @param bodyData
	 * @return
	 */
	public byte[] userDataToDelimiter(String delimiterValue, LinkedHashMap<String, Object> headerData, LinkedHashMap<String, Object> bodyData) throws Exception {
		return UserDataToDelimiter.userDataToDelimiter(delimiterValue, headerData, bodyData);
	}
}
