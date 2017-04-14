<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.application.model.User" %>
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
  	String message = "";
  	String contextPah = request.getContextPath();
	String messageTagId = "errormsg";
	//String contextPah = request.getContextPath();
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
					<h3 class="page-header"><i class="fa fa-laptop"></i>Change Password</h3>
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
						<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/<%=customURI%>/changepassword">Change Password</a></li>						  	
					</ol>
				</div>
			</div>
          <br>
		
		<div class="row">
<!-- 			 <div class="col-md-6 portlets"> -->
			<div class="col-lg-reg">
              <section class="panel">
                          <header class="panel-heading">
                              Create Profile
                          </header>
                          <div class="panel-body">
	                              <div class="form">
	                                  <form class="form-validate form-horizontal" id="feedback_form"  method="post" action="<%=contextPah%>/<%=customURI%>/changepassword">
	                                      <div class="form-group ">
	                                          <label for="cname" class="control-label col-lg-2">Old Password<span class="required">*</span></label>
	                                          <div class="col-lg-10">
	                                              <input class="form-control" name="oldpassword" type="password" min="5" maxlength="20" required/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <label for="cname" class="control-label col-lg-2">New Password<span class="required">*</span></label>
	                                          <div class="col-lg-10">
	                                              <input class="form-control" name="newpassword" type="password" min="5" maxlength="20" required/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <label for="cname" class="control-label col-lg-2">Confirm Password<span class="required">*</span></label>
	                                          <div class="col-lg-10">
	                                              <input class="form-control" name="confirmnewpassword" type="password" min="5" maxlength="20" required/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group">
	                                          <div class="col-lg-offset-2 col-lg-10">
	                                              <button class="btn btn-primary" id="changePassword" type="submit">Save</button>
	                                              <button class="btn btn-default" type="reset">Reset</button>
	                                              <br></br>
	                                              <p id="<%=messageTagId%>"><%=message%></p>
	                                          </div>
	                                      </div>
	                                  </form>
	                              </div>
	
	                          </div>
                      </section>
            </div>
          </div> 
          </section>
      </section>
  </section>
  <script src="<c:url value="/resources/JS/medishop.js"/>"></script>
  </body>
</html>