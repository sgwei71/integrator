package com.ibkglobal.common.repository.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Configuration
@EnableCaching
public class CacheConfig {
	
	@Autowired
	CacheManager cacheManager;
	
	public Cache getCacheRepository(String cacheName) {
		return cacheManager.getCache(cacheName);
	}
}
