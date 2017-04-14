package com.application.controller;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.application.logger.LogActivity;
import com.application.model.Notification;
import com.application.model.Order;
import com.application.model.OrderStatus;
import com.application.model.SMSModel;
import com.application.model.Seller;
import com.application.model.User;
import com.application.service.OrderService;
import com.application.service.SMSService;
import com.application.service.SellerService;
import com.application.service.SendSMSService;
import com.application.service.UserService;
import com.application.utility.OrderUtility;
import com.application.utility.SpringPropertiesUtil;

@Controller
@RequestMapping(value = "/order")
@Configuration
//@PropertySource({"classpath:../build/classes/loggeractivity.properties","classpath:SMS.properties"})
public class OrderController {
	
	@Autowired
	UserService userService;
	@Autowired
	OrderService orderService;
	@Autowired
	SellerService sellerService;
	@Autowired
	OrderStatus orderStatus;
	@Autowired
	Notification notification;
	@Autowired
	Order newOrder;
	@Autowired
	User user;
	@Autowired
	LogActivity logActivity;
	@Autowired
	SMSService smsService;
	@Autowired
	SMSModel smsModel;
	@Autowired
	JSONObject jsonObject;
	@Autowired
	SendSMSService sendSMSService;
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@RequestMapping(value = "/newcutomorder", method = RequestMethod.GET)
    public ModelAndView getNewCustomerOrderPage() {
		return new ModelAndView("neworder");
    }
	
	@RequestMapping(value = "/placeorder", method = RequestMethod.GET)
    public ModelAndView getOldCustomerOrderPage(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = ((User)session.getAttribute("user"));
		return new ModelAndView("placeorder", "command", user);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createorder", method = RequestMethod.POST)
    public String saveOrder(HttpServletRequest req,@RequestParam(value = "orderpecimage", required = true) MultipartFile image,
    		RedirectAttributes redirectAttributes) {
		user = new User();
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String usermobile = req.getParameter("user_mobile");
		String address = req.getParameter("address");
		String pincode = req.getParameter("user_pincode");
		String orderdetails = req.getParameter("orderdetils");
		String emergencyFlag = req.getParameter("emergencyflag");
		int emergencyflagValue = 0;
		if(emergencyFlag!= null && emergencyFlag.equalsIgnoreCase("on")){
			emergencyflagValue = 1;
		}
		String ipAddress = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		} catch (Exception e) {
			logger.info("ipAddress::"+e.getMessage());
		}
		logger.info("ipAddress::"+ipAddress);
		HttpSession session = req.getSession();
		newOrder.setOrderdetails(orderdetails);
		newOrder.setCancellationCmd("");
		newOrder.setEmergencyFlag(emergencyflagValue);
		newOrder.setShippingAddress(address);
		newOrder.setOrderPincode(pincode);
		List<Seller> sellerList = sellerService.getSeller("getSellerByPincode", pincode);
		if(sellerList.size() == 1){
			newOrder.setEmergencyPrice(new BigDecimal(0.00));
			newOrder.setOrderDiscountAmount(new BigDecimal(sellerList.get(0).getSellerDiscount()));
			newOrder.setOrderDeliveryAmount(sellerList.get(0).getSellerDeliveryPrice());
		}
		if(emergencyflagValue == 1){
			//List<Seller> sellerList = sellerService.getSeller("getSellerByPincode", pincode);
			if(sellerList.size() == 1){
				newOrder.setEmergencyPrice(sellerList.get(0).getSellerEmergencyPrice());
				//newOrder.setOrderDiscountAmount(new BigDecimal(sellerList.get(0).getSellerDiscount()));
				//newOrder.setOrderDeliveryAmount(sellerList.get(0).getSellerDeliveryPrice());
			}
			logger.info("getSellerEmergencyPrice::"+sellerList.get(0).getSellerEmergencyPrice());
		}
		try{
			newOrder.setOrderpecimage(image.getInputStream());
		}
		catch(Exception e){
			logger.error("Order Image Setting Exception: "+e.getMessage());
		}
		//newOrder.setOrderprice(0.0);
		newOrder.setOrderprice(new BigDecimal(0.00));
		newOrder.setOrderstatus("Pending");
		//user.setUser_mobile(usermobile);
		if(session.getAttribute("user")!=null){
			user = (User)session.getAttribute("user");
		}
		List<User> userList = userService.getUserListByMobileNo(user);
		int newOrderID = 0;
		if(userList.size()>0){
			user = userList.get(0);
			logActivity.saveLogActivity(user.getUser_id(), "This is before place order by existing user. IP: "+ipAddress, SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "saveOrder");
			newOrder.setOrdercreator(user.getUser_id());
			newOrder.setOrderlastmodify(OrderUtility.orderTimestamp());
			
			newOrderID = orderService.saveOrderDetails(newOrder);
			if(newOrderID>0){
				session.setAttribute("user", user);
				orderStatus.setOrderid(newOrderID);
				orderStatus.setOrdermodifier(user.getUser_id());
				orderStatus.setOrderstatus("Pending");
				orderStatus.setOrderPlacedIP(ipAddress);
				
				notification.setNotificationText("New order has been successfully created. Order ID is "+newOrderID+". "
						+ "Please, check the order progress path." );
				notification.setNotifyuser(user.getUser_id());
				
				orderService.OrderStoreProcService(notification, orderStatus);
				
				try{
					/*smsModel = new SMSModel();
					smsModel.setTo(user.getUser_mobile());
					List<String> smsVariableProperty = new ArrayList<String>();
					smsVariableProperty.add(String.valueOf(newOrderID));
					smsVariableProperty.add(orderStatus.getOrderstatus());
					smsModel.setSmsVariableProperties(smsVariableProperty);
					smsModel.setTemplateId(env.getProperty("PendingRefundOrderSMS_TEMPLATE_ID"));
					smsService.sendSMSJavaURLPOST(smsModel);*/
					jsonObject = new JSONObject();
					smsModel = new SMSModel();
					List<JSONObject> smsList = new ArrayList<JSONObject>();
					JSONObject jsonObjectSMS = new JSONObject();
					jsonObjectSMS.put("to", user.getUser_mobile());
					jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
					jsonObjectSMS.put("message", "Order id "+newOrderID+" has been successfully placed.");
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
				logActivity.saveLogActivity(user.getUser_id(), "The order has been successfully placed. Order Id is "+newOrderID, SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "saveOrder");
			}
			return "redirect:/order/showuserorder";
		}
		else{
			user.setUser_mobile(usermobile);
			user.setUser_address(address);
			user.setUser_email(email);
			user.setUser_name(username);
			user.setUser_id(usermobile);
			user.setUser_creator(usermobile);
			user.setUser_password(usermobile);
			user.setUser_pincode(pincode);
			user.setUser_role("customer");
			newOrder.setOrdercreator(user.getUser_id());
			logActivity.saveLogActivity(user.getUser_id(), "This is before place order by non-existing user. IP: "+ipAddress, SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "saveOrder");
			boolean insertStatus = userService.insertData(user);
			session.setAttribute("user", user);
			if(!insertStatus){
				logActivity.saveLogActivity(user.getUser_id(), "The user has been registered. User Name is "+user.getUser_id(), SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "saveOrder");
				newOrder.setOrderlastmodify(OrderUtility.orderTimestamp());
				newOrderID = orderService.saveOrderDetails(newOrder);
				/*
				 * Build OrderStatus and Notification object for store procedure
				 */
				if(newOrderID>0){
					orderStatus.setOrderid(newOrderID);
					orderStatus.setOrdermodifier(usermobile);
					orderStatus.setOrderstatus("Pending");
					orderStatus.setOrderPlacedIP(ipAddress);
					
					notification.setNotificationText("New order has been successfully created. Order ID is "+newOrderID+". "
							+ "Please, check the order progress path." );
					notification.setNotifyuser(usermobile);
					orderService.OrderStoreProcService(notification, orderStatus);
					
					try{
						/*smsModel = new SMSModel();
						smsModel.setTo(user.getUser_mobile());
						List<String> smsVariableProperty = new ArrayList<String>();
						smsVariableProperty.add(String.valueOf(newOrderID));
						smsVariableProperty.add(orderStatus.getOrderstatus());
						smsModel.setSmsVariableProperties(smsVariableProperty);
						smsModel.setTemplateId(env.getProperty("PendingRefundOrderSMS_TEMPLATE_ID"));
						smsService.sendSMSJavaURLPOST(smsModel);*/
						jsonObject = new JSONObject();
						smsModel = new SMSModel();
						List<JSONObject> smsList = new ArrayList<JSONObject>();
						JSONObject jsonObjectSMS = new JSONObject();
						jsonObjectSMS.put("to", user.getUser_mobile());
						jsonObjectSMS.put("sender", SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
						jsonObjectSMS.put("message", "Order id "+newOrderID+" has been successfully placed.");
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
					
					logActivity.saveLogActivity(user.getUser_id(), "The order has been successfully placed. Order Id is "+newOrderID, SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "saveOrder");
				}
				logger.info("New Order ID:"+newOrderID);
				return "redirect:/order/showuserorder";
			}
			else{
				logActivity.saveLogActivity(user.getUser_id(), "The user has not been registered. User Name is "+user.getUser_id(), SpringPropertiesUtil.getProperty("LogActivity.Unavailabl"), this.getClass().getName(), "saveOrder");
				redirectAttributes.addFlashAttribute("message", "User Not registered.");
			}
		}
		return "redirect:/order/newcustomorder";
    }
	
	@RequestMapping(value = "/showuserorder", method = RequestMethod.GET)
    public ModelAndView viewOrders() {
		return new ModelAndView("showorders");
		//return new ModelAndView("searchResults");
    }
	
	@RequestMapping(value = "/searchorder", method = RequestMethod.GET)
    public ModelAndView searchOrders() {
		return new ModelAndView("orderSearch");
    }
	
	/*@RequestMapping(value="/image/downloadimage/{orderid}", method = RequestMethod.GET)//produces = ", image/jpg"
	public void downloadOrderImage(@PathVariable String orderid,HttpServletResponse response)  {
		logger.info("Executing Method downloadOrderImage(). queryValue: "+orderid);
	    try {
	        InputStream is = orderService.getImageStream(orderid);
	        //OutputStream os = response.getOutputStream();
	        BufferedImage img = ImageIO.read(is);
	        ByteArrayOutputStream bao = new ByteArrayOutputStream();
	        ImageIO.write(img, "jpg", bao);
	        logger.info("Executing Method downloadOrderImage(). Returning Image.");
	        byte[] imageArray = new byte[bao.size()];
	        int length;
	    	while ((length = is.read(imageArray)) != -1) {
	    		os.write(imageArray, 0, length);
	    	}
	    	response.setContentType("application/octet-stream");
	        response.setContentLength(bao.size());
	        String headerValue = String.format("attachment; filename=\"%s\"",
	        		orderid+"_Prescription");
	        response.setHeader("Content-Disposition", headerValue);
	    	is.close();
	    	os.close();
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + orderid+"_Prescription" +"\""));
	        response.setContentLength(bao.size());
	        FileCopyUtils.copy(is, response.getOutputStream());
	    } catch (IOException e) {
	    	 logger.error("Executing Method downloadOrderImage(). Exception: "+e.getMessage());
	        throw new RuntimeException(e);
	    }
	}*/
	
	@RequestMapping(value = "/returnorder", method = RequestMethod.GET)
    public ModelAndView returnOrder() {
		return new ModelAndView("returnOrder");
    }
	
}
