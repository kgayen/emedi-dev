<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Page</title>
<style>
body {
    background-image: url("Image/Medishop.jpg");
}

p
 {
     padding:0;
     margin:0;
 }
ul
 {
     padding:0;
     margin:0;
 }
 li
 {
     padding:0;
     margin:0;
 }
div
 {
     padding:0;
     margin:0;
 }
 nav
 {
     padding:0;
     margin:0;
 }
 body
 {
     font-family:Calibri;
 }

 #menu {
     overflow: auto;
     position:relative;
     z-index:2;
     //padding-top: 150px;
 }

 .parent-menu {
     //background-color: #0c8fff;
     min-width:200px;
     float:left;
     background-color: #58C19A;
 }

/*.parent-sub-menu {
     list-style-type:none;
     background-color: #41CACA;
 }*/

 #menu ul
 {
     list-style-type:none;
 }

 #menu ul li a
 {
     padding:10px 15px;
     display:block;
     color:#fff;
     text-decoration:none;
 }

    #menu ul li a:hover
    {
        background-color:#007ee9;
    }

    #menu ul li:hover > ul {
        left: 200px;
        -webkit-transition: left 200ms ease-in;
        -moz-transition: left 200ms ease-in;
        -ms-transition: left 200ms ease-in;
        transition: left 200ms ease-in;
    }

    #menu ul li > ul {
        position: absolute;
        background-color: #41CACA;
        top: 0;
        left: -200px;
        min-width: 200px;
        z-index: -1;
        height: 100%;
        -webkit-transition: left 200ms ease-in;
        -moz-transition: left 200ms ease-in;
        -ms-transition: left 200ms ease-in;
        transition: left 200ms ease-in;
    }

    #menu ul li ul li > ul  {
        position: absolute;
        background-color: #378394;
        top: 0;
        left: -200px;
        min-width: 200px;
        z-index: -1;
        height: 100%;
        -webkit-transition: left 200ms ease-in;
        -moz-transition: left 200ms ease-in;
        -ms-transition: left 200ms ease-in;
        transition: left 200ms ease-in;
    }

    #menu ul li > ul li a:hover
    {
        background-color:#2e2e2e;
    }
    
#pop-up {
  display: none;
  position: absolute;
  width: 200px;
  padding: 0px;
  background: #95DCA5;
  color: #000000;
  border: 1px solid #1a1a1a;
  font-size: 90%;
}
</style>
<link rel="stylesheet" media="screen" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/styles.css">
<script src="JS/jquery-2.1.4.min.js"></script>
<script src="JS/script.js"></script>
<script>

	$(document).ready(function(){
		$("#pop-up").hide();
		$("#btn-success").click(function(){
			//alert("POP");
			$("#pop-up").show();
			setTimeout(function() {
				$("#pop-up").hide({}, {}, 500)
			}, 5000);
		});
		
	});

</script>
<style type="text/css">
<!--
.style1 {font-size: 10px}
.style2 {font-size: xx-small}
.style3 {font-size: xx-large}
.style4 {font-size: large}
.style5 {font-size: x-large}
#Layer1 {
	position:absolute;
	width:623px;
	height:41px;
	z-index:3;
	left: 340px;
	top: 10px;
}
.style6 {color: #009900}
#ContactLayer {
	position:absolute;
	width:215px;
	height:174px;
	z-index:4;
	left: 1004px;
	top: 9px;
	overflow: auto;
}
#Layer2 {
	position:absolute;
	width:151px;
	height:192px;
	z-index:4;
	left: 1064px;
	top: 10px;
}
#Layer3 {
	position:absolute;
	width:200px;
	height:92px;
	z-index:4;
	left: 1028px;
	top: 9px;
}
#Layer4 {
	position:absolute;
	width:200px;
	height:39px;
	z-index:4;
	left: 1017px;
	top: 9px;
}
-->
</style>
</head>
<body>
<div id="Layer1">
	  <div align="center">
	    <p><span class="style1"><span class="style2"><span class="style3"><span class="style4"><span class="style5"> <span class="style6">
	      <marquee>
	        <strong>Online Medicine Shopping</strong>
          </marquee>
	      </span> </span> </span> </span> </span> </span> 
  </div>
	</div>

	<div id="Layer4">
	  <div align="center"><button type="button" class="btn-success" id="btn-success">Contact</button><div id="pop-up">
<h3>Contact Info</h3>
<p>Online Medical Shopping...</p>
<p>Phone: 1800-258-589</p>
</div></div>
	</div>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	<div id='cssmenu'>
	<ul>
	   <li class='has-sub'><a href='#'><span>Order</span></a>
			<ul>
				 <li><a href='#'><span>Placed Order</span></a></li>
			</ul>
	   </li>
	   <li class='has-sub'><a href='#'><span>Customer</span></a>
		  <ul>
			 <li><a href='#'><span>Registration</span></a></li>
			 <li><a href='#'><span>Login</span></a></li>
			 <li class='last'><a href='#'><span>View Order Details</span></a></li>
		  </ul>
	   </li>
	   <li class='has-sub'><a href='#'><span>Other User</span></a>
		  <ul>
			 <li><a href='#'><span>Seller Login</span></a></li>
			 <li class='last'><a href='#'><span>Admin Login</span></a></li>
		  </ul>
	   </li>
	   <li class='last'><a href='#'><span>Search Medicine Shop</span></a></li>
	</ul>
	</div>
</body>
</html>