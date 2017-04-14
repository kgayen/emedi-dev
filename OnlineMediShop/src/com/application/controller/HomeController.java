package com.application.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "home";
    }
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
    public String homePage() {
        return "home";
    }
	
//	@RequestMapping(value = "/sellerdashboard", method = RequestMethod.GET)
//    public String sellerhomePage() {
//        return "SellerHomePage";
//    }
	
	@RequestMapping(value = "/admindashboard", method = RequestMethod.GET)
    public String adminhomePage() {
        return "SellerHomePage";
    }
	
	@RequestMapping(value = {"/loginpage"}, method = RequestMethod.GET)
    public String loginPage(@RequestParam String pagetype, HttpServletRequest request) {
		request.setAttribute("pagetype", pagetype);
        return "login";
    }

}
