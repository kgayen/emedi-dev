<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.application.model.User" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    
  </head>
	<%@ include file="customerdashboard.jsp" %>
  <body id="staticbody">
  <%
	/*String contextPah = request.getContextPath();
  	User user = (User)session.getAttribute("user");
  	String username = "";
  	String userid = "";
  	String mobileno = "";
  	String email = "";
  	String address = "";
  	String pincode = "";
  	String userrole = "";
  	if(user!=null){
  		username = user.getUser_name();
  		userid = user.getUser_id();
  		userrole = user.getUser_role();
  		pincode = user.getUser_pincode();
  		mobileno = user.getUser_mobile();
  		address = user.getUser_address();
  		email = user.getUser_email();
  	}*/
  	String message = "";
  	String messageTagId = "errormsg";
  	if(request.getAttribute("message")!=null){
  		message = (String)request.getAttribute("message");
  		if(message.contains("Successfully")){
  			messageTagId = "successmsg";
  		}
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
					<h3 class="page-header"><i class="fa fa-laptop"></i> Home</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-home"></i><a href="<%=contextPah%>/home">Home</a></li>
						<li><i class="fa fa-laptop"></i>Home</li>						  	
					</ol>
				</div>
			</div>
              
            <div class="row">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<div class="info-box blue-bg">
						<i class="fa fa-cloud-download"></i>
						<div class="count">6.674</div>
						<div class="title">Download</div>						
					</div><!--/.info-box-->			
				</div><!--/.col-->
				
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<div class="info-box brown-bg">
						<i class="fa fa-shopping-cart"></i>
						<div class="count">7.538</div>
						<div class="title">Purchased</div>						
					</div><!--/.info-box-->			
				</div><!--/.col-->	
				
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<div class="info-box dark-bg">
						<i class="fa fa-thumbs-o-up"></i>
						<div class="count">4.362</div>
						<div class="title">Order</div>						
					</div><!--/.info-box-->			
				</div><!--/.col-->
				
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<div class="info-box green-bg">
						<i class="fa fa-cubes"></i>
						<div class="count">1.426</div>
						<div class="title">Stock</div>						
					</div><!--/.info-box-->			
				</div><!--/.col-->
				
			</div><!--/.row-->
          <br>
		
		<div class="row">
<!-- 			 <div class="col-md-6 portlets"> -->
			<div class="col-lg-reg">
              <section class="panel">
                          <header class="panel-heading">
                              Form validations
                          </header>
                          <div class="panel-body">
                              <div class="form">
                                  <form class="form-validate form-horizontal" enctype="multipart/form-data" id="feedback_form" method="post" action="<%=contextPah%>/user/registration">
                                      <div class="form-group ">
                                          <label for="cname" class="control-label col-lg-2">User Id</label>
                                          <div class="col-lg-10">
                                              <input class="form-control" id="cname" name="userid" type="text" value="<%=userid %>" disabled="disabled"/>
                                          </div>
                                      </div>
                                      <div class="form-group ">
                                          <label for="cname" class="control-label col-lg-2">Full Name <span class="required">*</span></label>
                                          <div class="col-lg-10">
                                              <input class="form-control" id="cname" name="fullname" value="<%=username %>" type="text" required />
                                          </div>
                                      </div>
                                      <div class="form-group ">
                                          <label for="cemail" class="control-label col-lg-2">E-Mail <span class="required">*</span></label>
                                          <div class="col-lg-10">
                                              <input class="form-control " id="cemail" type="email" name="email" value="<%=email %>" required />
                                          </div>
                                      </div>
                                      <div class="form-group ">
                                          <label for="curl" class="control-label col-lg-2">Mobile No<span class="required">*</span></label>
                                          <div class="col-lg-10">
                                              <input class="form-control " id="curl" type="text" name="mobileno" value="<%=mobileno %>" required />
                                          </div>
                                      </div>
                                      <div class="form-group ">
                                          <label for="cname" class="control-label col-lg-2">Address <span class="required">*</span></label>
                                          <div class="col-lg-10">
                                              <input class="form-control" id="subject" name="address" min="5" type="text" value="<%=address %>" required />
                                          </div>
                                      </div>
                                      <div class="form-group ">
                                          <label for="cname" class="control-label col-lg-2">Pincode <span class="required">*</span></label>
                                          <div class="col-lg-10">
                                              <input class="form-control" id="subject" name="pincode" min="7" maxlength="7" type="text" value="<%=pincode %>" required />
                                          </div>
                                      </div>
<!--                                       <div class="form-group"> -->
<!-- 	                                      <label for="ccomment" class="control-label col-lg-2">Profile Picture</label> -->
<!-- 	                                      <div class="col-lg-10"> -->
<!-- 	                                      	<input class="form-control" type="file" name="profpic" id="exampleInputFile"> -->
<!-- 	                                     	 <p class="help-block">Example block-level help text here.</p> -->
<!-- 	                                      </div> -->
<!-- 	                                  </div> -->
                                      <div class="form-group">
                                          <div class="col-lg-offset-2 col-lg-10">
                                              <button class="btn btn-primary" type="submit">Save</button>
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
  </body>
</html>