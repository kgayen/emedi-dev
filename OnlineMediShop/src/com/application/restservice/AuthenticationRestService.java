package com.application.restservice;

import javax.xml.bind.DatatypeConverter;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.exception.CustomGenericException;
import com.application.model.SMSModel;
import com.application.service.AuthenticationService;
import com.application.service.SendSMSService;
import com.application.utility.SpringPropertiesUtil;


@RestController
@RequestMapping(value = "/authenticationservice")
@Configuration
//@PropertySource({"classpath:tablename.properties","classpath:notification.properties","classpath:errorTable.properties","classpath:SMS.properties"})
public class AuthenticationRestService {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationRestService.class);
	
	@Autowired
	private JSONObject jsonObject;
	@Autowired
	private SMSModel smsModel;
	@Autowired
	SendSMSService sendSMSService;
	@Autowired
	AuthenticationService authenticationService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrive/forgetpassword", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> retrivePassword(@RequestParam(value="mobilenumber")String mobilenumber) {
		
		JSONObject jsonResponseObject = new JSONObject();
		try{
			byte[] decoded = DatatypeConverter.parseBase64Binary(mobilenumber);
			logger.info("Executing Method retrivePassword(). Decode: "+new String(decoded,"UTF-8"));
			String getPassword = "";
			try{
				getPassword = authenticationService.retrivePassword(new String(decoded,"UTF-8"));
			}
			catch(Exception e){
				String errorMsg = SpringPropertiesUtil.getProperty("1113") + e.getMessage();
				throw  new CustomGenericException("1113",errorMsg,
						Thread.currentThread().getStackTrace()[1].getClassName(),
						Thread.currentThread().getStackTrace()[1].getMethodName(),
						mobilenumber);
			}
			if(getPassword != "" && getPassword != null){
				List<JSONObject> smsList = new ArrayList<JSONObject>();
				JSONObject jsonObjectSMS = new JSONObject();
				jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
				jsonObjectSMS.put("message", "Your password is "+getPassword);
				smsList.add(jsonObjectSMS);
				sendSMSService.processSMS(smsList, Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")), Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
				jsonResponseObject.put("message", SpringPropertiesUtil.getProperty("4001"));
			}
			else{
				jsonResponseObject.put("message", SpringPropertiesUtil.getProperty("4002"));
			}
		}
		catch(Exception excep){
			logger.info("Exception occured at the time of sending SMS: "+excep);
			jsonResponseObject.put("message", SpringPropertiesUtil.getProperty("4002"));
		}
		return new ResponseEntity<JSONObject>(jsonResponseObject, HttpStatus.OK);
    }
}
