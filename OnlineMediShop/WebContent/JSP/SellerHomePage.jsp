<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<%@ include file="SellerDashboard.jsp" %>
	<body>
		
		<%
		String errormessage = "";
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
							<h3 class="page-header"><i class="fa fa-laptop"></i> Dashboard</h3>
							<ol class="breadcrumb">
								<li><i class="fa fa-laptop"></i><a href="<%=contextPah%>/seller/sellerdashboard">Dashboard</a></li>					  	
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
						
					</div>
					
					<%
						if(userrole.equalsIgnoreCase("admin")){
					%>
					<div class="row">
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
						</div>
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
						
						<!--/.col-->
						
					</div>
					<%
						}
					%>
			  </section>
		  </section>
	  </section>
	</body>
</html>