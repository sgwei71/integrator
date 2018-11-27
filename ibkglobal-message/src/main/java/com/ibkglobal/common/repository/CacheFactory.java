package com.ibkglobal.common.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibkglobal.common.repository.config.CacheConfig;

import net.sf.ehcache.Cache;

@Component
public class CacheFactory {
	
	@Autowired
	CacheConfig cacheConfig;
	
	public Cache getCacheRepository(CacheType cacheType) {
		
		// MCA
		if      (cacheType == CacheType.IBK_MCA_INTERFACE)          { return cacheConfig.getCacheRepository("IBKInterface"); }
		
		// EAI
		else if (cacheType == CacheType.IBK_EAI_INTERFACE)          { return cacheConfig.getCacheRepository("IBKInterfaceEAI"); }
		else if (cacheType == CacheType.IBK_EAI_INTERFACE_KEY)      { return cacheConfig.getCacheRepository("IBKInterfaceEAIKey"); }
		
		// MCA / EAI Common
		else if (cacheType == CacheType.IBK_SOURCE_IO)              { return cacheConfig.getCacheRepository("IBKSourceIo"); }
		else if (cacheType == CacheType.IBK_TARGET_IO)              { return cacheConfig.getCacheRepository("IBKTargetIo"); }
		else if (cacheType == CacheType.IBK_MAPPING_IN)             { return cacheConfig.getCacheRepository("IBKMappingIn"); }
		else if (cacheType == CacheType.IBK_MAPPING_OUT)            { return cacheConfig.getCacheRepository("IBKMappingOut"); }
		
		// FEP
		else if (cacheType == CacheType.IBK_FEP_INTERFACE_EXT_INFO) { return cacheConfig.getCacheRepository("IBKInterfaceExt"); }
		else if (cacheType == CacheType.IBK_FEP_INTERFACE_KEY)      { return cacheConfig.getCacheRepository("IBKInterfaceKey"); }
		else if (cacheType == CacheType.IBK_FEP_MESSAGE)            { return cacheConfig.getCacheRepository("IBKMessage"); }
		else if (cacheType == CacheType.IBK_FEP_HEADER_MESSAGE)     { return cacheConfig.getCacheRepository("IBKHeaderMessage"); }
		else if (cacheType == CacheType.IBK_FEP_UNIT)               { return cacheConfig.getCacheRepository("IBKUnit"); }
		else if (cacheType == CacheType.IBK_FEP_MAPPING)            { return cacheConfig.getCacheRepository("IBKFEPMapping"); }
		
		// FEPADMIN
		else if (cacheType == CacheType.IBK_FEP_RECV_MSG)            { return cacheConfig.getCacheRepository("IBKFEPRecvMsg"); }
		
		return null;
	}
}
