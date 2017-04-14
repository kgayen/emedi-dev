<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.application.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">

	<script type="text/javascript">
			//$('#example').dataTable();
	</script>
	
</head>
<%@ include file="SellerDashboard.jsp" %>
<body id="staticbody">
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
							<h3 class="page-header"><i class="fa fa-search"></i>Search Order</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-home"></i><a href="<%=contextPah%>/home">Home</a></li>
								<li><i class="fa fa-search"></i><a href="<%=contextPah%>/order/searchorder">Search Order</a></li>						  	
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
									  <a class="btn btn-success" data-toggle="modal" href="#searchByDateRange">
		                                  Search By Date Range
		                              </a>
		                              <a class="btn btn-success" data-toggle="modal" href="#searchByOrderID">
		                                  Search By Order ID
		                              </a>
		                              <a class="btn btn-success" data-toggle="modal" href="#searchByAdvance">
		                                  Advance Search
		                              </a>
		                              <a class="btn btn-success" data-toggle="modal" href="#editOrderDetails">
		                                  Coming Soon..
		                              </a>
									  <!-- editOrderDetails -->
		                              <div class="modal fade" id="searchByDateRange" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
																	      <input type="text" class="form-control" name="fromDate">
																	      <span class="input-group-addon">
																	      <span class="glyphicon glyphicon-calendar"></span>
																	      </span>
																	   	</div>
																  </div>
															      <div class="form-group">
															      		<label for="cname" class="control-label col-lg-2">To Date </label> 
																		<div class="input-group date" id="datetimepicker2">
																	      <input type="text" class="form-control" name="toDate">
																	      <span class="input-group-addon">
																	      <span class="glyphicon glyphicon-calendar"></span>
																	      </span>
																	   	</div>
																  </div>
	<%-- 														   </form> --%>
														  </div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="orderSearchByDate">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
		                              <!-- Modal -->
		                              <div class="modal fade" id="searchByOrderID" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
																	      <textarea class="form-control" placeholder="Please enter your Order ID" name="searchorderID" maxlength="100" id="searchorderID"></textarea>
																	      <span>Multiple orders separated  by <b>, (Comma)</b></span>
																	      <span id="mobileerrormsg"></span>
																	   	</div>
																  </div>
	<%-- 														   </form> --%>
														  </div>
			                                          </div>
			                                          <div class="modal-footer">
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="orderSearchByID">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
		                              <!-- Modal -->
		                              <div class="modal fade" id="searchByAdvance" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
			                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="orderSearchByAdvance">Search</button>
			                                              <button class="btn btn-default" type="reset" >Reset</button>
			                                              <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
			                                          </div>
		                                          </form>
		                                      </div>
		                                  </div>
		                              </div>
		                              <!-- modal -->
		                              <!-- Modal -->
		                              <div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		                                  <div class="modal-dialog">
		                                      <div class="modal-content">
		                                          <div class="modal-header">
		                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		                                              <h4 class="modal-title">Modal Tittle</h4>
		                                          </div>
		                                          <div class="modal-body">
		
		                                              Body goes here...
		
		                                          </div>
		                                          <div class="modal-footer">
		                                              <button class="btn btn-danger" type="button"> Ok</button>
		                                          </div>
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
							  		<div id="Order_Search_Details">
							  		</div>
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
		          </section>
	      </section>
	</section>
</body>