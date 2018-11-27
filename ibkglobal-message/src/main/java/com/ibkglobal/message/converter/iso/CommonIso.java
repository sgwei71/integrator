package com.ibkglobal.message.converter.iso;

import java.util.LinkedHashMap;

import org.jpos.iso.packager.GenericPackager;
import org.springframework.util.ResourceUtils;

import lombok.Getter;

public class CommonIso {
	
	@Getter
	private static GenericPackager genericPackager;
	
	@Getter
	private static LinkedHashMap<Integer, String> headerField = new LinkedHashMap<>(); // 인니 고정 헤더 필드
	
	@Getter
	private static LinkedHashMap<String, Integer> fieldInfo = new LinkedHashMap<>(); // ISO 스키마
	
	private static Integer ISO_LENGTH = 128; // 128 고정
	
	static {
		try {
			genericPackager = new GenericPackager(ResourceUtils.getURL("classpath:iso87ascii.xml").getPath());
			
			// SMR 스키마와의 매핑을 위함
			for (int n = 0; n <= ISO_LENGTH; n++) {
				fieldInfo.put(genericPackager.getFieldPackager(n).getDescription(), n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		headerField.put(0,  "nrcnTrnDcd");  // NRCN_TRN_DCD(정상취소거래구분코드)
		headerField.put(13, "trnYmd");      // TRN_YMD(거래년월일)
		headerField.put(3,  "tltrCdTlcn");  // TLTR_CD_TLCN(주전문거래코드전문내용)
		headerField.put(32, "tlgrRqitCd");  // TLGR_RQIT_CD(전문요청기관코드)
		headerField.put(11, "trnUnqNo");    // TRN_UNQ_NO(거래고유번호)
	}
}