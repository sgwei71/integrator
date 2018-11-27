package com.ibkglobal.message.struct.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.ibk.ibkglobal.data.io.MessageCacheFEP;
import com.ibkglobal.common.file.FilePath;
import com.ibkglobal.common.file.FileReader;
import com.ibkglobal.message.struct.loader.MessageRepository;
import com.ibkglobal.message.struct.loader.model.MessageLoader;

public class MessageFEP extends MessageRepository<MessageLoader, MessageCacheFEP> {

	private FileReader fileReader;
	
	public void initLoad(MessageLoader tloader, FileReader fileReader) {
		this.fileReader = fileReader;
		init(tloader, null);
	}
	
	@Override
	public void init(MessageLoader tloader, Map<String, MessageCacheFEP> tdataMap) {
		// 로더 초기화
		setLoader(tloader);
		
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());
		tloader.getMessageInfo().forEach(loader -> {
			String clazz = loader.getClazz();
			String jlid = loader.getJlid();
			String path = FilePath.FEP_MESSAGE + (!StringUtils.isEmpty(jlid) ? "/" + jlid : "") + "/" + clazz + ".json";
			try {
				MessageCacheFEP message = fileReader.readFile(path, MessageCacheFEP.class);
				
				// message 정렬
				message.getData().getSequence().sort((b, c) -> {
		    	   return b.getSeq().compareTo(c.getSeq());
				});
				
				put(message.getInopIdnNm(), message);				
			} catch (Exception e) {}
		});
	}

	@Override
	public MessageCacheFEP get(String key) {
		if ("file".equals(type)) {
			return getDataList().get(key);
		}
		return (MessageCacheFEP) getCache().get(key).getObjectValue();
	}

	@Override
	public void put(String key, MessageCacheFEP tdata) {
		getDataList().put(key, tdata);
	}

	@Override
	public void remove(String key) {
		getDataList().remove(key);
	}

}
