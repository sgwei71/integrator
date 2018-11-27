package com.ibkglobal.message.struct.io;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibkglobal.message.struct.loader.model.IoLoader;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class IoMCA extends IoDefault<IoCacheMCA> {
	
	Cache sourceCache;
	Cache targetCache;

	@Override
	public void init(IoLoader ioLoader, Map<String, IoCacheMCA> ioCacheMap) {
		
		super.init(ioLoader, ioCacheMap);
				
		// 데이터 초기화
		setDataList(new ConcurrentHashMap<>());		
		ioCacheMap.values().forEach(data -> {
			if (getLoader().getIoInfo()
					       .stream()
					       .anyMatch(p -> p.getClazz().equals(data.getInopId())))
				put(data.getInopId(), data);
		});
	}
	
	/**
	 * Init
	 * @param source
	 * @param target
	 */
	public void init(Cache source, Cache target) {
		// 캐시 설정
		this.sourceCache = source;
		this.targetCache = target;
	}
	
	@Override
	public IoCacheMCA get(String key) {
		
		if ("file".equals(type)) {
			return getDataList().get(key);
		} 
		
		Element value = sourceCache.get(key);
		
		if (value != null) {
			return (IoCacheMCA) value.getObjectValue();
		}
		
		return (IoCacheMCA) targetCache.get(key).getObjectValue();
	}
}
