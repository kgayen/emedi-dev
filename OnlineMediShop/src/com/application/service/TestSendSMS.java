package com.application.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSendSMS {
	public static void main(String[] args) throws Exception
    {
		 /*send_sms smsObj = new send_sms();
	     smsObj.setparams("http://trans.kapsystem.com","sms.json","fff%20A18e2a3f84d385bf3d746b56ee864a7ba","KAPMSG");
	     smsObj.send_sms("{\"unicode\": 1,\"flash\": 0,\"sms\": [{\"to\": 9903060754,\"sender\": \"KAPMSG\",\"message\": \"Order Delivered.\"}, {\"to\": 8013653194,\"sender\": \"KAPMSG\",\"message\": \"One Order in your bucket.\"}]}");
	     */
	     ApplicationContext context = new ClassPathXmlApplicationContext("emailConfiguration.xml");
	     EmailService mm = (EmailService) context.getBean("emailService");
	     mm.sendEmail();
     }
}
