package com.ibkglobal.message.struct.io;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.IoCacheFEP;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.model.IoLoader;

public class IoFEP extends IoDefault<IoCacheFEP> {

	private FileReader fileReader;
	
	public void initLoad(IoLoader ioLoader, FileReader fileReader) {
		this.fileReader = fileReader;
		init(ioLoader, null);
	}
	
	@Override
	public void init(IoLoader ioLoader, Map<String, IoCacheFEP> ioCacheMap) {
		
		// 로더 초기화
		setLoader(ioLoader);
				
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());		
		ioLoader.getIoInfo().forEach(entity -> {
			
			String clazz = entity.getClazz();
			String jlid = entity.getJlid();
			String path = FilePath.FEP_IO + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".json";
			try {
				IoCacheFEP io = fileReader.readFile(path, IoCacheFEP.class);
				put(io.getUtInopIdnNm(), io);
			} catch (Exception e) {}
		});
	}
}
