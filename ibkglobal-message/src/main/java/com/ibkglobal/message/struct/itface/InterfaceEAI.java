package com.ibkglobal.message.struct.itface;

import com.ibk.ibkglobal.data.intf.IntfKey;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class InterfaceEAI extends InterfaceDefault {
	
	protected Cache idnCache; // EAI 대내와의 연계 시 필요한 캐시
	
	public void init(Cache cache, Cache idnCache) {
		setCache(cache);
		this.idnCache = idnCache;
	}
	
	public String getEaiKey(String key) {
		if ("file".equals(type)) {
			return "";
		}
		
		Element element = idnCache.get(key);
		if (element != null) {
			IntfKey intfKey = (IntfKey)element.getObjectValue(); 
			return intfKey.getIntfId();
		} else {
			return null;
		}
	}
}
