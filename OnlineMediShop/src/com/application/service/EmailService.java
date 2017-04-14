package com.application.service;
import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.application.dao.EmailDao;

public class EmailService extends  BaseService implements EmailDao {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	private JavaMailSender mailSender;
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;  
    }
	
	public void sendEmail() {
		try{  
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);  
	        helper.setFrom("kaustav.1000@gmail.com");  
	        helper.setTo("kaustav.1000@gmail.com");  
	        helper.setSubject("TEST");  
	        helper.setText("");  
	        // attach the file  
	        FileSystemResource file = new FileSystemResource(new File("D:/IMG_20160210_154234.jpg"));  
	        helper.addAttachment("mybrothermage.jpg", file);//image will be sent by this name  
	  
	        mailSender.send(message);  
	        }catch(MessagingException e){
	        	e.printStackTrace();
	        }  
	    }
}