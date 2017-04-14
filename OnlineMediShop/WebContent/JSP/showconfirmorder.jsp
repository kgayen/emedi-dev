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
  <body id="staticbody" onload="loadSellerConfirmedOrderResult('<%=userid%>','Confirm')">
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
					<h3 class="page-header"><i class="fa fa-thumbs-o-up"></i> Confirmed Orders</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>
						<li><i class="fa fa-thumbs-o-up"></i><a href="<%=contextPah%>/selleractivity/showconfirmorder">Confirmed Orders</a></li>					  	
					</ol>
				</div>
			   </div>
				   <div class="row">
					<div id="successModifiedAlert" style="display:none">
					     <i class="fa fa-check-square-o"></i> <strong id="successAlertMessage"></strong>
					</div>
					<div id="successModifiedErrorAlert" style="display:none">
					     <i class="fa fa-exclamation-triangle"></i> <strong id="errorAlertMessage"></strong>
					</div>
				</div>
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
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="seller_confirmOrderSearchByDate">Search</button>
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
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="seller_confirmOrderSearchByID">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
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
					  
					  <div class="row" id="action_button_row">
						<div class="col-lg-4">
							<ol id="action_button_breadcrumb">
								<a class="btn btn-success btn-xs" title="Bulk Delivered" data-toggle="modal" href="#bulkDelivered">
									<i class="fa fa-check-square-o"> </i>  Bulk Delivered
								</a>
								<a class="btn btn-danger btn-xs" title="Bulk Cancel Request" data-toggle="modal" href="#bulkCancelRequest">
									<i class="fa fa-undo"></i>  Bulk Cancel Request
								</a>					  	
							</ol>
						</div>
					  </div>
					  
					  <div class="modal fade" id="bulkDelivered" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                         <div class="modal-dialog">
                              <div class="modal-content">
                                  <div class="modal-header">
                                      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                      <h4 class="modal-title"><b>Bulk Deliver</b></h4>
                                  </div>
                                  <form class="form-horizontal" id="orderBulkDelivered" action="POST">
	                                   <div class="modal-body">
	                                     <div class="form">
											      <div class="alert alert-info fade in">
											      		<strong>Are you really want to bulk deliver of below selected orders?</strong>
											      </div>
										  </div>
										  <div class="form-group" id="returnErrorDivBulkDelivered" style="display: none;">
										   	<div class="col-lg-10"><span id="returnErrorMessageBulkDelivered" style="color: #FF0000; font-family: Lato, sans-serif"></span></div>
									   	  </div>
	                                   </div>
	                                   
	                                   <div class="modal-footer">
	                                       <button class="btn btn-success" title="Deliver" type="button" id="seller_bulkdeliver_button">Deliver</button>
	                                       <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
	                                   </div>
                                  </form>
                              </div>
                          </div>
                      </div>
                      
                      <div class="modal fade" id="bulkCancelRequest" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                         <div class="modal-dialog">
                              <div class="modal-content">
                                  <div class="modal-header">
                                      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                      <h4 class="modal-title"><b>Bulk Cancel Request</b></h4>
                                  </div>
                                  <form class="form-horizontal" id="orderBulkCancelRequest" action="POST">
	                                   <div class="modal-body">
	                                     <div class="form">
											      <div class="alert alert-info fade in">
											      		<strong>Are you really want to submit cancel request of below selected orders?</strong>
											      </div>
											      <div class="form-group" style="padding-top: 8px">
												      	<label for="cname" class="control-label col-lg-2">Cancellation Reason<span class="required">*</span> </label>
												      	<div class="col-lg-10" style="padding-top:8px">
												      		<input class="form-control" id="seller_bulk_cancellation_request_note" type="text" placeholder="Please Enter cancellation Note" name="cancellation_Note" required="required">
												      	</div>
											      </div>
										  </div>
										  <div class="form-group" id="returnErrorDivBulkCancel" style="display: none;">
										   		<div class="col-lg-10"><span id="returnErrorMessageBulkCancel" style="color: #FF0000; font-family: Lato, sans-serif"></span></div>
									   	  </div>
	                                   </div>
	                                   <div class="modal-footer">
	                                       <button class="btn btn-danger" title="Cancel Request" type="button" id="seller_bulkcancelrequest_button">Cancel Request</button>
	                                       <button class="btn btn-default" type="reset" >Reset</button>
	                                       <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
	                                   </div>
                                  </form>
                              </div>
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
		              <div id="Seller_Order_Cancel_Request_View">
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