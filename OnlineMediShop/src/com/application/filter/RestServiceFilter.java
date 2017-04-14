package com.application.filter;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestServiceFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(RestServiceFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse rsp = (HttpServletResponse)response;
	      HttpSession session = req.getSession();
	      String uri = req.getRequestURI();
	      String ipAddress = "";
		  InetAddress ip;
		  try {
				ip = InetAddress.getLocalHost();
				ipAddress = ip.getHostAddress();
		  } catch (Exception e) {
				logger.info("ipAddress::"+e.getMessage());
		  }
		  logger.info("Requested ipAddress::"+ipAddress);
		  req.setAttribute("requestedIP", ipAddress);
	      logger.info("Rest Service URI : "+ uri);//|| uri.indexOf("selleractivity")!=-1 || uri.indexOf("adminactivity")!=-1  || (uri.indexOf("sellerrestservice") !=-1)
	      if(session.getAttribute("user") == null && ((uri.indexOf("orderservice") !=-1))){
	    	  logger.info("Invalid user session. Redirect to Login Page!!");
	    	  rsp.sendRedirect(req.getContextPath()+"/");
	      }
	      else{
	    	  logger.info("User Session is valid.");
	    	  chain.doFilter(request,response);
	      }
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	

}
