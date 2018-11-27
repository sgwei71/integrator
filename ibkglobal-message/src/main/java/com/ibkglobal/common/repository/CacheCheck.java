package com.ibkglobal.common.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibkglobal.common.repository.config.CacheConfig;

import net.sf.ehcache.Cache;

@RestController
public class CacheCheck {
	
	@Autowired
	CacheConfig cacheConfig;
	
	@RequestMapping(value = "/cache/size", method = RequestMethod.GET)
	public void checkSize() {
		Cache cache = cacheConfig.getCacheRepository("IBKInterfaceInfo");
		System.out.println("캐시 사이즈 확인 : " + cache.getSize());
	}
	
	@RequestMapping(value = "/cache/find", method = RequestMethod.POST)
	public void checkFind(@RequestBody String key) {
		Cache cache = cacheConfig.getCacheRepository("IBKInterfaceInfo");
		System.out.println("캐시 데이터 확인 : " + cache.get(key));
	}
}
