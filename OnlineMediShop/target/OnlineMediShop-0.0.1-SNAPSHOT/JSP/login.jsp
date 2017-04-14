<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Creative - Bootstrap 3 Responsive Admin Template">
    <meta name="author" content="GeeksLabs">
    <meta name="keyword" content="Creative, Dashboard, Admin, Template, Theme, Bootstrap, Responsive, Retina, Minimal">
    <link rel="shortcut icon" href="<c:url value="/resources/img/title.png"/>">

    <title>Online Medicine shop</title>

    <!-- Bootstrap CSS -->    
    <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet">
    <!--external css-->
    <!-- font icon -->
    <link href="<c:url value="/resources/css/elegant-icons-style.css"/>" rel="stylesheet" />
    <link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/style-responsive.css"/>" rel="stylesheet" />

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>

  <body class="login-img3-body">
    <%
		String contextPah = request.getContextPath();
	%>
	<%
		String pageType = (String)request.getAttribute("pagetype");
		String logginmsg = (String)request.getAttribute("logginmsg");
		if(logginmsg == null){
			logginmsg = "";
		}
	%>
    <div class="container">
		<div id="successModifiedAlert" style="display:none;width:100%">
		     <i class="fa fa-check-square-o"></i> <strong id="successAlertMessage"></strong>
		</div>
		<div id="successModifiedErrorAlert" style="display:none;width:100%">
		     <i class="fa fa-exclamation-triangle"></i> <strong id="errorAlertMessage"></strong>
		</div>
	  <input name='contextValue' id='contextValue' value='<%=contextPah%>' hidden='hidden'/>
      <form class="login-form" action="<%=contextPah%>/retail/login" method="post">        
        <div class="login-wrap">
            <p class="login-img"><i class="icon_lock_alt"></i></p>
            <div class="input-group">
              <span class="input-group-addon"><i class="icon_profile"></i></span>
              <input type="text" id="userIDField" name="userId" class="form-control" placeholder="Username" autofocus>
            </div>
            <div class="input-group">
                <span class="input-group-addon"><i class="icon_key_alt"></i></span>
                <input type="password" id="passwordField" name="userPassword" class="form-control" placeholder="Password">
            </div>
            <label class="checkbox">
                <input type="checkbox" value="remember-me"> Remember me
                <span class="pull-right"> <a data-toggle="modal" href="#forgetPassword"> Forgot Password?</a></span>
            </label>
            <button class="btn btn-primary btn-lg btn-block" type="submit">Login</button>
			<button class="btn btn-primary btn-lg btn-block" id="signup" type="button" data-toggle="modal" onclick="location.href='/OnlineMediShop/retail/newcustomerreg';">Signup</button>
            <input type="text" name="pagetype" id="pagetype" hidden="hidden" value="<%=pageType%>"/>
            <div id="errormsg"><span><%=logginmsg%></span></div>
        </div>
      </form>
		<div class="modal fade" id="forgetPassword" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-dialog">
				  <div class="modal-content">
					  <div class="modal-header">
						  <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						  <h4 class="modal-title"><b>Retrieve Password</b></h4>
					  </div>
					  <form class="form-horizontal" id="retrievePasswordForm" action="POST">
						  <div class="modal-body">
							   <div class="form">
									  <div class="form-group">
											<label for="cname" style="padding-top: 6px;width: 18%;" class="control-label col-lg-2">Mobile No <span class="required">*</span></label> 
											<div class="input-group date">
											  <span class="input-group-addon"><i class="icon_profile"></i></span>
							              	  <input type="text" id="forgetUserIdField" style="width: 60%" name="forgetUserId" class="form-control" placeholder="User ID or Mobile No" required="required" autofocus="">
											</div>
									  </div>
							  </div>
						  </div>
						  <div class="modal-body" id="forgetPasswordErrorMessage" style="display: none">
                          	<div class="col-lg-10">
								<span id="forgetPasswordErrorMessageSpan" style="color: #FF0000; font-family: 'Lato', sans-serif"></span>
							</div>
                          </div>
						  <div class="modal-footer">
							  <button class="btn btn-success" type="button" data-dismiss="modal" id="retrievePasswordButton">Submit</button>
							  <button class="btn btn-default" type="reset" >Reset</button>
							  <button class="btn btn-default" type="button" data-dismiss="modal">Close</button>
						  </div>
					  </form>
				  </div>
			  </div>
			</div>
    </div>
  </body>
	<script src="<c:url value="/resources/JS/jquery-1.8.3.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/login.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery-ui-1.10.4.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery-1.8.3.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/JS/jquery-ui-1.9.2.custom.min.js"/>"></script>
    
    <!-- Start Search Template -->
   	<link href="<c:url value="/resources/css/dataTables.bootstrap.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/resources/JS/jquery-1.12.0.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/resources/JS/dataTables.bootstrap.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/dataTables.bootstrap.js"/>"></script>
	<script src="<c:url value="/resources/JS/bootstrap.min.js"/>"></script>
    <!-- nice scroll -->
    <script src="<c:url value="/resources/JS/jquery.scrollTo.min.js"/>"></script>
    <script src="<c:url value="/resources/JS/jquery.nicescroll.js"/>" type="text/javascript"></script>
    <!-- charts scripts -->
    <script src="<c:url value="/resources/assets/jquery-knob/js/jquery.knob.js"/>"></script>
    <script src="<c:url value="/resources/JS/jquery.sparkline.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/assets/jquery-easy-pie-chart/jquery.easy-pie-chart.js"/>"></script>
    <script src="<c:url value="/resources/JS/owl.carousel.js"/>" ></script>
    <!-- jQuery full calendar -->
    <script src="<c:url value="/resources/JS/fullcalendar.min.js"/>"></script> <!-- Full Google Calendar - Calendar -->
	<script src="<c:url value="/resources/assets/fullcalendar/fullcalendar/fullcalendar.js"/>"></script>
    <!--script for this page only-->
    <script src="<c:url value="/resources/JS/calendar-custom.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.rateit.min.js"/>"></script>
    <!-- custom select -->
    <script src="<c:url value="/resources/JS/jquery.customSelect.min.js"/>" ></script>
	<script src="<c:url value="/resources/assets/chart-master/Chart.js"/>"></script>
	<!-- Gitter Script -->
   	<script src="<c:url value="/resources/JS/gritter.js"/>" type="text/javascript" ></script>
    <!--custome script for all page-->
    <script src="<c:url value="/resources/JS/scripts.js"/>"></script>
    <!-- custom script for this page-->
    <script src="<c:url value="/resources/JS/sparkline-chart.js"/>"></script>
    <script src="<c:url value="/resources/JS/easy-pie-chart.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery-jvectormap-1.2.2.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery-jvectormap-world-mill-en.js"/>"></script>
	<script src="<c:url value="/resources/JS/xcharts.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.autosize.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.placeholder.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/gdp-data.js"/>"></script>	
	<script src="<c:url value="/resources/JS/morris.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/sparklines.js"/>"></script>	
	<script src="<c:url value="/resources/JS/charts.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.slimscroll.min.js"/>"></script>
	<script src="<c:url value="/resources/JS/jquery.blockUI.js"/>"></script>
	<script src="<c:url value="/resources/JS/medishop.js"/>"></script>
<%-- 	<script src="<c:url value="/resources/JS/bootstrap-datepicker.js"/>"></script> --%>
	<script src="<c:url value="/resources/JS/moment.js"/>"></script>
	<script src="<c:url value="/resources/JS/bootstrap-datetimepicker.min.js"/>"></script>
</html>