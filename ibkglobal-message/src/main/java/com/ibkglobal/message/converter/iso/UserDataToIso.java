package com.ibkglobal.message.converter.iso;

import java.util.LinkedHashMap;

import org.jpos.iso.ISOMsg;

public class UserDataToIso {
	
	public static byte[] userDataToIso(LinkedHashMap<String, Object> userData) throws Exception {	
		
		ISOMsg isoMsg = new ISOMsg();
		
		isoMsg.setPackager(CommonIso.getGenericPackager());
		
		// MTI 값 세팅
		//isoMsg.setMTI(mtiValue);
		
		userData.forEach((key, value) -> {
			Integer num = CommonIso.getFieldInfo().get(key);			
			isoMsg.set(num, value.toString());
		});
		
//		GenericPackager packager = CommonIso.getInstance().getGenericPackager();
//
//		ISOMsg isoMsg = new ISOMsg();
//		isoMsg.setPackager(packager);
//		isoMsg.setMTI("0200");
//		isoMsg.set(3, "201234");
//		isoMsg.set(4, "10000");
//		isoMsg.set(7, "110722180");
//		isoMsg.set(11, "123456");
//		isoMsg.set(44, "A5DFGR");
//		isoMsg.set(105, "ABCDEFGHIJ 1234567890");
		
		return isoMsg.pack();
	}
}
