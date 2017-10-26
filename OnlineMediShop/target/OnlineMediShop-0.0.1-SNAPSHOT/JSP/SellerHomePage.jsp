<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<%@ include file="SellerDashboard.jsp" %>
	<body onload="loadOrderStatus('<%=userid%>','load')">
		
		<%
		String errormessage = "";
		String messageTagId = "errormsg";
		if(request.getAttribute("message")!=null){
			errormessage = (String)request.getAttribute("message");
		}
	   %>
	   
	  <button id="load_order_status" hidden="true" value="<%=userid%>"></button>
		<!-- container section start -->
	  <section id="container" class="">   
		  <!--main content start-->
		  <section id="main-content">
			  <section class="wrapper">            
				  <!--overview start-->
				 <div class="row">
						<div class="col-lg-12">
							<h3 class="page-header"><i class="fa fa-laptop"></i> Dashboard</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>					  	
							</ol>
						</div>
				</div>
				<div class="row">
				
						<a href="<%=contextPah%>/selleractivity/showpendingorder" title="Pending Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-shopping-cart"></i>
									<div class="count" id="showpendingorder">0</div>
									<div class="title">Pending Order</div>						
								</div><!--/.info-box-->			
							</div>
						</a>
						
						<a href="<%=contextPah%>/selleractivity/showconfirmorder" title="Confirmed Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-thumbs-o-up"></i>
									<div class="count" id="showconfirmorder">0</div>
									<div class="title">Confirmed Order</div>						
								</div><!--/.info-box-->			
							</div>
						</a>	
						
						<a href="<%=contextPah%>/selleractivity/showcancelorder" title="Cancelled Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-times-circle-o"></i>
									<div class="count" id="showcancelorder">0</div>
									<div class="title">Cancelled Order</div>						
								</div><!--/.info-box-->			
							</div>
						</a>
						
						<a href="<%=contextPah%>/selleractivity/showdeliveredorder" title="Delivered Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-check-square-o"></i>
									<div class="count" id="showdeliveredorder">0</div>
									<div class="title">Delivered Order</div>						
								</div><!--/.info-box-->			
							</div>
						</a>
						
						<a href="<%=contextPah%>/selleractivity/processrefund" title="Process Refund Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-hourglass-half"></i>
									<div class="count" id="processrefund">0</div>
									<div class="title">Process Refund Order</div>						
								</div><!--/.info-box-->			
							</div>
						</a>
						
						<a href="<%=contextPah%>/selleractivity/refundedrefund" title="Refunded Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-exchange"></i>
									<div class="count" id="refundedrefund">0</div>
									<div class="title">Refunded Order</div>						
								</div><!--/.info-box-->			
							</div>	
						</a>
						
						<a href="<%=contextPah%>/selleractivity/confirmedrefund" title="Confirmed Refund Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-check-square-o"></i>
									<div class="count" id="confirmedrefund">0</div>
									<div class="title">Confirmed Refund Order</div>						
								</div><!--/.info-box-->			
							</div>
						</a>
						
						<a href="<%=contextPah%>/selleractivity/cancelledrefund" title="Cancelled Refund Order">
							<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
								<div class="info-box dark-bg">
									<i class="fa fa-times-circle-o"></i>
									<div class="count" id="cancelledrefund">0</div>
									<div class="title">Cancelled Refund Order</div>						
								</div><!--/.info-box-->			
							</div>
						</a>
						
				</div>
					
					<%
						if(userrole.equalsIgnoreCase("admin")){
					%>
					<div class="row">
						<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
							<div class="info-box dark-bg">
								<i class="fa fa-thumbs-o-up"></i>
								<div class="count">4.362</div>
								<div class="title">Order</div>						
							</div><!--/.info-box-->			
						</div><!--/.col-->
						
						<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
							<div class="info-box green-bg">
								<i class="fa fa-cubes"></i>
								<div class="count">1.426</div>
								<div class="title">Stock</div>						
							</div><!--/.info-box-->			
						</div>
						<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
							<div class="info-box blue-bg">
								<i class="fa fa-cloud-download"></i>
								<div class="count">6.674</div>
								<div class="title">Download</div>						
							</div><!--/.info-box-->			
						</div><!--/.col-->
						
						<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
							<div class="info-box brown-bg">
								<i class="fa fa-shopping-cart"></i>
								<div class="count">7.538</div>
								<div class="title">Purchased</div>						
							</div><!--/.info-box-->			
						</div><!--/.col-->	
						
						<div class="col-lg-3-custom col-md-3 col-sm-12 col-xs-12">
							<div class="info-box dark-bg">
								<i class="fa fa-thumbs-o-up"></i>
								<div class="count">50</div>
								<div class="title">Order</div>						
							</div><!--/.info-box-->			
						</div><!--/.col-->
						<!--/.col-->
						
					</div>
					<%
						}
					%>
					
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