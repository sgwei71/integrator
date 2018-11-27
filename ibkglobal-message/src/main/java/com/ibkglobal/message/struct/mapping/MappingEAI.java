package com.ibkglobal.message.struct.mapping;

import com.ibk.ibkglobal.data.mapp.Mapping;

import net.sf.ehcache.Cache;

public class MappingEAI extends MappingDefault {

	Cache inCache;
	Cache outCache;
	
	/**
	 * Init
	 * @param in
	 * @param out
	 */
	public void init(Cache in, Cache out) {
		// 캐시 설정
		this.inCache  = in;
		this.outCache = out;
	}
	
	/**
	 * Get
	 * @param key
	 * @param mappingType
	 * @return
	 */
	public Mapping get(String key, String mappingType) {
		if ("file".equals(type)) {
			return getDataList().get(key + "_" + mappingType + "BOUND");
		}
		
		if ("IN".equals(mappingType)) {
			return (Mapping) inCache.get(key).getObjectValue();
		}
		return (Mapping) outCache.get(key).getObjectValue();
	}
}
