<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.application.model.User" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <script src="<c:url value="/resources/JS/jquery-1.12.0.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/returnOrder.js"/>"></script>
  </head>
  <%@ include file="SellerDashboard.jsp" %>
  <body id="staticbody" onload="loadSellerRefundOrders('<%=userid%>','Load',null,null,null,new Array('Pending'))">
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
					<h3 class="page-header"><i class="fa fa-hourglass-half"></i> Process Refunds</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>
						<li><i class="fa fa-hourglass-half"></i><a href="<%=contextPah%>/selleractivity/processrefund">Process Refunds</a></li>					  	
					</ol>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div id="successModifiedAlert" style="display:none">
					     <i class="fa fa-check-square-o"></i> <strong id="successAlertMessage"></strong>
					</div>
					<div id="successModifiedErrorAlert" style="display:none">
					     <i class="fa fa-exclamation-triangle"></i> <strong id="errorAlertMessage"></strong>
					</div>
				</div>
			</div>
			<div class="row">
                  <div class="col-lg-12">
<!-- 		                      <section class="panel"> -->
                          <header class="panel-heading">
                              <i class="fa fa-search"></i><b> Search Options</b>
                              <p id="<%=messageTagId%>"></p>
	                          <div>
								  <a class="btn btn-success" data-toggle="modal" href="#searchReturnRequest">
				                                  Search Refund Request
				                  </a>
				              </div>
                          </header>
                              <!-- modal -->
                              <!-- Modal -->
                              <div class="col-lg-12">
									  <!-- Return Order -->
		                              <div class="modal fade" id="initiateReturnRequest" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title"><b>Refund Order</b></h4>
		                                          </div>
		                                          <form class="form-horizontal" id="returnOrderForm" action="POST">
			                                       	   <div class="modal-body">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Order ID<span class="required">*</span></label> 
																		<div class="col-lg-10">
																				  <input class="form-control" placeholder="Please Provide Order Id" id="returnOrderId" type="text" name="returnOrderId" required="required"/>
								                                        </div>
																  </div>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Bill No.<span class="required">*</span></label> 
																		<div class="col-lg-10">
																				  <input class="form-control" placeholder="Please Provide Bill No." id="returnBillNo" type="text" name="returnBillNo" required="required"/>
								                                        </div>
																  </div>
																  <div class="form-group" id="returnErrorDiv" style="display: none">
															      		<label for="cname" class="control-label col-lg-2"></label> 
															      		<div class="col-lg-10">
																			<span id="returnErrorMessage" style="color: #FF0000; font-family: 'Lato', sans-serif"></span>
																		</div>
																  </div>
														  	</div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="returnOrderClick">Submit</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
		                              <div class="modal fade" id="returnRequest" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title"><b>Initiate Refund Request</b></h4>
		                                          </div>
		                                          <form class="form-horizontal" id="returnRequestForm" action="POST">
			                                       	   <div class="modal-body">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Order ID<span class="required">*</span></label> 
																		<div class="col-lg-10">
																				  <input class="form-control" id="refundRequestOrderId" readonly="readonly" type="text" name="refundRequestOrderId" required="required"/>
																				  <input id="refundRequestSellerId" type="text" hidden='hidden'/>
								                                        </div>
																  </div>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Bill No.<span class="required">*</span></label> 
																		<div class="col-lg-10">
																				  <input class="form-control" id="refundRequestBillNo" readonly="readonly" type="text" name="refundRequestBillNo" required="required"/>
								                                        </div>
																  </div>
																  <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Refund Amount</label> 
																		<div class="col-lg-10">
																				  <input class="form-control" id="refundRequestAmount" type="text" readonly="readonly" name="refundRequestAmount" required="required"/>
								                                        </div>
																  </div>
																  <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Valid Reason<span class="required">*</span></label> 
																		<div class="col-lg-10">
																				  <textarea class="form-control" id="refundRequestReason" name="refundRequestReason" required="required"></textarea>
								                                        </div>
																  </div>
																  <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">T&C<span class="required">*</span></label> 
																		<div class="col-lg-10">
																			  <div class="checkbox">
									                                              <label>
									                                                  <input type="checkbox" id="refundTermsCondition" value="">
									                                                  <a href="#refundTerms" data-toggle="modal">I agree to the terms & conditions.</a>
									                                              </label>
									                                          </div>
								                                        </div>
																  </div>
																  <div class="form-group" id="refundRequestErrorDiv" style="display: none">
															      		<label for="cname" class="control-label col-lg-2"></label> 
															      		<div class="col-lg-10">
																			<span id="refundRequestErrorMessage" style="color: #FF0000; font-family: 'Lato', sans-serif"></span>
																		</div>
																  </div>
														  	</div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="refundRequestClick">Initiate Refund</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              
		                              <!-- Search Refund Order -->
		                              <div class="modal fade" id="searchReturnRequest" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title"><b>Advance Search</b></h4>
		                                          </div>
		                                          <form class="form-horizontal" id="refundorderSearchByAdvanceForm" action="POST">
		                                          	  <div class="modal-body">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Option </label>
															      		<div class="col-lg-8">
									                                          <label class="radio-inline">
									                                              <input type="radio" name="option" id="refundByDateOtion" value="refundByDateOtion"> By Date Range
									                                          </label>
									                                          <label class="radio-inline">
									                                              <input type="radio" name="option" id="refundByOrderIdOtion" value="refundByOrderIdOtion"> Refund Request ID
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
									                                              <input type="checkbox" id="inlineCheckbox1" name="refundStatusOption" value="Pending"> Pending
									                                          </label>
									                                          <label class="checkbox-inline">
									                                              <input type="checkbox" id="inlineCheckbox2" name="refundStatusOption" value="Confirm"> Confirmed
									                                          </label>
									                                          <label class="checkbox-inline">
									                                              <input type="checkbox" id="inlineCheckbox3" name="refundStatusOption" value="Cancel"> Cancelled
									                                          </label>
									                                          <label class="checkbox-inline">
									                                              <input type="checkbox" id="inlineCheckbox3" name="refundStatusOption" value="Refund"> Refunded
									                                          </label>
									
									                                      </div>
																  </div>
														  </div>
			                                          </div>
			                                          <div class="modal-body" id="refunddateRange" style="display: none">
				                                           <div class="form">
	<%-- 														   <form class="form-horizontal" id="" action="POST"> --%>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">From Date </label>
															      		<div class="col-lg-10"> 
																			<div class="input-group date" id="datetimepicker5">
																		      <input type="text" placeholder="Please enter From Date" class="form-control" name="refundstartDate">
																		      <span class="input-group-addon">
																		      <span class="glyphicon glyphicon-calendar"></span>
																		      </span>
																		   	</div>
																		 </div>
																  </div>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">To Date </label>
															      		<div class="col-lg-10"> 
																			<div class="input-group date" id="datetimepicker6">
																		      <input type="text" placeholder="Please enter To Date" class="form-control" name="refundendDate">
																		      <span class="input-group-addon">
																		      <span class="glyphicon glyphicon-calendar"></span>
																		      </span>
																		   	</div>
																	   	</div>
																  </div>
	<%-- 														   </form> --%>
														  </div>
			                                          </div>
			                                          <div class="modal-body" id="refundorderid" style="display: none">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Order ID</label> 
																		<div class="col-lg-10">
																	      <textarea class="form-control" placeholder="Please enter your Refund Request ID" name="refundadvanceSearchorderID" maxlength="100" id="refundsearchorderID"></textarea>
																	      <span>Multiple orders separated  by <b>, (Comma)</b></span>
<!-- 																	      <span id="mobileerrormsg"></span> -->
																	   	</div>
																  </div>
														  </div>
			                                          </div>
			                                          <div class="modal-body" id="refundadvanceErrorMessage" style="display: none">
				                                           <div class="col-lg-10">
															      <span id="refundadvanceErrorMessageSpan" style="color: #FF0000; font-family: 'Lato', sans-serif"></span>
														  </div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="refundorderSearchByAdvance" value="SellerSearch">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- Search Refund Order -->
		                              
		                              <div class="modal fade" id="refundTerms" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title"><b>Refund Terms and Conditions</b></h4>
		                                          </div>
		                                          <form class="form-horizontal" id="refundTermsForm" action="POST">
			                                       	   <div class="modal-body">
				                                           <div class="form">
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">Order ID<span class="required">*</span></label> 
																		<div class="col-lg-10">
																				  <input class="form-control" id="refundRequestOrderId" readonly="readonly" type="text" name="refundRequestOrderId"/>
								                                        </div>
																  </div>
														  	</div>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
	                      </div>
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
			  		<div id="Refund_Order_Search_Details">
			  		</div>
			  </section>
			</div>
		  	</div>
                <div id="Refund_Order_View_Details">
             </div>
             <div id="Refund_Order_Edit_Details">
             </div>
             <div id="Refund_Order_Image_View">
             </div>
             <div id="Refund_Order_Status_View">
             </div>
             <div id="Refund_Order_Cancel_View">
             </div>
             <div id="Refund_Order_Confirm_View">
             </div>
             <div id="Refund_Order_Refunded_View">
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
          <br>		
          </section>
      </section>
  </section>
  </body>
</html>