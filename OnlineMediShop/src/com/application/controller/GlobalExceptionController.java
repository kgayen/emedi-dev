package com.application.controller;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.application.exception.CustomGenericException;
import com.application.logger.LogActivity;
import com.application.utility.SpringPropertiesUtil;

@ControllerAdvice
public class GlobalExceptionController {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);
	
	@Autowired
	LogActivity logActivity;

	/*@ExceptionHandler(CustomGenericException.class)
	public @ResponseBody ExceptionInfo handleCustomException(CustomGenericException customException) {
		return customException.exceptionInfo();
	}*/
	
	@SuppressWarnings("finally")
	@ExceptionHandler(CustomGenericException.class)
	public @ResponseBody String handleCustomException(CustomGenericException customException) {
		try{
			logActivity.saveErrorLog(customException);
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Save Error Log In DB" ,customException.getExecutableMethodName() ,e.getMessage()));
		}
		finally{
			return customException.toString();
		}
	}
	
	/*@ExceptionHandler(CustomGenericException.class)
	public @ResponseBody JSONObject handleCustomException(CustomGenericException customException) throws JSONException {
		JSONObject last = new JSONObject(customException.toString()).getJSONArray("result").getJSONObject(0);
		return last;
	}*/
}