package com.application.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.application.exception.CustomGenericException;
import com.application.logger.LogActivity;
import com.application.model.CancellationRequest;
import com.application.model.CancellationRequestSearch;
import com.application.model.ConfirmOrderRequest;
import com.application.model.DeliverOrderRequest;
import com.application.model.Notification;
import com.application.model.Order;
import com.application.model.OrderStatus;
import com.application.model.RefundOrder;
import com.application.model.RefundOrderRequest;
import com.application.model.SMSModel;
import com.application.model.SearchRefundOrder;
import com.application.model.Seller;
import com.application.model.SellerStatisticData;
import com.application.model.SellerUpdateRequest;
import com.application.model.User;
import com.application.service.NotificationService;
import com.application.service.OrderService;
import com.application.service.SellerService;
import com.application.service.SendSMSService;
import com.application.service.UserService;
import com.application.utility.OrderUtility;
import com.application.utility.SpringPropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping(value = "/sellerrestservice")
public class SellerRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerRestController.class);
	
	@Autowired
	SellerService sellerService;
	@Autowired
	OrderService orderService;
	@Autowired
	SendSMSService sendSMSService;
	@Autowired
    UserService userService;
	@Autowired
	LogActivity logActivity;
	@Autowired
	NotificationService notificationService;
	@Autowired
	SearchRefundOrder searchRefundOrder;
	@Autowired
	SimpleDateFormat sdf;
	
	@RequestMapping(value = "/{urlPath}/{queryValue}", method = RequestMethod.GET)
    public ResponseEntity<List<Seller>> sellerInfo(@PathVariable String queryValue,
    												@PathVariable String urlPath) {
		logger.info("Executing Method sellerInfo(). urlPath: "+urlPath+" , queryValue: "+queryValue);
		List<Seller> sellerList = sellerService.getSeller(urlPath, queryValue);
		logger.info("Executing Method sellerInfo(). User Size: "+sellerList.size());
        if(sellerList.isEmpty()){
            return new ResponseEntity<List<Seller>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Seller>>(sellerList,HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/confirmOrder", method = RequestMethod.POST,consumes={"application/json"})
    public ResponseEntity<JSONObject> confirmedOrder(@RequestBody ConfirmOrderRequest confirmOrderReq,HttpServletRequest request)
    											throws CustomGenericException{
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		String ipAddress = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		} catch (Exception e) {
			logger.info("ipAddress::"+e.getMessage());
		}
		int queryExeStatus = 0;
		JSONObject confirmOrderResponse = new JSONObject();
		try{
			queryExeStatus = sellerService.confrimOrderNew(confirmOrderReq, user);
			//queryExeStatus = 0;
			if(queryExeStatus == 1){
				try{
					List<Order> orders = orderService.getOrderList("getOrderByID", confirmOrderReq.getOrderid());
					Order order = orders.get(0);
					Notification notification = new Notification();
					notification.setNotificationText(MessageFormat.format((String)SpringPropertiesUtil.getProperty("Notification.confirmedOrder"), confirmOrderReq.getOrderid(), SpringPropertiesUtil.getProperty("Notification.confirmedOrderStatus")));
					notification.setNotifyuser(order.getOrdercreator());
					OrderStatus orderStatus = new OrderStatus();
					orderStatus.setOrderid(Long.valueOf(confirmOrderReq.getOrderid()));
					orderStatus.setOrdermodifier(user.getUser_id());
					orderStatus.setOrderstatus(SpringPropertiesUtil.getProperty("confirm"));
					orderStatus.setOrderPlacedIP(ipAddress);
					orderService.OrderStoreProcService(notification, orderStatus);
					try{
						User orderCreator = new User();
						orderCreator = userService.getDataListByUserId(order.getOrdercreator()).get(0);
						JSONObject jsonObject = new JSONObject();
						SMSModel smsModel = new SMSModel();
						List<JSONObject> smsList = new ArrayList<JSONObject>();
						JSONObject jsonObjectSMS = new JSONObject();
						jsonObjectSMS.put("to", orderCreator.getUser_mobile());
						jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
						jsonObjectSMS.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("Notification.confirmedOrder"), confirmOrderReq.getOrderid(), SpringPropertiesUtil.getProperty("Notification.confirmedOrderStatus")));
						smsList.add(jsonObjectSMS);
						jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
						jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
						jsonObject.put("sms", smsList);
						logger.info("SMS JSON String : "+jsonObject.toJSONString());
						smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
						sendSMSService.process_sms(smsModel);
					}
					catch(Exception e){
						logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "SMS Sending" ,confirmOrderReq.getOrderid() ,e.getMessage()));
					}
				}
				catch(Exception e){
					logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Notifiction and Status" ,confirmOrderReq.getOrderid() ,e.getMessage()));
				}
				confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
				confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("SuccessResponse"), confirmOrderReq.getOrderid() , "confirmed"));
			}
			else{
				confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
				confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("FailureResponse"), confirmOrderReq.getOrderid() , "confirm"));
			}
		}
		catch(Exception e){
			logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Cashmemo Details" ,confirmOrderReq.getOrderid() ,e.getMessage()));
			throw  new CustomGenericException("6001",MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Cashmemo Details" ,confirmOrderReq.getOrderid() ,e.getMessage()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
        return new ResponseEntity<JSONObject>(confirmOrderResponse,HttpStatus.OK);
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/cancelOrder",method=RequestMethod.POST)
	public ResponseEntity<JSONObject> cancelOrder(
							@RequestParam(value="orderid")String orderid,
							@RequestParam(value="cancellationNote")String cancellationNote,
							@RequestParam(value="cancellationProcedure")String cancellationProcedure,
							HttpServletRequest req) throws CustomGenericException{
		logger.info("Executing Method cancelOrder(). Invoke cancelOrder method.");
		HttpSession session = req.getSession();
		User user = ((User)session.getAttribute("user"));
		JSONObject cancelResponse = new JSONObject();
		try{
			List<Order> orders = orderService.getOrderList("getOrderByID", orderid);
			if(orders.size()>0){
				Order order = orders.get(0);
				if(order.getOrderstatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("canel"))){
					cancelResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
					cancelResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("1127"), order.getOrderid() , SpringPropertiesUtil.getProperty("canel")));
					return new ResponseEntity<JSONObject>(cancelResponse,HttpStatus.OK);
				}
				Object[] fieldValues = {SpringPropertiesUtil.getProperty("canel"),cancellationNote,new Timestamp(System.currentTimeMillis()),orderid};
				String[] fieldNames = {"order_status","order_cancellationcmd","order_last_modify"};
				int[] fieldDataTypes = {Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.BIGINT};
				List<String> whereClauseFieldNames = new ArrayList<String>();
				whereClauseFieldNames.add("order_id");
				int i = orderService.updateOrderDetails(fieldValues, fieldNames, fieldDataTypes, SpringPropertiesUtil.getProperty("Order.TableName"),
						OrderUtility.convertToWhereClauseString(whereClauseFieldNames), whereClauseFieldNames);
				if(i>0){
					logActivity.saveLogActivity(user.getUser_id(), "Order has been cancelled successfully. Order id is "+orderid+". Requested IP Address is "+OrderUtility.getIPAddress(), SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getCanonicalName(), "cancelOrder");
					OrderStatus orderStatus = new OrderStatus();
					orderStatus.setOrderid(Long.valueOf(orderid));
					orderStatus.setOrdermodifier(user.getUser_id());
					orderStatus.setOrderstatus("Cancel");
					orderStatus.setOrderPlacedIP(OrderUtility.getIPAddress());
					Notification notification = new Notification();
					notification.setNotificationText(MessageFormat.format((String)SpringPropertiesUtil.getProperty("SuccessResponse"), orderid, SpringPropertiesUtil.getProperty("Notification.cancelledOrderStatus")));
					notification.setNotifyuser(order.getOrdercreator());
					orderService.OrderStoreProcService(notification, orderStatus);
					
					try{
						User orderCreator = new User();
						orderCreator = userService.getDataListByUserId(order.getOrdercreator()).get(0);
						JSONObject jsonObject = new JSONObject();
						SMSModel smsModel = new SMSModel();
						List<JSONObject> smsList = new ArrayList<JSONObject>();
						JSONObject jsonObjectSMS = new JSONObject();
						jsonObjectSMS.put("to", orderCreator.getUser_mobile());
						jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
						jsonObjectSMS.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("SuccessResponse"), orderid, SpringPropertiesUtil.getProperty("Notification.cancelledOrderStatus")));
						smsList.add(jsonObjectSMS);
						jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
						jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
						jsonObject.put("sms", smsList);
						logger.info("SMS JSON String : "+jsonObject.toJSONString());
						smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
						sendSMSService.process_sms(smsModel);
					}
					catch(Exception excep){
						logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "SMS Sending" ,order.getOrderid() ,excep.getMessage()));
					}
					cancelResponse.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
					cancelResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("SuccessResponse"), order.getOrderid() , SpringPropertiesUtil.getProperty("Notification.cancelledOrderStatus")));
				}
				else{
					logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Cancel Status" ,order.getOrderid() ,SpringPropertiesUtil.getProperty("1119")));
					cancelResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
					cancelResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("FailureResponse"), order.getOrderid() , "cancel"));
				}
			}
			else{
				logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Cancel Status" ,orderid ,SpringPropertiesUtil.getProperty("1115")));
				cancelResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
				cancelResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("FailureResponse"), orderid , "cancel"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
			String errorMsg = SpringPropertiesUtil.getProperty("1120");
			errorMsg.concat(e.getMessage());
			throw  new CustomGenericException("1120",errorMsg,
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		return new ResponseEntity<JSONObject>(cancelResponse,HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deliverOrder", method = RequestMethod.POST,consumes={"application/json"})
    public ResponseEntity<JSONObject> deliveredOrder(@RequestBody DeliverOrderRequest deliverOrderRequest,HttpServletRequest request)
    											throws CustomGenericException{
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		String ipAddress = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		} catch (Exception e) {
			logger.info("ipAddress::"+e.getMessage());
		}
		int queryExeStatus = 0;
		JSONObject confirmOrderResponse = new JSONObject();
		try{
			queryExeStatus = sellerService.deliverOrder(deliverOrderRequest, user);
			if(queryExeStatus > 0){
				try{
					List<Order> orders = orderService.getBulkOrderList("getBulkOrder", deliverOrderRequest.getConfirmOrders());
					List<Notification> notificationsList = new ArrayList<Notification>();
					List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
					List<JSONObject> smsList = new ArrayList<JSONObject>();
					for(Order order : orders){
						Notification notification = new Notification();
						notification.setNotificationText(MessageFormat.format((String)SpringPropertiesUtil.getProperty("Notification.orderStatusChange"), order.getOrderid(), SpringPropertiesUtil.getProperty("Notification.deliverOrderStatus")));
						notification.setNotifyuser(order.getOrdercreator());
						notificationsList.add(notification);
						
						OrderStatus orderStatus = new OrderStatus();
						orderStatus.setOrderid(Long.valueOf(order.getOrderid()));
						orderStatus.setOrdermodifier(user.getUser_id());
						orderStatus.setOrderstatus(SpringPropertiesUtil.getProperty("deliver"));
						orderStatus.setOrderPlacedIP(ipAddress);
						orderStatusList.add(orderStatus);
						
						User orderCreator = new User();
						orderCreator = userService.getDataListByUserId(order.getOrdercreator()).get(0);
						JSONObject jsonObjectSMS = new JSONObject();
						jsonObjectSMS.put("to", orderCreator.getUser_mobile());
						jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
						jsonObjectSMS.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("Notification.orderStatusChange"), order.getOrderid(), SpringPropertiesUtil.getProperty("Notification.deliverOrderStatus")));
						smsList.add(jsonObjectSMS);
					}
					
					try{
						orderService.orderStatusBulkInsert(orderStatusList);
					}
					catch(Exception excep){
						logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Insert bulk order status" ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()),excep.getMessage()));
					}
					
					try{
						notificationService.notificationBulkInsert(notificationsList);
					}
					catch(Exception excep){
						logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Insert bulk notification" ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()),excep.getMessage()));
					}

					try{
						if(smsList.size()>0){
							JSONObject jsonObject = new JSONObject();
							SMSModel smsModel = new SMSModel();
							jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
							jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
							jsonObject.put("sms", smsList);
							logger.info("SMS JSON String : "+jsonObject.toJSONString());
							smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
							sendSMSService.process_sms(smsModel);
						}
					}
					catch(Exception e){
						logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "SMS Sending" ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()),e.getMessage()));
					}
				}
				catch(Exception e){
					logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Notifiction and Status" ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) ,e.getMessage()));
				}
				confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
				confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("SuccessResponse"), OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) , "Delivered"));
			}
			else{
				confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
				confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("FailureResponse"), OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) , "Delivered"));
			}
		}
		catch(Exception e){
			logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update order Delivery Status" ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) ,e.getMessage()));
			throw  new CustomGenericException("6001",MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update order Delivery Status" ,deliverOrderRequest.getConfirmOrders() ,e.getMessage()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
			//confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
			//confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("FailureResponse"), OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) , "Delivered"));

		}
        return new ResponseEntity<JSONObject>(confirmOrderResponse,HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cancelRequest", method = RequestMethod.POST,consumes={"application/json"})
    public ResponseEntity<JSONObject> orderCancelRequest(@RequestBody DeliverOrderRequest deliverOrderRequest,HttpServletRequest request)
    											throws CustomGenericException{
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		String ipAddress = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		} catch (Exception e) {
			logger.info("ipAddress::"+e.getMessage());
		}
		int queryExeStatus = 0;
		JSONObject confirmOrderResponse = new JSONObject();
		try{
			queryExeStatus = sellerService.cancelRequest(deliverOrderRequest, user);
			if(queryExeStatus == 1){
				try{
					List<Order> orders = orderService.getBulkOrderList("getBulkOrder", deliverOrderRequest.getConfirmOrders());
					List<Notification> notificationsList = new ArrayList<Notification>();
					List<JSONObject> smsList = new ArrayList<JSONObject>();
					for(Order order : orders){
						Notification notification = new Notification();
						notification.setNotificationText(MessageFormat.format((String)SpringPropertiesUtil.getProperty("Notification.ordercancelRequest"), order.getOrderid(), SpringPropertiesUtil.getProperty("Notification.ordercancelRequestStatus")));
						notification.setNotifyuser(user.getUser_id());
						notificationsList.add(notification);
						
						JSONObject jsonObjectSMS = new JSONObject();
						jsonObjectSMS.put("to", user.getUser_mobile());
						jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
						jsonObjectSMS.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("Notification.ordercancelRequest"), order.getOrderid(), SpringPropertiesUtil.getProperty("Notification.ordercancelRequestStatus")));
						smsList.add(jsonObjectSMS);
					}
					
					try{
						if(notificationsList.size()>0){
							notificationService.notificationBulkInsert(notificationsList);
						}
					}
					catch(Exception excep){
						logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Insert bulk notification" ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()),excep.getMessage()));
					}

					try{
						if(smsList.size()>0){
							JSONObject jsonObject = new JSONObject();
							SMSModel smsModel = new SMSModel();
							jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
							jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
							jsonObject.put("sms", smsList);
							logger.info("SMS JSON String : "+jsonObject.toJSONString());
							smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
							sendSMSService.process_sms(smsModel);
						}
					}
					catch(Exception e){
						logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "SMS Sending " ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()),e.getMessage()));
					}
				}
				catch(Exception e){
					logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Notifiction and SMS " ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) ,e.getMessage()));
				}
				confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
				confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("SuccessResponse"), OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) , SpringPropertiesUtil.getProperty("cancelRequest")));
			}
			else{
				confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
				confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("FailureResponse"), OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) , SpringPropertiesUtil.getProperty("cancelRequest")));
			}
		}
		catch(Exception e){
			logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), SpringPropertiesUtil.getProperty("cancelRequest") ,OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) ,e.getMessage()));
			throw  new CustomGenericException("6001",MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), SpringPropertiesUtil.getProperty("cancelRequest") ,deliverOrderRequest.getConfirmOrders() ,e.getMessage()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
			//confirmOrderResponse.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
			//confirmOrderResponse.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("FailureResponse"), OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders()) , "Delivered"));

		}
        return new ResponseEntity<JSONObject>(confirmOrderResponse,HttpStatus.OK);
    }
	
	@RequestMapping(value = "/getsellerrefundorders", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> searchRefundOrder(@RequestParam(value = "searchRefundOrder", required = true) String requestedJSONString,
																HttpServletRequest req) throws CustomGenericException {
		HttpSession session = req.getSession();
		User user = ((User)session.getAttribute("user"));
		logger.info("Executing Method searchRefundOrder(). UserID: "+user.getUser_id());
		JSONObject jsonObject;
		try{
			searchRefundOrder = new ObjectMapper().readValue(requestedJSONString, SearchRefundOrder.class);
			jsonObject = orderService.searchRefundOrders(searchRefundOrder, user);
		}
		catch(Exception jsonPExcp){
			jsonPExcp.printStackTrace();
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
	@RequestMapping(value = "/executeSellerRefundorders", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> executeSellerRefundorders(@RequestBody RefundOrderRequest refundOrderRequest,
    															HttpServletRequest request) throws CustomGenericException {
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		logger.info("Executing Method executeSellerRefundorders(). UserID: "+user.getUser_id());
		logger.info("Executing Method executeSellerRefundorders(). RefundOrderRequest: "+refundOrderRequest.toString());
		JSONObject jsonObject = new JSONObject();
		
		List<Object> updatedFieldValues = new ArrayList<Object>();
		List<String> modifiedFieldNames = new ArrayList<String>();
		List<Integer> typeList = new ArrayList<Integer>();
		List<String> whereClauseFieldNames = new ArrayList<String>();
		if(!refundOrderRequest.getRefundOrderStatus().isEmpty() || refundOrderRequest.getRefundOrderStatus()!=null){
			updatedFieldValues.add(refundOrderRequest.getRefundOrderStatus());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("refund_status");
			if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("canel"))){
				if(refundOrderRequest.getSellerComments() != null && !refundOrderRequest.getSellerComments().isEmpty() && !refundOrderRequest.getSellerComments().equals("")){
					updatedFieldValues.add(refundOrderRequest.getSellerComments());
					typeList.add(Types.VARCHAR);
					modifiedFieldNames.add("refund_seller_comments");
				}
			}
		}
		else{
			throw  new CustomGenericException("3001",SpringPropertiesUtil.getProperty("3001"),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		updatedFieldValues.add(new Timestamp(System.currentTimeMillis()));
		typeList.add(Types.TIMESTAMP);
		modifiedFieldNames.add("refund_detail_last_modified");
		
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
		catch(Exception jsonPExcp){
			throw  new CustomGenericException("1113",SpringPropertiesUtil.getProperty("1113").concat(jsonPExcp.getMessage()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		
		//============================= Notification and SMS =======================================
		String successProperty = "";
		String failProperty = "";
		String notificationStatus = "";
		if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("canel"))){
			successProperty = SpringPropertiesUtil.getProperty("refund.cancel");
			failProperty = SpringPropertiesUtil.getProperty("canel");
			notificationStatus = "cancelled";
		}
		else if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("confirm"))){
			successProperty = SpringPropertiesUtil.getProperty("refund.confirm");
			failProperty = SpringPropertiesUtil.getProperty("confirm");
			notificationStatus = "confirmed";
		}
		else if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("approve"))){
			successProperty = SpringPropertiesUtil.getProperty("refund.approve");
			failProperty = SpringPropertiesUtil.getProperty("approve");
			notificationStatus = "refunded";
		}
		
		if(updateReturn > 0){
			RefundOrder refundOrder = new RefundOrder();
			List<JSONObject> smsList = new ArrayList<JSONObject>();
			JSONObject jsonObjectSMS = new JSONObject();
			DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm");
			Calendar calendar = Calendar.getInstance();
		    calendar.setTimeInMillis(System.currentTimeMillis());
		    
		    JSONObject jsonObjectSearchResult = new JSONObject();
		    SearchRefundOrder refundOrderSearch = new SearchRefundOrder();
		    refundOrderSearch.setRefundSearchType("refundByOrderIdOtion");
		    List<String> refundOrderList = new ArrayList<String>();
		    refundOrderList.add(refundOrderRequest.getRefundOrderStatus());
		    refundOrderSearch.setRefundOrderStatusList(refundOrderList);
			List<String> refundOrderIdList = new ArrayList<String>();
			refundOrderIdList.add(refundOrderRequest.getRefundRequestId());
			refundOrderSearch.setRefundOrderIdList(refundOrderIdList);
			jsonObjectSearchResult = orderService.searchRefundOrders(refundOrderSearch, user);
			List<RefundOrder> refundOrders = (List<RefundOrder>)jsonObjectSearchResult.get("refundOrders");
			refundOrder = (RefundOrder)refundOrders.get(0);
			
			try{
			    refundOrderRequest.setRequestedHostIP(String.valueOf(request.getAttribute("requestedIP")));
				refundOrderRequest.setRefundNotificattionText(MessageFormat.format((String)SpringPropertiesUtil.getProperty("Seller.RefundRequestSMS"), refundOrderRequest.getRefundRequestId(), notificationStatus, formatter.format(calendar.getTime()),refundOrder.getRefundAmount()));
				refundOrderRequest.setRefundInitiateUser(user.getUser_id());
				int executeProcFlag = 0;			
				try{
					executeProcFlag = orderService.insertNotificationAndStatusRefund(refundOrderRequest);
				}
				catch(Exception excep){
					logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Insert bulk notification" ,refundOrderRequest.getRefundRequestId(),excep.getMessage()));
				}
			}
			catch(Exception e){
				e.printStackTrace();
				logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "Update Notifiction and SMS " ,refundOrderRequest.getRefundRequestId() ,e.getMessage()));
			}
			
			try{
				User refundCreator = new User();
				refundCreator = userService.getDataListByUserId(refundOrder.getRefundInitiateUser()).get(0);
				jsonObjectSMS.put("to", refundCreator.getUser_mobile());
				jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
				jsonObjectSMS.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("Seller.RefundRequestSMS"), refundOrderRequest.getRefundRequestId(), notificationStatus,formatter.format(calendar.getTime()),refundOrder.getRefundAmount()));
				smsList.add(jsonObjectSMS);			
				if(smsList.size()>0){
						jsonObject = new JSONObject();
						SMSModel smsModel = new SMSModel();
						jsonObject.put("unicode", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_unicode")));
						jsonObject.put("flash", Integer.valueOf(SpringPropertiesUtil.getProperty("JSON_flash")));
						jsonObject.put("sms", smsList);
						logger.info("SMS JSON String : "+jsonObject.toJSONString());
						smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
						sendSMSService.process_sms(smsModel);
				}
			}
			catch(Exception e){
				logger.error(MessageFormat.format((String)SpringPropertiesUtil.getProperty("6001"), "SMS Sending " ,refundOrderRequest.getRefundRequestId(),e.getMessage()));
			}
			
			jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
			jsonObject.put("message",MessageFormat.format((String)SpringPropertiesUtil.getProperty("RefundSuccessResponse"),
					refundOrderRequest.getRefundRequestId(),
					successProperty));
		}
		else{
			jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
			jsonObject.put("message",MessageFormat.format((String)SpringPropertiesUtil.getProperty("RefundFailureResponse"),
					refundOrderRequest.getRefundRequestId(),
					failProperty));
		}
		//==========================================================================================
		
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/executeCancellationRequest", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> executeCancellationRequest(@RequestBody CancellationRequest cancellationRequest,
    															HttpServletRequest request) throws CustomGenericException {
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		cancellationRequest.setInitiateUser(user.getUser_id());
		logger.info("Executing Method executeCancellationRequest(). UserID: "+user.getUser_id());
		logger.info("Executing Method executeCancellationRequest(). CancellationRequest : "+cancellationRequest.toString());
		JSONObject jsonObject = new JSONObject();
		
		int updateReturn = 0;
		/*List<Object> updatedFieldValues = new ArrayList<Object>();
		List<String> modifiedFieldNames = new ArrayList<String>();
		List<Integer> typeList = new ArrayList<Integer>();
		List<String> whereClauseFieldNames = new ArrayList<String>();
		if(!refundOrderRequest.getRefundOrderStatus().isEmpty() || refundOrderRequest.getRefundOrderStatus()!=null){
			updatedFieldValues.add(refundOrderRequest.getRefundOrderStatus());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("refund_status");
			
			if(refundOrderRequest.getRefundOrderStatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("approve"))){
				if(refundOrderRequest.getRefundSellerCommt() != null && !refundOrderRequest.getRefundSellerCommt().isEmpty() && !refundOrderRequest.getRefundSellerCommt().equals("")){
					updatedFieldValues.add(refundOrderRequest.getRefundSellerCommt());
					typeList.add(Types.VARCHAR);
					modifiedFieldNames.add("refund_seller_comments");
				}
			}
		}
		else{
			throw  new CustomGenericException("3001",SpringPropertiesUtil.getProperty("3001"),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}
		updatedFieldValues.add(new Timestamp(System.currentTimeMillis()));
		typeList.add(Types.TIMESTAMP);
		modifiedFieldNames.add("refund_detail_last_modified");
		
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
		catch(Exception jsonPExcp){
			throw  new CustomGenericException("1113",SpringPropertiesUtil.getProperty("1113").concat(jsonPExcp.getMessage()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
		}*/
		/*if(jsonObject.get("status").toString().equalsIgnoreCase("error")){
        	throw  new CustomGenericException(jsonObject.get("errorcode").toString(),
        			SpringPropertiesUtil.getProperty(jsonObject.get("errorcode").toString()).concat(jsonObject.get("errormsg").toString()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					user.getUser_id());
        }*/
		if(updateReturn >0 ){
			if(cancellationRequest.getRequestStatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("cancelRequest.cancelRequestStatus"))){
				jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
				jsonObject.put("message",MessageFormat.format((String)SpringPropertiesUtil.getProperty("RefundSuccessResponse"),
						cancellationRequest.getRefundRequestId(),
						SpringPropertiesUtil.getProperty("cancelRequest")));
			}
			else{
				jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
				jsonObject.put("message",MessageFormat.format((String)SpringPropertiesUtil.getProperty("CancellationSuccessResponse"),
						cancellationRequest.getCancellationRequestId(),
						cancellationRequest.getRequestStatus()));
			}
		}
		else{
			if(cancellationRequest.getRequestStatus().equalsIgnoreCase(SpringPropertiesUtil.getProperty("cancelRequest.cancelRequestStatus"))){
				jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
				jsonObject.put("message",MessageFormat.format((String)SpringPropertiesUtil.getProperty("RefundFailureResponse"),
						cancellationRequest.getRefundRequestId(),
					SpringPropertiesUtil.getProperty("cancelRequest")));
			}
			else{
				jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
				jsonObject.put("message",MessageFormat.format((String)SpringPropertiesUtil.getProperty("CancellationFailureResponse"),
						cancellationRequest.getCancellationRequestId(),
						cancellationRequest.getRequestStatus()));
			}
			
		}
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getsellerinfo/{urlPath}/{sellerId}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> getSellerInfo(@PathVariable String sellerId,
    												@PathVariable String urlPath){

		logger.info("Executing Method getSellerInfo(). SellerID: "+sellerId);
		JSONObject jsonObject =new JSONObject();
		try{
			
			List<Seller> sellers = sellerService.getSeller(urlPath,sellerId);
			List<Seller> activeSellers = new ArrayList<Seller>();
			if(sellers.size()>0){
				for(Seller seller : sellers){
					if(seller.getSellerStatus().equalsIgnoreCase("Active")){
						activeSellers.add(seller);
					}
				}
				if(activeSellers.size()>0){
					jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
					jsonObject.put("sellers", activeSellers);
				}
				else{
					jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
					jsonObject.put("message", SpringPropertiesUtil.getProperty("5005"));
				}
			}
			else{
				jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
				jsonObject.put("message", SpringPropertiesUtil.getProperty("5005"));
			}
		}
		catch(Exception jsonPExcp){
			jsonPExcp.printStackTrace();
			jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
			jsonObject.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("6002"),Thread.currentThread().getStackTrace()[1].getMethodName()));
		}
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updatesellerinfo", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> updateSellerInfo(@RequestBody Seller seller,
    															HttpServletRequest request) throws CustomGenericException {
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		logger.info("Executing Method updateSellerInfo(). Request : "+seller.toString());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "success");
		
		List<Object> updatedFieldValues = new ArrayList<Object>();
		List<String> modifiedFieldNames = new ArrayList<String>();
		List<Integer> typeList = new ArrayList<Integer>();
		List<String> whereClauseFieldNames = new ArrayList<String>();
		
		if(seller.getSellerAddress() != null && seller.getSellerAddress() !=""){
			updatedFieldValues.add(seller.getSellerAddress());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_address");
		}
		
		if(seller.getSellerComission() !=0.0){
			updatedFieldValues.add(seller.getSellerComission());
			typeList.add(Types.DOUBLE);
			modifiedFieldNames.add("seller_comission");
		}
		
		if(seller.getSellerDeliveryPincode() != null && seller.getSellerDeliveryPincode() !=""){
			updatedFieldValues.add(seller.getSellerDeliveryPincode());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_delivery_pincode");
		}
		
		if(seller.getSellerDeliveryPrice() != null){
			updatedFieldValues.add(seller.getSellerDeliveryPrice());
			typeList.add(Types.DECIMAL);
			modifiedFieldNames.add("seller_delivery_price");
		}
		
		if(seller.getSellerDiscount() != 0.0){
			updatedFieldValues.add(seller.getSellerDiscount());
			typeList.add(Types.DOUBLE);
			modifiedFieldNames.add("seller_discount");
		}
		
		if(seller.getSellerEmailId() != null && seller.getSellerEmailId() !=""){
			updatedFieldValues.add(seller.getSellerEmailId());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_email_id");
		}
		
		if(seller.getSellerEmergencyPrice() != null){
			updatedFieldValues.add(seller.getSellerEmergencyPrice());
			typeList.add(Types.DECIMAL);
			modifiedFieldNames.add("seller_emergency_price");
		}
		
		if(seller.getSellerExpiryDate() != null){
			updatedFieldValues.add(seller.getSellerExpiryDate());
			typeList.add(Types.TIMESTAMP);
			modifiedFieldNames.add("seller_expiry_date");
		}
		
		if(seller.getSellerMobileNo() != null && seller.getSellerMobileNo() != ""){
			updatedFieldValues.add(seller.getSellerMobileNo());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_mobileno");
		}
		
		if(seller.getSellerPriority() != 0){
			updatedFieldValues.add(seller.getSellerPriority());
			typeList.add(Types.INTEGER);
			modifiedFieldNames.add("seller_priority");
		}
		
		if(seller.getSellerRegistrationNo()!= null && seller.getSellerRegistrationNo()!= ""){
			updatedFieldValues.add(seller.getSellerRegistrationNo());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_registration_no");
		}
		
		if(seller.getSellerShopCloseDay()!= null && seller.getSellerShopCloseDay()!= ""){
			updatedFieldValues.add(seller.getSellerShopCloseDay());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_shop_close_day");
		}
		
		if(seller.getSellerShopName() != null && seller.getSellerShopName()!= ""){
			updatedFieldValues.add(seller.getSellerShopName());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_shop_name");
		}
		
		if(seller.getSellerStatus() != null && seller.getSellerStatus()!= ""){
			updatedFieldValues.add(seller.getSellerStatus());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_status");
		}
		
		if(seller.getShopCloseTime()!= null && seller.getShopCloseTime()!= ""){
			updatedFieldValues.add(seller.getShopCloseTime());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_shop_close_time");
		}
		
		if(seller.getShopOpenTime()!= null && seller.getShopOpenTime()!= ""){
			updatedFieldValues.add(seller.getShopOpenTime());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_shop_open_time");
		}
		
		if(seller.getTaxCategory()!= null && seller.getTaxCategory()!= ""){
			updatedFieldValues.add(seller.getTaxCategory());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_tax_category");
		}
		
		if(seller.getTaxType()!= null && seller.getTaxType()!= ""){
			updatedFieldValues.add(seller.getTaxType());
			typeList.add(Types.VARCHAR);
			modifiedFieldNames.add("seller_tax_type");
		}
		
		updatedFieldValues.add(new Timestamp(System.currentTimeMillis()));
		typeList.add(Types.TIMESTAMP);
		modifiedFieldNames.add("seller_last_modify_date");
		
		whereClauseFieldNames.add("seller_id");
		updatedFieldValues.add(seller.getSellerid());
		typeList.add(Types.BIGINT);
		String[] modifiedFieldNameStrings = OrderUtility.convertToStringArray(modifiedFieldNames);
		
		int[] types = OrderUtility.convertToInt(typeList);
		int updateReturn = 0;
		try{
			updateReturn = orderService.updateOrderDetails(updatedFieldValues.toArray(),modifiedFieldNameStrings,types,SpringPropertiesUtil.getProperty("SellerDetails.TableName")
					,OrderUtility.convertToWhereClauseString(whereClauseFieldNames),whereClauseFieldNames);
		}
		catch(Exception e){
			e.printStackTrace();
			jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
			jsonObject.put("message", MessageFormat.format((String)SpringPropertiesUtil.getProperty("6002"),Thread.currentThread().getStackTrace()[1].getMethodName()));
		}
		
		if(updateReturn >0 ){
				jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
				jsonObject.put("message",SpringPropertiesUtil.getProperty("7001"));
				try{
					logActivity.saveLogActivity(user.getUser_id(),
							MessageFormat.format((String)SpringPropertiesUtil.getProperty("LogActivity.TEXT"),(String)SpringPropertiesUtil.getProperty("LogActivity.UpdateSellerInfo"),request.getAttribute("requestedIP"),seller.toString().length()>1999 ? seller.toString().substring(0, 1999) : seller.toString()),
							(String)SpringPropertiesUtil.getProperty("LogActivity.Success"),
							this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}
				catch(Exception e){}
		}
		else{
			jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
			jsonObject.put("message",SpringPropertiesUtil.getProperty("7002"));
		}
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sellerstatistic/{sellerId}/{fromDate}/{toDate}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<List<Object>>> getSellerSellStatistic(@PathVariable String sellerId, @PathVariable long fromDate, @PathVariable long toDate){
		logger.info("Executing Method getSellerSellStatistic().");
		List<List<Object>> dataLists = new ArrayList<List<Object>>();
		dataLists = sellerService.getSellsReport(sellerId, fromDate, toDate);
        return new ResponseEntity<List<List<Object>>>(dataLists, HttpStatus.OK);
    }
	
	@RequestMapping(value="/downloadreport/{sellerId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public @ResponseBody void getSellerReport(@PathVariable String sellerId, @PathVariable long fromDate, @PathVariable long toDate,
			HttpServletRequest request, HttpServletResponse response)  {
		logger.info("Executing Method getSellerReport(). queryValue: "+sellerId+", fromDate : "+fromDate+", toDate : "+toDate);
	    try {
	        Map<String,List<Object>> data = sellerService.generateSellerReport(sellerId,fromDate,toDate);
	        OutputStream os = response.getOutputStream();
	        
	        HSSFWorkbook workbook = new HSSFWorkbook(); 
	        //Create a blank sheet
	        
    		for (Map.Entry<String,List<Object>> entry : data.entrySet())
    		{
    		    System.out.println(entry.getKey() + "/" + entry.getValue());
    		    HSSFSheet spreadsheet;
    		    Map < String, Object[] > dataInfo =  new TreeMap < String, Object[] >();
    		    int count = 0;
    		    if(entry.getKey().equalsIgnoreCase("Order")){
    		    	spreadsheet = workbook.createSheet(entry.getKey());
    		    	Row row;
    		    	dataInfo = new TreeMap < String, Object[] >();
    		    	count = 1;
    		    	dataInfo.put(String.valueOf(count), new Object[] { 
    				        "Order Id", "Order Status", "Order Create Date","Order Price", "Order Delivery Price", "Order Emergency Price", "Order Discount"});
    		    	List<Object> orderList = entry.getValue();
    		    	Order order = new Order();
    		    	for(Object orderObj : orderList){
    		    		order = (Order) orderObj;
    		    		count +=1;
    		    		dataInfo.put(String.valueOf(count), new Object[] { 
    		    			order.getOrderid(), order.getOrderstatus(), sdf.format(order.getOrderCreateDate()), order.getOrderprice().toString(), order.getOrderDeliveryAmount().toString(), order.getEmergencyPrice().toString(), order.getOrderDiscountAmount().toString()});
    		    	}
    		    	
    		    	Set < String > keyid = dataInfo.keySet();
    		        int rowid = 0;
    		        for (String key : keyid)
    		        {
    		           row = spreadsheet.createRow(rowid++);
    		           Object [] objectArr = dataInfo.get(key);
    		           int cellid = 0;
    		           for (Object obj : objectArr)
    		           {
    		              Cell cell = row.createCell(cellid++);
    		              cell.setCellValue((String)obj);
    		           }
    		        }
    		        
    		    	count = 0;
    		    	dataInfo.clear();
    		    }
    		    else if(entry.getKey().equalsIgnoreCase("RefundOrder")){
    		    	spreadsheet = workbook.createSheet(entry.getKey());
    		    	Row row;
    		    	dataInfo =  new TreeMap < String, Object[] >();
    		    	count = 1;
    		    	
    		    	dataInfo.put(String.valueOf(count), new Object[] { 
				        "Refund Request Id", "Order Id","Cashmemo No.", "Status","Refund Amount", "Initiate Date"});
			    	List<Object> orderList = entry.getValue();
			    	RefundOrder refundOrder = new RefundOrder();
			    	for(Object orderObj : orderList){
			    		refundOrder = (RefundOrder) orderObj;
			    		count +=1;
			    		dataInfo.put(String.valueOf(count), new Object[] { 
			    			refundOrder.getRefunRequestNo(), refundOrder.getRefundOrderid(), refundOrder.getCashmemoNo(), refundOrder.getRefundOrderStatus(),refundOrder.getRefundAmount().toString(), sdf.format(refundOrder.getRefundInitiateTime())});
			    	}
    		    	
			    	Set < String > keyid = dataInfo.keySet();
    		        int rowid = 0;
    		        for (String key : keyid)
    		        {
    		           row = spreadsheet.createRow(rowid++);
    		           Object [] objectArr = dataInfo.get(key);
    		           int cellid = 0;
    		           for (Object obj : objectArr)
    		           {
    		              Cell cell = row.createCell(cellid++);
    		              cell.setCellValue((String)obj);
    		           }
    		        }
			    	
    		    	count = 0;
    		    	dataInfo.clear();
    		    }
    		}
	        workbook.write(os);
	    	response.setContentType("application/vnd.ms-excel");
	    	String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",sellerId+"_Report.xls");
			response.setHeader(headerKey, headerValue);
	    } catch (IOException e) {
	        logger.error(e.getMessage());
	    }
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/raisecancelrequest", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> getCancelRequest(@RequestBody CancellationRequestSearch cancellationRequestSearch,
    													HttpServletRequest request) {
		logger.info("Executing Method getCancelRequest().");
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
		jsonObject.put("message", SpringPropertiesUtil.getProperty("CancellationRequestList.SUCCESS.Message"));
		List<CancellationRequest> cancellationRequestList = new ArrayList<CancellationRequest>();
		
		try{
			cancellationRequestList = sellerService.getCancelRequest(cancellationRequestSearch.getRequester(),cancellationRequestSearch.getFromDate(),
					cancellationRequestSearch.getToDate(),cancellationRequestSearch.getSearchType(),user,
					cancellationRequestSearch.getCancelRequestIdList(),cancellationRequestSearch.getCancelRequestStatusList());
		}
		catch(Exception e){
			e.printStackTrace();
			jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
        	jsonObject.put("message", SpringPropertiesUtil.getProperty("CancellationRequestList.ERROR.Message"));
        	return new ResponseEntity<JSONObject>(jsonObject,HttpStatus.OK);
		}
		logger.info("Executing Method getCancelRequest(). Cancel Request Size: "+cancellationRequestList.size());
        if(cancellationRequestList == null || cancellationRequestList.isEmpty() || cancellationRequestList.size()==0){
        	jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
        	jsonObject.put("message", SpringPropertiesUtil.getProperty("CancellationRequestList.FAILED.Message"));
            return new ResponseEntity<JSONObject>(jsonObject,HttpStatus.OK);
        }
        else{
        	jsonObject.put("cancelRequestList", cancellationRequestList);
        }
        return new ResponseEntity<JSONObject>(jsonObject,HttpStatus.OK);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getordercount/{sellerId}/{fromDate}/{toDate}", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JSONObject> getOrderCount(@PathVariable String sellerId,
    								@PathVariable long fromDate, @PathVariable long toDate){
		logger.info("Executing Method getOrderCount().");
		Map<String,Integer> dataMap = new HashMap<String,Integer>();
		dataMap = sellerService.getOrderCount(sellerId, fromDate, toDate);
		System.out.println("dataMap  ::: "+dataMap.toString());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonSuccess"));
		if(dataMap.size() == 0){
			jsonObject.put("status", SpringPropertiesUtil.getProperty("jsonFail"));
			jsonObject.put("message", SpringPropertiesUtil.getProperty("sellerdashboard.errormessage"));
		}
		jsonObject.put("record", dataMap);
        return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
    }
}
