package com.application.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.application.model.User;
import com.application.service.UserService;

@Controller
@RequestMapping(value = "/retail")
public class RetailController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/placeorder", method = RequestMethod.GET)
    public String getPlaceOrderPage() {
        return "placeorder";
    }
	
	@RequestMapping(value = "/newcustomerreg", method = RequestMethod.GET)
    public ModelAndView getnewCustomerRegistration() {
		return new ModelAndView("newcustomerreg", "command", new User());
    }
	
	@RequestMapping(value = "/customerlogin", method = RequestMethod.GET)
    public String getCustomerLoginPage() {
		System.out.println("customerlogin RetailController...........");
        return "login";
    }
	
	@RequestMapping(value = "/sellerlogin", method = RequestMethod.GET)
    public String getSellerLoginPage() {
		System.out.println("sellerlogin RetailController...........");
        return "login";
    }
	
	@RequestMapping(value = "/adminlogin", method = RequestMethod.GET)
    public String getAdminLoginPage() {
		System.out.println("adminlogin RetailController...........");
        return "login";
    }
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUser(HttpServletRequest req,HttpServletResponse res) {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		req.setAttribute("logginmsg", "Wrong user id and password.");
		User user = new User();
		user.setUser_id(userId);
		user.setUser_password(userPassword);
		List<User> status= (List<User>) userService.getDataList(user);
		if(status.size()>0){
			req.getSession().setAttribute("user", status.get(0));
			if(status.get(0).getUser_role().equalsIgnoreCase("seller") || status.get(0).getUser_role().equalsIgnoreCase("admin")){
				return "redirect:/seller/sellerdashboard";
			}
			return "redirect:/user/placeorder";
		}
		else{
			return "login";
		}
    }
}
