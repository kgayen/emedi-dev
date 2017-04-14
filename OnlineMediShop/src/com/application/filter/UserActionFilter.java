package com.application.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserActionFilter implements HandlerInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(UserActionFilter.class);

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = (HttpSession)request.getSession();
		String uri = request.getRequestURI();
		logger.info("Request Handler uri = "+uri);
		if(session.getAttribute("user") == null){
			if(uri.indexOf("newregistration")!=-1){
				return true;
			}
			else{
				response.sendRedirect("../home");
				return false;
			}
		}
		else{
			/*User user = (User)session.getAttribute("user");
			String userRole = user.getUser_role();
			if(userRole.equalsIgnoreCase("seller")){
				response.sendRedirect("../sellerdashboard");
			}
			else if(userRole.equalsIgnoreCase("admin")){
				response.sendRedirect("../sellerdashboard");
			}*/
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
