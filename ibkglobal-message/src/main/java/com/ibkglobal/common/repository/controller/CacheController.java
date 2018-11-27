package com.ibkglobal.common.repository.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibk.ibkglobal.data.intf.Interface;
import com.ibk.ibkglobal.data.io.IoCacheMCA;
import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.repository.CacheFactory;
import com.ibkglobal.common.repository.CacheType;
import com.ibkglobal.common.repository.service.CacheService;

@RestController
public class CacheController {

	@Autowired
	CacheService cacheService;
	
//	@RequestMapping(value = "/cache/find", method = RequestMethod.POST)
//	public void find(@RequestBody String key) {
//		cacheService.find(CacheType.IBKINTERFACEINFO, key);
//	}
	
	@Autowired
	CacheFactory cacheFactory;
	
	@RequestMapping(value = "/cache/find/{type}/{id}", method = {RequestMethod.GET, RequestMethod.POST})
	public String findEx(@PathVariable String type, @PathVariable String id) throws JsonProcessingException {
		
		System.out.println("타입 : " + type);
		System.out.println("명명 : " + id);
		
		Object result = null;
		
		switch (type) {
		case "IBK_MCA_INTERFACE" :
			result = cacheService.find(CacheType.IBK_MCA_INTERFACE, id);
			break;
		case "IBK_SOURCE_IO" :
			result = cacheService.find(CacheType.IBK_SOURCE_IO, id);
			break;
		case "IBK_TARGET_IO" :
			result = cacheService.find(CacheType.IBK_TARGET_IO, id);
			break;
		case "IBK_MAPPING_IN" :
			result = cacheService.find(CacheType.IBK_MAPPING_IN, id);
			break;
		case "IBK_MAPPING_OUT" :
			result = cacheService.find(CacheType.IBK_MAPPING_OUT, id);
			break;
		case "IBK_EAI_INTERFACE" :
			result = cacheService.find(CacheType.IBK_EAI_INTERFACE, id);
			break;
		case "IBK_EAI_INTERFACE_KEY" :
			result = cacheService.find(CacheType.IBK_EAI_INTERFACE_KEY, id);
			break;
		
		case "IBK_FEP_INTERFACE_EXT_INFO" :
			result = cacheService.find(CacheType.IBK_FEP_INTERFACE_EXT_INFO, id);
			break;
		case "IBK_FEP_INTERFACE_KEY" :
			result = cacheService.find(CacheType.IBK_FEP_INTERFACE_KEY, id);
			break;
		case "IBK_FEP_MESSAGE" :
			result = cacheService.find(CacheType.IBK_FEP_MESSAGE, id);
			break;
		case "IBK_FEP_HEADER_MESSAGE" :
			result = cacheService.find(CacheType.IBK_FEP_HEADER_MESSAGE, id);
			break;
		case "IBK_FEP_UNIT" :
			result = cacheService.find(CacheType.IBK_FEP_UNIT, id);
			break;
		case "IBK_FEP_MAPPING" :
			result = cacheService.find(CacheType.IBK_FEP_MAPPING, id);
			break;
			
		case "IBK_FEP_RECV_MSG" :
			result = cacheService.find(CacheType.IBK_FEP_RECV_MSG, id);
		default:
			break;
		}		
		
		if (result != null) {
			return Converter.mapper.writeValueAsString(result);
		}
		return "";
	}
	
	@RequestMapping(value = "/cache/find", method = RequestMethod.GET)
	public void find() {
		IoCacheMCA test = cacheService.find(CacheType.IBK_TARGET_IO, "MCBKT011833");
		
		System.out.println(test);
	}
	
	@RequestMapping(value = "/cache/findAll", method = RequestMethod.GET)
	public void findAll() {
		System.out.println(cacheService.findAll(CacheType.IBK_MCA_INTERFACE));
		System.out.println(cacheService.findAll(CacheType.IBK_SOURCE_IO));
		System.out.println(cacheService.findAll(CacheType.IBK_TARGET_IO));
		System.out.println(cacheService.findAll(CacheType.IBK_MAPPING_IN));
		System.out.println(cacheService.findAll(CacheType.IBK_MAPPING_OUT));
		
		System.out.println("키 리스트1 : " + cacheFactory.getCacheRepository(CacheType.IBK_FEP_INTERFACE_EXT_INFO).getKeys());
		System.out.println("키 리스트2 : " + cacheFactory.getCacheRepository(CacheType.IBK_FEP_INTERFACE_KEY).getKeys());
		System.out.println("키 리스트3 : " + cacheFactory.getCacheRepository(CacheType.IBK_FEP_MESSAGE).getKeys());
		System.out.println("키 리스트4 : " + cacheFactory.getCacheRepository(CacheType.IBK_FEP_HEADER_MESSAGE).getKeys());
		System.out.println("키 리스트5 : " + cacheFactory.getCacheRepository(CacheType.IBK_FEP_UNIT).getKeys());
		System.out.println("키 리스트6 : " + cacheFactory.getCacheRepository(CacheType.IBK_FEP_MAPPING).getKeys());
		
		Interface intf = cacheService.find(CacheType.IBK_FEP_INTERFACE_EXT_INFO, "FEPO00000580");
		
		System.out.println("특정 인터페이스 : " + intf);
		
		System.out.println(cacheService.findAll(CacheType.IBK_FEP_INTERFACE_EXT_INFO));
		System.out.println(cacheService.findAll(CacheType.IBK_FEP_INTERFACE_KEY));
		System.out.println(cacheService.findAll(CacheType.IBK_FEP_MESSAGE));
		System.out.println(cacheService.findAll(CacheType.IBK_FEP_HEADER_MESSAGE));
		System.out.println(cacheService.findAll(CacheType.IBK_FEP_UNIT));
		System.out.println(cacheService.findAll(CacheType.IBK_FEP_MAPPING));
	}
	
	@RequestMapping(value = "/cache/findCount", method = RequestMethod.GET)
	public void findCount() {
		cacheService.findCount(CacheType.IBK_MCA_INTERFACE);
		cacheService.findCount(CacheType.IBK_SOURCE_IO);
		cacheService.findCount(CacheType.IBK_TARGET_IO);
		cacheService.findCount(CacheType.IBK_MAPPING_IN);
		cacheService.findCount(CacheType.IBK_MAPPING_OUT);
		
		cacheService.findCount(CacheType.IBK_FEP_INTERFACE_EXT_INFO);
		cacheService.findCount(CacheType.IBK_FEP_INTERFACE_KEY);
		cacheService.findCount(CacheType.IBK_FEP_MESSAGE);
		cacheService.findCount(CacheType.IBK_FEP_HEADER_MESSAGE);
		cacheService.findCount(CacheType.IBK_FEP_UNIT);
		cacheService.findCount(CacheType.IBK_FEP_MAPPING);
		
		cacheService.findCount(CacheType.IBK_FEP_RECV_MSG);
		
		System.out.println("키 리스트 확인 : " + cacheFactory.getCacheRepository(CacheType.IBK_FEP_RECV_MSG).getKeys());
	}
}
