$( document ).ready(function() {
	/*
	 * This function used to compare password and confirm password.
	 * If true then OK, otherwise return false
	 */
	
	$("#registration").click(function(){
		var password = $("input[name='user_password']").val();
		var confirmpassword = $("input[name='confirmpassword']").val();
		if(password != confirmpassword){
			alert("Password mismatch !!");
			$("input[name='confirmpassword']").focus();
			return false;
		}
		return true;
	});
	$("input[name='emergencyflag']").click(function(){
		//var emergencyflag = $("input[name='emergencyflag']").attr('checked');
		var emergencyflag = $("input[name='emergencyflag']").prop("checked");
		if(emergencyflag){
			//$("#emergencymsg").html("<p>Emergency charge is <i class='fa fa-inr'></i> 30.00</p>");
			getEmergencyPriceOrderPlaced();
		}
		else{
			$("#emergencymsg").html("");
		}
	});
	
	/*
	 * This function used check number format for keypress event. 
	 * If true then OK, otherwise return false
	 */
	$("input[name='user_mobile'],input[name='user_pincode']").keypress(function (e) {
		var errorMessageId = "";
		if($(this).attr("name") == "user_mobile"){
			errorMessageId = "#mobileerrormsg";
		}
		else if($(this).attr("name") == "user_pincode"){
			errorMessageId = "#pincodeerrormsg";
			$("input[name='emergencyflag']").removeAttr("checked");
			$("#emergencymsg").html("");
		}
	    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	       //display error message
	       $(errorMessageId).html("Digits Only").show().fadeOut("slow");
	       return false;
	    }
	  });
	
	/*
	 * Check old, new and new confirm password
	 */
	$("#changePassword").click(function(){
		var oldpassword = $("input[name='oldpassword']").val();
		var newpassword = $("input[name='newpassword']").val();
		var confirmnewpassword = $("input[name='confirmnewpassword']").val();
		if((oldpassword!= "") &&(newpassword!= "") && (confirmnewpassword!= "") && (newpassword != confirmnewpassword)){
			alert("Password mismatch !!");
			$("input[name='confirmnewpassword']").val("");
			$("input[name='newpassword']").val("");
			$("input[name='confirmnewpassword']").focus();
			return false;
		}
		return true;
	});
	
	$(document).keypress(function(e){
	    if (e.which == 13){
	        $("#search-button").click();
	    }
	});
	
	/*$("#saveorder").click(function(){
		var pincode = $("input[name='user_pincode']").val();
		alert(pincode);
		//var flag = searchPincode(pincode);
		return false;
	});*/

});
var context = $("#contextValue").val();
var taxdata = getTaxDetails();


function blockUI(loadingString){
	top.$.blockUI({
		baseZ:"9999",
		overlayCSS:{
			backgroundColor:"#B8B8B8"
		},//fa-spinner
		message: "<i class='fa fa-spinner fa-spin fa-5x fa-fw margin-bottom'></i> <span class='search-icon-text'><b>  "+loadingString+"</b></span>"
		/*message: "<i class='fa fa-circle-o-notch fa-spin fa-5x'></i>"*/
	});
}

function unblockUI(){
	top.$.unblockUI();
	$(".blockUI").fadeOut("slow"); 
}

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};


function loadSearchResults(userid){
	blockUI("Loading Orders...");
	var confirmBar = "<div class='progress-bar progress-bar-info'  style='width: 18.27%;padding-right: 50px;'></div>";
	var pendingBar = "<div class='progress-bar progress-bar-warning'  style='width: 18.26%;padding-right: 50px;'></div>";
	var cancelBar = "<div class='progress-bar progress-bar-danger'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliverBar = "<div class='progress-bar progress-bar-success'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliveryTaxData = getTaxValues("DeliveryTax");
	var emergencyTaxData = getTaxValues("EmergencyTax");
	setTimeout(function () {
		$.ajax({
			url: context+"/orderservice/rest/getOrderByUser/"+userid,
			type: "GET",
			contentType : "application/json",
			dataType: "json",
			async: false,
			success: function(data){
				if(data != null){
					if(data.length>0){
						//--------------------------
						//--------------------------
						$.each(data, function(key,orderObject) {
							var d = new Date(orderObject.order.orderCreateDate);
							var formattedDate = d.getFullYear() + "-" + (((d.getMonth() + 1) <10) ? "0" + (d.getMonth() + 1):(d.getMonth() + 1)) + "-" + ((d.getDate()<10)?"0" +d.getDate():d.getDate());
							var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
							var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
							var seconds = (d.getSeconds() < 10) ? "0" + d.getSeconds() : d.getSeconds();
							var formattedTime = hours + ":" + minutes + ":" + seconds;
							formattedDate = formattedDate + " " + formattedTime;
							var price = orderObject.order.orderprice;
							price = price.toString();
							if(price.length > 0 && price.indexOf(".") == -1){
								price = price+".00";
							}
							/*---------Start: Order Status Bar creation----------*/
							var orderStatusList = orderObject.orderstatus;
							var placeDateTime = dateConvertFormatter(orderStatusList[0].orderActionTimestamp);
							var orderStatusBar = "<a title='"+placeDateTime+"' href='#'><span class='label label-default'>Placed Order</span></a>";
							var orderStatusCnt;
							var prevStatus = "";
							for(orderStatusCnt = 0; orderStatusCnt<orderStatusList.length;orderStatusCnt++){
								var orderStatusObj = orderStatusList[orderStatusCnt];
								var statusText = "";
								var bar = "";
								var dataValue = dateConvertFormatter(orderStatusObj.orderActionTimestamp);
								if(orderStatusObj.orderstatus == "Confirm"){
									statusText = "<a title='"+dataValue+"' href='#'><span class='label label-primary'>Confirmed</span></a>";
									bar = confirmBar;
								}
								else if(orderStatusObj.orderstatus == "Pending"){
									statusText = "<a title='"+dataValue+"' href='#'><span class='label label-warning'>Pending</span></a>";
									bar = pendingBar;
								}
								else if(orderStatusObj.orderstatus == "Cancel"){
									bar = cancelBar;
									statusText = "<a title='"+dataValue+"' href='#'><span class='label label-danger'>Cancelled</span></a>";
									if(prevStatus == "Pending"){
										bar = "<div class='progress-bar progress-bar-danger'  style='width: 48%;padding-right: 50px;'></div>";
									}
								}
								else if(orderStatusObj.orderstatus == "Deliver"){
									statusText = "<a title='"+dataValue+"' href='#'><span class='label label-success'>Delivered</span></a>";
									bar = deliverBar;
								}
								prevStatus = orderStatusObj.orderstatus;
								orderStatusBar = orderStatusBar + bar;
								orderStatusBar = orderStatusBar + statusText;
							}
							/*-----------End: Order Status Bar creation----------*/
							
							/*-----------Start: Order Status link creation----------*/
							var orderStatusLink = "";
							if(orderObject.order.orderstatus == "Pending"){
								orderStatusLink = "<a href='#orderStatus"+orderObject.order.orderid+"' class='label label-warning' data-toggle='modal'>"+orderObject.order.orderstatus+"</a>";
							}
							else if(orderObject.order.orderstatus == "Confirm"){
								orderStatusLink = "<a href='#orderStatus"+orderObject.order.orderid+"' class='label label-primary' data-toggle='modal'>"+orderObject.order.orderstatus+"ed</a>";
							}
							else if(orderObject.order.orderstatus == "Deliver"){
								orderStatusLink = "<a href='#orderStatus"+orderObject.order.orderid+"' class='label label-success' data-toggle='modal'>"+orderObject.order.orderstatus+"ed</a>";
							}
							else if(orderObject.order.orderstatus == "Cancel"){
								orderStatusLink = "<a href='#orderStatus"+orderObject.order.orderid+"' class='label label-danger' data-toggle='modal'>"+orderObject.order.orderstatus+"led</a>";
							}
							/*-----------End: Order Status link creation----------*/
							
							/*-----------Start Tax Details------------------------*/ 
							var sellerTaxData = getTaxValues(orderObject.seller.taxCategory);
							var totalAmountHtml = "";
							var taxbreakupHTML = "";
							if((orderObject.order.orderstatus == "Confirm" || orderObject.order.orderstatus == "Deliver") && parseFloat(price)>0.00 && sellerTaxData.length >0 && deliveryTaxData.length >0){
								taxbreakupHTML = taxbreakupHTML.concat('<table style="font-size: 12px !important;" class="table table-bordered"><thead><tr><th>Product Name</th><th>Tax Type(Tax %)</th><th>Amount</th></tr></thead><tbody>');
								var totalGross = 0.00;
								var sum = "";
								if(orderObject.seller.taxType == "exclude"){
									var amountAfterDiscount = getAmountAfterDiscount(price,orderObject.order.orderDiscountAmount);
									sum = amountAfterDiscount;
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Medicine Price(Exclude Tax)</b></td><td>Net Amount</td><td>'+amountAfterDiscount+'</td></tr>');
									for(var i = 0; i<sellerTaxData.length;i++){
										var sellerTaxDataObj = sellerTaxData[i];
										var rowSpanTD = "";
										if(i == 0){
											rowSpanTD = '<td rowspan="'+(sellerTaxData.length+1)+'"></td>';
										}
										var taxValue = roundToTwo(getTaxAmount(parseFloat(sellerTaxDataObj["taxValue"]),parseFloat(amountAfterDiscount)));
										taxbreakupHTML = taxbreakupHTML.concat('<tr>'+rowSpanTD+'<td>'+sellerTaxDataObj["taxType"]+' ('+sellerTaxDataObj["taxValue"]+'%)</td><td>'+taxValue+'</td></tr>');
										sum = parseFloat(sum)+parseFloat(taxValue);
									}
									totalGross = parseFloat(roundToTwo(sum))+parseFloat(totalGross);
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Gross Amount</b></td><td>'+roundToTwo(sum)+'</td></tr>');
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td colspan = "3"></td></tr>');
								}
								else if(orderObject.seller.taxType == "include"){
									var amountAfterDiscount = getAmountAfterDiscount(price,orderObject.order.orderDiscountAmount);
									var netAmount = getNetAmount(amountAfterDiscount,sellerTaxData);
									sum = netAmount;
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Medicine Price(Exclude Tax)</b></td><td>Net Amount</td><td>'+netAmount+'</td></tr>');
									for(var i = 0; i<sellerTaxData.length;i++){
										var sellerTaxDataObj = sellerTaxData[i];
										var rowSpanTD = "";
										if(i == 0){
											rowSpanTD = '<td rowspan="'+(sellerTaxData.length+1)+'"></td>';
										}
										var taxValue = roundToTwo(getTaxAmount(parseFloat(sellerTaxDataObj["taxValue"]),parseFloat(netAmount)));
										taxbreakupHTML = taxbreakupHTML.concat('<tr>'+rowSpanTD+'<td>'+sellerTaxDataObj["taxType"]+' ('+sellerTaxDataObj["taxValue"]+'%)</td><td>'+taxValue+'</td></tr>');
										sum = parseFloat(sum)+parseFloat(taxValue);
									}
									totalGross = parseFloat(roundToTwo(sum))+parseFloat(totalGross);
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Gross Amount</b></td><td>'+roundToTwo(sum)+'</td></tr>');
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td colspan = "3"></td></tr>');
									//alert();
								}
								var deliveryPriceNet = getNetAmount(orderObject.order.orderDeliveryAmount,deliveryTaxData);
								sum = deliveryPriceNet;
								taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Delivery Price(Exclude Tax)</b></td><td>Net Amount</td><td>'+deliveryPriceNet+'</td></tr>');
								for(var i = 0; i<deliveryTaxData.length;i++){
									var deliveryTaxDataObj = deliveryTaxData[i];
									var rowSpanTD = "";
									if(i == 0){
										rowSpanTD = '<td rowspan="'+(deliveryTaxData.length+1)+'"></td>';
									}
									var taxValue = roundToTwo(getTaxAmount(parseFloat(deliveryTaxDataObj["taxValue"]),parseFloat(deliveryPriceNet)));
									taxbreakupHTML = taxbreakupHTML.concat('<tr>'+rowSpanTD+'<td>'+deliveryTaxDataObj["taxType"]+' ('+deliveryTaxDataObj["taxValue"]+'%)</td><td>'+taxValue+'</td></tr>');
									sum = parseFloat(sum)+parseFloat(taxValue);
								}
								taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Gross Amount</b></td><td>'+roundToTwo(sum)+'</td></tr>');
								totalGross = parseFloat(roundToTwo(sum))+parseFloat(totalGross);
								if(parseInt(orderObject.order.emergencyFlag) == 1 && parseInt(orderObject.order.emergencyPrice)>0.00){
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td colspan = "3"></td></tr>');
									var emergencyPriceNet = getNetAmount(orderObject.order.emergencyPrice,emergencyTaxData);
									sum = emergencyPriceNet;
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Emergency Price(Exclude Tax)</b></td><td>Net Amount</td><td>'+emergencyPriceNet+'</td></tr>');
									for(var i = 0; i<emergencyTaxData.length;i++){
										var emergencyTaxDataObj = emergencyTaxData[i];
										var rowSpanTD = "";
										if(i == 0){
											rowSpanTD = '<td rowspan="'+(emergencyTaxData.length+1)+'"></td>';
										}
										var taxValue = roundToTwo(getTaxAmount(parseFloat(emergencyTaxDataObj["taxValue"]),parseFloat(emergencyPriceNet)));
										taxbreakupHTML = taxbreakupHTML.concat('<tr>'+rowSpanTD+'<td>'+emergencyTaxDataObj["taxType"]+' ('+emergencyTaxDataObj["taxValue"]+'%)</td><td>'+taxValue+'</td></tr>');
										sum = parseFloat(sum)+parseFloat(taxValue);
									}
									totalGross = parseFloat(roundToTwo(sum))+parseFloat(totalGross);
									taxbreakupHTML = taxbreakupHTML.concat('<tr><td><b>Gross Amount</b></td><td>'+roundToTwo(sum)+'</td></tr>');
								}
								taxbreakupHTML = taxbreakupHTML.concat('<tr><td colspan = "2"><b>Total Gross Amount (Payable Amount)</b></td><td><b>'+roundToTwo(totalGross)+'</b></td></tr></tbody></table>');
								totalAmountHtml = '<div class="form-group"> <label for="cname" class="control-label col-lg-2">Payable Amount:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+roundToTwo(totalGross)+'</span><a title="View Tax Breakup" data-toggle="modal" href="#taxbreakup'+orderObject.order.orderid+'">Tax Breakup</a> </strong> </div> </div>';
							}
							$('#Order_TaxBreakup_View').append('<div class="modal fade" id="taxbreakup'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title"><b>Tax Breakup</b></h4> </div> <div class="modal-body"><div class="form"> '+taxbreakupHTML+'  </div></div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
						 /*------------End Tax Details------------------------*/
							var emergencyPricetext = "";
							emergencyPricetext = priceCoverter(orderObject.order.emergencyPrice);
							$('#userOrderList').append('<tr><td>'+orderObject.order.orderid+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+price+'</td><td>'+orderObject.order.orderPincode+'</td><td>'+orderObject.user.user_mobile+'</td><td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a><a class="btn btn-success" title="Edit Order" data-toggle="modal" href="#editorderdetails'+orderObject.order.orderid+'"><i class="fa fa-pencil"></i></a><a class="btn btn-danger" title="Cancel Order" data-toggle="modal" href="#orderCancel'+orderObject.order.orderid+'"><i class="icon_close_alt2"></i></a></div></td></tr>');
							//$('#Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button class="btn btn-success" type="button" data-toggle="modal" href="#editorderdetails'+orderObject.order.orderid+'" title="Edit Order">Edit Order</button><button class="btn btn-success" type="button" data-toggle="modal" href="#invoice'+orderObject.order.orderid+'" title="Download Order Invoice">Invoice</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
							$('#Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button class="btn btn-success" type="button" data-toggle="modal" href="#editorderdetails'+orderObject.order.orderid+'" title="Edit Order">Edit Order</button><button class="btn btn-success" type="button" data-toggle="modal" data-dismiss="modal" href="" title="Order Invoice" onclick="downloadInvoice('+orderObject.order.orderid+')">Invoice</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
							var orderEditHtml = '<div class="modal fade" id="editorderdetails'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order status is '+orderObject.order.orderstatus+'</h4> </div> <div class="modal-body"> <div class="alert alert-block alert-danger fade in"> <button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> <strong>Oh!</strong> The order status is <strong>'+orderObject.order.orderstatus+'</strong> and you can change Order only when Order status is <strong>Pending</strong>. </div> </div> <div class="modal-footer"> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
							var orderCancelHtml = '<div class="modal fade" id="orderCancel'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order status is '+orderObject.order.orderstatus+'</h4> </div> <div class="modal-body"> <div class="alert alert-block alert-danger fade in"> <button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> <strong>Oh!</strong> The order status is <strong>'+orderObject.order.orderstatus+'</strong> and you can change Order only when Order status is <strong>Pending</strong>. </div> </div> <div class="modal-footer"> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
							if(orderObject.order.orderstatus == "Pending"){
								var emergencyFlag = "";
								var emergencyPrice = "";
								if(orderObject.order.emergencyFlag == 1){
									emergencyFlag = "checked = \"checked\"";
									var emPrice = priceCoverter(orderObject.order.emergencyPrice);
									emergencyPrice = "<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+emPrice+"</p>";
								}
								var emPrice = priceCoverter(orderObject.order.emergencyPrice);
								orderEditHtml = '<div class="modal fade" id="editorderdetails'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Edit Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal" id="cust_order_edit_form_'+orderObject.order.orderid+'" action="POST" enctype="multipart/form-data"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <input class="form-control" type="text" id="cust_order_edit_orderid_'+orderObject.order.orderid+'" value = "'+orderObject.order.orderid+'" readonly="readonly"/> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Address </label> <div class="col-lg-10"><input class="form-control" id="cust_order_edit_shippedAddress_'+orderObject.order.orderid+'" type="text" min="5" name="address" value="'+orderObject.order.shippingAddress+'"/></div></div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode </label> <div class="col-lg-10"><input class="form-control" id="cust_order_edit_pincode_'+orderObject.order.orderid+'" type="text" maxlength="6" name="pincode" value="'+orderObject.order.orderPincode+'" onkeyup="getEmergencyPrice('+orderObject.order.orderid+')"/></div></div><div class="form-group"> <label for="ccomment" class="control-label col-lg-2">Emergency </label> <div class="col-lg-10"><label class="checkbox-inline"><input type="checkbox" id="cust_order_edit_emergencyflag_'+orderObject.order.orderid+'" name="emergencyflag" onclick="showEmergencyPrice('+orderObject.order.orderid+')" '+emergencyFlag+'/><input id="cust_order_orginal_pincode_'+orderObject.order.orderid+'" type="text" hidden="hidden" value="'+orderObject.order.orderPincode+'"/><input id="cust_order_orginal_emergency_price_'+orderObject.order.orderid+'" type="text" hidden="hidden" value="'+emPrice+'"/><input id="cust_order_emergency_flag_'+orderObject.order.orderid+'" type="text" hidden="hidden" value="'+orderObject.order.emergencyFlag+'"/><strong id="emergencymsg'+orderObject.order.orderid+'">'+emergencyPrice+'</strong><span id="cust_order_edit_emergencyerror_'+orderObject.order.orderid+'"></span></label></div></div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <textarea class="form-control" name="ordereditdetails" id="cust_order_edit_details_'+orderObject.order.orderid+'">'+orderObject.order.orderdetails+'</textarea> </div> </div> <div class="form-group"> <label for="ccomment" class="control-label col-lg-2">Prescipetion Image:</label> <div class="col-lg-10"> <input class="form-control" type="file" name="orderpecimage" id="cust_order_edit_pecimage_'+orderObject.order.orderid+'"/> </div> </div> </form> </div> </div> <div class="modal-footer"> <button class="btn btn-success" data-dismiss="modal" id="cust_order_edit_button_'+orderObject.order.orderid+'" type="button" onclick="editOrderByCustomer('+orderObject.order.orderid+')">Save Changes</button> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
								orderCancelHtml = '<div class="modal fade" id="orderCancel'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Order['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_order_cancel_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+orderObject.order.orderid+' order?</strong></div></form></div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-danger" id="cust_order_cancel_button_'+orderObject.order.orderid+'" type="button" onclick="cancelOrder('+orderObject.order.orderid+')">Cancel Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>';
							}
							$('#Order_Edit_Details').append(orderEditHtml);
							var prescriptionImageSrc = context+"/orderservice/image/getimage/"+orderObject.order.orderid;
							var prescriptionDownloadLink = "'"+context+"\/orderservice\/image\/downloadimage\/"+orderObject.order.orderid+"'";
							prescriptionDownloadLink = prescriptionDownloadLink.toString();
							$('#Order_Image_View').append('<div class="modal fade" id="viewprescription'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Prescription Image</h4> </div> <div class="modal-body"> <img id="prescribtion_image_view" style="width: 547px;height: 700px" src="'+prescriptionImageSrc+'"></img> </div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-success" type="button" title="Download Prescripton" onclick="window.location.href='+prescriptionDownloadLink+'"><i class="fa fa-cloud-download"> </i> Download Image</button> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>');
							$('#Order_Status_View').append('<div class="modal fade" id="orderStatus'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Status['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="progress progress-sm">'+orderStatusBar+'</div><div>Cancellation Note: '+orderObject.order.cancellationCmd+'</div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
							$('#Order_Cancel_View').append(orderCancelHtml);
						});
					}
				}
				else{
					$("#errormsg").html("<span class='glyphicon glyphicon-remove-circle'>" +
					" </span><b> No Order Available!</b>");
				}
				
			},
			error: function(){
				$("#errormsg").html("<span class='glyphicon glyphicon-remove-circle'>" +
				" </span><b> Error Occurs During Fetching Order!</b>");
			}
		},unblockUI());
		
	},200);
	//searchUser(getUrlParameter('query'));
}

function loadDashboardStats(){
	//$("#successErrorMessage").css("display","none");
	blockUI("Loading Orders...");
	setTimeout(function () {
		$.ajax({
			url: contextPath+"/getDashboardStats",
			type: "GET",
			contentType : "application/json",
			dataType: "json",
			success: function(data){
				$("#userCount").html(data.userRegistrationCount);
				$("#assetCount").html(data.assetRegistrationCount);
				$("#searchCount").html(data.searchCount);
				$("#pageHits").html(data.pageHitCounter);
			},
			error: function(){
				$("#successErrorMessage").html("<span class='glyphicon glyphicon-remove-circle'><b> Failure: </b>" +
						"</span> Oops something's not right here!");
				$("#successErrorMessage").css("display","block");
				$("#successErrorMessage").addClass("alert alert-danger");
			}
		},unblockUI());
	},200);
}

function loadDiv(){
	 //window.location.href = "#editorderdetails1008";
	 alert("OK");
}

function searchPincodeNewOrder(){
	var pincode=$("input[name=user_pincode]").val();
	//alert("searchPincode::::"+pincode);
	var flag = true;
	$.ajax({
		url: context+"/sellerrestservice/getSellerByPincode/"+pincode,
		type: "GET",
		contentType : "application/json",
		dataType: "json",
		async: false,
		success: function(data){
			if(data != null){
				if(data.length==1){
					flag = orderConfirmation("Shop is available and discount percentage is "+data[0].sellerDiscount);
				}
				else if(data.length>1){
					$("#errormsg").html("<span class='glyphicon glyphicon-remove-circle'>" +
					" </span><b> Shop Not Available!</b>");
					$(".row").focus();
					flag ==  false;
				}
			}
			else{
				$("#errormsg").html("<span class='glyphicon glyphicon-remove-circle'>" +
				" </span><b> Shop Not Available!</b>");
				$(".row").focus();
				flag =  false;
			}
		},
		error: function(){
			$("#errormsg").html("<span class='glyphicon glyphicon-remove-circle'>" +
			" </span><b> Shop Not Available!</b>");
			$("#errormsg").focus();
			flag =  false;
		}
	});
	return flag;
}

function searchPincode(pincode){
	var flag = true;
	$.ajax({
		url: context+"/sellerrestservice/getSellerByPincode/"+pincode,
		type: "GET",
		contentType : "application/json",
		dataType: "json",
		async: false,
		success: function(data){
			if(data != null){
				if(data.length==1){
					flag = true;
				}
				else if(data.length>1){
					flag ==  false;
				}
			}
			else{
				flag =  false;
			}
		},
		error: function(){
			flag =  false;
		}
	});
	return flag;
}

function orderConfirmation(message) {
	var docHeight = $(document).height();
	$("body").append("<div id='overlay'></div>");
	$("#overlay")
    .height(docHeight)
    .css({
       'opacity' : 0.4,
       'position': 'absolute',
       'top': 0,
       'left': 0,
       'background-color':'#B8B8B8',
       'width': '100%',
       'z-index': 5000
    });
    if (confirm(message) == true) {
        return true;
    } else {
    	$("#overlay").fadeOut("slow");
    	$("#overlay").remove();
    	return false;
    }
}

function showEmergencyPrice(orderid){
	var emergencyflag = $("input[id='cust_order_edit_emergencyflag_"+orderid+"']").prop("checked");
	var pincode = $("input[id='cust_order_edit_pincode_"+orderid+"']").val();
	var pincodeOrgVal = $("input[id='cust_order_orginal_pincode_"+orderid+"']").val();
	var emergencyFlagVal = $("input[id='cust_order_emergency_flag_"+orderid+"']").val();
	//if((emergencyflag == "checked") && (pincode.trim()!="")){
	//alert(emergencyflag);
	if(emergencyflag && (pincode.trim()!="")){
		if((pincode.trim().length == 6) && (emergencyFlagVal == "0")){
			blockUI("Loading Emergency Price...");
			setTimeout(function () {
				$.ajax({
					url: context+"/sellerrestservice/getSellerByPincode/"+pincode,
					type: "GET",
					contentType : "application/json",
					dataType: "json",
					async: false,
					success: function(data){
						if(data != null){
							if(data.length==1){
								$("#emergencymsg"+orderid).html("<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+data[0].sellerEmergencyPrice+"</p>");
							}
							else if(data.length>1){
								$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
								" </span><b> Shop Not Available!</b></p>");
								$(".row").focus();
							}
						}
						else{
							$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
							" </span><b> Shop Not Available in "+pincode+" pincode!</b></p>");
							$(".row").focus();
						}
					},
					error: function(){
						$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
						" </span><b> OOps! Received error.</b></p>");
						$("#errormsg").focus();
					}
				},unblockUI());
			},200);
				
		}
		if((pincode.trim().length == 6) && (emergencyFlagVal == "1")&& (pincode !=pincodeOrgVal)){
			blockUI("Loading Emergency Price...");
			setTimeout(function () {
				$.ajax({
					url: context+"/sellerrestservice/getSellerByPincode/"+pincode,
					type: "GET",
					contentType : "application/json",
					dataType: "json",
					async: false,
					success: function(data){
						if(data != null){
							if(data.length==1){
								$("#emergencymsg"+orderid).html("<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+data[0].sellerEmergencyPrice+"</p>");
							}
							else if(data.length>1){
								$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
								" </span><b> Shop Not Available!</b></p>");
								$(".row").focus();
							}
						}
						else{
							$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
							" </span><b> Shop Not Available in "+pincode+" pincode!</b></p>");
							$(".row").focus();
						}
					},
					error: function(){
						$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
						" </span><b> OOps! Received error.</b></p>");
						$("#errormsg").focus();
					}
				},unblockUI());
			},200);
		}
		else if((pincode.trim().length == 6) && (pincode ==pincodeOrgVal) && (emergencyFlagVal == "1")){
			var pincodeOrgEmergencyPriceVal = $("input[id='cust_order_orginal_emergency_price_"+orderid+"']").val();
			$("#emergencymsg"+orderid).html("<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+pincodeOrgEmergencyPriceVal+"</p>");
		}
		else if((pincode.trim().length != 6)){
			$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
			" </span><b> Please provide 7 digits pincode</b></p>");
			$(".row").focus();
		}
	}
	//else if((emergencyflag == "checked") && (pincode.trim()=="")){
	else if((emergencyflag) && (pincode =="")){
		$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
		" </span><b> Please provide corrct pincode</b></p>");
		$(".row").focus();
	}
	else{
		$("#emergencymsg"+orderid).html("");
		$("#cust_order_edit_emergencyerror_"+orderid).html("");
	}
}

function getEmergencyPrice(orderid){
	var pincode = $("input[id='cust_order_edit_pincode_"+orderid+"']").val();
	pincode = pincode.trim();
	//var emergencyflag = $("input[id='cust_order_edit_emergencyflag_"+orderid+"']").attr('checked');
	var emergencyflag = $("input[id='cust_order_edit_emergencyflag_"+orderid+"']").prop("checked");
	$("#cust_order_edit_emergencyerror_"+orderid).html("");
	$("#emergencymsg"+orderid).html("");
	var pincodeOrgVal = $("input[id='cust_order_orginal_pincode_"+orderid+"']").val();
	var emergencyFlagVal = $("input[id='cust_order_emergency_flag_"+orderid+"']").val();
	//if(pincode.length == 6 && emergencyflag == "checked"){
	if(pincode.length == 6 && emergencyflag){
		if((emergencyFlagVal == "1")&& (pincode !=pincodeOrgVal)){
			blockUI("Loading Emergency Price...");
			setTimeout(function () {
				$.ajax({
					url: context+"/sellerrestservice/getSellerByPincode/"+pincode,
					type: "GET",
					contentType : "application/json",
					dataType: "json",
					async: false,
					success: function(data){
						if(data != null){
							if(data.length==1){
								$("#emergencymsg"+orderid).html("<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+data[0].sellerEmergencyPrice+"</p>");
							}
							else if(data.length>1){
								$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
										" </span><b> Shop Not Available in "+pincode+" pincode!</b></p>");
								$(".row").focus();
							}
						}
						else{
							$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
									" </span><b> Shop Not Available in "+pincode+" pincode!</b></p>");
							$(".row").focus();
						}
					},
					error: function(){
						$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
						" </span><b> OOps! Received error.</b></p>");
						$("#errormsg").focus();
					}
				},unblockUI());
			},200);
		}
		else if(emergencyFlagVal == "0"){
			blockUI("Loading Emergency Price...");
			setTimeout(function () {
				$.ajax({
					url: context+"/sellerrestservice/getSellerByPincode/"+pincode,
					type: "GET",
					contentType : "application/json",
					dataType: "json",
					async: false,
					success: function(data){
						if(data != null){
							if(data.length==1){
								$("#emergencymsg"+orderid).html("<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+data[0].sellerEmergencyPrice+"</p>");
							}
							else if(data.length>1){
								$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
										" </span><b> Shop Not Available in "+pincode+" pincode!</b></p>");
								$(".row").focus();
							}
						}
						else{
							$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
									" </span><b> Shop Not Available in "+pincode+" pincode!</b></p>");
							$(".row").focus();
						}
					},
					error: function(){
						$("#cust_order_edit_emergencyerror_"+orderid).html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
						" </span><b> OOps! Received error.</b></p>");
						$("#errormsg").focus();
					}
				},unblockUI());
			},200);
		}
		else if((emergencyFlagVal == "1")&& (pincode == pincodeOrgVal)){
			var pincodeOrgEmergencyPriceVal = $("input[id='cust_order_orginal_emergency_price_"+orderid+"']").val();
			$("#emergencymsg"+orderid).html("<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+pincodeOrgEmergencyPriceVal+"</p>");
		}
	}
}

function dateConvertFormatter(timestame){
	var d = new Date(timestame);
	var formattedDate = d.getFullYear() + "-" + (((d.getMonth() + 1) <10) ? "0" + (d.getMonth() + 1):(d.getMonth() + 1)) + "-" + ((d.getDate()<10)?"0" +d.getDate():d.getDate());
	var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
	var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
	var seconds = (d.getSeconds() < 10) ? "0" + d.getSeconds() : d.getSeconds();
	var formattedTime = hours + ":" + minutes + ":" + seconds;
	formattedDate = formattedDate + " " + formattedTime;
	return formattedDate;
}

function editOrderByCustomer(orderid){
	var formId = "#cust_order_edit_form_"+orderid;
    var pincodeValue = $(formId+" input[id=cust_order_edit_pincode_"+orderid+"]").val();
    $('#errorAlertMessage').text("");
    
    if(pincodeValue == undefined || pincodeValue == "" || pincodeValue.length == -1){
		$('#errorAlertMessage').text(" Pincode field should not be blank.");
		$('#successModifiedErrorAlert').show();
		setTimeout(function () {
			$('#successModifiedErrorAlert').hide();
		},5000);
		return false;
    }
    else{
    	var pincodeFlag = searchPincode(pincodeValue);
    	if(!pincodeFlag){
    		$('#errorAlertMessage').text(" Shop is not available on this pincode.");
			$('#successModifiedErrorAlert').show();
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},5000);
			return false;
    	}
    }
    
    blockUI("Saving Orders...");
    var imgFile = document.getElementById("cust_order_edit_pecimage_"+orderid);//
    var imgfileList = imgFile.files;
    var formdata = new FormData();
    if (imgfileList && imgfileList != null && imgfileList.length > 0) {
        formdata.append("editPrescriptionImg", imgfileList[0]);
    }
    var other_data = $(formId+' :input').serializeArray();
    $.each(other_data, function (key, input) {
        formdata.append(input.name, input.value);
    });
    //var emergencyflag = $(formId+" input[name=emergencyflag]").val();
    var emergencyPrice = $(formId+" p[id=emergencyflag]").text();
    emergencyPrice = emergencyPrice.match(/\d+[\.]\d+|\d+$/);
    if(emergencyPrice != null){
    	emergencyPrice = emergencyPrice[0];
    }
    else{
    	emergencyPrice = "0.00";
    }
    formdata.append("emergencyprice", emergencyPrice);
    formdata.append("orderid", orderid);
    setTimeout(function () {
	    $.ajax({
			url: context+"/orderservice/modifyCustOrder",
			type: "POST",
			contentType: false,
			processData: false,
			data:formdata,
			async: false,
			success: function(data){
				if(data.indexOf("SUCCESS")!=-1){
					$("#successAlertMessage").html(data);
					$("#successModifiedAlert").show();
		            setTimeout(function() {
		            		$("#successModifiedAlert").hide();
		            		window.location= context+"/order/showuserorder";
		            	},
		            	5000
		            );
				}
				else{
					$("#errorAlertMessage").html(data);
					$("#successModifiedErrorAlert").show();
		            setTimeout(function() {
		            		$("#successModifiedErrorAlert").hide();
		            	},
		            	5000
		            );
				}
			},
			error: function(data){
				$("#errorAlertMessage").html("ERROR: Error occured during modification.");
				$("#successModifiedErrorAlert").show();
	            setTimeout(function() {
	            		$("#successModifiedErrorAlert").hide();
	            	},
	            	5000
	            );
			}
		},unblockUI());
    },200);
}

function priceCoverter(price){
	price = price.toString();
	if(price.length == 1 && price.indexOf(".") == -1){
		price = price+".00";
	}
	return price;
}

function cancelOrder(orderid){
	blockUI("Cancel Orders...");
	var formdata = new FormData();
	formdata.append("orderid", orderid);
	setTimeout(function () {
	    $.ajax({
			url: context+"/orderservice/cancelOrder",
			type: "POST",
			contentType: false,
			processData: false,
			data:formdata,
			async: false,
			success: function(data){
				if(data.indexOf("SUCCESS")!=-1){
					$("#successAlertMessage").html(data);
					$("#successModifiedAlert").show();
		            setTimeout(function() {
		            		$("#successModifiedAlert").hide();
		            		window.location= context+"/order/showuserorder";
		            	},
		            	5000
		            );
				}
				else{
					var errormsg = data["error"];
					errormsg = errormsg[0].errorMsg;
					$("#errorAlertMessage").html(errormsg);
					$("#successModifiedErrorAlert").show();
		            setTimeout(function() {
		            		$("#successModifiedErrorAlert").hide();
		            	},
		            	5000
		            );
				}
			},
			error: function(data){
				$("#errorAlertMessage").html("ERROR: Error occured during modification.");
				$("#successModifiedErrorAlert").show();
	            setTimeout(function() {
	            		$("#successModifiedErrorAlert").hide();
	            	},
	            	5000
	            );
			}
		},unblockUI());
	},200);
}

function getEmergencyPriceOrderPlaced(){
	var emergencyflag = $("input[name='emergencyflag']").prop("checked");
	var pincode = $("input[name='user_pincode").val();
	if(pincode=="" || pincode==undefined || pincode==null){
		$("#emergencymsg").html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
		" </span><b> Please enter pincode.</b></p>");
		$(".row").focus();
		return false;
	}
	if(emergencyflag && (pincode.trim()!="")){
		//if((pincode.trim().length == 6) && (emergencyFlagVal == "0")){
		blockUI("Loading Emergency Price...");
		setTimeout(function () {
			$.ajax({
				url: context+"/sellerrestservice/getSellerByPincode/"+pincode,
				type: "GET",
				contentType : "application/json",
				dataType: "json",
				async: false,
				success: function(data){
					if(data != null){
						if(data.length==1){
							$("#emergencymsg").html("<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+data[0].sellerEmergencyPrice+"</p>");
						}
						else if(data.length>1){
							$("#emergencymsg").html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
							" </span><b> Shop Not Available!</b></p>");
							$(".row").focus();
						}
					}
					else{
						$("#emergencymsg").html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
						" </span><b> Shop Not Available in "+pincode+" pincode!</b></p>");
						$(".row").focus();
					}
				},
				error: function(){
					$("#emergencymsg").html("<p id='errormsg'><span class='glyphicon glyphicon-remove-circle'>" +
					" </span><b> OOps! Received error.</b></p>");
					$("#errormsg").focus();
				}
			},unblockUI());
		},200);
	}
}

function getTaxDetails(){
	var taxdataValue = "";
	$.ajax({
		url: context+"/tax/getTaxDetails",
		type: "GET",
		contentType : "application/json",
		dataType: "json",
		async: false,
		success: function(data){
			if(data != null || data == undefined){
				taxdataValue = data;
			}
		},
		error: function(){
			alert("Error in retriveing tax.");
			taxdataValue = "";
		}
	});
	//alert("taxdataValue: "+taxdataValue);
	return taxdataValue;
}