package com.ibkglobal.integrator.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ibkglobal.message.IBKMessageProperties;
import com.ibkglobal.message.struct.resource.ResourceMCA;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageTest {
	
	@Value("${integrator.config.json-file-url}")
	String jsonFileUrl;
			
	@Autowired
	IBKMessageProperties ibkMessageProperties;
	
	@Autowired
	ResourceMCA resourceMCA;

	
	@Test
	public void mca() throws Exception {
		resourceMCA.init("file");

	}
}
