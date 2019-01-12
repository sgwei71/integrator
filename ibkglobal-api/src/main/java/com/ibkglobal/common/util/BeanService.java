/*
 * Copyright 2019. IBK(INDUSTRIAL BANK OF KOREA) All rights reserved.
 */

package com.ibkglobal.common.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


/**
 * @since 2019. 1. 11.
 * @author d25856
 * <PRE>
 * 2019. 1. 11. d25856 : 최초작성
 * </PRE>
 */
@Component
public class BeanService implements ApplicationContextAware{
	
	@Getter @Setter @Autowired
	private static ApplicationContext applicationContext;
	@Getter @Setter @Autowired
	private static ListableBeanFactory beanFactory;
	@Getter @Setter 
	private static boolean allowEagerInit = true;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanService.applicationContext = applicationContext;
	}
	
	public static Object getBean(String beanName) {
		return getBeanFactory().getBean(beanName);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType) {
		Map<String, T> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(getBeanFactory(), requiredType,true,allowEagerInit);
		return beans.get(name);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return BeanFactoryUtils.beanOfTypeIncludingAncestors(getBeanFactory(), requiredType,true,allowEagerInit);
	}
	 
	public static <T> T getBeanOfType(Class<T> requiredType) {
		return BeanFactoryUtils.beanOfTypeIncludingAncestors(getBeanFactory(), requiredType, true, allowEagerInit);
	}
	
	
	
}
