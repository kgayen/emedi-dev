<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
					<h3 class="page-header"><i class="fa fa-laptop"></i>Create Profile</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-home"></i><a href="<%=contextPah%>/home">Home</a></li>
						<li><i class="icon_document_alt"></i><a href="<%=contextPah%>/<%=customURI%>/registration">Create Profile</a></li>						  	
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
                              <p id="<%=messageTagId%>"><%=message%></p>
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
													  <form:input class="form-control " id="curl" type="text" name="user_mobile" path="user_mobile" min="10" maxlength="10" readonly="required"/>
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
	                                              <form:input class="form-control" id="subject" type="text" name="user_pincode" path="user_pincode" min="7" maxlength="7" required="required" readonly="true"/>
	                                              <span id="pincodeerrormsg"></span>
	                                          </div>
	                                      </div>
<!-- 	                                      <div class="form-group"> -->
<%-- 		                                      <form:label for="ccomment" class="control-label col-lg-2" path="user_image">Profile Picture</form:label> --%>
<!-- 		                                      <div class="col-lg-10"> -->
<!-- 		                                      	<input class="form-control" type="file" name="image" id="exampleInputFile"/> -->
<!-- 		                                      </div> -->
<!-- 		                                  </div> -->
		                                  <form:input type="text" path="user_role" hidden="hidden"/>
		                                  <form:input type="text" path="user_creator" hidden="hidden"/>
	                                      <div class="form-group">
	                                          <div class="col-lg-offset-2 col-lg-10">
	                                              <button class="btn btn-primary" id="registration" type="submit">Save</button>
	                                              <button class="btn btn-default" type="reset">Reset</button>
	                                              <br></br>
<%-- 	                                              <p id="<%=messageTagId%>"><%=message%></p> --%>
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
													  <form:input class="form-control " id="curl" type="text" name="user_mobile" path="user_mobile" min="10" maxlength="10" readonly="required"/>
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
		                                  <form:input type="text" path="user_role" hidden="hidden"/>
		                                  <form:input type="text" path="user_creator" hidden="hidden"/>
	                                      <div class="form-group">
	                                          <div class="col-lg-offset-2 col-lg-10">
	                                              <button class="btn btn-primary" id="registration" type="submit">Save</button>
	                                              <button class="btn btn-default" type="reset">Reset</button>
	                                              <br></br>
<%-- 	                                              <p id="<%=messageTagId%>"><%=message%></p> --%>
	                                          </div>
	                                      </div>
	                                  </form:form>
	                               <%
	                              	}
	                               %>   
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