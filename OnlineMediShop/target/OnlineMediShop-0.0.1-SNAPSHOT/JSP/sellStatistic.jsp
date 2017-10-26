<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.application.model.User" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
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
  
  <%
  	User user = (User)session.getAttribute("user");
  %>
  <body id="staticbody" onload="loadSellerStatisticData('<%= user.getUser_id()%>',null,null)">
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
   
   <input type="hidden" value="<%= user.getUser_id()%>" id="reported_seller_id"/>
  <!-- container section start -->
  <section id="container" class="">
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">            
              <!--overview start-->
			  <div class="row">
				<div class="col-lg-12">
					<h3 class="page-header"><i class="fa fa-pie-chart"></i> Sell Statistic</h3>
					<ol class="breadcrumb">
						<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>
						<li><i class="fa fa-pie-chart"></i><a href="<%=contextPah%>/seller/sellStatistic">Sell Statistic</a></li>					  	
					</ol>
				</div>
			  </div>
			  
			  <div class="row">
                  <div class="col-lg-12">
                          <header class="panel-heading">
                              <i class="fa fa-pie-chart"></i><b> Generate Report</b>
                              <p id="errormsg"></p>
                          <div>
							  <a class="btn btn-success" data-toggle="modal" href="#seller_reportSearch">
                                  Generate Report
                              </a>
                              <div class="modal fade" id="seller_reportSearch" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                  <div class="modal-dialog">
                                      <div class="modal-content">
                                          <div class="modal-header">
                                              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                              <h4 class="modal-title"><b>Enter Date Range</b></h4>
                                          </div>
                                          <form class="form-horizontal" id="sellerReportSearch" action="POST">
	                                          <div class="modal-body">
		                                           <div class="form">
<%-- 														   <form class="form-horizontal" id="" action="POST"> --%>
													      <div class="form-group">
													      		<label for="cname" class="control-label col-lg-2">From Date </label> 
																<div class="input-group date" id="datetimepicker1">
															      <input type="text" class="form-control" name="seller_report_fromDate">
															      <span class="input-group-addon">
															      <span class="glyphicon glyphicon-calendar"></span>
															      </span>
															   	</div>
														  </div>
													      <div class="form-group">
													      		<label for="cname" class="control-label col-lg-2">To Date </label> 
																<div class="input-group date" id="datetimepicker2">
															      <input type="text" class="form-control" name="seller_report_toDate">
															      <span class="input-group-addon">
															      <span class="glyphicon glyphicon-calendar"></span>
															      </span>
															   	</div>
														  </div>
														  <div class="form-group" id="refundRequestErrorDiv" style="display: none">
													      		<label for="cname" class="control-label col-lg-2"></label> 
													      		<div class="col-lg-10">
																	<span id="refundRequestErrorMessage" style="color: #FF0000; font-family: 'Lato', sans-serif"></span>
																</div>
														  </div>
														  <div class="form-group" id="refundRequestErrorDiv" style="display: none">
													      		<a id="seller_report_download_link" href=""></a>
														  </div>
												  </div>
	                                          </div>
	                                          <div class="modal-footer">
	                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="seller_report_searchByDate">Show In Graph</button>
	                                              <button class="btn btn-success" type="button" data-dismiss="modal" id="seller_export_report">Export Report</button>
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
			
			<div class="row">
				<div class="col-lg-12">
					<section class="panel">
						  <header class="panel-heading">
							  <i class="fa fa-pie-chart"></i>Sell Statistic
							  <p id="errormsg"></p>
						  </header>
						  <div class="panel-body">
								  <div class="form">
									 <div id="piechart"></div>
								  </div>
							  </div>
					  </section>
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