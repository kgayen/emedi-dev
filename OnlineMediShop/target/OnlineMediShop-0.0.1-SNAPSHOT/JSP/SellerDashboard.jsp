<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.application.model.User" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Creative - Bootstrap 3 Responsive Admin Template">
    <meta name="author" content="GeeksLabs">
    <meta name="keyword" content="Creative, Dashboard, Admin, Template, Theme, Bootstrap, Responsive, Retina, Minimal">
    <link rel="shortcut icon" href="<c:url value="/resources/img/title.png"/>">

    <title>Online Medicine Shop : Service Dashboard</title>

    <!-- Bootstrap CSS -->    
    <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet">
    <!--external css-->
    <!-- font icon -->
    <link href="<c:url value="/resources/css/elegant-icons-style.css"/>" rel="stylesheet" />
    <link href="<c:url value="/resources/css/font-awesome.min.css"/>" rel="stylesheet" />    
    <!-- full calendar css-->
    <link href="<c:url value="/resources/assets/fullcalendar/fullcalendar/bootstrap-fullcalendar.css"/>" rel="stylesheet" />
	<link href="<c:url value="/resources/assets/fullcalendar/fullcalendar/fullcalendar.css"/>" rel="stylesheet" />
    <!-- easy pie chart-->
    <link href="<c:url value="/resources/assets/jquery-easy-pie-chart/jquery.easy-pie-chart.css"/>" rel="stylesheet" type="text/css" media="screen"/>
    <!-- owl carousel -->
    <link rel="stylesheet" href="<c:url value="/resources/css/owl.carousel.css"/>" type="text/css">
	<link href="<c:url value="/resources/css/jquery-jvectormap-1.2.2.css"/>" rel="stylesheet">
    <!-- Custom styles -->
	<link rel="stylesheet" href="<c:url value="/resources/css/fullcalendar.css"/>">
	<link href="<c:url value="/resources/css/widgets.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/style-responsive.css"/>" rel="stylesheet" />
	<link href="<c:url value="/resources/css/xcharts.min.css"/>" rel=" stylesheet">	
	<link href="<c:url value="/resources/css/jquery-ui-1.10.4.min.css"/>" rel="stylesheet">
	<link href="<c:url value="/resources/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">
	<link href="<c:url value="/resources/css/flaticon.css"/>" rel="stylesheet">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <script src="js/respond.min.js"></script>
      <script src="js/lte-ie7.js"></script>
    <![endif]-->
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
		<script src="<c:url value="/resources/JS/jquery-ajax-native.js"/>"></script>
    <!-- End Search Template -->
    
    <style type="text/css">
    body{
		  background: url("<c:url value="/resources/img/Medishop.jpg"/>") no-repeat center center fixed; 
		  -webkit-background-size: cover;
		  -moz-background-size: cover;
		  -o-background-size: cover;
		  background-size: cover;
		}
    </style>
  </head>

  <body id="staticbody">
  <%
	String contextPah = request.getContextPath();
  	User user = (User)session.getAttribute("user");
  	String username = "";
  	String userid = "";
  	String mobileno = "";
  	String email = "";
  	String address = "";
  	String pincode = "";
  	String userrole = "";
  	if(user!=null){
  		username = user.getUser_name();
  		userid = user.getUser_id();
  		userrole = user.getUser_role();
  		pincode = user.getUser_pincode();
  		mobileno = user.getUser_mobile();
  		address = user.getUser_address();
  		email = user.getUser_email();
  	}
   %>
  <!-- container section start -->
  <section id="container" class="">
     
      <input name='contextValue' id='contextValue' value='<%=contextPah%>' hidden='hidden'/>
      <header class="header dark-bg">
            <div class="toggle-nav">
            	<a href = "#"><span class="icon-task-l"></span></a>
                <div class="icon-reorder tooltips" data-original-title="Toggle Navigation" data-placement="bottom"></div>
            </div>

            <!--logo start-->
            <!--<span class="lite">Shop</span>-->
            <a href="<%=contextPah%>/home" class="logo"><strong>Medicine Shop</strong></a>
            <!--logo end-->

            <div class="nav search-row" id="top_menu">
                <!--  search form start -->
                <ul class="nav top-menu">                    
                    <li>
                        <form class="navbar-form">
                            <input class="form-control" placeholder="Search" type="text">
                        </form>
                    </li>                    
                </ul>
                <!--  search form end -->                
            </div>

            <div class="top-nav notification-row">                
                <!-- notificatoin dropdown start-->
                <ul class="nav pull-right top-menu">
                    
                    <!-- task notificatoin start -->
                    <li id="task_notificatoin_bar" class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="icon-task-l"></i>
                            <span class="badge bg-important">6</span>
                        </a>
                        <ul class="dropdown-menu extended tasks-bar">
                            <div class="notify-arrow notify-arrow-blue"></div>
                            <li>
                                <p class="blue">You have 6 pending letter</p>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="task-info">
                                        <div class="desc">Design PSD </div>
                                        <div class="percent">90%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="90" aria-valuemin="0" aria-valuemax="100" style="width: 90%">
                                            <span class="sr-only">90% Complete (success)</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="task-info">
                                        <div class="desc">
                                            Project 1
                                        </div>
                                        <div class="percent">30%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="30" aria-valuemin="0" aria-valuemax="100" style="width: 30%">
                                            <span class="sr-only">30% Complete (warning)</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="task-info">
                                        <div class="desc">Digital Marketing</div>
                                        <div class="percent">80%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 80%">
                                            <span class="sr-only">80% Complete</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="task-info">
                                        <div class="desc">Logo Designing</div>
                                        <div class="percent">78%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100" style="width: 78%">
                                            <span class="sr-only">78% Complete (danger)</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="task-info">
                                        <div class="desc">Mobile App</div>
                                        <div class="percent">50%</div>
                                    </div>
                                    <div class="progress progress-striped active">
                                        <div class="progress-bar"  role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: 50%">
                                            <span class="sr-only">50% Complete</span>
                                        </div>
                                    </div>

                                </a>
                            </li>
                            <li class="external">
                                <a href="#">See All Tasks</a>
                            </li>
                        </ul>
                    </li>
                    <!-- task notificatoin end -->
                    <!-- inbox notificatoin start-->
                    <li id="mail_notificatoin_bar" class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <i class="icon-envelope-l"></i>
                            <span class="badge bg-important">5</span>
                        </a>
                        <ul class="dropdown-menu extended inbox">
                            <div class="notify-arrow notify-arrow-blue"></div>
                            <li>
                                <p class="blue">You have 5 new messages</p>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="photo"><img alt="avatar" src='<c:url value="/resources/img/avatar-mini.jpg"/>'></span>
                                    <span class="subject">
                                    <span class="from">Greg  Martin</span>
                                    <span class="time">1 min</span>
                                    </span>
                                    <span class="message">
                                        I really like this admin panel.
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="photo"><img alt="avatar" src='<c:url value="/resources/img/avatar-mini2.jpg"/>'></span>
                                    <span class="subject">
                                    <span class="from">Bob   Mckenzie</span>
                                    <span class="time">5 mins</span>
                                    </span>
                                    <span class="message">
                                     Hi, What is next project plan?
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="photo"><img alt="avatar" src="<c:url value="/resources/img/avatar-mini3.jpg"/>"></span>
                                    <span class="subject">
                                    <span class="from">Phillip   Park</span>
                                    <span class="time">2 hrs</span>
                                    </span>
                                    <span class="message">
                                        I am like to buy this Admin Template.
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="photo"><img alt="avatar" src="<c:url value="/resources/img/avatar-mini4.jpg"/>"></span>
                                    <span class="subject">
                                    <span class="from">Ray   Munoz</span>
                                    <span class="time">1 day</span>
                                    </span>
                                    <span class="message">
                                        Icon fonts are great.
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="#">See all messages</a>
                            </li>
                        </ul>
                    </li>
                    <!-- inbox notificatoin end -->
                    <!-- alert notification start-->
                    <li id="alert_notificatoin_bar" class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">

                            <i class="icon-bell-l"></i>
                            <span class="badge bg-important">7</span>
                        </a>
                        <ul class="dropdown-menu extended notification">
                            <div class="notify-arrow notify-arrow-blue"></div>
                            <li>
                                <p class="blue">You have 4 new notifications</p>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="label label-primary"><i class="icon_profile"></i></span> 
                                    Friend Request
                                    <span class="small italic pull-right">5 mins</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="label label-warning"><i class="icon_pin"></i></span>  
                                    John location.
                                    <span class="small italic pull-right">50 mins</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="label label-danger"><i class="icon_book_alt"></i></span> 
                                    Project 3 Completed.
                                    <span class="small italic pull-right">1 hr</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="label label-success"><i class="icon_like"></i></span> 
                                    Mick appreciated your work.
                                    <span class="small italic pull-right"> Today</span>
                                </a>
                            </li>                            
                            <li>
                                <a href="#">See all notifications</a>
                            </li>
                        </ul>
                    </li>
                    <!-- alert notification end-->
                    <!-- user login dropdown start-->
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="username"><%=((User)session.getAttribute("user")).getUser_id()%></span>
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu extended logout">
                            <div class="log-arrow-up"></div>
                            <li class="eborder-top">
                                <a href="<%=contextPah%>/seller/userprofile"><i class="icon_profile"></i> My Profile</a>
                            </li>
                            <li>
                                <a href="#"><i class="icon_mail_alt"></i> My Inbox</a>
                            </li>
                            <li>
                                <a href="#"><i class="icon_clock_alt"></i> Timeline</a>
                            </li>
                            <li>
                                <a href="#"><i class="icon_chat_alt"></i> Chats</a>
                            </li>
                            <li>
                                <a href="<%=contextPah%>/seller/logout"><i class="icon_key_alt"></i> Log Out</a>
                            </li>
                            <li>
                                <a href="documentation.html"><i class="icon_key_alt"></i> Documentation</a>
                            </li>
                            <li>
                                <a href="documentation.html"><i class="icon_key_alt"></i> Documentation</a>
                            </li>
                        </ul>
                    </li>
                    <!-- user login dropdown end -->
                </ul>
                <!-- notificatoin dropdown end-->
            </div>
      </header>      
      <!--header end-->

      <!--sidebar start-->
      <aside>
          <div id="sidebar"  class="nav-collapse ">
              <!-- sidebar menu start-->
              <ul class="sidebar-menu">                
                  <li class="">
                      <a class="" href="<%=contextPah%>/seller/sellerdashboard">
                          <i class="fa fa-laptop"></i>
                          <span>Dashboard</span>
                      </a>
                  </li>    
                  <li class="sub-menu">
                      <a href="#" class="dropdown-toggle">
                          <i class="fa fa-shopping-cart"></i>
                          <span>Order</span>
                          <span class="menu-arrow arrow_carrot-right"></span>
                      </a>
                      <ul class="sub">
                      
                      	<%
		                  	if(userrole.equalsIgnoreCase("admin")){
		                 %>
								<li><a class="" href="<%=contextPah%>/adminactivity/searchorder">Process Orders</a></li>
								<li><a class="" href="<%=contextPah%>/seller/placeorder">Placed Order</a></li>
								<li><a class="" href="<%=contextPah%>/order/returnorder">Initiate Refund Order</a></li>
		                 <%		
		                  	}
		                  	else if(userrole.equalsIgnoreCase("seller")){
		                  %>
		                  		  <li><a class="" href="<%=contextPah%>/selleractivity/showpendingorder">Pending Orders</a></li>
		                          <li><a class="" href="<%=contextPah%>/selleractivity/showconfirmorder">Confirm Orders</a></li>
		                          <li><a class="" href="<%=contextPah%>/selleractivity/showdeliveredorder">Delivered Orders</a></li>
		                          <li><a class="" href="<%=contextPah%>/selleractivity/showcancelorder">Cancel Orders</a></li>
		                          <li><a class="" href="<%=contextPah%>/selleractivity/processrefund">Process Refund Orders</a></li>
		                          <li><a class="" href="<%=contextPah%>/selleractivity/searchorder">Search Order</a></li>
<%--                           		  <li><a class="" href="<%=contextPah%>/seller/placeorder">Placed Order</a></li> --%>
		                  <% 		
		                  	}
		                  %>
                          
                          
                      </ul>
                  </li>
                  <%
                  	if(userrole.equalsIgnoreCase("admin")){
                  %>
                  	<li class="">
                      <a class="" href="<%=contextPah%>/adminactivity/processcancelrequest">
                          <i class="fa fa-cogs"></i>
                          <span>Process Request</span>
                      </a>
                  	</li>
                  	<li class="">
                      <a class="" href="<%=contextPah%>/adminactivity/searchSeller">
                          <i class="fa fa-search"></i>
                          <span>Search Seller</span>
                      </a>
                  	</li>
                  	<li class="">
                      <a class="" href="<%=contextPah%>/adminactivity/logrequestactivity">
                          <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
                          <span>Log Request Tracker</span>
                      </a>
                  	</li>
                  <%		
                  	}
                  	else if(userrole.equalsIgnoreCase("seller")){
                   %>
                   <li class="">
                      <a class="" href="<%=contextPah%>/selleractivity/raisecancelrequest">
                          <i class="fa fa-undo" aria-hidden="true"></i>
                          <span>Cancel Request</span>
                      </a>
                  	</li>
                  <% 		
                  	}
                  %>
                  
                  <li class="">
                      <a class="" href="<%=contextPah%>/seller/sellStatistic">
                          <i class="fa fa-bar-chart"></i>
                          <span>Selle Statistic</span>
                      </a>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;" class="">
                          <i class="icon_documents_alt"></i>
                          <span>Customer</span>
                          <span class="menu-arrow arrow_carrot-right"></span>
                      </a>
                      <ul class="sub">                          
                          <li><a class="" href="<%=contextPah%>/seller/registration">Create Profile</a></li>
                          <li><a class="" href="<%=contextPah%>/seller/changepassword"><span>Change Password</span></a></li>
                      </ul>
                  </li>
                  
              </ul>
              <!-- sidebar menu end-->
          </div>
      </aside>
      <!--main content end-->
  </section>
  <!-- container section start -->

    <!-- javascripts -->
<%--     <script src="<c:url value="/resources/JS/jquery.js"/>"></script> --%>
<%-- 	<script src="<c:url value="/resources/JS/jquery-ui-1.10.4.min.js"/>"></script> --%>
<%--     <script src="<c:url value="/resources/JS/jquery-1.8.3.min.js"/>"></script> --%>
<%--     <script type="text/javascript" src="<c:url value="/resources/JS/jquery-ui-1.9.2.custom.min.js"/>"></script> --%>
    
    
    <!-- bootstrap -->
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
	<script src="<c:url value="/resources/JS/searchOrder.js"/>"></script>
	<script src="<c:url value="/resources/JS/sellerservice.js"/>"></script>
  <script>

      //knob
      $(function() {
        $(".knob").knob({
          'draw' : function () { 
            $(this.i).val(this.cv + '%')
          }
        })
      });

      //carousel
      $(document).ready(function() {
          $("#owl-slider").owlCarousel({
              navigation : true,
              slideSpeed : 300,
              paginationSpeed : 400,
              singleItem : true

          });
      });

      //custom select box

      $(function(){
          $('select.styled').customSelect();
      });
	  
	  /* ---------- Map ---------- */
	$(function(){
	  $('#map').vectorMap({
	    map: 'world_mill_en',
	    series: {
	      regions: [{
	        values: gdpData,
	        scale: ['#000', '#000'],
	        normalizeFunction: 'polynomial'
	      }]
	    },
		backgroundColor: '#eef3f7',
	    onLabelShow: function(e, el, code){
	      el.html(el.html()+' (GDP - '+gdpData[code]+')');
	    }
	  });
	});
 </script>

  </body>
</html>