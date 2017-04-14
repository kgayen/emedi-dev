<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.application.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
</head>
<%@ include file="customerdashboard.jsp" %>
<body id="staticbody" onload="loadSearchResults('<%=userid%>')">
  <%
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
						<h3 class="page-header"><i class="icon_table"></i>Orders List</h3>
						<ol class="breadcrumb">
							<li><i class="fa fa-home"></i><a href="<%=contextPah%>/home">Home</a></li>
							<li><i class="icon_table"></i><a href="<%=contextPah%>/order/showuserorder">Orders List</a></li>						  	
						</ol>
					</div>
				</div>
	          <br>
			<div id="successModifiedAlert" style="display:none">
	            <i class="fa fa-check-square-o"></i> <strong id="successAlertMessage"></strong>
	      	</div>
	      	<div id="successModifiedErrorAlert" style="display:none">
	            <i class="fa fa-exclamation-triangle"></i> <strong id="errorAlertMessage"></strong>
	      	</div>
	      	<br>
			<div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                              Order Table
                              <p id="<%=messageTagId%>"></p>
                          </header>
                          
                          <table class="table table-striped table-advance table-hover"  id="userOrderList">
                           <tbody>
                              <tr>
                                 <th><i class="icon_check"></i> Order Id</th>
                                 <th><i class="icon_calendar"></i> Date</th>
                                 <th><i class="fa fa-dot-circle-o"></i> Status</th>
                                 <th><i class="fa fa-inr"></i> Price</th>
                                 <th><i class="icon_pin_alt"></i> Pincode</th>
                                 <th><i class="fa fa-mobile"></i> Mobile</th>
                                 <th><i class="icon_cogs"></i> Action</th>
                              </tr>                    
                           </tbody>
                        </table>
                      </section>
                  </div>
              </div>
              <div id="Order_View_Details">
              </div>
              <div id="Order_Edit_Details">
              </div>
              <div id="Order_Image_View">
              </div>
              <div id="Order_Status_View">
              </div>
              <div id="Order_Cancel_View">
              </div>
              <div id="Order_TaxBreakup_View">
		      </div>
	          </section>
	      </section>
	</section>
</body>