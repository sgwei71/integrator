package com.ibkglobal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ibkglobal.common.ApplicationReadyListener;
import com.ibkglobal.common.util.BeanService;
import com.ibkglobal.message.converter.service.ConverterServiceAPI;


@SpringBootTest
@RunWith(SpringRunner.class)
public class IbkglobalApiApplicationTest {

	@Test
	public void contextLoads() {
		System.out.println("------> "+BeanService.getApplicationContext());
		System.out.println("------> "+BeanService.getBean(ConverterServiceAPI.class));
	}
}
