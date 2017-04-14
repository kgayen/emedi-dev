package com.application.filter;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class OrderFilter implements HandlerInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(OrderFilter.class);

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("Executing Method preHandle(). Request Passthrough Order Filter.");
		HttpSession session = (HttpSession)request.getSession();
		String uri = request.getRequestURI();
		logger.info("Executing Method preHandle(). Request Order Handler uri = "+uri);
		if(session.getAttribute("user") == null){
			if(uri.indexOf("newcutomorder")!=-1 || uri.indexOf("createorder")!=-1){
				return true;
			}
			response.sendRedirect("../home");
			return false;
		}
		else{
			return true;
		}
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
