package com.ibkglobal.message.release.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibkglobal.common.convert.Converter;
import com.ibkglobal.common.repository.service.CacheService;
import com.ibkglobal.message.release.service.ReleaseService;

@RestController
public class ReleaseController {
	
	@Autowired
	ReleaseService releaseService;
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	Converter converter;
	
	@RequestMapping(value = "/release/all", method = RequestMethod.GET)
	public void releaseAll() {
		// 배포 시작
		releaseService.releaseAll();
	}
}
