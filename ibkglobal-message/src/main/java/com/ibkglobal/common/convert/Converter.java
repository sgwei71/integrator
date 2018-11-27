package com.ibkglobal.common.convert;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Converter {
	
	@Autowired
	ObjectMapper objectMapper;
	
	public static ObjectMapper mapper;
	
	@PostConstruct
	public void setMapper() {
		mapper = objectMapper;
	}
}
