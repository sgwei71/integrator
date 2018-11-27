package com.ibkglobal.message.struct.mapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.mapp.Mapping;
import com.ibkglobal.message.struct.loader.MessageRepository;
import com.ibkglobal.message.struct.loader.model.MappingLoader;

public class MappingDefault extends MessageRepository<MappingLoader, Mapping> {

	@Override
	public void init(MappingLoader mappingLoader, Map<String, Mapping> mappingMap) {
		// 로더 초기화
		setLoader(mappingLoader);
				
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());		
		mappingMap.values().forEach(data -> {
			if (getLoader().getMapperInfo()
					       .stream()
					       .anyMatch(p -> p.getXcid().equals(data.getIntfId())))
				
				// 소스, 타겟 순서 정렬
				data.getMappingList().forEach(mapping -> {
					mapping.getSourceNm()
					       .sort((b, c) -> {
					    	   return b.getFldPara().compareTo(c.getFldPara());
					       });
					
					mapping.getTargetNm()
					       .sort((b, c) -> {
					    	   return b.getFldPara().compareTo(c.getFldPara()); 
					       });
				});	
			
				put(data.getIntfId(), data);
		});
	}

	@Override
	public Mapping get(String key) {
		if ("file".equals(type)) {
			return getDataList().get(key);
		}
		return (Mapping) getCache().get(key).getObjectValue();
	}

	@Override
	public void put(String intfId, Mapping tdata) {
		String type = "";
		String inndOtbnCd = tdata.getInndOtbnCd();
		if (!StringUtils.isEmpty(inndOtbnCd)) {
			type = "I".equals(inndOtbnCd)  ? "_INBOUND" : "_OUTBOUND";
		}
		getDataList().put(intfId + type, tdata);
	}

	@Override
	public void remove(String key) {
		getDataList().remove(key);
	}
}
