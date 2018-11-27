package com.ibkglobal.message.struct.header;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.HeaderCacheFEP;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.MessageRepository;
import com.ibkglobal.message.struct.loader.model.HeaderLoader;

public class HeaderFEP extends MessageRepository<HeaderLoader, HeaderCacheFEP> {

	private FileReader fileReader;
	
	public void initLoad(HeaderLoader tloader, FileReader fileReader) {
		this.fileReader = fileReader;
		init(tloader, null);
	}
	
	@Override
	public void init(HeaderLoader tloader, Map<String, HeaderCacheFEP> tdataMap) {
		// 로더 초기화
		setLoader(tloader);
		
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());	
		tloader.getHeaderInfo().forEach(loader -> {
			String clazz = loader.getClazz();
			String jlid = loader.getJlid();
			String path = FilePath.FEP_HEADER + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".json";
			try {
				HeaderCacheFEP headerCacheFEP = fileReader.readFile(path, HeaderCacheFEP.class);
				
				// header 정렬
				headerCacheFEP.getData().getSequence().sort((b, c) -> {
		    	   return b.getSeq().compareTo(c.getSeq());
				});
				
				put(headerCacheFEP.getUtInopIdnNm(), headerCacheFEP);
			} catch (Exception e) {}
		});
	}

	@Override
	public HeaderCacheFEP get(String key) {
		if ("file".equals(type)) {
			return getDataList().get(key);
		}
		return (HeaderCacheFEP) getCache().get(key).getObjectValue();
	}

	@Override
	public void put(String key, HeaderCacheFEP tdata) {
		getDataList().put(key, tdata);
	}

	@Override
	public void remove(String key) {
		getDataList().remove(key);
	}

}
