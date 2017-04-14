package com.application.restservice;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.MessageFormat;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.exception.CustomGenericException;
import com.application.logger.LogActivity;
import com.application.model.ConfirmOrderRequest;
import com.application.model.DeliverOrderRequest;
import com.application.model.Notification;
import com.application.model.Order;
import com.application.model.OrderStatus;
import com.application.model.SMSModel;
import com.application.model.SearchRefundOrder;
import com.application.model.Seller;
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
}
