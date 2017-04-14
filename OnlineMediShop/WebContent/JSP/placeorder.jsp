<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
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
<body>
	
	<%
  	String errormessage = "";
	String contextPah = request.getContextPath();
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
					<h3 class="page-header"><i class="fa fa-laptop"></i>Place Order</h3>
					<ol class="breadcrumb">
						<%
						  	if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("admin") || ((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("seller")){
						  		%>
							  		<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>
									<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/order/placeorder">Place Order</a></li>
						  		<%
						  	}
						  	else if(((User)session.getAttribute("user")).getUser_role().equalsIgnoreCase("customer")){
						  		%>
							  		<li><i class="fa fa-home"></i><a href="<%=contextPah%>/home">Home</a></li>
									<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/order/placeorder">Place Order</a></li>	
						  		<%
						  	}
						  %>	  	
					</ol>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-reg">
	              <section class="panel">
	                          <header class="panel-heading">
	                              Place Order
	                          </header>
	                          <div class="panel-body">
	                              <div class="form">
									<form class="form-validate form-horizontal" enctype="multipart/form-data" id="feedback_form"  method="post" action="<%=contextPah%>/order/createorder">
	                                      <div class="form-group ">
	                                          <label for="cname" class="control-label col-lg-2">User Name<span class="required">*</span></label>
	                                          <div class="col-lg-10">
													  <input class="form-control" id="cname" type="text" min="5" name="username" maxlength="20" required="required"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <label for="cemail" class="control-label col-lg-2">E-Mail</label>
	                                          <div class="col-lg-10">
	                                              <input class="form-control" id="cemail" type="email" min="5" maxlength="45" name="email"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <label for="curl" class="control-label col-lg-2" >Mobile No<span class="required">*</span></label>
	                                          <div class="col-lg-10">
													  <input class="form-control " id="curl" type="text" name="user_mobile" min="10" maxlength="10" required="required"/>
													  <span id="mobileerrormsg"></span>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <label for="cname" class="control-label col-lg-2">Address <span class="required">*</span></label>
	                                          <div class="col-lg-10">
	                                              <input class="form-control" id="subject" type="text" min="5" name="address" required="required"/>
	                                          </div>
	                                      </div>
	                                      <div class="form-group ">
	                                          <label for="cname" class="control-label col-lg-2">Pincode <span class="required">*</span></label>
	                                          <div class="col-lg-10">
	                                              <input class="form-control" id="subject" type="text" name="user_pincode" min="7" maxlength="7" required="required"/>
	                                              <span id="pincodeerrormsg"></span>
	                                          </div>
	                                      </div>
	                                      <div class="form-group">
		                                      <label for="ccomment" class="control-label col-lg-2">Prescipetion Image <span class="required">*</span></label>
		                                      <div class="col-lg-10">
		                                      	<input class="form-control" type="file" name="orderpecimage" id="orderpecimage" required="required"/>
		                                      </div>
		                                  </div>
		                                  <div class="form-group">
		                                      <label for="ccomment" class="control-label col-lg-2">Order Details </label>
		                                      <div class="col-lg-10">
		                                      	<textarea rows="" cols="" class="form-control" name="orderdetils" id="orderdetils"></textarea>
		                                      </div>
		                                  </div>
		                                  <div class="form-group">
		                                      <label for="ccomment" class="control-label col-lg-2">Emergency </label>
		                                      <div class="col-lg-10">
												  <label class="checkbox-inline">
													  <input type="checkbox" id="inlineCheckbox1" name="emergencyflag">
													  <strong id="emergencymsg"></strong>
												  </label>
											  </div>
		                                  </div>
		                                  <input type="text" hidden="hidden" value="customer"/>
	                                      <div class="form-group">
	                                          <div class="col-lg-offset-2 col-lg-10">
	                                              <button class="btn btn-primary" id="saveorder" type="submit">Save</button>
	                                              <button class="btn btn-default" type="reset">Reset</button>
	                                              <br></br>
	                                              <p id="<%=messageTagId%>"><%=errormessage%></p>
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