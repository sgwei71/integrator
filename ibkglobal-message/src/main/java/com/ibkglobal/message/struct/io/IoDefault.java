package com.ibkglobal.message.struct.io;

import java.util.Map;

import com.ibkglobal.message.struct.loader.MessageRepository;
import com.ibkglobal.message.struct.loader.model.IoLoader;

public abstract class IoDefault<T> extends MessageRepository<IoLoader, T> {

	@Override
	public void init(IoLoader ioLoader, Map<String, T> ioCacheMap) {
		
		// 로더 초기화
		setLoader(ioLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get(String key) {
		if ("file".equals(type)) {
			return getDataList().get(key);
		}
		return (T) getCache().get(key).getObjectValue();
	}
	
	@Override
	public void put(String inopId, T tdata) {
		getDataList().put(inopId, tdata);
	}

	@Override
	public void remove(String key) {
		getDataList().remove(key);
	}
}
