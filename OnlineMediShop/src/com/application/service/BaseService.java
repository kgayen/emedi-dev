package com.application.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BaseService implements ApplicationContextAware {
	
	protected static ApplicationContext context;

	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext context){
		this.context = context;
	}
}