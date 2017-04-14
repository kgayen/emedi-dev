<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.application.model.User" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
  </head>
  <%@ include file="SellerDashboard.jsp" %>
  <body id="staticbody" onload="loadSellerOrderResult('<%=userid%>','Pending')">
  <%
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
					<h3 class="page-header"><i class="fa fa-clock-o"></i> Pending Order</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>
						<li><i class="fa fa-clock-o"></i><a href="<%=contextPah%>/selleractivity/showpendingorder">Pending Orders</a></li>					  	
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
<!-- 		                      <section class="panel"> -->
		                          <header class="panel-heading">
		                              <i class="fa fa-search"></i><b> Search Options</b>
		                              <p id="<%=messageTagId%>"></p>
		                          <div>
									  <a class="btn btn-success" data-toggle="modal" href="#seller_searchByDateRange">
		                                  Search By Date Range
		                              </a>
		                              <a class="btn btn-success" data-toggle="modal" href="#seller_searchByOrderID">
		                                  Search By Order ID
		                              </a>
<!-- 		                              <a class="btn btn-success" data-toggle="modal" href="#seller_searchByAdvance"> -->
<!-- 		                                  Advance Search -->
<!-- 		                              </a> -->
<!-- 		                              <a class="btn btn-success" data-toggle="modal" href="#editOrderDetails"> -->
<!-- 		                                  Coming Soon.. -->
<!-- 		                              </a> -->
									  <!-- editOrderDetails -->
		                              <div class="modal fade" id="seller_searchByDateRange" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title"><b>Search By Date Range</b></h4>
		                                          </div>
		                                          <form class="form-horizontal" id="orderSearchByDateForm" action="POST">
			                                          <div class="modal-body">
				                                           <div class="form">
	<%-- 														   <form class="form-horizontal" id="" action="POST"> --%>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">From Date </label> 
																		<div class="input-group date" id="datetimepicker1">
																	      <input type="text" class="form-control" name="seller_fromDate">
																	      <span class="input-group-addon">
																	      <span class="glyphicon glyphicon-calendar"></span>
																	      </span>
																	   	</div>
																  </div>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">To Date </label> 
																		<div class="input-group date" id="datetimepicker2">
																	      <input type="text" class="form-control" name="seller_toDate">
																	      <span class="input-group-addon">
																	      <span class="glyphicon glyphicon-calendar"></span>
																	      </span>
																	   	</div>
																  </div>
	<%-- 														   </form> --%>
														  </div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="seller_orderSearchByDate">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
		                              <!-- Modal -->
		                              <div class="modal fade" id="seller_searchByOrderID" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title"><b>Search By Order ID</b></h4>
		                                          </div>
		                                          <form class="form-horizontal" id="orderSearchByDateForm" action="POST">
			                                          <div class="modal-body">
				                                           <div class="form">
	<%-- 														   <form class="form-horizontal" id="" action="POST"> --%>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Order ID</label> 
																		<div class="col-lg-10">
																	      <textarea class="form-control" placeholder="Please enter your Order ID" name="seller_searchorderID" maxlength="100" id="searchorderID"></textarea>
																	      <span>Multiple orders separated  by <b>, (Comma)</b></span>
																	      <span id="mobileerrormsg"></span>
																	   	</div>
																  </div>
	<%-- 														   </form> --%>
														  </div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="seller_orderSearchByID">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
		                              <!-- Modal -->
		                              <div class="modal fade" id="seller_searchByAdvance" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title"><b>Advance Search</b></h4>
		                                          </div>
		                                          <form class="form-horizontal" id="orderSearchByAdvanceForm" action="POST">
		                                          	  <div class="modal-body">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Option </label>
															      		<div class="col-lg-8">
									                                          <label class="radio-inline">
									                                              <input type="radio" name="option" id="ByDateOtion" value="ByDateOtion"> By Date Range
									                                          </label>
									                                          <label class="radio-inline">
									                                              <input type="radio" name="option" id="ByOrderIdOtion" value="ByOrderIdOtion"> Order ID
									                                          </label>
									
									                                      </div> 
																  </div>
														  </div>
			                                          </div>
			                                          <div class="modal-body">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Status </label>
															      		<div class="col-lg-10">
									                                          <label class="checkbox-inline">
									                                              <input type="checkbox" id="inlineCheckbox1" name="statusOption" value="Pending"> Pending
									                                          </label>
									                                          <label class="checkbox-inline">
									                                              <input type="checkbox" id="inlineCheckbox2" name="statusOption" value="Confirm"> Confirmed
									                                          </label>
									                                          <label class="checkbox-inline">
									                                              <input type="checkbox" id="inlineCheckbox3" name="statusOption" value="Cancel"> Cancelled
									                                          </label>
									                                          <label class="checkbox-inline">
									                                              <input type="checkbox" id="inlineCheckbox3" name="statusOption" value="Deliver"> Delivered
									                                          </label>
									
									                                      </div>
																  </div>
														  </div>
			                                          </div>
			                                          <div class="modal-body" id="dateRange" style="display: none">
				                                           <div class="form">
	<%-- 														   <form class="form-horizontal" id="" action="POST"> --%>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">From Date </label>
															      		<div class="col-lg-10"> 
																			<div class="input-group date" id="datetimepicker3">
																		      <input type="text" class="form-control" name="startDate">
																		      <span class="input-group-addon">
																		      <span class="glyphicon glyphicon-calendar"></span>
																		      </span>
																		   	</div>
																		 </div>
																  </div>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">To Date </label>
															      		<div class="col-lg-10"> 
																			<div class="input-group date" id="datetimepicker4">
																		      <input type="text" class="form-control" name="endDate">
																		      <span class="input-group-addon">
																		      <span class="glyphicon glyphicon-calendar"></span>
																		      </span>
																		   	</div>
																	   	</div>
																  </div>
	<%-- 														   </form> --%>
														  </div>
			                                          </div>
			                                          <div class="modal-body" id="orderid" style="display: none">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Order ID</label> 
																		<div class="col-lg-10">
																	      <textarea class="form-control" placeholder="Please enter your Order ID" name="advanceSearchorderID" maxlength="100" id="searchorderID"></textarea>
																	      <span>Multiple orders separated  by <b>, (Comma)</b></span>
<!-- 																	      <span id="mobileerrormsg"></span> -->
																	   	</div>
																  </div>
														  </div>
			                                          </div>
			                                          <div class="modal-body" id="advanceErrorMessage" style="display: none">
				                                           <div class="col-lg-10">
															      <span id="advanceErrorMessageSpan" style="color: #FF0000; font-family: 'Lato', sans-serif"></span>
														  </div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="seller_orderSearchByAdvance">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
	                          </div>
	                          </header>
		              </div>
		            </div>
	                  <div class="row">
						  <div class="col-lg-12">
							  <section class="panel">
							  </section>
						  </div>
					  </div>
					  <div class="row">
						  <div class="col-lg-12">
							  <section class="panel">
							  		<div id="Seller_Order_Search_Details">
							  		</div>
							  </section>
						  </div>
					  </div>
	                  <div id="Seller_Order_View_Details">
		              </div>
		              <div id="Seller_Order_Confirm_Details">
		              </div>
		              <div id="Seller_Order_Image_View">
		              </div>
		              <div id="Seller_Order_Status_View">
		              </div>
		              <div id="Seller_Order_Cancel_View">
		              </div>
		              <div id="Seller_Order_TaxBreakup_View">
		              </div>
		              <div id="Order_Invoice">
		              	<div class="modal fade in" id="invoice" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" style="display: none;">
		              		<div class="modal-dialog">
		              			<div class="modal-content">
		              				<div class="modal-header">
		              					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		              					<h4 class="modal-title"><strong>Invoice</strong></h4> 
		              				</div> 
		              				<div class="modal-body" id="invoiceText">
<!-- 		              					<div class="alert alert-block alert-danger fade in"> -->
<!-- 		              						<button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> -->
<!-- 		              						The order status is and you can change Order only when Order status is. -->
<!-- 		              					</div> -->
		              				</div> 
	              					 <div class="modal-footer">
	              					 	<button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
	              					 </div>
		              			  </div>
		              		</div>
		              	</div>
		              </div>
		              <div class="modal fade" id="statusDialogBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			              <div class="modal-dialog">
				              <div class="modal-content">
				              <button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="display:none">×</button>
							   <div class="modal-body">
							      <div class="form">
							      	 <div id="successDialog" style="display:none">
							      	 	<div id="successModifiedAlert"  style="width: 100%">
							      	 		<i class="fa fa-check-square-o"></i>
							      	 		<strong id="successAlertMessage"></strong>
							      	 	</div>
							      	 </div>
							      	 <div id="errorDialog" style="display:none">
							      	 	<div id="successModifiedErrorAlert"  style="width: 100%">
							      	 		<i class="fa fa-exclamation-triangle"></i>
							      	 		<strong id="errorAlertMessage"></strong>
							      	 	</div>
							      	 </div>
							      </div>
							   </div>
							  </div>
						  </div>
					  </div>
					  <a data-toggle="modal" href="#statusDialogBox" style="display:none"></a>		
          </section>
      </section>
  </section>
  </body>
</html>