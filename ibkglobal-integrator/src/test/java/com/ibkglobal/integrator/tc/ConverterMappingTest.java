package com.ibkglobal.integrator.tc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibk.ibkglobal.data.io.model.Tlgr;
import com.ibk.ibkglobal.data.mapp.TlgrMapping;
import com.ibkglobal.message.converter.service.ConverterService;

@Component
public class ConverterMappingTest {
	
	@Autowired
	ConverterService converterService;
	
	/**
	 * 유저데이터를 타겟 형식에 맞게 매핑
	 * @param mappingList // 매핑정보
	 * @param sourceData  // 매핑 전 유저데이터
	 * @param targetList  // 타겟 스키마
	 * @param inOut       // 인바운드, 아웃바운드
	 * @param nullable
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Object> mapping(List<TlgrMapping> mappingList, Map<String, Object> sourceData, List<Tlgr> targetList, String inOut, boolean nullable) throws Exception {
		LinkedHashMap<String, Object> result = null;
		
		result = converterService.mapping(mappingList, sourceData, targetList, inOut, nullable);
		
		return result;
	}
}
