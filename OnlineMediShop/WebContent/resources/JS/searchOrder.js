$( document ).ready(function() {
	$('#order_Search_Table').dataTable();
	
	$('#datetimepicker1').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#datetimepicker2').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#datetimepicker3').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#datetimepicker4').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$("#orderSearchByDate").click(function(){
		var fromDate = $("input[name='fromDate']").val();
		var toDate = $("input[name='toDate']").val();
		fromDate = new Date(fromDate);
		fromDate = fromDate.getTime();
		toDate = new Date(toDate);
		toDate = toDate.getTime();
		if(fromDate>=toDate){
			$("#errorAlertMessage").text("From date should not be greater than or equal to todate");
			$('#successModifiedErrorAlert').show();
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
		else{
			//alert("OK");
			loadOrderSearchResult("ByDateRange",fromDate,toDate,null,null,null);
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
		
	});
	
	$("textarea[name='searchorderID']").keypress(function (e) {
	    if (e.which != 8 && e.which != 0 && e.which != 44 && (e.which < 48 || e.which > 57)) {
	       alert("Only Comma(,) accepted!");
	       return false;
	    }
	  });
	
	$("#orderSearchByID").click(function(){
		$('#errorChar').html("");
		var orderIdList = $("textarea[name='searchorderID']").val();
		orderIdList = orderIdList.trim();
		if(orderIdList.charAt(0) === ","){
			orderIdList = orderIdList.substring(1,orderIdList.length);
		}
		if(orderIdList.charAt(orderIdList.length-1) == ","){
			orderIdList = orderIdList.substring(0,orderIdList.length-1);
		}
		var checkCharFlag = true;
		var eventValue = "";
		for(var i=0;i<orderIdList.length;i++){
			eventValue = orderIdList.charAt(i);
			if (eventValue != ',' && !(/[0-9]/.test(eventValue))) {
				checkCharFlag = false;
				i = orderIdList.length;
			}
		}
		if(!checkCharFlag){
			$('#mobileerrormsg').html("</br><b>"+eventValue + " </b>is invalid character to search. Please use comma as a separator.").show();
			setTimeout(function () {
				$('#mobileerrormsg').hide();
			},10000);
			return false;
		}
		if(checkCharFlag){
			orderIdList = orderIdList.split(",");
			loadOrderSearchResult("ByOrderID",null,null,orderIdList,null,null);
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
	});
	
	$("#orderSearchByAdvance").click(function(){
		var optionValue = $("input[name='option']:checked").val();
		if(optionValue == undefined || optionValue == "" || optionValue == null){
			$('#advanceErrorMessageSpan').html("Please, choose one option.").show();
			$('#advanceErrorMessage').show();
			setTimeout(function () {
				$('#advanceErrorMessage').hide();
			},10000);
			return false;
		}
		else{
			//alert(optionValue);
			var statusValues = $("input[name='statusOption']:checked");
			if(statusValues.length == 0){
				$('#advanceErrorMessageSpan').html("Please, choose atleast one status option.").show();
				$('#advanceErrorMessage').show();
				setTimeout(function () {
					$('#advanceErrorMessage').hide();
				},10000);
				return false;
			}
			else{
				var statusValuesArray = new Array();
				for(var i = 0;i<statusValues.length;i++){
					statusValuesArray[i] = statusValues[i].value;
				}
				if(optionValue == "ByDateOtion"){
					var fromDate = $("input[name='startDate']").val();
					var toDate = $("input[name='endDate']").val();
					fromDate = new Date(fromDate);
					fromDate = fromDate.getTime();
					toDate = new Date(toDate);
					toDate = toDate.getTime();
					if(fromDate>=toDate){
						$("#advanceErrorMessageSpan").text("From date should not be greater than or equal to todate");
						$('#advanceErrorMessage').show();
						setTimeout(function () {
							$('#advanceErrorMessage').hide();
						},3000);
						return false;
					}
					else{
						//alert("OK");
						loadOrderSearchResult("ByAdvanceSearch",fromDate,toDate,null,statusValuesArray,new Array("ByDateOtion"));
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},3000);
					}
				}
				else if(optionValue == "ByOrderIdOtion"){
					var orderIdList = $("textarea[name='advanceSearchorderID']").val();
					orderIdList = orderIdList.trim();
					if(orderIdList.charAt(0) === ","){
						orderIdList = orderIdList.substring(1,orderIdList.length);
					}
					if(orderIdList.charAt(orderIdList.length-1) == ","){
						orderIdList = orderIdList.substring(0,orderIdList.length-1);
					}
					var checkCharFlag = true;
					var eventValue = "";
					for(var i=0;i<orderIdList.length;i++){
						eventValue = orderIdList.charAt(i);
						if (eventValue != ',' && !(/[0-9]/.test(eventValue))) {
							checkCharFlag = false;
							i = orderIdList.length;
						}
					}
					if(!checkCharFlag){
						$('#advanceErrorMessageSpan').html("<b>"+eventValue + " </b>is invalid character to search. Please use comma as a separator.");
						$('#advanceErrorMessage').show();
						setTimeout(function () {
							$('#advanceErrorMessage').hide();
						},10000);
						return false;
					}
					if(checkCharFlag){
						orderIdList = orderIdList.split(",");
						loadOrderSearchResult("ByAdvanceSearch",null,null,orderIdList,statusValuesArray,new Array("ByOrderIdOtion"));
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},3000);
					}
				}
			}
		}		
	});
	
	$("#ByDateOtion").click(function(){
		$('#dateRange').show();
		$('#orderid').hide();
	});
	
	$("#ByOrderIdOtion").click(function(){
		$('#dateRange').hide();
		$('#orderid').show();
	});
	
});

var context = $("#contextValue").val();

function loadOrderSearchResult(orderSearchType,fromDate,toDate,orderIdList,orderStatusList,
		orderFetchedBy){
	blockUI("Searching Orders...");
	var postObject = new Object();
	postObject.orderSearchType = orderSearchType;
	postObject.fromDate = fromDate;
	postObject.toDate = toDate;
	postObject.orderIdList = orderIdList;
	postObject.orderStatusList = orderStatusList;
	postObject.orderFetchedBy = orderFetchedBy;
	queryStr = JSON.stringify(postObject);
	var confirmBar = "<div class='progress-bar progress-bar-info'  style='width: 18.27%;padding-right: 50px;'></div>";
	var pendingBar = "<div class='progress-bar progress-bar-warning'  style='width: 18.26%;padding-right: 50px;'></div>";
	var cancelBar = "<div class='progress-bar progress-bar-danger'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliverBar = "<div class='progress-bar progress-bar-success'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliveryTaxData = getTaxValues("DeliveryTax");
	var emergencyTaxData = getTaxValues("EmergencyTax");
	//alert("deliveryTaxData:::"+JSON.stringify(deliveryTaxData));
	$('#Order_Search_Details').html("");
	setTimeout(function () {
		$.ajax({
			url: context+"/orderservice/searchOrder",
			type: "POST",
			data: queryStr,
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.length == undefined && data["error"].length>0){
					$("#errorAlertMessage").html(data.error[0].errorMsg);
					$('#successModifiedErrorAlert').show();
				}
				else{
					if(data != null){
						if(data.length>0){
							var htmlFile = '<header class="panel-heading"> Search Orders </header><table id="order_Search_Table" class="table table-striped table-advance table-hover"> <thead> <tr> <th><i class="icon_check"></i> Order Id</th><th><i class="icon_calendar"></i> Date</th><th><i class="fa fa-dot-circle-o"></i> Status</th> <th><i class="fa fa-inr"></i> Price</th><th><i class="icon_pin_alt"></i> Pincode</th> <th><i class="fa fa-mobile"></i> Mobile</th> <th><i class="icon_cogs"></i> Action</th> </tr> </thead> <tbody>';
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
								htmlFile = htmlFile.concat('<tr><td>'+orderObject.order.orderid+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+price+'</td><td>'+orderObject.order.orderPincode+'</td><td>'+orderObject.user.user_mobile+'</td><td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a><a class="btn btn-success" title="Edit Order" data-toggle="modal" href="#editorderdetails'+orderObject.order.orderid+'"><i class="fa fa-pencil"></i></a><a class="btn btn-danger" title="Cancel Order" data-toggle="modal" href="#orderCancel'+orderObject.order.orderid+'"><i class="icon_close_alt2"></i></a></div></td></tr>');
								//$('#Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button class="btn btn-success" type="button" data-toggle="modal" href="#editorderdetails'+orderObject.order.orderid+'" title="Edit Order">Edit Order</button><button class="btn btn-success" type="button" data-toggle="modal" href="#invoice'+orderObject.order.orderid+'" title="Download Order Invoice">Invoice</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
								$('#Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button class="btn btn-success" type="button" data-toggle="modal" href="#editorderdetails'+orderObject.order.orderid+'" title="Edit Order">Edit Order</button><button class="btn btn-success" type="button" data-toggle="modal" data-dismiss="modal" href="" title="Order Invoice" onclick="downloadInvoice('+orderObject.order.orderid+')">Invoice</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
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
							htmlFile = htmlFile.concat('</tbody> </table>');
							$('#Order_Search_Details').html(htmlFile);
							$('#order_Search_Table').dataTable();
						}
					}
					else{
						$("#errorAlertMessage").html("No Order Available!");
						$('#successModifiedErrorAlert').show();
					}
				}
			},
			error: function(){
				$("#errorAlertMessage").html("Error Occurs During Fetching Order!");
				$('#successModifiedErrorAlert').show();
			}
		},unblockUI());
	},200);
}


function getTaxValues(taxCategory){
	var taxInfo = taxdata["taxInfoMap"];
	taxInfo = taxInfo[taxCategory];
	var output = [], item;
	for (var type in taxInfo) {
	    item = {};
	    item.taxType = type;
	    item.taxValue = taxInfo[type];
	    output.push(item);
	}
	return output;
}

function getNetAmount(grossAmount,taxArray){
	var taxsum = 100;
	for(var taxCnt = 0; taxCnt<taxArray.length;taxCnt++){
		var taxDataObj = taxArray[taxCnt];
		taxsum = parseFloat(taxsum)+parseFloat(taxDataObj["taxValue"]);
	}
	var netAmount = ((grossAmount*100)/taxsum);
	return roundToTwo(netAmount);
}

function getAmountAfterDiscount(amount,discountPercentage){
	var amountAfterDiscount = (parseFloat(amount)-(parseFloat(amount)*(discountPercentage/100)));
	return roundToTwo(amountAfterDiscount);
}

function getTaxAmount(taxPercentage,amount){
	var taxamount = (amount*(taxPercentage/100));
	return taxamount;
}

function roundToTwo(number){
	return (+(Math.round(number+"e+2")+"e-2")).toFixed(2);
}

function downloadInvoice(orderid){
	var htmlLink = context+"/invoicegenerator/generateInvoice?orderno="+orderid;
	$.ajax({
		  dataType: 'native',
		  url: htmlLink,
		  xhrFields: {
		    responseType: 'blob',
		  },
		  success: function(response, status, xhr){
			  console.log(response.size);
			  var filename = "";
              var disposition = xhr.getResponseHeader("Content-Disposition");
              if (disposition && disposition.indexOf("filename") != -1) {
                  var filenameRegex = /filename[^;=\n]*=(([""]).*?\2|[^;\n]*)/;
                  var matches = filenameRegex.exec(disposition);
                  if (matches != null && matches[1]){
                      filename = matches[1].replace(/[""]/g, "");
                  }
              }
              
              if(filename != ""){
            	  var link=document.createElement('a');
			      link.href=window.URL.createObjectURL(response);
			      link.download=filename;
			      link.click();
              }
              else{
					var responseText = {};
					var blob = new Blob([response], { type: 'plain/text', endings: 'native' });
					var reader = new FileReader();
					reader.onload = function(event){
						responseText = reader.result;
						responseText = JSON.parse(responseText);
						if(responseText["error"] != undefined && responseText["error"].length>0){
							$("#errorAlertMessage").html(responseText.error[0].errorMsg);
							$('#successModifiedErrorAlert').show();
							$('#successModifiedErrorAlert').focus();
							setTimeout(function () {
								$('#successModifiedErrorAlert').hide();
							},5000);
					  	}
					};
					reader.readAsText(blob);
              }
		  },
		  error: function(){
			  $("#errorAlertMessage").html("Error has been occured when generating PDF.");
				$('#successModifiedErrorAlert').show();
				$('#successModifiedErrorAlert').focus();
				setTimeout(function () {
					$('#successModifiedErrorAlert').hide();
				},5000);
		  }
		});
}