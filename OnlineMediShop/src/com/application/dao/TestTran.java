package com.application.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.application.model.User;
import com.application.service.UserService;

public class TestTran {

	public static void main(String args[]){
		@SuppressWarnings("resource")
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		@SuppressWarnings("rawtypes")
		UserService dao = (UserService)ctx.getBean("userDao");
		User user = new User();
		user.setUser_id("kgayen");
		user.setUser_password("kgayen");
		@SuppressWarnings("unchecked")
		List<User> status= (List<User>) dao.getDataList(user);
		System.out.println(status.size());
		System.out.println(status.get(0).getUser_name()); 
	}
}
