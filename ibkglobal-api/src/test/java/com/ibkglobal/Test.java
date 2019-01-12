/*
 * Copyright 2019. IBK(INDUSTRIAL BANK OF KOREA) All rights reserved.
 */

package com.ibkglobal;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @since 2019. 1. 9.
 * @author d25856
 * <PRE>
 * 2019. 1. 9. d25856 : 최초작성
 * </PRE>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com")
public class Test {

	@org.junit.Test
	public void test1() {}
	
	@org.junit.Test
	public void test() {
	
		System.out.println(com.ibkglobal.common.util.BeanService.getApplicationContext());
	}
}
