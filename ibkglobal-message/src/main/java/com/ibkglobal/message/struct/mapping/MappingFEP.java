package com.ibkglobal.message.struct.mapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.mapp.Mapping;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.model.MappingLoader;

public class MappingFEP extends MappingDefault {
	
	private FileReader fileReader;
	
	public void initLoad(MappingLoader mappingLoader, FileReader fileReader) {
		this.fileReader = fileReader;
		init(mappingLoader, null);
	}
	
	@Override
	public void init(MappingLoader mappingLoader, Map<String, Mapping> mappingMap) {
		// 로더 초기화
		setLoader(mappingLoader);
		
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());
		mappingLoader.getMapperInfo().forEach(mapping -> {
			
			String clazz = mapping.getClazz();
			String jlid = mapping.getJlid();
			String path = FilePath.FEP_MAPPING + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".json";
			try {
				Mapping mapper = fileReader.readFile(path, Mapping.class);
				
				// 소스, 타겟 순서 정렬
				mapper.getMappingList().forEach(list -> {
					list.getSourceNm()
					       .sort((b, c) -> {
					    	   return b.getFldPara().compareTo(c.getFldPara());
					       });
					
					list.getTargetNm()
					       .sort((b, c) -> {
					    	   return b.getFldPara().compareTo(c.getFldPara()); 
					       });
				});
				
				put(mapper.getIntfIdnNm(), mapper);
			} catch (Exception e) {}
		});
	}

	@Override
	public void put(String key, Mapping tdata) {
		getDataList().put(key, tdata);
	}
}
