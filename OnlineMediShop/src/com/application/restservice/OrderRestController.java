package com.application.restservice;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.exception.CustomGenericException;
import com.application.logger.LogActivity;
import com.application.model.Notification;
import com.application.model.Order;
import com.application.model.OrderSearchDao;
import com.application.model.OrderStatus;
import com.application.model.OrderWrapper;
import com.application.model.RefundOrder;
import com.application.model.RefundOrderRequest;
import com.application.model.ReturnValidationRequest;
import com.application.model.SMSModel;
import com.application.model.SearchRefundOrder;
import com.application.model.Seller;
import com.application.model.User;
import com.application.service.NotificationService;
import com.application.service.OrderService;
import com.application.service.SMSService;
import com.application.service.SellerService;
import com.application.service.SendSMSService;
import com.application.service.UserService;
import com.application.utility.OrderUtility;
import com.application.utility.SpringPropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Configuration;

@RestController
@RequestMapping(value = "/orderservice")
@Configuration
//@PropertySource({"classpath:tablename.properties","classpath:notification.properties","classpath:errorTable.properties","classpath:SMS.properties"})
public class OrderRestController {
	
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
	@Autowired
	SendSMSService sendSMSService;
	
	private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);
	/*
	 * This controller method used to return user list.
	 * Request mapping URL contains two path variable, urlPath and queryValue.
	 * urlPath can accept two values. getuserbymobile and getuserbyuserid. That means user return by two way. One is by mobile numbe and 
	 * another is by userid. Password is not returning in the contain data.
	 */
	@RequestMapping(value = "/{urlPath}/{queryValue}", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getuserInfo(@PathVariable String queryValue,
    												@PathVariable String urlPath) {
		
		logger.info("Executing Method getuserInfo(). urlPath: "+urlPath+" , queryValue: "+queryValue);
		List<User> users = new ArrayList<User>();
		if(urlPath.equalsIgnoreCase("getuserbymobile")){
			user.setUser_mobile(queryValue);
			users = userService.getUserListByMobileNo(user);
		}
		else if(urlPath.equalsIgnoreCase("getuserbyuserid")){
			users = userService.getDataListByUserId(queryValue);
		}
		logger.info("Executing Method getuserInfo(). User Size: "+users.size());
        if(users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
	
	/*@RequestMapping(value = "/rest/{urlPath}/{queryValue}", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getOrderInfo(@PathVariable String queryValue,
    												@PathVariable String urlPath) {
		
		logger.info("Executing Method getOrderInfo(). urlPath: "+urlPath+" , queryValue: "+queryValue);
		List<Order> orders = orderService.getOrderList(urlPath, queryValue);
		logger.info("Executing Method getOrderInfo(). User Size: "+orders.size());
        if(orders.isEmpty()){
            return new ResponseEntity<List<Order>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }*/
	
	@RequestMapping(value = "/rest/{urlPath}/{queryValue}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderWrapper>> getOrderWrapperInfo(@PathVariable String queryValue,
    												@PathVariable String urlPath) {
		
		logger.info("Executing Method getOrderWrapperInfo(). urlPath: "+urlPath+" , queryValue: "+queryValue);
		List<OrderWrapper> orders = orderService.getWrapperOrderList(urlPath, queryValue);
		logger.info("Executing Method getOrderWrapperInfo(). User Size: "+orders.size());
        if(orders.isEmpty()){
            return new ResponseEntity<List<OrderWrapper>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<OrderWrapper>>(orders, HttpStatus.OK);
    }
	
	@RequestMapping(value="/image/getimage/{queryValue}", method = RequestMethod.GET, produces = "image/jpg")
	public @ResponseBody byte[] getOrderImage(@PathVariable String queryValue)  {
		logger.info("Executing Method getOrderImage(). queryValue: "+queryValue);
	    try {
	        InputStream is = orderService.getImageStream(queryValue);
	        BufferedImage img = ImageIO.read(is);
	        ByteArrayOutputStream bao = new ByteArrayOutputStream();
	        ImageIO.write(img, "jpg", bao);
	        logger.info("Executing Method getOrderImage(). Returning Image.");
	        return bao.toByteArray();
	    } catch (IOException e) {
	        logger.error(e.getMessage());
	        throw new RuntimeException(e);
	    }
	}
	
	@RequestMapping(value="/image/downloadimage/{queryValue}", method = RequestMethod.GET, produces = "image/jpg")
	public @ResponseBody void downloadOrderImage(@PathVariable String queryValue,
			HttpServletRequest request, HttpServletResponse response)  {
		logger.info("Executing Method getOrderImage(). queryValue: "+queryValue);
	    try {
	        InputStream is = orderService.getImageStream(queryValue);
	        OutputStream os = response.getOutputStream();
	    	response.setContentType("image/jpg");
	    	String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"","Prescription_"+queryValue+".jpg");
			response.setHeader(headerKey, headerValue);
			//logActivity.saveLogActivity(((User)request.getAttribute("user")).getUser_id(), "Order image has been donloaded successfully. Order id is "+queryValue+".", env.getProperty("LogActivity.Success"), this.getClass().getCanonicalName(), "downloadOrderImage");
			IOUtils.copy(is, os);
	    } catch (IOException e) {
	        logger.error(e.getMessage());
	        throw new RuntimeException(e);
	    }
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/modifyCustOrder",method=RequestMethod.POST,consumes = {"multipart/form-data"})
	public @ResponseBody String modifyCustOrder(
							@RequestParam(value="orderid")String orderid,
							@RequestParam(value="address")String address,
							@RequestParam(value="pincode")String pincode,
							@RequestParam(value="ordereditdetails", required = false)String ordereditdetails,
							@RequestParam(value = "editPrescriptionImg", required = false) MultipartFile image,
							@RequestParam(value = "emergencyflag", required = false) String emergencyflag,
							@RequestParam(value = "emergencyprice") String emergencyprice,
							HttpServletRequest req){
		logger.info("Executing Method modifyCustOrder(). Returning String.");
		HttpSession session = req.getSession();
		User user = ((User)session.getAttribute("user"));
		try {
			List<Order> orders = orderService.getOrderList("getOrderByID", orderid);
			List<Seller> sellerList = sellerService.getSeller("getSellerByPincode", pincode);
			if(orders.size() == 1){
				Order order = orders.get(0);
				List<Object> updatedFieldValues = new ArrayList<Object>();
				List<String> modifiedFieldNames = new ArrayList<String>();
				List<Integer> typeList = new ArrayList<Integer>();
				List<String> whereClauseFieldNames = new ArrayList<String>();
				if(image!=null){
					updatedFieldValues.add(image.getBytes());
					typeList.add(Types.BLOB);
					modifiedFieldNames.add("order_pec_image");
				}
				if(!address.equalsIgnoreCase(order.getShippingAddress())){
					updatedFieldValues.add(address);
					typeList.add(Types.VARCHAR);
					modifiedFieldNames.add("order_address");
				}
				if(!pincode.equalsIgnoreCase(order.getOrderPincode())){
					updatedFieldValues.add(pincode);
					typeList.add(Types.VARCHAR);
					modifiedFieldNames.add("order_pincode");
					
					updatedFieldValues.add(sellerList.get(0).getSellerid());
					typeList.add(Types.BIGINT);
					modifiedFieldNames.add("order_seller_id");
					
					updatedFieldValues.add(sellerList.get(0).getSellerDeliveryPrice());
					typeList.add(Types.DOUBLE);
					modifiedFieldNames.add("order_delivery_price");
					
					updatedFieldValues.add(sellerList.get(0).getSellerDiscount());
					typeList.add(Types.DOUBLE);
					modifiedFieldNames.add("order_discount_amount");
				}
				if(!ordereditdetails.equalsIgnoreCase(order.getOrderdetails())){
					updatedFieldValues.add(ordereditdetails);
					typeList.add(Types.VARCHAR);
					modifiedFieldNames.add("order_details");
				}
				if(emergencyflag!=null && emergencyflag.equalsIgnoreCase("on")){
					updatedFieldValues.add(1);
					updatedFieldValues.add(new BigDecimal(emergencyprice));
					typeList.add(Types.INTEGER);
					typeList.add(Types.DECIMAL);
					modifiedFieldNames.add("order_emergencyFlag");
					modifiedFieldNames.add("order_emergency_price");
				}
				else{
					updatedFieldValues.add(0);
					updatedFieldValues.add(new BigDecimal(emergencyprice));
					typeList.add(Types.INTEGER);
					typeList.add(Types.DECIMAL);
					modifiedFieldNames.add("order_emergencyFlag");
					modifiedFieldNames.add("order_emergency_price");
				}
				
				typeList.add(Types.BIGINT);
				modifiedFieldNames.add("order_id");
				updatedFieldValues.add(BigInteger.valueOf(Long.valueOf(orderid)));
				whereClauseFieldNames.add("order_id");
				
				logger.info("Executing Method modifyCustOrder(). modifiedFieldNames size="+modifiedFieldNames.size());
				logger.info("Executing Method modifyCustOrder(). typeList size="+typeList.size());
				logger.info("Executing Method modifyCustOrder(). updatedFieldValues size="+updatedFieldValues.size());
				logger.info("Executing Method modifyCustOrder(). Table Name ="+SpringPropertiesUtil.getProperty("Order.TableName"));
				Object[] updatedFieldObjects = updatedFieldValues.toArray();
				String[] modifiedFieldNameStrings = OrderUtility.convertToStringArray(modifiedFieldNames);
				int[] types = OrderUtility.convertToInt(typeList);
				int updateReturn = orderService.updateOrderDetails(updatedFieldObjects,modifiedFieldNameStrings,types,SpringPropertiesUtil.getProperty("Order.TableName")
						,OrderUtility.convertToWhereClauseString(whereClauseFieldNames),whereClauseFieldNames);
				logger.info("Executing Method modifyCustOrder(). Updated Row Counts:"+updateReturn);
				if(updateReturn == 1){
					notification.setNotificationText(SpringPropertiesUtil.getProperty("Notification.updateText")+orderid+".");
					notification.setNotifyuser(user.getUser_id());
					notificationService.insertNotoficationDetails(notification);
					if(!pincode.equalsIgnoreCase(order.getOrderPincode())){
						try{
							jsonObject = new JSONObject();
							smsModel = new SMSModel();
							List<JSONObject> smsList = new ArrayList<JSONObject>();
							JSONObject jsonObjectSMS = new JSONObject();
							jsonObjectSMS.put("to", user.getUser_mobile());
							jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
							jsonObjectSMS.put("message", "Order Id "+order.getOrderid()+" has been modified successfully. Modified Pincode is "+pincode+".");
							smsList.add(jsonObjectSMS);
							jsonObjectSMS = new JSONObject();
							jsonObjectSMS.put("to", sellerList.get(0).getSellerMobileNo());
							jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
							jsonObjectSMS.put("message", "Order Id "+order.getOrderid()+" has been moved from your bucket.");
							smsList.add(jsonObjectSMS);
							jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
							jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
							jsonObject.put("sms", smsList);
							smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
							logger.info("SMS JSON String : "+jsonObject.toJSONString());
							sendSMSService.process_sms(smsModel);
						}
						catch(Exception excep){
							logger.info("Exception occured at the time of sending SMS: "+excep.getMessage());
						}
					}
					return "SUCCESS: Order successfully modified.";
				}
				else{
					return "ERROR: Order unavailable.";
				}
			}
			else{
				return "ERROR: Order unavailable.";
			}
			//System.out.println(image.getInputStream());
		} catch (Exception e) {
			logger.info("Executing Method modifyCustOrder(). Exception:"+e.getMessage());
			return "ERROR: During modify order id "+orderid+".";
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/cancelOrder",method=RequestMethod.POST)
	public @ResponseBody String cancelOrder(
							@RequestParam(value="orderid")String orderid,
							HttpServletRequest req) throws CustomGenericException{
		logger.info("Executing Method cancelOrder(). Invoke cancelOrder method.");
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		String statusStr = SpringPropertiesUtil.getProperty("2001");
		try{
			List<Order> orders = orderService.getOrderList("getOrderByID", orderid);
			if(orders.size()>0){
				logger.info("Executing Method cancelOrder(). Valid Order Id. Order Id is "+orderid);
				Object[] fieldValues = {"Cancel",orderid};
				String[] fieldNames = {"order_status"};
				int[] fieldDataTypes = {Types.VARCHAR,Types.BIGINT};
				List<String> whereClauseFieldNames = new ArrayList<String>();
				whereClauseFieldNames.add("order_id");
				int i = orderService.updateOrderDetails(fieldValues, fieldNames, fieldDataTypes, SpringPropertiesUtil.getProperty("Order.TableName"),
						OrderUtility.convertToWhereClauseString(whereClauseFieldNames), whereClauseFieldNames);
				if(i>0){
					logActivity.saveLogActivity(user.getUser_id(), "Order has been cancelled successfully. Order id is "+orderid+". Requested IP Address is "+OrderUtility.getIPAddress(), SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getCanonicalName(), "cancelOrder");
					logger.info("Executing Method cancelOrder(). Cancel Order successfully done. Order Id is "+orderid);
					
					orderStatus.setOrderid(Long.valueOf(orderid));
					orderStatus.setOrdermodifier(user.getUser_id());
					orderStatus.setOrderstatus("Cancel");
					orderStatus.setOrderPlacedIP(OrderUtility.getIPAddress());
					notification.setNotificationText(SpringPropertiesUtil.getProperty("Notification.cancelText")+orderid+".");
					notification.setNotifyuser(user.getUser_id());
					orderService.OrderStoreProcService(notification, orderStatus);
					
					try{
						/*smsModel = new SMSModel();
						smsModel.setTo(user.getUser_mobile());
						List<String> smsVariableProperty = new ArrayList<String>();
						smsVariableProperty.add(orderid);
						DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm");
						Calendar calendar = Calendar.getInstance();
					    calendar.setTimeInMillis(System.currentTimeMillis());
						smsVariableProperty.add(formatter.format(calendar.getTime()));
						smsModel.setSmsVariableProperties(smsVariableProperty);
						smsModel.setTemplateId(env.getProperty("CancelOrderSMS_TEMPLATE_ID"));
						smsModel.setSmsVariableProperties(smsVariableProperty);
						smsService.sendSMSJavaURLPOST(smsModel);*/
						jsonObject = new JSONObject();
						smsModel = new SMSModel();
						List<JSONObject> smsList = new ArrayList<JSONObject>();
						JSONObject jsonObjectSMS = new JSONObject();
						jsonObjectSMS.put("to", user.getUser_mobile());
						jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
						jsonObjectSMS.put("message", "Order id "+orderid+" has been successfully cancelled.");
						smsList.add(jsonObjectSMS);
						jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
						jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
						jsonObject.put("sms", smsList);
						logger.info("SMS JSON String : "+jsonObject.toJSONString());
						smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
						sendSMSService.process_sms(smsModel);
					}
					catch(Exception excep){
						logger.info("Exception occured at the time of sending SMS: "+excep.getMessage());
					}
				}
				else{
					logger.info("Executing Method cancelOrder(). Cancel Order failed. Order Id is "+orderid);
					throw  new CustomGenericException("1119",SpringPropertiesUtil.getProperty("1119"),
							Thread.currentThread().getStackTrace()[1].getClassName(),
							Thread.currentThread().getStackTrace()[1].getMethodName(),
							user.getUser_id());
				}
			}
			else{
				logger.info("Executing Method cancelOrder(). Invalid Order Id. Order Id is "+orderid);
				throw  new CustomGenericException("1115",SpringPropertiesUtil.getProperty("1115"),
						Thread.currentThread().getStackTrace()[1].getClassName(),
						Thread.currentThread().getStackTrace()[1].getMethodName(),
						user.getUser_id());
			}
		}
		catch(Exception e){
			String errorMsg = SpringPropertiesUtil.getProperty("1120");
			errorMsg.concat(e.getMessage());
			throw  new CustomGenericException("1120",errorMsg,
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		return statusStr;
	}
	
	/*@RequestMapping(value="/testexception",method=RequestMethod.POST)
	@ResponseBody
	public String testException(//ExceptionInfo
							@RequestParam(value="orderid")String orderid,
							HttpServletRequest req) throws CustomGenericException{
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		if(orderid.equals("0")){
			throw  new CustomGenericException("1111",env.getProperty("1111"),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		return "OK";
	}*/

	@RequestMapping(value = "/searchOrder", method = RequestMethod.POST,consumes={"application/json"})
    public ResponseEntity<List<OrderWrapper>> getOrdersBySearch(@RequestBody OrderSearchDao jsonData,
																HttpServletRequest req) throws CustomGenericException {
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		logger.info("Executing Method getOrdersBySearch(). UserID: "+user.getUser_id());
		List<OrderWrapper> orders = new ArrayList<OrderWrapper>();
		if(jsonData.getOrderSearchType().equalsIgnoreCase("ByDateRange")){
			orders = orderService.getSearchOrderList(jsonData, user);
		}
		else if(jsonData.getOrderSearchType().equalsIgnoreCase("ByOrderID")){
			orders = orderService.getSearchOrderList(jsonData, user);
		}
		else{
			orders = orderService.getSearchOrderList(jsonData, user);
		}
		logger.info("Executing Method getOrderWrapperInfo(). User Size: "+orders.size());
        if(orders.isEmpty()){
            //return new ResponseEntity<List<OrderWrapper>>(HttpStatus.NO_CONTENT);
        	throw  new CustomGenericException("1116",SpringPropertiesUtil.getProperty("1116"),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
        }
        return new ResponseEntity<List<OrderWrapper>>(orders, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/refundordervalidation", method = RequestMethod.POST,consumes={"application/json"})
    public ResponseEntity<JSONObject> validateRefundOrder(@RequestBody ReturnValidationRequest validationRequest,
																HttpServletRequest req) throws CustomGenericException {
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		jsonObject = orderService.validateRefundOrder(validationRequest, user);
		logger.info("Executing Method validateRefundOrder(). Status: "+jsonObject.get("status").toString());
        if(jsonObject.get("status").toString().equalsIgnoreCase("error")){
        	throw  new CustomGenericException(jsonObject.get("errorcode").toString(),
        			SpringPropertiesUtil.getProperty(jsonObject.get("errorcode").toString()).concat(jsonObject.get("errormsg").toString()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
        }
        else if(jsonObject.get("status").toString().equalsIgnoreCase("invalid")){
        	throw  new CustomGenericException(jsonObject.get("errorcode").toString(),
        			SpringPropertiesUtil.getProperty(jsonObject.get("errorcode").toString()).concat(jsonObject.get("errormsg").toString()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
        }
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/refundorderrequest", method = RequestMethod.POST,consumes={"application/json"})
    public ResponseEntity<JSONObject> saveRefundOrderRequest(@RequestBody RefundOrderRequest refundOrderRequest,
																HttpServletRequest req) throws CustomGenericException {
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		logger.info("Executing Method saveRefundOrderRequest(). UserID and OrderId: "+user.getUser_id()+ " and "+refundOrderRequest.getRefundOrderid());
		refundOrderRequest.setRequestedHostIP(String.valueOf(req.getAttribute("requestedIP")));
		refundOrderRequest.setRefundNotificattionText(SpringPropertiesUtil.getProperty("Notification.refundcanInsertText")+refundOrderRequest.getRefundRequestId()+".");
		refundOrderRequest.setRefundInitiateUser(user.getUser_id());
		jsonObject.clear();
		refundOrderRequest.setRefundRequestId("RFO"+String.valueOf(System.currentTimeMillis()));
		jsonObject = orderService.saveRefundOrderRequest(refundOrderRequest, user);
		logger.info("Executing Method saveRefundOrderRequest(). RequestID and  Status: "+refundOrderRequest.getRefundRequestId()+ " and "+ jsonObject.get("status").toString());
        if(jsonObject.get("status").toString().equalsIgnoreCase("error")){
        	throw  new CustomGenericException(jsonObject.get("errorcode").toString(),
        			SpringPropertiesUtil.getProperty(jsonObject.get("errorcode").toString()).concat(jsonObject.get("errormsg").toString()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
        }
        else if(jsonObject.get("status").toString().equalsIgnoreCase("invalid")){
        	throw  new CustomGenericException(jsonObject.get("errorcode").toString(),
        			SpringPropertiesUtil.getProperty(jsonObject.get("errorcode").toString()).concat(jsonObject.get("errormsg").toString()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
        }
        
        try{
			/*smsModel = new SMSModel();
			smsModel.setTo(user.getUser_mobile());
			List<String> smsVariableProperty = new ArrayList<String>();
			smsVariableProperty.add(order.getOrderid());
			smsVariableProperty.add(refundOrderRequest.getRefundRequestId());
			smsVariableProperty.add(String.valueOf(refundOrderRequest.getRefundAmount()));
			smsModel.setTemplateId(env.getProperty("PendingRefundOrderSMS_TEMPLATE_ID"));
			smsService.sendSMSJavaURLPOST(smsModel);*/
			jsonObject = new JSONObject();
			smsModel = new SMSModel();
			List<JSONObject> smsList = new ArrayList<JSONObject>();
			JSONObject jsonObjectSMS = new JSONObject();
			jsonObjectSMS.put("to", user.getUser_mobile());
			jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
			jsonObjectSMS.put("message", "Your refund request has been submitted for order "+refundOrderRequest.getRefundOrderid()+". Refund Request ID is "+refundOrderRequest.getRefundRequestId()+". Refund amound is "+refundOrderRequest.getRefundAmount()+".");
			smsList.add(jsonObjectSMS);
			jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
			jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
			jsonObject.put("sms", smsList);
			logger.info("SMS JSON String : "+jsonObject.toJSONString());
			smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
			sendSMSService.process_sms(smsModel);
		}
		catch(Exception excep){
			logger.info("Exception occured at the time of sending SMS: "+excep.getMessage());
		}
        
        logActivity.saveLogActivity(user.getUser_id(), "Refund Order has been initiated successfully. Order id is "+refundOrderRequest.getRefundOrderid()+" and "
        		+ " refund request id is "+refundOrderRequest.getRefundRequestId()+"."
        		+ " Requested IP Address is "+String.valueOf(req.getAttribute("requestedIP")), SpringPropertiesUtil.getProperty("LogActivity.Success"),
        		Thread.currentThread().getStackTrace()[1].getClassName(),Thread.currentThread().getStackTrace()[1].getMethodName());
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/getRefundOrders", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> searchRefundOrder(@RequestParam(value = "searchRefundOrder", required = true) String requestedJSONString,
																HttpServletRequest req) throws CustomGenericException {
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		logger.info("Executing Method searchRefundOrder(). UserID: "+user.getUser_id());
		try{
			searchRefundOrder = new ObjectMapper().readValue(requestedJSONString, SearchRefundOrder.class);
			logger.info("Executing Method searchRefundOrder(). Search Type: "+searchRefundOrder.getRefundSearchType());
			jsonObject = orderService.searchRefundOrders(searchRefundOrder, user);
		}
		catch(Exception jsonPExcp){
			throw  new CustomGenericException("3001",SpringPropertiesUtil.getProperty("3001"),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		if(jsonObject.get("status").toString().equalsIgnoreCase("error")){
        	throw  new CustomGenericException(jsonObject.get("errorcode").toString(),
        			SpringPropertiesUtil.getProperty(jsonObject.get("errorcode").toString()).concat(jsonObject.get("errormsg").toString()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
        }
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/changerefundorderstatus", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> changeRefundOrderStatus(@RequestBody RefundOrderRequest refundOrderRequest,
																HttpServletRequest req) throws CustomGenericException {
		HttpSession session = req.getSession();
		user = ((User)session.getAttribute("user"));
		logger.info("Executing Method cancelRefundOrder(). UserID: "+user.getUser_id());
		refundOrderRequest.setRequestedHostIP(String.valueOf(req.getAttribute("requestedIP")));
		refundOrderRequest.setRefundNotificattionText(SpringPropertiesUtil.getProperty("Notification.refundCancelText")+refundOrderRequest.getRefundRequestId()+".");
		refundOrderRequest.setRefundInitiateUser(user.getUser_id());
		
		jsonObject.clear();
		searchRefundOrder.setRefundSearchType("SearchByRefundId"); 
		List<String> refundOrderIdList = new ArrayList<String>();
		refundOrderIdList.add("'"+refundOrderRequest.getRefundRequestId()+"'");
		searchRefundOrder.setRefundOrderIdList(refundOrderIdList);
		jsonObject = orderService.searchRefundOrders(searchRefundOrder, user);
		if(jsonObject.get("status").toString().equalsIgnoreCase("success")){
			List<RefundOrder> refundOrders = (List<RefundOrder>)jsonObject.get("refundOrders");
			if(refundOrders.size()>0 && refundOrders.get(0).getRefundOrderStatus().equalsIgnoreCase("Cancel")){
				throw  new CustomGenericException("1126",
						SpringPropertiesUtil.getProperty("1126"),
						Thread.currentThread().getStackTrace()[1].getClassName(),
						Thread.currentThread().getStackTrace()[1].getMethodName(),
						user.getUser_id());
			}
		}
		else if(jsonObject.get("status").toString().equalsIgnoreCase("error")){
			throw  new CustomGenericException("1122",
					SpringPropertiesUtil.getProperty("1122").concat(jsonObject.get("errormsg").toString()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		
		List<Object> updatedFieldValues = new ArrayList<Object>();
		List<String> modifiedFieldNames = new ArrayList<String>();
		List<Integer> typeList = new ArrayList<Integer>();
		List<String> whereClauseFieldNames = new ArrayList<String>();
		if(!refundOrderRequest.getRefundOrderStatus().isEmpty() || refundOrderRequest.getRefundOrderStatus()!=null){
			updatedFieldValues.add(refundOrderRequest.getRefundOrderStatus());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("refund_status");
		}
		whereClauseFieldNames.add("refund_request_id");
		updatedFieldValues.add(refundOrderRequest.getRefundRequestId());
		typeList.add(Types.VARCHAR);
		String[] modifiedFieldNameStrings = OrderUtility.convertToStringArray(modifiedFieldNames);
		int[] types = OrderUtility.convertToInt(typeList);
		int updateReturn = 0;
		try{
			updateReturn = orderService.updateOrderDetails(updatedFieldValues.toArray(),modifiedFieldNameStrings,types,SpringPropertiesUtil.getProperty("RefundOrder.TableName")
					,OrderUtility.convertToWhereClauseString(whereClauseFieldNames),whereClauseFieldNames);
		}
		catch(Exception e){
			throw  new CustomGenericException("1125",
					SpringPropertiesUtil.getProperty("1125").concat(e.getMessage()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		
		jsonObject.clear();
		if(updateReturn >0){
			logActivity.saveLogActivity(user.getUser_id(), "Refund Order has been cancelled successfully. Refund request id is "+refundOrderRequest.getRefundRequestId()+"."
	        		+ " Requested IP Address is "+String.valueOf(req.getAttribute("requestedIP")), SpringPropertiesUtil.getProperty("LogActivity.Success"),
	        		Thread.currentThread().getStackTrace()[1].getClassName(),Thread.currentThread().getStackTrace()[1].getMethodName());
			jsonObject.put("status", "success");
			jsonObject.put("message", "Refund order "+refundOrderRequest.getRefundRequestId()+ " has been successfully cancelled.");

			String notificationMsg = "";
			String activity = "LogActivity.Success";
			int executeProcFlag = 0;
			try{
				executeProcFlag = orderService.insertNotificationAndStatusRefund(refundOrderRequest);
				if(executeProcFlag !=0){
					notificationMsg =  "Notification and status update successfully.";
				}
				else{
					notificationMsg = "Notification and statu are not updated successfully due to unknown reason.";
					activity = "LogActivity.NOTUPDATE";
				}
			}
			catch(Exception excp){
				notificationMsg = "Executing Method insertNotificationAndStatusRefund(). Exception : "+excp.getMessage();
				activity = "LogActivity.Exception";
				logger.info(notificationMsg);
			}
				
			try{
				/*smsModel = new SMSModel();
				smsModel.setTo(user.getUser_mobile());
				List<String> smsVariableProperty = new ArrayList<String>();
				DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm");
				Calendar calendar = Calendar.getInstance();
			    calendar.setTimeInMillis(System.currentTimeMillis());
				smsVariableProperty.add(refundOrderRequest.getRefundRequestId());
				smsVariableProperty.add(formatter.format(calendar.getTime()));
				smsModel.setTemplateId(env.getProperty("CancelRefundOrderSMS_TEMPLATE_ID"));
				smsService.sendSMSJavaURLPOST(smsModel);*/
				DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm");
				Calendar calendar = Calendar.getInstance();
			    calendar.setTimeInMillis(refundOrderRequest.getRefundLastModifiedTime().getTime());
				String msg = "";
				if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase("Confirm")){
					msg = "Refund request "+refundOrderRequest.getRefundRequestId()+" has been confirmed by seller on date "+formatter.format(calendar.getTime())+". Refund amount is "+refundOrderRequest.getRefundAmount()+". Refund will be delivered shortly.";
				}
				else if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase("cancel")){
					msg = "Refund request id "+refundOrderRequest.getRefundRequestId()+" has been cancelled successfully.";
				}
				else if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase("refunded")){
					msg = "Refund request "+refundOrderRequest.getRefundRequestId()+" has been delivered successfully on date "+formatter.format(calendar.getTime())+". Refunded amount is "+refundOrderRequest.getRefundAmount()+".";
				}
				jsonObject = new JSONObject();
				smsModel = new SMSModel();
				List<JSONObject> smsList = new ArrayList<JSONObject>();
				JSONObject jsonObjectSMS = new JSONObject();
				jsonObjectSMS.put("to", user.getUser_mobile());
				jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
				jsonObjectSMS.put("message", msg);
				smsList.add(jsonObjectSMS);
				jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
				jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
				jsonObject.put("sms", smsList);
				logger.info("SMS JSON String : "+jsonObject.toJSONString());
				smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
				sendSMSService.process_sms(smsModel);
			}
			catch(Exception excep){
				logger.info("Exception occured at the time of sending SMS: "+excep.getMessage());
			}
			logActivity.saveLogActivity(user.getUser_id(),notificationMsg , SpringPropertiesUtil.getProperty(activity),
	        		Thread.currentThread().getStackTrace()[1].getClassName(),Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		else{
			throw  new CustomGenericException("1122",
					SpringPropertiesUtil.getProperty("1122").concat(" Refund order is not cancelled."),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
}
