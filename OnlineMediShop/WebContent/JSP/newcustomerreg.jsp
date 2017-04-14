<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.application.model.User" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Creative - Bootstrap 3 Responsive Admin Template">
    <meta name="author" content="GeeksLabs">
    <meta name="keyword" content="Creative, Dashboard, Admin, Template, Theme, Bootstrap, Responsive, Retina, Minimal">
    <link rel="shortcut icon" href="<c:url value="/resources/img/title.png"/>">

    <title>Online Medicine shop</title>

    <!-- Bootstrap CSS -->    
    <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet">
    <!--external css-->
    <!-- font icon -->
    <link href="<c:url value="/resources/css/elegant-icons-style.css"/>" rel="stylesheet" />
    <link href="<c:url value="/resources/css/font-awesome.min.css"/>" rel="stylesheet" />    
    <!-- full calendar css-->
    <link href="<c:url value="/resources/assets/fullcalendar/fullcalendar/bootstrap-fullcalendar.css"/>" rel="stylesheet" />
	<link href="<c:url value="/resources/assets/fullcalendar/fullcalendar/fullcalendar.css"/>" rel="stylesheet" />
    <!-- easy pie chart-->
    <link href="<c:url value="/resources/assets/jquery-easy-pie-chart/jquery.easy-pie-chart.css"/>" rel="stylesheet" type="text/css" media="screen"/>
    <!-- owl carousel -->
    <link rel="stylesheet" href="<c:url value="/resources/css/owl.carousel.css"/>" type="text/css">
	<link href="<c:url value="/resources/css/jquery-jvectormap-1.2.2.css"/>" rel="stylesheet">
    <!-- Custom styles -->
	<link rel="stylesheet" href="<c:url value="/resources/css/fullcalendar.css"/>">
	<link href="<c:url value="/resources/css/widgets.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/style-responsive.css"/>" rel="stylesheet" />
	<link href="<c:url value="/resources/css/xcharts.min.css"/>" rel=" stylesheet">	
	<link href="<c:url value="/resources/css/jquery-ui-1.10.4.min.css"/>" rel="stylesheet">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <script src="js/respond.min.js"></script>
      <script src="js/lte-ie7.js"></script>
    <![endif]-->
    <script src="<c:url value="/resources/JS/jquery.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery-ui-1.10.4.min.js"/>"></script>
    <script src="<c:url value="/resources/JS/jquery-1.8.3.min.js"/>"></script>
    <script src="<c:url value="/resources/JS/script.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/JS/jquery-ui-1.9.2.custom.min.js"/>"></script>
    <style type="text/css">
    body{
		  background: url("<c:url value="/resources/img/Medishop.jpg"/>") no-repeat center center fixed; 
		  -webkit-background-size: cover;
		  -moz-background-size: cover;
		  -o-background-size: cover;
		  background-size: cover;
		}
    </style>
  </head>

  <body id="staticbody">
  <%@include file="index.jsp" %>
  <%
  	if(user!=null){
  		display = "block";
  		userName = user.getUser_id();
  	}
  	String errormessage = "";
  	String messageTagId = "errormsg";
  	if(request.getAttribute("message")!=null){
  		errormessage = (String)request.getAttribute("message");
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
					<h3 class="page-header"><i class="fa fa-laptop"></i>Customer Registration</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-home"></i><a href="<%=contextPah%>/home">Home</a></li>
						<li><i class="icon_document_alt"></i><a href="<%=contextPah%>/retail/newcustomerreg">Customer Registration</a></li>					  	
					</ol>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-reg">
	              <section class="panel">
	                          <header class="panel-heading">
	                              Customer Registration
	                              <p id="<%=messageTagId%>"><%=errormessage%></p>
	                          </header>
	                          <div class="panel-body">
<!-- 	                          		enctype="multipart/form-data" -->
	                              <div class="form">
	                                  <form:form class="form-validate form-horizontal" enctype="multipart/form-data" id="feedback_form"  method="post" action="/OnlineMediShop/user/newregistration">
	                                      <div class="form-group ">
	                                          <form:label for="cname" class="control-label col-lg-2" path="user_id">User Id <span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
													  <form:input class="form-control" id="cname" type="text" min="5" maxlength="50" path="user_id" required="required"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <form:label for="cname" class="control-label col-lg-2" path="user_password">Password <span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
	                                              <form:input class="form-control" id="cname" name="user_password" path="user_password" type="password" min="5" maxlength="20" required="required"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <form:label for="cname" class="control-label col-lg-2" path="">Confirm Password <span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
	                                              <input class="form-control" id="cname" name="confirmpassword" type="password" min="5" maxlength="20" required/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <form:label for="cname" class="control-label col-lg-2" path="user_name">Full Name <span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
	                                              <form:input class="form-control" id="cname" path="user_name" type="text" min="5" maxlength="45" required="required"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <form:label for="cemail" class="control-label col-lg-2" path="user_email">E-Mail <span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
	                                              <form:input class="form-control" id="cemail" type="email" path="user_email" min="5" maxlength="45" required="required"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <form:label for="curl" class="control-label col-lg-2" path="user_mobile">Mobile No<span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
													  <form:input class="form-control " id="curl" type="text" name="user_mobile" path="user_mobile" min="10" maxlength="10" required="required"/>
													  <span id="mobileerrormsg"></span>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <form:label for="cname" class="control-label col-lg-2" path="user_address">Address <span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
	                                              <form:input class="form-control" id="subject" type="text" path="user_address" min="5" required="required"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <form:label for="cname" class="control-label col-lg-2" path="user_pincode">Pincode <span class="required">*</span></form:label>
	                                          <div class="col-lg-10">
	                                              <form:input class="form-control" id="subject" type="text" name="user_pincode" path="user_pincode" min="7" maxlength="7" required="required"/>
	                                              <span id="pincodeerrormsg"></span>
	                                          </div>
	                                      </div>
<!-- 	                                      <div class="form-group"> -->
<%-- 		                                      <form:label for="ccomment" class="control-label col-lg-2" path="user_image">Profile Picture</form:label> --%>
<!-- 		                                      <div class="col-lg-10"> -->
<!-- 		                                      	<input class="form-control" type="file" name="image" id="exampleInputFile"/> -->
<!-- 		                                      </div> -->
<!-- 		                                  </div> -->
		                                  <form:input type="text" path="user_role" hidden="hidden" value="customer"/>
	                                      <div class="form-group">
	                                          <div class="col-lg-offset-2 col-lg-10">
	                                              <button class="btn btn-primary" id="registration" type="submit">Save</button>
	                                              <button class="btn btn-default" type="reset">Reset</button>
	                                              <br></br>
<%-- 	                                              <p id="<%=messageTagId%>"><%=errormessage%></p> --%>
	                                          </div>
	                                      </div>
	                                  </form:form>
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