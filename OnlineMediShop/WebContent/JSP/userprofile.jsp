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
					<h3 class="page-header"><i class="fa fa-laptop"></i>Profile</h3>
					<ol class="breadcrumb">
					
						<%
						  	if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("admin") || ((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("seller")){
						  		%>
						  			<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Home</a></li>
						  		<%
						  	}
						  	else if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("customer")){
						  		%>
						  			<li><i class="fa fa-home"></i><a href="<%=contextPah%>/home">Home</a></li>
						  		<%
						  	}
						%>
						
						<li><i class="fa fa-user-md"></i><a href="<%=contextPah%>/<%=customURI%>/userprofile">Profile</a></li>						  	
					</ol>
				</div>
			</div>
          <br>
		
		<div class="row">
<!-- 			 <div class="col-md-6 portlets"> -->
			<div class="col-lg-reg">
              <section class="panel">
                          <header class="panel-heading">
                              Profile
                          </header>
                          <div class="panel-body">
	                              <div class="form">
	                              	  <%
			                              	if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("admin") || ((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("seller")){
			                           %>
			                           		<form:form class="form-validate form-horizontal" enctype="multipart/form-data" id="feedback_form"  method="post" action="/OnlineMediShop/seller/registration">
				                           		<div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_id">User Id <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
														  <form:input class="form-control" id="cname" type="text" min="5" maxlength="50" path="user_id" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_name">Full Name <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="cname" path="user_name" type="text" min="5" maxlength="45" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cemail" class="control-label col-lg-2" path="user_email">E-Mail <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="cemail" type="email" path="user_email" min="5" maxlength="45" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="curl" class="control-label col-lg-2" path="user_mobile">Mobile No<span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
														  <form:input class="form-control " id="curl" type="text" name="user_mobile" path="user_mobile" min="10" maxlength="15" readonly="true"/>
														  <span id="mobileerrormsg"></span>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_address">Address <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="subject" type="text" path="user_address" min="5" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_pincode">Pincode <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="subject" type="text" name="user_pincode" path="user_pincode" min="7" maxlength="7" required="required" readonly="true"/>
		                                              <span id="pincodeerrormsg"></span>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_pincode">User Role <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="subject" type="text" name="user_role" path="user_role" min="7" maxlength="7" required="required" readonly="true"/>
		                                              <span id="pincodeerrormsg"></span>
		                                          </div>
		                                      </div>
		                                      <div class="form-group">
		                                          <div class="col-lg-offset-2 col-lg-10">
	                                          	  <br>
	                                              <a class="btn btn-primary"  href="<%=contextPah%>/<%=customURI%>/registration">Edit</a>
	                                              <br></br>
	                                          </div>
		                                      </div>
		                                  </form:form>
			                           <%
			                              	}
			                              	else{
			                            %>
			                            	<form:form class="form-validate form-horizontal" enctype="multipart/form-data" id="feedback_form"  method="post" action="/OnlineMediShop/user/registration">
				                            	<div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_id">User Id <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
														  <form:input class="form-control" id="cname" type="text" min="5" maxlength="50" path="user_id" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_name">Full Name <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="cname" path="user_name" type="text" min="5" maxlength="45" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cemail" class="control-label col-lg-2" path="user_email">E-Mail <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="cemail" type="email" path="user_email" min="5" maxlength="45" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="curl" class="control-label col-lg-2" path="user_mobile">Mobile No<span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
														  <form:input class="form-control " id="curl" type="text" name="user_mobile" path="user_mobile" min="10" maxlength="15" readonly="true"/>
														  <span id="mobileerrormsg"></span>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_address">Address <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="subject" type="text" path="user_address" min="5" required="required" readonly="true"/>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_pincode">Pincode <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="subject" type="text" name="user_pincode" path="user_pincode" min="7" maxlength="7" required="required" readonly="true"/>
		                                              <span id="pincodeerrormsg"></span>
		                                          </div>
		                                      </div>
		                                      <div class="form-group ">
		                                          <form:label for="cname" class="control-label col-lg-2" path="user_pincode">User Role <span class="required">*</span></form:label>
		                                          <div class="col-lg-10">
		                                              <form:input class="form-control" id="subject" type="text" name="user_role" path="user_role" min="7" maxlength="7" required="required" readonly="true"/>
		                                              <span id="pincodeerrormsg"></span>
		                                          </div>
		                                      </div>
		                                      <div class="form-group">
		                                          <div class="col-lg-offset-2 col-lg-10">
	                                          	  <br>
	                                              <a class="btn btn-primary"  href="<%=contextPah%>/<%=customURI%>/registration">Edit</a>
	                                              <br></br>
	                                          </div>
		                                      </div>
		                                  </form:form>
			                            <%
			                              	}
	                              	  %>
	                              		
<%-- 	                                  <form:form class="form-validate form-horizontal" enctype="multipart/form-data" id="feedback_form"  method="post" action="/OnlineMediShop/<%=customURI%>/registration"> --%>
	                                      
	                              </div>
	
	                          </div>
                      </section>
            </div>
          </div> 
          </section>
      </section>
  </section>
  </body>
</html>