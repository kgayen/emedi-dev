package com.application.restservice;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.application.exception.CustomGenericException;
import com.application.logger.LogActivity;
import com.application.model.Notification;
import com.application.model.Order;
import com.application.model.OrderStatus;
import com.application.model.SMSModel;
import com.application.model.SearchRefundOrder;
import com.application.model.User;
import com.application.service.NotificationService;
import com.application.service.OrderService;
import com.application.service.SMSService;
import com.application.service.SellerService;
import com.application.service.UserService;

@RestController
@RequestMapping(value = "/testService")
public class TestService {
	
	@Autowired
	UserService userService;
	@Autowired
	OrderService orderService;
	@Autowired
	NotificationService notificationService;
	@Autowired
	OrderStatus orderStatus;
	@Autowired
	User user;
	@Autowired
	Order order;
	@Autowired
	Notification notification;
	@Autowired
	LogActivity logActivity;
	@Autowired
	JSONObject jsonObject;
	@Autowired
	SearchRefundOrder searchRefundOrder;
	@Autowired
	SellerService sellerService;
	@Autowired
	SMSService smsService;
	@Autowired
	SMSModel smsModel;
	
	private static final Logger logger = LoggerFactory.getLogger(TestService.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/smsservice", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> sendSMS(@RequestBody SMSModel smsModel,
																HttpServletRequest req) throws CustomGenericException {
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		logger.info("Executing Method sendSMS(). UserID: "+smsModel.getMessage());
		try{
			//smsService.sendSMS(smsModel);
			//smsService.sendSMSJavaPOST(smsModel);
			smsService.sendSMSJavaURLPOST(smsModel);
			jsonObject.put("status", "success");
		}
		catch(Exception smsExcep){
			logger.info("Exception sendSMS() exception message: "+smsExcep.getMessage());
			jsonObject.put("status", "exception");
			jsonObject.put("message", smsExcep.getMessage());
		}
		
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }

}
