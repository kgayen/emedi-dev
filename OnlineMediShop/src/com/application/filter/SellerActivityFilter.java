package com.application.filter;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SellerActivityFilter implements HandlerInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(SellerActivityFilter.class);

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("Executing Method preHandle(). Request Passthrough SellerActivityFilter.");
		HttpSession session = (HttpSession)request.getSession();
		HttpServletRequest req = (HttpServletRequest)request;
		String uri = request.getRequestURI();
		String ipAddress = "";
		InetAddress ip;
		logger.info("Executing Method preHandle(). Request Seller Activity Filter Handler uri = "+uri);
		if(session.getAttribute("user") == null){
			response.sendRedirect("../home");
			return false;
		}
		else{
			try {
				ip = InetAddress.getLocalHost();
				ipAddress = ip.getHostAddress();
			} catch (Exception e) {
				logger.info("ipAddress::"+e.getMessage());
			}
			req.setAttribute("requestedIP", ipAddress);
			return true;
		}
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}