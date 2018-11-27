package com.ibkglobal.message.converter.iso;

import java.util.LinkedHashMap;
import java.util.List;

import org.jpos.iso.ISOMsg;

import com.ibk.ibkglobal.data.io.model.Tlgr;

public class IsoToUserData {

	public static LinkedHashMap<String, Object> isoToUserData(byte[] message, List<Tlgr> headerList, List<Tlgr> bodyList) throws Exception {

		LinkedHashMap<String, Object> userData = new LinkedHashMap<>();

		userData.putAll(isoToHeaderData(message, headerList));
		userData.putAll(isoToBodyData(message, bodyList));

		return userData;
	}

	/**
	 * iso -> User Header Data
	 * @param message
	 * @param headerList
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap<String, Object> isoToHeaderData(byte[] message, List<Tlgr> headerList) throws Exception {

		LinkedHashMap<String, Object> headerData = new LinkedHashMap<>();

		ISOMsg isoMsg = new ISOMsg();

		isoMsg.setPackager(CommonIso.getGenericPackager());
		isoMsg.unpack(message);

		// 헤더 셋
		headerList.forEach(header -> {
			Integer num = CommonIso.getFieldInfo().get(header.getEnsnFldNm());
			headerData.put(header.getEnsnFldNm(), isoMsg.getValue(num));
		});

		return headerData;
	}
	
	/**
	 * iso -> User Body Data
	 * @param message
	 * @param bodyList
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap<String, Object> isoToBodyData(byte[] message, List<Tlgr> bodyList) throws Exception {
		
		LinkedHashMap<String, Object> bodyData = new LinkedHashMap<>();

		ISOMsg isoMsg = new ISOMsg();

		isoMsg.setPackager(CommonIso.getGenericPackager());
		isoMsg.unpack(message);

		// 헤더 셋
		bodyList.forEach(body -> {
			Integer num = CommonIso.getFieldInfo().get(body.getEnsnFldNm());
			bodyData.put(body.getEnsnFldNm(), isoMsg.getValue(num));
		});

		return bodyData;
	}
}
