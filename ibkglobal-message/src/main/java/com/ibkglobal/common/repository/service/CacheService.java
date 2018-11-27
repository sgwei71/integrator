package com.ibkglobal.common.repository.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.common.repository.CacheFactory;
import com.ibkglobal.common.repository.CacheType;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Component
public class CacheService {
	
	@Autowired
	CacheFactory cacheFactory;
	
	public void findCount(CacheType cacheType) {
		Cache cache = cacheFactory.getCacheRepository(cacheType);
		System.out.println("캐시명 : " + cacheType + " 카운트 : " + cache.getSize());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T find(CacheType cacheType, String id) {
		Element cache = cacheFactory.getCacheRepository(cacheType).get(id);
		
		if (cache == null) {
			return null;
		}
		
		return (T)cacheFactory.getCacheRepository(cacheType).get(id).getObjectValue();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(CacheType cacheType) {
		Cache cache = cacheFactory.getCacheRepository(cacheType);
		
		if (cache.getSize() == 0) {
			return null;
		}
		
		return cache.getAll(cache.getKeys()).values()
				                            .stream()
				                            .map(p -> (T) p.getObjectValue())
				                            .collect(Collectors.toList());
	}
	
	public void push(CacheType cacheType, String id, Object value) {
		Cache cache = cacheFactory.getCacheRepository(cacheType);
		
		cache.put(new Element(id, value));
	}
	
	@Deprecated
	public void remove(CacheType cacheType, String id) {
		Cache cache = cacheFactory.getCacheRepository(cacheType);
		
		cache.remove(id);
	}
	
	@Deprecated
	public void removeAll(CacheType cacheType) {
		Cache cache = cacheFactory.getCacheRepository(cacheType);
		
		cache.removeAll();
	}
}
