package com.application.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class RetailFilter implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(RetailFilter.class);

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = (HttpSession)request.getSession();
		String uri = request.getRequestURI();
		logger.info("Executing method preHandle(). uri = "+uri);
		if(session.getAttribute("user") == null){
			//return false;
			if(uri.indexOf("placeorder")!=-1){
				//res.sendRedirect("../loginpage?pagetype=customerLogin");
				return true;
			}
			if(uri.indexOf("student")!=-1){
				//res.sendRedirect("../loginpage?pagetype=customerLogin");
				return true;
			}
			if(uri.indexOf("newcustomerreg")!=-1){
				//res.sendRedirect("../loginpage?pagetype=customerLogin");
				return true;
			}
			
			else if(uri.indexOf("customerlogin")!=-1){
				//req.setAttribute("logginmsg", "Wrong user id and password.");
				response.sendRedirect("../loginpage?pagetype=customerLogin");
			}
			else if(uri.indexOf("adminlogin")!=-1){
				response.sendRedirect("../loginpage?pagetype=adminLogin");
			}
			else if(uri.indexOf("sellerlogin")!=-1){
				response.sendRedirect("../loginpage?pagetype=sellerLogin");
			}
			else if(uri.indexOf("login")!=-1){
				//session.setAttribute("user","KAUSTAVGAYEN");
				//res.sendRedirect("../loginpage?pagetype=sellerLogin");
				return true;
			}
			else{
				response.sendRedirect("../home");
			}
		}
		else{
			if(uri.indexOf("login")!=-1){
				System.out.println("VALID USER:::"+session.getAttribute("user").toString());
				response.sendRedirect("../user/placeorder");
				return true;
			}
			if(uri.indexOf("newcustomerreg")!=-1){
				response.sendRedirect("../user/placeorder");
				return true;
			}
		}
		return false;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}
