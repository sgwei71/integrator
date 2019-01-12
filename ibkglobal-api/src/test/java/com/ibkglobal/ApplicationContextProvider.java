/*
 * Copyright 2019. IBK(INDUSTRIAL BANK OF KOREA) All rights reserved.
 */

package com.ibkglobal;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @since 2019. 1. 9.
 * @author d25856
 * <PRE>
 * 2019. 1. 9. d25856 : 최초작성
 * </PRE>
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	private static ApplicationContext ctx = null;
	
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = ctx;
		
	}
	
}
