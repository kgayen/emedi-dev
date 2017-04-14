<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.application.model.User" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
  </head>
  <%
  	if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("admin") || ((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("seller")){
  		%>
  		<%@ include file="SellerDashboard.jsp" %>
  		<%
  	}
  	else if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("customer")){
  		%>
  		<%@ include file="customerdashboard.jsp" %>
  		<%
  	}
  %>
  
  <body id="staticbody">
  <%
  	String contextPah = request.getContextPath();
  	String message = "";
	String messageTagId = "errormsg";
	if(request.getAttribute("message")!=null){
		message = (String)request.getAttribute("message");
		if(message.contains("Successfully")){
			messageTagId = "successmsg";
		}
	}
	String customURI = "user";
	if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("admin") || ((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("seller")){
  		customURI = "seller";
  	}
   %>
  <!-- container section start -->
  <section id="container" class="">
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">            
              <!--overview start-->
			  <div class="row">
				<div class="col-lg-12">
					<h3 class="page-header"><i class="fa fa-bar-chart"></i> Sell Statistic</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>
						<li><i class="fa fa-bar-chart"></i><a href="<%=contextPah%>/seller/sellStatistic">Sell Statistic</a></li>					  	
					</ol>
				</div>
			</div>
          <br>		
          </section>
      </section>
  </section>
  </body>
</html>