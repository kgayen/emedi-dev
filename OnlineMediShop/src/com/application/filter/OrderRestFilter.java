package com.application.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class OrderRestFilter implements HandlerInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderRestFilter.class);

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("Executing method preHandle().");
		HttpSession session = (HttpSession)request.getSession();
		String contxtUrl = request.getContextPath();
		logger.info("Executing method preHandle(). contxtUrl : "+contxtUrl);
		logger.info("Executing method preHandle(). Get User Session : "+session.getAttribute("user"));
		if(session.getAttribute("user") == null){
			response.sendRedirect(contxtUrl+"/");
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
