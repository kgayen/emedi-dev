$( document ).ready(function() {
	$('#seller_order_Search_Table').dataTable();
	
	$('#seller_datetimepicker1').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#seller_datetimepicker2').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#seller_datetimepicker3').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#seller_datetimepicker4').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#seller_cancelrequest_fromdate_datetimepicker').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#seller_cancelrequest_todate_datetimepicker').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$("#cancelRequestByDateOtion").click(function(){
		$('#canccelRequestDateRange').show();
		$('#cancelRequestById').hide();
	});
	
	$("#cancelRequestByOrderIdOtion").click(function(){
		$('#canccelRequestDateRange').hide();
		$('#cancelRequestById').show();
	});
	
	//--------------------------- Search Cancel Request -----------------------
	$("#cancelRequestSearchByAdvance").click(function(){
		var optionValue = $("input[name='option']:checked").val();
		var searchForm = $("#cancelRequestSearchByAdvance").val();
		var requester = $("#user_id").val();
		if(optionValue == undefined || optionValue == "" || optionValue == null){
			$('#cancelRequestAdvanceErrorMessageSpan').html("Please, choose one option.").show();
			$('#cancelRequestAdvanceErrorMessage').show();
			setTimeout(function () {
				$('#cancelRequestAdvanceErrorMessage').hide();
			},10000);
			return false;
		}
		else{
			var statusValues = $("input[name='cancelRequestStatusOption']:checked");
			if(statusValues.length == 0){
				$('#cancelRequestAdvanceErrorMessageSpan').html("Please, choose atleast one status option.").show();
				$('#cancelRequestAdvanceErrorMessage').show();
				setTimeout(function () {
					$('#cancelRequestAdvanceErrorMessage').hide();
				},10000);
				return false;
			}
			else{
				var statusValuesArray = new Array();
				for(var i = 0;i<statusValues.length;i++){
					statusValuesArray[i] = statusValues[i].value;
				}
				if(optionValue == "cancelRequestByDateOtion"){
					var fromDate = $("input[name='cancelRequeststartDate']").val();
					var toDate = $("input[name='cancelRequestEndDate']").val();
					if(fromDate == "" || fromDate == null || fromDate == undefined){
						$("#cancelRequestAdvanceErrorMessageSpan").text("Please enter From Date");
						$('#cancelRequestAdvanceErrorMessage').show();
						setTimeout(function () {
							$('#cancelRequestAdvanceErrorMessage').hide();
						},3000);
						return false;
					}
					if(toDate == "" || toDate == null || toDate == undefined){
						$("#cancelRequestAdvanceErrorMessageSpan").text("Please enter To Date");
						$('#cancelRequestAdvanceErrorMessage').show();
						setTimeout(function () {
							$('#cancelRequestAdvanceErrorMessage').hide();
						},3000);
						return false;
					}
					fromDate = new Date(fromDate);
					fromDate = fromDate.getTime();
					toDate = new Date(toDate);
					toDate = toDate.getTime();
					
					if(fromDate>=toDate){
						$("#cancelRequestAdvanceErrorMessageSpan").text("From date should not be greater than or equal to todate");
						$('#cancelRequestAdvanceErrorMessage').show();
						setTimeout(function () {
							$('#cancelRequestAdvanceErrorMessage').hide();
						},3000);
						return false;
					}
					else{
						//loadRefundOrders("ByAdvanceSearch",fromDate,toDate,null,statusValuesArray,new Array("refundByDateOtion"));
						
						if(searchForm == "Seller_CancelRequestSearch"){
							loadCancelRequest(requester,fromDate,toDate,"advance",statusValuesArray,null);
						}
						if(searchForm == "Admin_CancelRequestSearch"){
							loadCancelRequest(requester,fromDate,toDate,"advance",statusValuesArray,null);
						}
						
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},3000);
					}
				}
				else if(optionValue == "cancelRequestByOrderIdOtion"){
					var orderIdList = $("textarea[name='cancelRequestAdvanceSearchorderID']").val();
					orderIdList = orderIdList.trim();
					if(orderIdList.charAt(0) === ","){
						orderIdList = orderIdList.substring(1,orderIdList.length);
					}
					if(orderIdList.charAt(orderIdList.length-1) == ","){
						orderIdList = orderIdList.substring(0,orderIdList.length-1);
					}
					var checkCharFlag = true;
					/*var eventValue = "";
					for(var i=0;i<orderIdList.length;i++){
						eventValue = orderIdList.charAt(i);
						if (eventValue != ',' && !(/[0-9]/.test(eventValue))) {
							checkCharFlag = false;
							i = orderIdList.length;
						}
					}*/
					/*if(!checkCharFlag){
						$('#cancelRequestAdvanceErrorMessageSpan').html("<b>"+eventValue + " </b>is invalid character to search. Please use comma as a separator.");
						$('#cancelRequestAdvanceErrorMessage').show();
						setTimeout(function () {
							$('#cancelRequestAdvanceErrorMessage').hide();
						},10000);
						return false;
					}*/
					if(checkCharFlag){
						orderIdList = orderIdList.split(",");
						
						if(searchForm == "Seller_CancelRequestSearch"){
							loadCancelRequest(requester,null,null,"advance",statusValuesArray,orderIdList);
						}
						if(searchForm == "Admin_CancelRequestSearch"){
							loadCancelRequest(requester,null,null,"advance",statusValuesArray,orderIdList);
						}
						
						
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},3000);
					}
				}
			}
		}		
	});
	//-------------------------------------------------------------------------
	
	//--------------------------- Pending Order --------------------------------
	$("#seller_orderSearchByDate").click(function(){
		var fromDate = $("input[name='seller_fromDate']").val();
		var toDate = $("input[name='seller_toDate']").val();
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
			loadSellerSearchResult("ByDateRange",fromDate,toDate,null,null,null,'Pending');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
		
	});
	
	$("#seller_orderSearchByID").click(function(){
		$('#errorChar').html("");
		var orderIdList = $("textarea[name='seller_searchorderID']").val();
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
			loadSellerSearchResult("ByOrderID",null,null,orderIdList,null,null,'Pending');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
	});
	//----------------------------------------------------------------------
	
	//------------------------ Confirm Order -------------------------------
	$("#seller_confirmOrderSearchByDate").click(function(){
		var fromDate = $("input[name='seller_fromDate']").val();
		var toDate = $("input[name='seller_toDate']").val();
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
			loadSellerSearchResult("ByDateRange",fromDate,toDate,null,null,null,'Confirm');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
		
	});
	
	$("#seller_confirmOrderSearchByID").click(function(){
		$('#errorChar').html("");
		var orderIdList = $("textarea[name='seller_searchorderID']").val();
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
			loadSellerSearchResult("ByOrderID",null,null,orderIdList,null,null,'Confirm');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
	});
	//----------------------------------------------------------------------
	
	//------------------------ Deliver Order -------------------------------
	$("#seller_deliverOrderSearchByDate").click(function(){
		var fromDate = $("input[name='seller_fromDate']").val();
		var toDate = $("input[name='seller_toDate']").val();
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
			loadSellerSearchResult("ByDateRange",fromDate,toDate,null,null,null,'Deliver');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
		
	});
	
	$("#seller_deliverOrderSearchByID").click(function(){
		$('#errorChar').html("");
		var orderIdList = $("textarea[name='seller_searchorderID']").val();
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
			loadSellerSearchResult("ByOrderID",null,null,orderIdList,null,null,'Deliver');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
	});
	//----------------------------------------------------------------------
	
	//------------------------ Cancel Order -------------------------------
	$("#seller_cancelOrderSearchByDate").click(function(){
		var fromDate = $("input[name='seller_fromDate']").val();
		var toDate = $("input[name='seller_toDate']").val();
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
			loadSellerSearchResult("ByDateRange",fromDate,toDate,null,null,null,'Cancel');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
		
	});
	
	$("#seller_cancelOrderSearchByID").click(function(){
		$('#errorChar').html("");
		var orderIdList = $("textarea[name='seller_searchorderID']").val();
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
			loadSellerSearchResult("ByOrderID",null,null,orderIdList,null,null,'Cancel');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
	});
	//----------------------------------------------------------------------
	
	$("textarea[name='seller_searchorderID']").keypress(function (e) {
	    if (e.which != 8 && e.which != 0 && e.which != 44 && (e.which < 48 || e.which > 57)) {
	       alert("Only Comma(,) accepted!");
	       return false;
	    }
	  });
	
	$("#seller_orderSearchByID").click(function(){
		$('#errorChar').html("");
		var orderIdList = $("textarea[name='seller_searchorderID']").val();
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
			loadSellerSearchResult("ByOrderID",null,null,orderIdList,null,null,'Pending');
			setTimeout(function () {
				$('#successModifiedErrorAlert').hide();
			},3000);
		}
	});
	
	$("#seller_orderSearchByAdvance").click(function(){
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
	
	
	$("input[id^='sellerconfirmOrder']").click(function(){
		alert("Checkbox id : "+this.id);
		//$("#action_button_row").show();
	});
	
	$("#seller_bulkdeliver_button").click(function(){
		var ids = $("input[id^='sellerconfirmOrder']:checked");
		var bulkDeliverArr = [];
		var i = 0;
        $(ids).each(function () {
        	var orderidString = $(this).attr("id").split("_")[1];
        	bulkDeliverArr[i++] = Number(orderidString);
        });
		sellerDeliverOrder(bulkDeliverArr);
	});
	
	$("#seller_bulkcancelrequest_button").click(function(){
		var ids = $("input[id^='sellerconfirmOrder']:checked");
		var bulkCancelReqArr = [];
		var i = 0;
        $(ids).each(function () {
        	var orderidString = $(this).attr("id").split("_")[1];
        	bulkCancelReqArr[i++] = Number(orderidString);
        });
		sellerCancelOrderRequest(bulkCancelReqArr);
	});
	
	//------------------------ Sells Report -------------------------------
	$("#seller_report_searchByDate").click(function(){
		var fromDate = $("input[name='seller_report_fromDate']").val();
		var toDate = $("input[name='seller_report_toDate']").val();
		var sellerId = $("#reported_seller_id").val();
		loadSellerStatisticData(sellerId,fromDate,toDate);
	});
	
	$("#seller_export_report").click(function(){
		var fromDate = $("input[name='seller_report_fromDate']").val();
		var toDate = $("input[name='seller_report_toDate']").val();
		var sellerId = $("#reported_seller_id").val();
		var flag = true;
		if(fromDate == null || fromDate == undefined || fromDate == ""){
			$('#refundRequestErrorMessage').html();
			$('#refundRequestErrorMessage').html("Please enter From Date.");
			$('#refundRequestErrorDiv').show();
			setTimeout(function () {
				$('#refundRequestErrorDiv').hide();
				$("input[name='seller_report_fromDate']").focus();
			},4000);
			flag = false;
			return false;
		}
		
		if(toDate == null || toDate == undefined || toDate == ""){
			$('#refundRequestErrorMessage').html();
			$('#refundRequestErrorMessage').html("Please enter To Date.");
			$('#refundRequestErrorDiv').show();
			setTimeout(function () {
				$('#refundRequestErrorDiv').hide();
				$("input[name='seller_report_toDate']").focus();
			},4000);
			flag = false;
			return false;
		}
		
		if(flag){
			downloadSellerReport(sellerId,fromDate,toDate);
		}
	});
	//-------------------------------------------------------------------
	
	$("#load_order_status").click(function(){
		//alert("Checkbox id : "+this.id);
		loadOrderStatus(this.value,null);
		//$("#action_button_row").show();
	});
	
});

var context = $("#contextValue").val();

function blockUIForSellerMessage(loadingString,status){
	var finalMessage="";
	if(status == "SUCCESS"){
		finalMessage="<i class='fa fa-check-square-o'></i> <strong id='successAlertMessage'>"+loadingString+"</strong>";
	}
	else if(status == "FAILED"){
		finalMessage="<i class='fa fa-exclamation'></i> <strong id='errorAlertMessage'>"+loadingString+"</strong>";
	}
	top.$.blockUI({
		baseZ:"9999",
		overlayCSS:{
			backgroundColor:"#B8B8B8"
		},
		message: finalMessage
	});
}

function unblockUIForSellerMessage(){
	top.$.unblockUI();
	$(".blockUIForMessage").fadeOut("slow"); 
}


function loadSellerOrderResult(sellerId,orderstatus){
	blockUI("Loading Pending Orders...");
	var confirmBar = "<div class='progress-bar progress-bar-info'  style='width: 18.27%;padding-right: 50px;'></div>";
	var pendingBar = "<div class='progress-bar progress-bar-warning'  style='width: 18.26%;padding-right: 50px;'></div>";
	var cancelBar = "<div class='progress-bar progress-bar-danger'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliverBar = "<div class='progress-bar progress-bar-success'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliveryTaxData = getTaxValues("DeliveryTax");
	var emergencyTaxData = getTaxValues("EmergencyTax");
	//alert("deliveryTaxData:::"+JSON.stringify(deliveryTaxData));
	$('#Seller_Order_Search_Details').html("");
	setTimeout(function () {
		$.ajax({
			url: context+"/orderservice/rest/getOrderBySeller/"+sellerId,
			type: "GET",
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
							var htmlFile = '<header class="panel-heading"> Pending Orders </header><table id="order_Search_Table" class="table table-striped table-advance table-hover"> <thead> <tr> <th><i class="icon_check"></i> Order Id</th><th><i class="icon_calendar"></i> Date</th><th><i class="fa fa-dot-circle-o"></i> Status</th> <th><i class="fa fa-inr"></i> Price</th><th><i class="icon_pin_alt"></i> Pincode</th> <th><i class="fa fa-mobile"></i> Mobile</th> <th><i class="icon_cogs"></i> Action</th> </tr> </thead> <tbody>';
							$.each(data, function(key,orderObject) {
								//=============== Pending Order =================
								if(orderstatus.toUpperCase() == orderObject.order.orderstatus.toUpperCase()){
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
										$('#Seller_Order_TaxBreakup_View').append('<div class="modal fade" id="taxbreakup'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title"><b>Tax Breakup</b></h4> </div> <div class="modal-body"><div class="form"> '+taxbreakupHTML+'  </div></div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									 /*------------End Tax Details------------------------*/
									var emergencyPricetext = "";
									emergencyPricetext = priceCoverter(orderObject.order.emergencyPrice);
									var wrappedMobile = "";
									var mobilenoStrLen = orderObject.user.user_mobile.length;
									wrappedMobile = orderObject.user.user_mobile.substring((mobilenoStrLen-4), mobilenoStrLen);
									htmlFile = htmlFile.concat('<tr><td>'+orderObject.order.orderid+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+price+'</td><td>'+orderObject.order.orderPincode+'</td><td>XXXXXX'+wrappedMobile+'</td><td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a><a class="btn btn-success" title="Confirm Order" data-toggle="modal" href="#confirmOrder'+orderObject.order.orderid+'"><i class="fa fa-thumbs-o-up"></i></a><a class="btn btn-danger" title="Cancel Order" data-toggle="modal" href="#orderCancel'+orderObject.order.orderid+'"><i class="icon_close_alt2"></i></a></div></td></tr>');
									$('#Seller_Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile'+orderObject.order.orderid+'">XXXXXX'+wrappedMobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button class="btn btn-success" type="button" data-toggle="modal" href="#confirmOrder'+orderObject.order.orderid+'" title="Confirm Order">Confirm Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									var orderConfirmHtml = '';//'<div class="modal fade" id="confirmOrder'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order status is '+orderObject.order.orderstatus+'</h4> </div> <div class="modal-body"> <div class="alert alert-block alert-danger fade in"> <button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> <strong>Oh!</strong> The order status is <strong>'+orderObject.order.orderstatus+'</strong> and you can change Order only when Order status is <strong>Pending</strong>. </div> </div> <div class="modal-footer"> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
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
										orderConfirmHtml = '<div class="modal fade" id="confirmOrder'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Confirm Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal" id="cust_order_confirm_form_'+orderObject.order.orderid+'" action="POST" enctype="multipart/form-data"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <input class="form-control" type="text" id="cust_order_edit_orderid_'+orderObject.order.orderid+'" value = "'+orderObject.order.orderid+'" readonly="readonly"/> </div> </div><div class="form-group" style="padding-top: 20px"> <label for="cname" class="control-label col-lg-2">Price Without Discount<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:20px"><input class="form-control" id="cust_order_confirm_price_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter Order Price" name="order_confirm_price" value="" required="required"/></div></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2">Cashmemo No.<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="cust_order_confirm_cashmemo_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter Cashmemo Number" name="confirm_cashmemo" value="" required="required"/></div></div> </form> </div> </div> <div class="modal-footer"> <button class="btn btn-success" data-dismiss="modal" id="cust_order_confirm_button_'+orderObject.order.orderid+'" type="button" onclick="confirmOrderBySeller('+orderObject.order.orderid+')">Confirm Order</button> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
										//orderCancelHtml = '<div class="modal fade" id="orderCancel'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Order['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_order_cancel_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+orderObject.order.orderid+' order?</strong></div></form></div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-danger" id="cust_order_cancel_button_'+orderObject.order.orderid+'" type="button" onclick="cancelOrder('+orderObject.order.orderid+')">Cancel Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>';
										orderCancelHtml = '<div class="modal fade" id="orderCancel'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Order['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-validate form-horizontal" id="cust_order_cancel_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+orderObject.order.orderid+' order?</strong></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2">Cancellation Note<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="seller_cancellation_note_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter cancellation Note" name="cancellation_Note" required="required"></div></div><div class="form-group" id="returnErrorDiv'+orderObject.order.orderid+'" style="display: none"><label for="cname" class="control-label col-lg-2"></label> <div class="col-lg-10"><span id="returnErrorMessage'+orderObject.order.orderid+'" style="color: #FF0000; font-family: Lato, sans-serif"></span></div></div></form></div></div><div class="modal-footer"><button class="btn btn-danger" id="seller_order_cancel_button_'+orderObject.order.orderid+'" type="button" onclick="sellerCancelOrder('+orderObject.order.orderid+')">Cancel Order</button><button data-dismiss="modal" id="seller_order_cancel_close_button_'+orderObject.order.orderid+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
									}
									$('#Seller_Order_Confirm_Details').append(orderConfirmHtml);
									var prescriptionImageSrc = context+"/orderservice/image/getimage/"+orderObject.order.orderid;
									var prescriptionDownloadLink = "'"+context+"\/orderservice\/image\/downloadimage\/"+orderObject.order.orderid+"'";
									prescriptionDownloadLink = prescriptionDownloadLink.toString();
									$('#Seller_Order_Image_View').append('<div class="modal fade" id="viewprescription'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Prescription Image</h4> </div> <div class="modal-body"> <img id="prescribtion_image_view" style="width: 547px;height: 700px" src="'+prescriptionImageSrc+'"></img> </div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-success" type="button" title="Download Prescripton" onclick="window.location.href='+prescriptionDownloadLink+'"><i class="fa fa-cloud-download"> </i> Download Image</button> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>');
									$('#Seller_Order_Status_View').append('<div class="modal fade" id="orderStatus'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Status['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="progress progress-sm">'+orderStatusBar+'</div><div>Cancellation Note: '+orderObject.order.cancellationCmd+'</div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									$('#Seller_Order_Cancel_View').append(orderCancelHtml);
								}
								//=============== End Pending Order =================	
							});
							htmlFile = htmlFile.concat('</tbody> </table>');
							$('#Seller_Order_Search_Details').html(htmlFile);
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


function loadSellerSearchResult(orderSearchType,fromDate,toDate,orderIdList,orderStatusList,
		orderFetchedBy,orderstatus){
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
	$('#Seller_Order_Search_Details').html("");
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
							var orderstatusHeader = "Pending";
							if(orderstatus == "Confirm"){
								orderstatusHeader = "Confirmed";
							}
							else if(orderstatus == "Deliver"){
								orderstatusHeader = "Delivered";
							}
							else if(orderstatus == "Cancel"){
								orderstatusHeader = "Cancelled";
							}
							var htmlFile = '<header class="panel-heading"> '+orderstatusHeader+' Orders </header><table id="order_Search_Table" class="table table-striped table-advance table-hover"> <thead> <tr> <th><i class="icon_check"></i> Order Id</th><th><i class="icon_calendar"></i> Date</th><th><i class="fa fa-dot-circle-o"></i> Status</th> <th><i class="fa fa-inr"></i> Price</th><th><i class="icon_pin_alt"></i> Pincode</th> <th><i class="fa fa-mobile"></i> Mobile</th> <th><i class="icon_cogs"></i> Action</th> </tr> </thead> <tbody>';
							$.each(data, function(key,orderObject) {
								//=============== Pending Order =================
								if(orderstatus.toUpperCase() == orderObject.order.orderstatus.toUpperCase()){
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
										$('#Seller_Order_TaxBreakup_View').append('<div class="modal fade" id="taxbreakup'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title"><b>Tax Breakup</b></h4> </div> <div class="modal-body"><div class="form"> '+taxbreakupHTML+'  </div></div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									 /*------------End Tax Details------------------------*/
									var emergencyPricetext = "";
									emergencyPricetext = priceCoverter(orderObject.order.emergencyPrice);
									var wrappedMobile = "";
									var orderCheckbox = "";
									var mobilenoStrLen = orderObject.user.user_mobile.length;
									wrappedMobile = 'XXXXXX'+orderObject.user.user_mobile.substring((mobilenoStrLen-4), mobilenoStrLen);
									if(orderstatus == "Confirm"){
										wrappedMobile = orderObject.user.user_mobile;
										orderCheckbox = '<input type="checkbox" id="sellerconfirmOrder_'+orderObject.order.orderid+'">    ';
									}
									else if(orderstatus == "Cancel"){
										wrappedMobile = orderObject.user.user_mobile;
									}
									else if(orderstatus == "Deliver"){
										wrappedMobile = orderObject.user.user_mobile;
									}
									var actionTab = '<td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a><a class="btn btn-success" title="Confirm Order" data-toggle="modal" href="#confirmOrder'+orderObject.order.orderid+'"><i class="fa fa-thumbs-o-up"></i></a><a class="btn btn-danger" title="Cancel Order" data-toggle="modal" href="#orderCancel'+orderObject.order.orderid+'"><i class="icon_close_alt2"></i></a></div></td>';
									if(orderstatus == "Confirm"){
										actionTab = '<td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a><a class="btn btn-success" title="Deliver Order" data-toggle="modal" href="#deliverOrder'+orderObject.order.orderid+'"><i class="fa fa-check-square-o"></i></a><a class="btn btn-danger" title="Cancel Request" data-toggle="modal" href="#sellerCancelRequest'+orderObject.order.orderid+'"><i class="fa fa-undo"></i></a></div></td>';
									}
									else if(orderstatus == "Cancel"){
										actionTab = '<td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a></div></td>';
									}
									else if(orderstatus == "Deliver"){
										actionTab = '<td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a></div></td>';
									}
									
									htmlFile = htmlFile.concat('<tr><td>'+orderCheckbox+orderObject.order.orderid+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+price+'</td><td>'+orderObject.order.orderPincode+'</td><td>'+wrappedMobile+'</td>'+actionTab+'</tr>');
									
									if(orderObject.order.orderstatus == "Confirm"){
										$('#Seller_Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile'+orderObject.order.orderid+'">XXXXXX'+wrappedMobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button class="btn btn-success" type="button" data-toggle="modal" href="#confirmOrder'+orderObject.order.orderid+'" title="Confirm Order">Confirm Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									}
									else if(orderObject.order.orderstatus == "Deliver"){
										$('#Seller_Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile'+orderObject.order.orderid+'">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									}
									else if(orderObject.order.orderstatus == "Cancel"){
										$('#Seller_Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile'+orderObject.order.orderid+'">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									}
									
									var orderConfirmHtml = '';//'<div class="modal fade" id="confirmOrder'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order status is '+orderObject.order.orderstatus+'</h4> </div> <div class="modal-body"> <div class="alert alert-block alert-danger fade in"> <button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> <strong>Oh!</strong> The order status is <strong>'+orderObject.order.orderstatus+'</strong> and you can change Order only when Order status is <strong>Pending</strong>. </div> </div> <div class="modal-footer"> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
									var orderCancelHtml = '';
									var orderCancelRequestHtml = '';
									if(orderObject.order.orderstatus == "Pending"){
										var emergencyFlag = "";
										var emergencyPrice = "";
										if(orderObject.order.emergencyFlag == 1){
											emergencyFlag = "checked = \"checked\"";
											var emPrice = priceCoverter(orderObject.order.emergencyPrice);
											emergencyPrice = "<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+emPrice+"</p>";
										}
										var emPrice = priceCoverter(orderObject.order.emergencyPrice);
										orderConfirmHtml = '<div class="modal fade" id="confirmOrder'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Confirm Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal" id="cust_order_confirm_form_'+orderObject.order.orderid+'" action="POST" enctype="multipart/form-data"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <input class="form-control" type="text" id="cust_order_edit_orderid_'+orderObject.order.orderid+'" value = "'+orderObject.order.orderid+'" readonly="readonly"/> </div> </div><div class="form-group" style="padding-top: 20px"> <label for="cname" class="control-label col-lg-2">Price Without Discount<span class="required">*</span></label> <div class="col-lg-10" style="padding-top:20px"><input class="form-control" id="cust_order_confirm_price_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter Order Price" name="order_confirm_price" value="" required="required"/></div></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2">Cashmemo No. <span class="required">*</span></label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="cust_order_confirm_cashmemo_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter Cashmemo Number" name="confirm_cashmemo" value="" required="required"/></div></div> </form> </div> </div> <div class="modal-footer"> <button class="btn btn-success" data-dismiss="modal" id="cust_order_confirm_button_'+orderObject.order.orderid+'" type="button" onclick="confirmOrderBySeller('+orderObject.order.orderid+')">Confirm Order</button> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
										//orderCancelHtml = '<div class="modal fade" id="orderCancel'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Order['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_order_cancel_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+orderObject.order.orderid+' order?</strong></div></form></div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-danger" id="cust_order_cancel_button_'+orderObject.order.orderid+'" type="button" onclick="cancelOrder('+orderObject.order.orderid+')">Cancel Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>';
										orderCancelHtml = '<div class="modal fade" id="orderCancel'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Order['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-validate form-horizontal" id="cust_order_cancel_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+orderObject.order.orderid+' order?</strong></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2">Cancellation Note<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="seller_cancellation_note_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter cancellation Note" name="cancellation_Note" required="required"></div></div><div class="form-group" id="returnErrorDiv'+orderObject.order.orderid+'" style="display: none"><label for="cname" class="control-label col-lg-2"></label> <div class="col-lg-10"><span id="returnErrorMessage'+orderObject.order.orderid+'" style="color: #FF0000; font-family: Lato, sans-serif"></span></div></div></form></div></div><div class="modal-footer"><button class="btn btn-danger" id="seller_order_cancel_button_'+orderObject.order.orderid+'" type="button" onclick="sellerCancelOrder('+orderObject.order.orderid+')">Cancel Order</button><button data-dismiss="modal" id="seller_order_cancel_close_button_'+orderObject.order.orderid+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
									}
									if(orderObject.order.orderstatus == "Confirm"){
										//alert("Confirm");
										orderCancelRequestHtml = '<div class="modal fade" id="sellerCancelRequest'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Request For Cancel Order ['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-validate form-horizontal" id="cust_order_cancel_request_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Are you really want to raise cancel request for orderId '+orderObject.order.orderid+'?</strong></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2">Cancellation Reason<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="seller_cancellation_request_note_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter cancellation Note" name="cancellation_Note" required="required"></div></div><div class="form-group" id="sellerCancelErrorDiv'+orderObject.order.orderid+'" style="display: none"><label for="cname" class="control-label col-lg-2"></label> <div class="col-lg-10"><span id="sellerCancelMessage'+orderObject.order.orderid+'" style="color: #FF0000; font-family: Lato, sans-serif"></span></div></div></form></div></div><div class="modal-footer"><button class="btn btn-danger" data-dismiss="modal" title="Cancel Request" id="seller_order_cancel_request_button_'+orderObject.order.orderid+'" type="button" onclick="sellerCancelOrderRequest('+orderObject.order.orderid+')">Cancel Request</button><button data-dismiss="modal" id="seller_order_cancel_request_close_button_'+orderObject.order.orderid+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
										orderCancelHtml = '<div class="modal fade" id="orderCancel'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order status is '+orderObject.order.orderstatus+'</h4> </div> <div class="modal-body"> <div class="alert alert-block alert-danger fade in"> <button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> <strong>Oh!</strong> The order status is <strong>'+orderObject.order.orderstatus+'</strong> and you can change Order only when Order status is <strong>Pending</strong>. </div> </div> <div class="modal-footer"> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
									}
									$('#Seller_Order_Cancel_Request_View').append(orderCancelRequestHtml);
									$('#Seller_Order_Confirm_Details').append(orderConfirmHtml);
									var prescriptionImageSrc = context+"/orderservice/image/getimage/"+orderObject.order.orderid;
									var prescriptionDownloadLink = "'"+context+"\/orderservice\/image\/downloadimage\/"+orderObject.order.orderid+"'";
									prescriptionDownloadLink = prescriptionDownloadLink.toString();
									$('#Seller_Order_Image_View').append('<div class="modal fade" id="viewprescription'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Prescription Image</h4> </div> <div class="modal-body"> <img id="prescribtion_image_view" style="width: 547px;height: 700px" src="'+prescriptionImageSrc+'"></img> </div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-success" type="button" title="Download Prescripton" onclick="window.location.href='+prescriptionDownloadLink+'"><i class="fa fa-cloud-download"> </i> Download Image</button> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>');
									$('#Seller_Order_Status_View').append('<div class="modal fade" id="orderStatus'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Status['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="progress progress-sm">'+orderStatusBar+'</div><div>Cancellation Note: '+orderObject.order.cancellationCmd+'</div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									$('#Seller_Order_Cancel_View').append(orderCancelHtml);
								}
								//=============== End Pending Order =================	
							});
							htmlFile = htmlFile.concat('</tbody> </table>');
							$('#Seller_Order_Search_Details').html(htmlFile);
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

function confirmOrderBySeller(orderId){
	blockUI("Please wait...");
	var cashmemoNo = $("#cust_order_confirm_cashmemo_"+orderId).val();
	var orderPrice = $("#cust_order_confirm_price_"+orderId).val();
	if(cashmemoNo == "" || cashmemoNo == undefined){
		alert("Please enter cachmemo number.");
		unblockUI();
		return false;
	}
	if(orderPrice == "" || orderPrice == undefined || orderPrice == "0.00"){
		alert("Please enter order price.");
		unblockUI();
		return false;
	}
	var invoiceId = orderId+"_"+cashmemoNo;
	var postObject = new Object();
	postObject.cashmemoNo = cashmemoNo;
	postObject.orderid = orderId;
	postObject.invoiceNo = "Initiated";
	postObject.invoiceId = invoiceId;
	postObject.orderprice = orderPrice;
	queryStr = JSON.stringify(postObject);
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/confirmOrder",
			type: "POST",
			data: queryStr,
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.length != undefined && data.hasOwnProperty("error")){
					$("#statusDialogBox #errorAlertMessage").text(data.error[0].errorMsg);
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
				else{
					if(data != null && data.hasOwnProperty("message")){
							if(data["status"] == "SUCCESS"){
								$("#statusDialogBox #successAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #successDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
									window.location= context+"/selleractivity/showpendingorder";
								},4000);
							}
							else if(data["status"] == "FAILED"){
								$("#statusDialogBox #errorAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #errorDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
								},4000);
							}
					}
					else{
						$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}

function sellerCancelOrder(orderId){
	var cancellationNote = $("#seller_cancellation_note_"+orderId).val();
	$("#returnErrorDiv"+orderId+" #returnErrorMessage"+orderId).text("");
	if(cancellationNote == "" || cancellationNote == undefined){
		$("#returnErrorDiv"+orderId+" #returnErrorMessage"+orderId).text("Please enter cancellation note.");
		$('#returnErrorDiv'+orderId).show();
		setTimeout(function () {
			$('#returnErrorDiv'+orderId).hide();
		},3000);
		return false;
	}
	$("#seller_order_cancel_close_button_"+orderId).click();
	blockUI("Please wait...");
	var postObject = new Object();
	postObject.cancellationNote = cancellationNote;
	postObject.orderid = orderId;
	postObject.cancellationProcedure = "Single";
	queryStr = JSON.stringify(postObject);
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/cancelOrder",
			type: "POST",
			data: "orderid="+orderId+"&cancellationNote="+cancellationNote+"&cancellationProcedure=Single",
			//contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.length != undefined && data.hasOwnProperty("error")){
					$("#statusDialogBox #errorAlertMessage").text(data.error[0].errorMsg);
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
				else{
					if(data != null && data.hasOwnProperty("message")){
							if(data["status"] == "SUCCESS"){
								$("#statusDialogBox #successAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #successDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
									window.location= context+"/selleractivity/showpendingorder";
								},4000);
							}
							else if(data["status"] == "FAILED"){
								$("#statusDialogBox #errorAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #errorDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
								},4000);
							}
					}
					else{
						$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}

function loadSellerConfirmedOrderResult(sellerId,orderstatus){
	var blockUIString = "";
	if(orderstatus == "Pending"){
		blockUIString = "Loading Pending Orders...";
	}
	else if(orderstatus == "Confirm"){
		blockUIString = "Loading Confirmed Orders...";
	}
	else if(orderstatus == "Deliver"){
		blockUIString = "Loading Delivered Orders...";
	}
	else if(orderstatus == "Cancel"){
		blockUIString = "Loading Cancelled Orders...";
	}
	blockUI(blockUIString);
	var confirmBar = "<div class='progress-bar progress-bar-info'  style='width: 18.27%;padding-right: 50px;'></div>";
	var pendingBar = "<div class='progress-bar progress-bar-warning'  style='width: 18.26%;padding-right: 50px;'></div>";
	var cancelBar = "<div class='progress-bar progress-bar-danger'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliverBar = "<div class='progress-bar progress-bar-success'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliveryTaxData = getTaxValues("DeliveryTax");
	var emergencyTaxData = getTaxValues("EmergencyTax");
	//alert("deliveryTaxData:::"+JSON.stringify(deliveryTaxData));
	$('#Seller_Order_Search_Details').html("");
	setTimeout(function () {
		$.ajax({
			url: context+"/orderservice/rest/getOrderBySeller/"+sellerId,
			type: "GET",
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
							var orderstatusHeader = "Pending";
							if(orderstatus == "Confirm"){
								orderstatusHeader = "Confirmed";
							}
							else if(orderstatus == "Deliver"){
								orderstatusHeader = "Delivered";
							}
							else if(orderstatus == "Cancel"){
								orderstatusHeader = "Cancelled";
							}
							var htmlFile = '<header class="panel-heading"> '+orderstatusHeader+' Orders </header><table id="order_Search_Table" class="table table-striped table-advance table-hover"> <thead> <tr> <th><i class="icon_check"></i> Order Id</th><th><i class="icon_calendar"></i> Date</th><th><i class="fa fa-dot-circle-o"></i> Status</th> <th><i class="fa fa-inr"></i> Price</th><th><i class="icon_pin_alt"></i> Pincode</th> <th><i class="fa fa-mobile"></i> Mobile</th> <th><i class="icon_cogs"></i> Action</th> </tr> </thead> <tbody>';
							$.each(data, function(key,orderObject) {
								//=============== Pending Order =================
								if(orderstatus.toUpperCase() == orderObject.order.orderstatus.toUpperCase()){
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
										$('#Seller_Order_TaxBreakup_View').append('<div class="modal fade" id="taxbreakup'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title"><b>Tax Breakup</b></h4> </div> <div class="modal-body"><div class="form"> '+taxbreakupHTML+'  </div></div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									 /*------------End Tax Details------------------------*/
									var emergencyPricetext = "";
									emergencyPricetext = priceCoverter(orderObject.order.emergencyPrice);
									var wrappedMobile = "";
									var mobilenoStrLen = orderObject.user.user_mobile.length;
									wrappedMobile = orderObject.user.user_mobile.substring((mobilenoStrLen-4), mobilenoStrLen);
									var actionButtonHTML="";
									var orderSeletionCheckbox="";
									if(orderObject.order.orderstatus == "Confirm"){
										actionButtonHTML = '<td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a><a class="btn btn-success" title="Deliver Order" data-toggle="modal" href="#deliverOrder'+orderObject.order.orderid+'"><i class="fa fa-check-square-o"></i></a><a class="btn btn-danger" title="Cancel Request" data-toggle="modal" href="#sellerCancelRequest'+orderObject.order.orderid+'"><i class="fa fa-undo"></i></a></div></td>';
										orderSeletionCheckbox='<input type="checkbox" id="sellerconfirmOrder_'+orderObject.order.orderid+'">';
									}
									else if(orderObject.order.orderstatus == "Deliver"){
										actionButtonHTML = '<td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a></div></td>';
									}
									else if(orderObject.order.orderstatus == "Cancel"){
										actionButtonHTML = '<td><div class="btn-group"><a class="btn btn-primary" title="View Order" data-toggle="modal" href="#myModal'+orderObject.order.orderid+'"><i class="icon_plus_alt2"></i></a></div></td>';
									}
									
									htmlFile = htmlFile.concat('<tr><td>'+orderSeletionCheckbox+'    '+orderObject.order.orderid+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+price+'</td><td>'+orderObject.order.orderPincode+'</td><td>'+orderObject.user.user_mobile+'</td>'+actionButtonHTML+'</tr>');
									
									if(orderObject.order.orderstatus == "Confirm"){
										$('#Seller_Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile'+orderObject.order.orderid+'">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button class="btn btn-success" type="button" data-dismiss="modal" data-toggle="modal" href="#deliverOrder'+orderObject.order.orderid+'" title="Deliver Order">Deliver Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									}
									else if(orderObject.order.orderstatus == "Deliver"){
										$('#Seller_Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile'+orderObject.order.orderid+'">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									}
									else if(orderObject.order.orderstatus == "Cancel"){
										$('#Seller_Order_View_Details').append('<div class="modal fade" id="myModal'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Order Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+orderObject.order.orderid+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Name:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+orderObject.user.user_name+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile'+orderObject.order.orderid+'">'+orderObject.user.user_mobile+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderObject.user.user_email+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+orderObject.order.shippingAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Pincode:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_pincode">'+orderObject.order.orderPincode+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Emergency Price:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+emergencyPricetext+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Discount :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_emergencyPrice">'+orderObject.order.orderDiscountAmount+'%</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Delivery Price :</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_deliveryprice">'+orderObject.order.orderDeliveryAmount+'</span> </strong> </div> </div> '+totalAmountHtml+' <div class="form-group"> <label for="cname" class="control-label col-lg-2">Details:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+orderObject.order.orderdetails+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer"><button class="btn btn-success" type="button" data-toggle="modal" href="#viewprescription'+orderObject.order.orderid+'" title="View Prescription">View Prescription</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									}
									
									var orderConfirmHtml = '';//'<div class="modal fade" id="confirmOrder'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order status is '+orderObject.order.orderstatus+'</h4> </div> <div class="modal-body"> <div class="alert alert-block alert-danger fade in"> <button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> <strong>Oh!</strong> The order status is <strong>'+orderObject.order.orderstatus+'</strong> and you can change Order only when Order status is <strong>Pending</strong>. </div> </div> <div class="modal-footer"> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
									var orderCancelRequestHtml = '<div class="modal fade" id="sellerCancelRequest'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Request For Cancel Order ['+orderObject.order.orderstatus+']</h4> </div> <div class="modal-body"> <div class="alert alert-block alert-danger fade in"> <button data-dismiss="alert" class="close close-sm" type="button"> <i class="icon-remove"></i> </button> <strong>Oh!</strong> You are not authorized person to cancel order id <strong>'+orderObject.order.orderstatus+'</strong>.</div> </div> <div class="modal-footer"> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>';
									if(orderObject.order.orderstatus == "Confirm"){
										var emergencyFlag = "";
										var emergencyPrice = "";
										if(orderObject.order.emergencyFlag == 1){
											emergencyFlag = "checked = \"checked\"";
											var emPrice = priceCoverter(orderObject.order.emergencyPrice);
											emergencyPrice = "<p id ='emergencyflag'>Emergency charge is <i class='fa fa-inr'></i> "+emPrice+"</p>";
										}
										var emPrice = priceCoverter(orderObject.order.emergencyPrice);
										orderConfirmHtml = '<div class="modal fade" id="deliverOrder'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Deliver Order['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-validate form-horizontal" id="cust_order_cancel_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Do you deliverd order id '+orderObject.order.orderid+'?</strong></div><div class="form-group" id="sellerCancelErrorDiv'+orderObject.order.orderid+'" style="display: none"><label for="cname" class="control-label col-lg-2"></label> <div class="col-lg-10"><span id="sellerCancelErrorMessage'+orderObject.order.orderid+'" style="color: #FF0000; font-family: Lato, sans-serif"></span></div></div></form></div></div><div class="modal-footer"><button class="btn btn-success" title="Deliver Order" id="seller_order_deliver_button_'+orderObject.order.orderid+'" type="button" onclick="sellerDeliverOrder('+orderObject.order.orderid+')">Deliver Order</button><button data-dismiss="modal" data-dismiss="modal" id="seller_order_deliver_close_button_'+orderObject.order.orderid+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
										orderCancelRequestHtml = '<div class="modal fade" id="sellerCancelRequest'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Request For Cancel Order ['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="form"> <form class="form-validate form-horizontal" id="cust_order_cancel_request_form_'+orderObject.order.orderid+'"><div class="alert alert-info fade in"><strong>Are you really want to raise cancel request for orderId '+orderObject.order.orderid+'?</strong></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2">Cancellation Reason<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="seller_cancellation_request_note_'+orderObject.order.orderid+'" type="text" placeholder="Please Enter cancellation Note" name="cancellation_Note" required="required"></div></div><div class="form-group" id="sellerCancelErrorDiv'+orderObject.order.orderid+'" style="display: none"><label for="cname" class="control-label col-lg-2"></label> <div class="col-lg-10"><span id="sellerCancelErrorMessage'+orderObject.order.orderid+'" style="color: #FF0000; font-family: Lato, sans-serif"></span></div></div></form></div></div><div class="modal-footer"><button class="btn btn-danger" data-dismiss="modal" title="Cancel Request" id="seller_order_cancel_request_button_'+orderObject.order.orderid+'" type="button" onclick="sellerCancelOrderRequest('+orderObject.order.orderid+')">Cancel Request</button><button data-dismiss="modal" id="seller_order_cancel_request_close_button_'+orderObject.order.orderid+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
									}
									$('#Seller_Order_Confirm_Details').append(orderConfirmHtml);
									$('#Seller_Order_Cancel_Request_View').append(orderCancelRequestHtml);
									var prescriptionImageSrc = context+"/orderservice/image/getimage/"+orderObject.order.orderid;
									var prescriptionDownloadLink = "'"+context+"\/orderservice\/image\/downloadimage\/"+orderObject.order.orderid+"'";
									prescriptionDownloadLink = prescriptionDownloadLink.toString();
									$('#Seller_Order_Image_View').append('<div class="modal fade" id="viewprescription'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Prescription Image</h4> </div> <div class="modal-body"> <img id="prescribtion_image_view" style="width: 547px;height: 700px" src="'+prescriptionImageSrc+'"></img> </div> <div class="modal-footer"><button data-dismiss="modal" class="btn btn-success" type="button" title="Download Prescripton" onclick="window.location.href='+prescriptionDownloadLink+'"><i class="fa fa-cloud-download"> </i> Download Image</button> <button data-dismiss="modal" class="btn btn-default" type="button">Close</button> </div> </div> </div> </div>');
									$('#Seller_Order_Status_View').append('<div class="modal fade" id="orderStatus'+orderObject.order.orderid+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Order Status['+orderObject.order.orderid+']</h4> </div> <div class="modal-body"><div class="progress progress-sm">'+orderStatusBar+'</div><div>Cancellation Note: '+orderObject.order.cancellationCmd+'</div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
									//$('#Seller_Order_Cancel_View').append(orderCancelHtml);
								}
								//=============== End Pending Order =================	
							});
							htmlFile = htmlFile.concat('</tbody> </table>');
							$('#Seller_Order_Search_Details').html(htmlFile);
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

function sellerDeliverOrder(orderid){
	var orderIdList = [];
	$("#returnErrorDivBulkDelivered #returnErrorMessageBulkDelivered").text("");
	if(typeof orderid === 'object'){
		orderIdList = orderid;
		if(orderIdList.length == 0){
			$("#returnErrorDivBulkDelivered #returnErrorMessageBulkDelivered").text("Please select orders.");
			$('#returnErrorDivBulkDelivered').show();
			setTimeout(function () {
				$('#returnErrorDivBulkDelivered').hide();
			},3000);
			return false;
		}
	}
	else if(typeof orderid === 'number'){
		orderIdList[0]=orderid;
	}
	var postObject = new Object();
	postObject.confirmOrders = orderIdList;
	postObject.status = "Deliver";
	queryStr = JSON.stringify(postObject);
	//alert("queryStr:: "+ queryStr);
	console.log("queryStr:: "+ queryStr);
	blockUI("Please wait...");
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/deliverOrder",
			type: "POST",
			data: queryStr,
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.length != undefined && data.hasOwnProperty("error")){
					$("#statusDialogBox #errorAlertMessage").text(data.error[0].errorMsg);
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
				else{
					if(data != null && data.hasOwnProperty("message")){
							if(data["status"] == "SUCCESS"){
								$("#statusDialogBox #successAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #successDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
									window.location= context+"/selleractivity/showconfirmorder";
								},4000);
							}
							else if(data["status"] == "FAILED"){
								$("#statusDialogBox #errorAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #errorDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
								},4000);
							}
					}
					else{
						$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}


function sellerCancelOrderRequest(orderid){
	var cancellationNote = "";
	var orderIdList = [];
	if(typeof orderid == 'object'){
		$("#returnErrorDivBulkCancel #returnErrorMessageBulkCancel").text("");
		orderIdList = orderid;
		if(orderIdList.length == 0){
			//$("#errorAlertMessage").text("Please select order.");
			//$("#successModifiedErrorAlert").show();
			//$("button[class = 'close']").click();
				$("#returnErrorDivBulkCancel #returnErrorMessageBulkCancel").text("Please select orders.");
				$('#returnErrorDivBulkCancel').show();
				setTimeout(function () {
					$('#returnErrorDivBulkCancel').hide();
				},3000);
				return false;
		}
		cancellationNote = $("#seller_bulk_cancellation_request_note").val();
		if(cancellationNote == undefined || cancellationNote.trim() == ""){
			$("#returnErrorDivBulkCancel #returnErrorMessageBulkCancel").text("Please enter cancellation note.");
			$('#returnErrorDivBulkCancel').show();
			setTimeout(function () {
				$('#returnErrorDivBulkCancel').hide();
			},3000);
			return false;
		}
	}
	else if(typeof orderid == 'number'){
		$("#sellerCancelErrorDiv"+orderid+" #sellerCancelErrorMessage"+orderid).text("");
		orderIdList[0]=orderid;
		cancellationNote = $("#seller_cancellation_request_note_"+orderid).val();
		if(cancellationNote == undefined || cancellationNote.trim() == ""){
			$("#sellerCancelErrorDiv"+orderid+" #sellerCancelErrorMessage"+orderid).text("Please enter note and raise cancel request.");
			$("div[id='sellerCancelErrorDiv"+orderid+"'").show();
			setTimeout(function () {
				$("div[id='sellerCancelErrorDiv"+orderid+"'").hide();
			},3000);
			return false;
		}
	}
	
	var postObject = new Object();
	postObject.confirmOrders = orderIdList;
	postObject.status = "Cancel";
	postObject.cancellationNote=cancellationNote;
	queryStr = JSON.stringify(postObject);
	//alert("queryStr:: "+ queryStr);
	console.log("queryStr:: "+ queryStr);
	blockUI("Please wait...");
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/cancelRequest",
			type: "POST",
			data: queryStr,
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.length != undefined && data.hasOwnProperty("error")){
					$("#statusDialogBox #errorAlertMessage").text(data.error[0].errorMsg);
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
				else{
					if(data != null && data.hasOwnProperty("message")){
							if(data["status"] == "SUCCESS"){
								$("#statusDialogBox #successAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #successDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
									window.location= context+"/selleractivity/showconfirmorder";
								},4000);
							}
							else if(data["status"] == "FAILED"){
								$("#statusDialogBox #errorAlertMessage").text(data["message"]);
								$("a[href = '#statusDialogBox']").click();
								$("#statusDialogBox #errorDialog").show();
								setTimeout(function () {
									$("button[class = 'close']").click();
								},4000);
							}
					}
					else{
						$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}

function loadSellerRefundOrders(sellerId,loadRefundOrders,fromDate,toDate,refundOrderIdList,refundOrderStatusList){
	var blockUIString = "Loading Refund Orders...";
	blockUI(blockUIString);
	var confirmBar = "<div class='progress-bar progress-bar-info'  style='width: 18.27%;padding-right: 50px;'></div>";
	var pendingBar = "<div class='progress-bar progress-bar-warning'  style='width: 18.26%;padding-right: 50px;'></div>";
	var cancelBar = "<div class='progress-bar progress-bar-danger'  style='width: 18.27%;padding-right: 50px;'></div>";
	var deliverBar = "<div class='progress-bar progress-bar-success'  style='width: 18.27%;padding-right: 50px;'></div>";
	var refundSearchObject = new Object();
	refundSearchObject.userId = sellerId;
	refundSearchObject.refundSearchType = loadRefundOrders;
	refundSearchObject.fromDate = fromDate;
	refundSearchObject.toDate = toDate;
	refundSearchObject.refundOrderIdList = refundOrderIdList;
	refundSearchObject.refundOrderStatusList = refundOrderStatusList;
	queryStr = JSON.stringify(refundSearchObject);
	$('#Order_Search_Details').html("");
	$('#Refund_Order_Cancel_View').html("");
	$('#Refund_Order_Confirm_View').html("");
	$('#Refund_Order_Refunded_View').html("");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/getsellerrefundorders",
			type: "GET",
			contentType : "application/json",
			data: "searchRefundOrder="+queryStr,
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				$("#Refund_Order_Search_Details").html("");
				if(data.hasOwnProperty("error")){
					$("#errorAlertMessage").html();
					$("#errorAlertMessage").html(data.error[0].errorMsg);
					$('#successModifiedErrorAlert').show();
					setTimeout(function () {
						$('#successModifiedErrorAlert').hide();
					},3000);
				}
				else if(data["refundOrders"].length>0){
							var htmlFile = '<header class="panel-heading"> Refund Orders </header><table id="refund_order_Search_Table" class="table table-striped table-advance table-hover"> <thead> <tr> <th><i class="fa fa-hashtag" aria-hidden="true"></i> Request ID</th><th><i class="icon_calendar"></i> Initated Date</th><th><i class="fa fa-dot-circle-o"></i> Status</th> <th><i class="fa fa-inr"></i> Refund Amount</th><th><i class="fa fa-check-square-o"></i> Refund Order Id</th> <th><i class="fa fa-check-square"></i> Cash Memo No.</th> <th><i class="icon_cogs"></i> Action</th> </tr> </thead> <tbody>';
							var refundOrderObjectList = data.refundOrders;
							for(var key = 0; key<refundOrderObjectList.length;key++){
								var d = new Date(refundOrderObjectList[key].refundInitiateTime);
								var formattedDate = d.getFullYear() + "-" + (((d.getMonth() + 1) <10) ? "0" + (d.getMonth() + 1):(d.getMonth() + 1)) + "-" + ((d.getDate()<10)?"0" +d.getDate():d.getDate());
								var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
								var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
								var seconds = (d.getSeconds() < 10) ? "0" + d.getSeconds() : d.getSeconds();
								var formattedTime = hours + ":" + minutes + ":" + seconds;
								formattedDate = formattedDate + " " + formattedTime;
								var price = refundOrderObjectList[key].refundAmount;
								price = price.toString();
								if(price.length == 1 && price.indexOf(".") == -1){
									price = price+".00";
								}
								
								/*---------Start: Order Status Bar creation----------*/
								var refundOrderStatusList = refundOrderObjectList[key].refundOrderStatusList;
								//var placeDateTime = dateConvertFormatter(refundOrderStatusList[0].refundOrderActionTimestamp);
								var orderStatusBar = "<a title='"+formattedDate+"' href='#'><span class='label label-default'>Initiated</span></a>";
								var orderStatusCnt;
								var prevStatus = "";
								for(orderStatusCnt = 0; orderStatusCnt<refundOrderStatusList.length;orderStatusCnt++){
									var refundOrderStatusObj = refundOrderStatusList[orderStatusCnt];
									var statusText = "";
									var bar = "";
									var dataValue = dateConvertFormatter(refundOrderStatusObj.refundOrderActionTimestamp);
									if(refundOrderStatusObj.refundOrderstatus == "Confirm"){
										statusText = "<a title='"+dataValue+"' href='#'><span class='label label-primary'>Confirmed</span></a>";
										bar = confirmBar;
									}
									else if(refundOrderStatusObj.refundOrderstatus == "Pending"){
										statusText = "<a title='"+dataValue+"' href='#'><span class='label label-warning'>Pending</span></a>";
										bar = pendingBar;
									}
									else if(refundOrderStatusObj.refundOrderstatus == "Cancel"){
										bar = cancelBar;
										statusText = "<a title='"+dataValue+"' href='#'><span class='label label-danger'>Cancelled</span></a>";
										if(prevStatus == "Pending"){
											bar = "<div class='progress-bar progress-bar-danger'  style='width: 51.57%;padding-right: 50px;'></div>";
										}
									}
									else if(refundOrderStatusObj.refundOrderstatus == "Refund"){
										statusText = "<a title='"+dataValue+"' href='#'><span class='label label-success'>Refunded</span></a>";
										bar = deliverBar;
									}
									prevStatus = refundOrderStatusObj.refundOrderstatus;
									orderStatusBar = orderStatusBar + bar;
									orderStatusBar = orderStatusBar + statusText;
								}
								/*-----------End: Order Status Bar creation----------*/
								
								/*-----------Start: Order Status link creation----------*/
								var orderStatusLink = "";
								if(refundOrderObjectList[key].refundOrderStatus == "Pending"){
									orderStatusLink = "<a href='#orderStatus"+refundOrderObjectList[key].refunRequestNo+"' class='label label-warning' data-toggle='modal'>"+refundOrderObjectList[key].refundOrderStatus+"</a>";
								}
								else if(refundOrderObjectList[key].refundOrderStatus == "Confirm"){
									orderStatusLink = "<a href='#orderStatus"+refundOrderObjectList[key].refunRequestNo+"' class='label label-primary' data-toggle='modal'>"+refundOrderObjectList[key].refundOrderStatus+"ed</a>";
								}
								else if(refundOrderObjectList[key].refundOrderStatus == "Refund"){
									orderStatusLink = "<a href='#orderStatus"+refundOrderObjectList[key].refunRequestNo+"' class='label label-success' data-toggle='modal'>"+refundOrderObjectList[key].refundOrderStatus+"ed</a>";
								}
								else if(refundOrderObjectList[key].refundOrderStatus == "Cancel"){
									orderStatusLink = "<a href='#orderStatus"+refundOrderObjectList[key].refunRequestNo+"' class='label label-danger' data-toggle='modal'>"+refundOrderObjectList[key].refundOrderStatus+"led</a>";
								}
								/*else if(refundOrderObjectList[key].refundOrderStatus == "Initiate"){
									orderStatusLink = "<a href='#orderStatus"+refundOrderObjectList[key].refunRequestNo+"' class='label label-danger' data-toggle='modal'>Initiated</a>";
								}*/
								/*-----------End: Order Status link creation----------*/
								var sellerId = refundOrderObjectList[key].sellerId;
								var actionLink = "";
								var actionbutton = "";
								if(refundOrderObjectList[key].refundOrderStatus == "Pending"){
									actionLink = "<a class='btn btn-primary' title='View Refund Order' data-toggle='modal' href='#refundorderview"+refundOrderObjectList[key].refunRequestNo+"'><i class='icon_plus_alt2'></i></a><a class='btn btn-success' title='Confirm Refund Order' data-toggle='modal' href='#refundOrderConfirm"+refundOrderObjectList[key].refunRequestNo+"'><i class='fa fa-check-square-o'></i></a><a class='btn btn-danger' title='Cancel Refund Order' data-toggle='modal' href='#refundOrderCancel"+refundOrderObjectList[key].refunRequestNo+"'><i class='icon_close_alt2'></i></a>";//<a class='btn btn-warning' title='Refunded' data-toggle='modal' href='#refundOrderRefunded"+refundOrderObjectList[key].refunRequestNo+"'><i class='fa fa-exchange'></i></a>
									actionbutton = '<button class="btn btn-success" type="button" data-toggle="modal" data-dismiss="modal" href="#refundOrderConfirm'+refundOrderObjectList[key].refunRequestNo+'" title="Confirm Refund Order">Confirm Refund Order</button><button class="btn btn-danger" type="button" data-toggle="modal" data-dismiss="modal" href="#refundOrderCancel'+refundOrderObjectList[key].refunRequestNo+'" title="Cancel Refund Order">Cancel Refund Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button>';//<button class="btn btn-warning" type="button" data-toggle="modal" data-dismiss="modal" href="#refundOrderRefunded'+refundOrderObjectList[key].refunRequestNo+'" title="Refunded">Refunded</button>
								}
								else if(refundOrderObjectList[key].refundOrderStatus == "Confirm"){
									actionLink = "<a class='btn btn-primary' title='View Refund Order' data-toggle='modal' href='#refundorderview"+refundOrderObjectList[key].refunRequestNo+"'><i class='icon_plus_alt2'></i></a><a class='btn btn-warning' title='Refunded' data-toggle='modal' href='#refundOrderRefunded"+refundOrderObjectList[key].refunRequestNo+"'><i class='fa fa-exchange'></i></a><a class='btn btn-danger' title='Cancel Refund Order' data-toggle='modal' href='#refundOrderCancel"+refundOrderObjectList[key].refunRequestNo+"'><i class='icon_close_alt2'></i></a>";
									actionbutton = '<button class="btn btn-warning" type="button" data-toggle="modal" data-dismiss="modal" href="#refundOrderRefunded'+refundOrderObjectList[key].refunRequestNo+'" title="Refunded">Refunded</button><button class="btn btn-danger" type="button" data-toggle="modal" data-dismiss="modal" href="#refundOrderCancel'+refundOrderObjectList[key].refunRequestNo+'" title="Cancel Refund Order">Cancel Refund Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button>';
								}
								else if(refundOrderObjectList[key].refundOrderStatus == "Refund"){
									actionLink = "<a class='btn btn-primary' title='View Refund Order' data-toggle='modal' href='#refundorderview"+refundOrderObjectList[key].refunRequestNo+"'><i class='icon_plus_alt2'></i></a>";//<a class='btn btn-danger' title='Cancel Refund Order' data-toggle='modal' href='#refundOrderCancelRequest"+refundOrderObjectList[key].refunRequestNo+"'><i class='fa fa-undo'></i></a>
									actionbutton = '<button data-dismiss="modal" class="btn btn-default" type="button">Close</button>';//<button class="btn btn-danger" type="button" data-toggle="modal" data-dismiss="modal" href="#refundOrderCancelRequest'+refundOrderObjectList[key].refunRequestNo+'" title="Cancellation Request">Cancellation Request</button>
									var refundedCancelRequestHtml  = '<div class="modal fade" id="refundOrderCancelRequest'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Request[<b>'+refundOrderObjectList[key].refunRequestNo+'</b>]</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_refundorder_cancel_request_form_'+refundOrderObjectList[key].refunRequestNo+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+refundOrderObjectList[key].refunRequestNo+' refund order?</strong></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2" style="width: 100%;text-align:left">Cancellation Request Reason<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="seller_refunded_cancellation_request_note_'+refundOrderObjectList[key].refunRequestNo+'" type="text" placeholder="Please Enter cancellation Note" name="cancellation_Note" required="required"></div></div><div class="form-group" id="sellerRefundCancelRequestErrorDiv_'+refundOrderObjectList[key].refunRequestNo+'" style="display: none"><label for="cname" class="control-label col-lg-2"></label> <div class="col-lg-10"><span id="sellerRefundCancelRequestErrorMessage_'+refundOrderObjectList[key].refunRequestNo+'" style="color: #FF0000; font-family: Lato, sans-serif"></span></div></div></form></div></div><div class="modal-footer"><button class="btn btn-danger" id="cust_refundorder_cancel_request_button_'+refundOrderObjectList[key].refunRequestNo+'" type="button" onclick="processRefundCancellationRequestBySeller(\''+refundOrderObjectList[key].refunRequestNo+'\',\'Requested\')">Cancellation Request</button><button data-dismiss="modal" id="seller_refundorder_close_button_'+refundOrderObjectList[key].refunRequestNo+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
									$('#Refund_Order_Refunded_View').append(refundedCancelRequestHtml);
								}
								else if(refundOrderObjectList[key].refundOrderStatus == "Cancel"){
									actionLink = "<a class='btn btn-primary' title='View Refund Order' data-toggle='modal' href='#refundorderview"+refundOrderObjectList[key].refunRequestNo+"'><i class='icon_plus_alt2'></i></a>";
									actionbutton = '<button data-dismiss="modal" class="btn btn-default" type="button">Close</button>';
								}
								
								htmlFile = htmlFile.concat('<tr><td>'+refundOrderObjectList[key].refunRequestNo+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+price+'</td><td>'+refundOrderObjectList[key].refundOrderid+'</td><td>'+refundOrderObjectList[key].cashmemoNo+'</td><td><div class="btn-group">'+actionLink+'</div></td></tr>');
								$('#Refund_Order_View_Details').append('<div class="modal fade" id="refundorderview'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Refund Order [<b>'+refundOrderObjectList[key].refunRequestNo+'</b>]</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Request Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+refundOrderObjectList[key].refunRequestNo+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Order ID:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="refund_order_view_cname">'+refundOrderObjectList[key].refundOrderid+'</span> </strong><input id="refund_seller_id" type="hidden" value="'+sellerId+'"/> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Cashmemo:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+refundOrderObjectList[key].cashmemoNo+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Initiator:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+refundOrderObjectList[key].refundInitiateUser+'</span> </strong> </div> </div><div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+refundOrderObjectList[key].initiatorMobileNo+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+refundOrderObjectList[key].initiatorEmailAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+refundOrderObjectList[key].initiatorAddress+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Refund Amount:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Reason:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+refundOrderObjectList[key].refundReason+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Seller Comment:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+refundOrderObjectList[key].refundOrderSellerComt+'</span> </strong> </div> </div></form> </div> </div> <div class="modal-footer">'+actionbutton+'</div></div></div></div>');
								var refundOrderCancelHtml  = '<div class="modal fade" id="refundOrderCancel'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Refund Order[<b>'+refundOrderObjectList[key].refunRequestNo+'</b>]</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_refundorder_cancel_form_'+refundOrderObjectList[key].refunRequestNo+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+refundOrderObjectList[key].refunRequestNo+' refund order?</strong></div><div class="form-group" style="padding-top: 8px"> <label for="cname" class="control-label col-lg-2">Cancellation Reason<span class="required">*</span> </label> <div class="col-lg-10" style="padding-top:8px"><input class="form-control" id="seller_refund_cancellation_request_note_'+refundOrderObjectList[key].refunRequestNo+'" type="text" placeholder="Please Enter cancellation Note" name="cancellation_Note" required="required"></div></div><div class="form-group" id="sellerRefundCancelErrorDiv_'+refundOrderObjectList[key].refunRequestNo+'" style="display: none"><label for="cname" class="control-label col-lg-2"></label> <div class="col-lg-10"><span id="sellerRefundCancelErrorMessage_'+refundOrderObjectList[key].refunRequestNo+'" style="color: #FF0000; font-family: Lato, sans-serif"></span></div></div></form></div></div><div class="modal-footer"><button class="btn btn-danger" id="cust_refundorder_cancel_button_'+refundOrderObjectList[key].refunRequestNo+'" type="button" onclick="processRefundOrderBySeller(\''+refundOrderObjectList[key].refunRequestNo+'\',\'Cancel\')">Cancel Refund Order</button><button data-dismiss="modal" id="seller_refundorder_close_button_'+refundOrderObjectList[key].refunRequestNo+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
								var refundOrderConfirmHtml = '<div class="modal fade" id="refundOrderConfirm'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Confirm Refund Order[<b>'+refundOrderObjectList[key].refunRequestNo+'</b>]</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_refundorder_confirm_form_'+refundOrderObjectList[key].refunRequestNo+'"><div class="alert alert-info fade in"><strong>Are you really want to confirm '+refundOrderObjectList[key].refunRequestNo+' refund request?</strong></div></form></div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-success" id="cust_refundorder_confirm_button_'+refundOrderObjectList[key].refunRequestNo+'" type="button" onclick="processRefundOrderBySeller(\''+refundOrderObjectList[key].refunRequestNo+'\',\'Confirm\')">Confirm Refund Order</button><button data-dismiss="modal" id="seller_refundorder_close_button_'+refundOrderObjectList[key].refunRequestNo+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
								var refundOrderRefundedHtml = '<div class="modal fade" id="refundOrderRefunded'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Refund Order[<b>'+refundOrderObjectList[key].refunRequestNo+'</b>]</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_refundorder_refunded_form_'+refundOrderObjectList[key].refunRequestNo+'"><div class="alert alert-info fade in"><strong>You have refunded the request id '+refundOrderObjectList[key].refunRequestNo+'.</strong></div></form></div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-warning" id="cust_refundorder_refunded_button_'+refundOrderObjectList[key].refunRequestNo+'" type="button" onclick="processRefundOrderBySeller(\''+refundOrderObjectList[key].refunRequestNo+'\',\'Refund\')">Refunded</button><button data-dismiss="modal" id="seller_refundorder_close_button_'+refundOrderObjectList[key].refunRequestNo+'" class="btn btn-default" type="button">Close</button></div></div></div></div>';
								$('#Refund_Order_Cancel_View').append(refundOrderCancelHtml);
								$('#Refund_Order_Confirm_View').append(refundOrderConfirmHtml);
								$('#Refund_Order_Refunded_View').append(refundOrderRefundedHtml);
								var sellerCmmnt = "";
								if(refundOrderObjectList[key].refundOrderSellerComt != null || refundOrderObjectList[key].refundOrderSellerComt != undefined){
									sellerCmmnt = refundOrderObjectList[key].refundOrderSellerComt.trim();
								}
								$('#Refund_Order_Status_View').append('<div class="modal fade" id="orderStatus'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Refund Order Status['+refundOrderObjectList[key].refunRequestNo+']</h4> </div> <div class="modal-body"><div class="progress progress-sm">'+orderStatusBar+'</div><div><b>Note</b>: '+sellerCmmnt+'</div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
							}
							htmlFile = htmlFile.concat('</tbody> </table>');
							$('#Refund_Order_Search_Details').html(htmlFile);
							$('#refund_order_Search_Table').dataTable();
				}
			},
			error: function(){
				$("#errorAlertMessage").html();
				$("#errorAlertMessage").html("Error Occurs During Fetching Order!");
				$('#successModifiedErrorAlert').show();
				setTimeout(function () {
					$('#successModifiedErrorAlert').hide();
				},3000);
			}
		},unblockUI());
	},200);
}

function processRefundOrderBySeller(requestid,status){
	var refundOrderid = $("#refundorderview"+requestid+" span[id='refund_order_view_cname']").text();
	var refundSellerid = $("#refundorderview"+requestid+" input[id='refund_seller_id']").val();
	var refundSellerCmd = '';
	var refreshLink = context+"/selleractivity/processrefund";
	if(status == "Cancel"){
		refundSellerCmd = $("#seller_refund_cancellation_request_note_"+requestid).val();
		if(refundSellerCmd == "" || refundSellerCmd == undefined || refundSellerCmd.length == 0 || refundSellerCmd.trim() == ""){
			$("span[id = 'sellerRefundCancelErrorMessage_"+requestid+"']").text("Please enter cancellation command.");
			$("div[id = 'sellerRefundCancelErrorDiv_"+requestid+"']").show();
			setTimeout(function () {
				$("div[id = 'sellerRefundCancelErrorDiv_"+requestid+"']").hide();
			},4000);
			return false;
		}
		else{
			refreshLink = context+"/selleractivity/cancelledrefund";
			$("button[id = 'seller_refundorder_close_button_"+requestid+"']").click();
		}
	}
	if(status == "Refund"){
		refreshLink = context+"/selleractivity/refundedrefund";
	}
	if(status == "Confirm"){
		refreshLink = context+"/selleractivity/confirmedrefund";
	}
	blockUI(status+" Refund Order...");
	var refundSearchObject = new Object();
	refundSearchObject.refundOrderid = refundOrderid;
	refundSearchObject.refundOrderStatus = status;
	refundSearchObject.refundRequestId = requestid;
	refundSearchObject.sellerID = refundSellerid;
	refundSearchObject.sellerComments=refundSellerCmd;
	queryStr = JSON.stringify(refundSearchObject);
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/executeSellerRefundorders",
			type: "POST",
			data: queryStr,
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.hasOwnProperty("status") && data.hasOwnProperty("message")){
					if(data["status"] == "SUCCESS"){
						$("#statusDialogBox #successAlertMessage").text(data["message"]);
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #successDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
							window.location= refreshLink;
						},4000);
					}
					else if(data["status"] == "FAILED"){
						$("#statusDialogBox #errorAlertMessage").text(data["message"]);
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				}
				else{
					$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}

function processRefundCancellationRequestBySeller(requestid,status){
	var refundOrderid = $("#refundorderview"+requestid+" span[id='refund_order_view_cname']").text();
	var refundSellerid = $("#refundorderview"+requestid+" input[id='refund_seller_id']").val();
	var refundSellerCmd = '';
	if(status == "Requested"){
		refundSellerCmd = $("#seller_refunded_cancellation_request_note_"+requestid).val();
		if(refundSellerCmd == "" || refundSellerCmd == undefined || refundSellerCmd.length == 0 || refundSellerCmd.trim() == ""){
			$("span[id = 'sellerRefundCancelRequestErrorMessage_"+requestid+"']").text("Please enter cancellation command.");
			$("div[id = 'sellerRefundCancelRequestErrorDiv_"+requestid+"']").show();
			setTimeout(function () {
				$("div[id = 'sellerRefundCancelRequestErrorDiv_"+requestid+"']").hide();
			},4000);
			return false;
		}
		else{
			$("button[id = 'seller_refundorder_close_button_"+requestid+"']").click();
		}
	}
	blockUI(" Please wait...");
	var refundSearchObject = new Object();
	refundSearchObject.orderid = refundOrderid;
	refundSearchObject.requestStatus = status;
	refundSearchObject.refundRequestId = requestid;
	refundSearchObject.requestReason=refundSellerCmd;
	queryStr = JSON.stringify(refundSearchObject);
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/executeCancellationRequest",
			type: "POST",
			data: queryStr,
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.hasOwnProperty("status") && data.hasOwnProperty("message")){
					if(data["status"] == "SUCCESS"){
						$("#statusDialogBox #successAlertMessage").text(data["message"]);
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #successDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
							window.location= context+"/selleractivity/raisecancelrequest";
						},4000);
					}
					else if(data["status"] == "FAILED"){
						$("#statusDialogBox #errorAlertMessage").text(data["message"]);
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				}
				else{
					$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}

/*
 * Load seller info using rest call service getSellerDetails(userId)
 * 
 */

function loadSellerInfo(sellerId){
	
	blockUI(" Please wait...");
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	$('#Seller_Info_Details_View').html("");
	
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/getsellerinfo/getSellerById/"+sellerId,
			type: "GET",
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.hasOwnProperty("status")){
					if(data["status"] == "SUCCESS"){
						var htmlFile = "";
						var sellerInfo = data.sellers;
						sellerInfo = sellerInfo[0];
						var sellerShopCloseDay = sellerInfo.sellerShopCloseDay;
						if(sellerShopCloseDay == null || sellerShopCloseDay == undefined){
							sellerShopCloseDay = "";
						}
						var shopOpenTime = sellerInfo.shopOpenTime;
						if(shopOpenTime == null || shopOpenTime == undefined){
							shopOpenTime = "";
						}
						var shopCloseTime = sellerInfo.shopCloseTime;
						if(shopCloseTime == null || shopCloseTime == undefined){
							shopCloseTime = "";
						}
						var selectTag = "";
						if(sellerShopCloseDay == "Sunday"){
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="">Select Day</option> <option value="Sunday" selected="selected">Sunday</option> <option value="Monday">Monday</option> <option value="Tuesday">Tuesday</option> <option value="Wednesday">Wednesday</option> <option value="Thursday">Thursday</option> <option value="Friday">Friday</option> <option value="Saturday">Saturday</option> </select>';
						}
						else if(sellerShopCloseDay == "Monday"){
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="">Select Day</option> <option value="Sunday">Sunday</option> <option value="Monday" selected="selected">Monday</option> <option value="Tuesday">Tuesday</option> <option value="Wednesday">Wednesday</option> <option value="Thursday">Thursday</option> <option value="Friday">Friday</option> <option value="Saturday">Saturday</option> </select>';
						}
						else if(sellerShopCloseDay == "Tuesday"){
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="">Select Day</option> <option value="Sunday">Sunday</option> <option value="Monday">Monday</option> <option value="Tuesday" selected="selected">Tuesday</option> <option value="Wednesday">Wednesday</option> <option value="Thursday">Thursday</option> <option value="Friday">Friday</option> <option value="Saturday">Saturday</option> </select>';
						}
						else if(sellerShopCloseDay == "Wednesday"){
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="">Select Day</option> <option value="Sunday">Sunday</option> <option value="Monday">Monday</option> <option value="Tuesday">Tuesday</option> <option value="Wednesday" selected="selected">Wednesday</option> <option value="Thursday">Thursday</option> <option value="Friday">Friday</option> <option value="Saturday">Saturday</option> </select>';
						}
						else if(sellerShopCloseDay == "Thursday"){
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="">Select Day</option> <option value="Sunday">Sunday</option> <option value="Monday">Monday</option> <option value="Tuesday">Tuesday</option> <option value="Wednesday">Wednesday</option> <option value="Thursday" selected="selected">Thursday</option> <option value="Friday">Friday</option> <option value="Saturday">Saturday</option> </select>';
						}
						else if(sellerShopCloseDay == "Friday"){
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="">Select Day</option> <option value="Sunday">Sunday</option> <option value="Monday">Monday</option> <option value="Tuesday">Tuesday</option> <option value="Wednesday">Wednesday</option> <option value="Thursday">Thursday</option> <option value="Friday" selected="selected">Friday</option> <option value="Saturday">Saturday</option> </select>';
						}
						else if(sellerShopCloseDay == "Saturday"){
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="">Select Day</option> <option value="Sunday">Sunday</option> <option value="Monday">Monday</option> <option value="Tuesday">Tuesday</option> <option value="Wednesday">Wednesday</option> <option value="Thursday">Thursday</option> <option value="Friday">Friday</option> <option value="Saturday" selected="selected">Saturday</option> </select>';
						}
						else{
							selectTag = '<select class="form-control input-sm m-bot15" id="seller_shop_close_day"> <option value="" selected="selected">Select Day</option> <option value="Sunday">Sunday</option> <option value="Monday">Monday</option> <option value="Tuesday">Tuesday</option> <option value="Wednesday">Wednesday</option> <option value="Thursday">Thursday</option> <option value="Friday">Friday</option> <option value="Saturday">Saturday</option> </select>';
						}
						htmlFile = htmlFile.concat('<div class="col-lg-reg"> <section class="panel"> <header class="panel-heading"> Seller Information </header> <div class="panel-body"> <div class="form"> <form class="form-validate form-horizontal" id="feedback_form" method="post""> <div class="form-group "> <label for="cname" style="padding-right: 30px;width:25%;text-align: left;" class="control-label col-lg-2" >Shop Name <span class="required">*</span></label> <div class="col-lg-10" style="padding-top: 1px;width:50%;"> <input id = "seller_shop_name" placeholder="Please Enter Seller Shop Name" class="form-control" type="text" value="'+sellerInfo.sellerShopName+'" required="required"/> </div> </div><div class="form-group "> <label for="cname" style="padding-right: 30px;width:25%;text-align: left;" class="control-label col-lg-2" >Discount Percentage <span class="required">*</span></label> <div class="col-lg-10" style="padding-top: 1px;width:50%;"> <input id = "seller_discount_percentage" placeholder="Please Enter Discount Percentage" class="form-control" type="text" min="5" maxlength="50" value="'+sellerInfo.sellerDiscount+'" required="required"/> </div> </div> <div class="form-group "> <label for="cname" style="padding-right: 30px;width:25%;text-align: left;" class="control-label col-lg-2" >Emergency Price <span class="required">*</span></label> <div class="col-lg-10" style="padding-top: 1px;width:50%;"> <input placeholder="Please Enter Emergency Price" class="form-control" id="seller_emergency_price" type="text" value="'+sellerInfo.sellerEmergencyPrice+'" required="required"/> </div> </div> <div class="form-group "> <label for="cemail" style="padding-right: 30px;width:25%;text-align: left;" class="control-label col-lg-2" >Delivery Price <span class="required">*</span></label> <div class="col-lg-10" style="padding-top: 1px;width:50%"> <input class="form-control" placeholder="Please Enter Delivery Price" id="seller_delivery_price" type="text" value="'+sellerInfo.sellerDeliveryPrice+'" required="required"/> </div> </div> <div class="form-group "> <label for="curl" style="padding-right: 30px;width:25%;text-align: left;" class="control-label col-lg-2" >Shop Close Day<span class="required">*</span></label> <div class="col-lg-10" style="padding-top: 1px;width:50%"> '+selectTag+' </div> </div> <div class="form-group "> <label for="cname" style="padding-right: 30px;width:25%;text-align: left;" class="control-label col-lg-2" >Shop Open Time <span class="required">*</span></label> <div class="col-lg-10" style="padding-top: 1px;width:50%"> <input class="form-control" placeholder="Please Enter Shop Open Time" id="seller_shop_open_time" type="text" value="'+shopOpenTime+'"/><span>Format - HH:MM</span> </div> </div> <div class="form-group "> <label for="cname" style="padding-right: 30px;width:25%;text-align: left;" class="control-label col-lg-2" >Shop Close Time <span class="required">*</span></label> <div class="col-lg-10" style="padding-top: 1px;width:50%"> <input class="form-control" id="seller_shop_close_time" type="text" value="'+shopCloseTime+'" placeholder="Please Enter Shop Close Time"/><span>Format - HH:MM</span> </div> </div> <div class="form-group"> <div class="col-lg-offset-2 col-lg-10" style="margin-left: 40.666666666666664%;"> <br> <a class="btn btn-primary" id="ChangeSellerInfo_Button" onclick="updateSellerInfo('+sellerInfo.sellerid+')">Submit</a> <br></br> </div> </div> </form> </div> </div> </section> </div>');
						$('#Seller_Info_Details_View').html(htmlFile);
					}
					else if(data["status"] == "FAILED"){
						$("#statusDialogBox #errorAlertMessage").text(data["message"]);
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				}
				else{
					$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}

function updateSellerInfo(sellerid){
	
	var sellerShopName = $("#seller_shop_name").val();
	var sellerDiscountPercentage = $("#seller_discount_percentage").val();
	var sellerEmergencyPrice = $("#seller_emergency_price").val();
	var sellerDeliveryPrice = $("#seller_delivery_price").val();
	var sellerShopCloseDay = $("#seller_shop_close_day").val();
	var sellerShopOpenTime = $("#seller_shop_open_time").val();
	var sellerShopCloseTime = $("#seller_shop_close_time").val();
	var timeRegex = /([01]\d|2[0-3]):([0-5]\d)/;
	var priceRegex = /^-{0,1}\d*\.{0,1}\d+$/;
	var flag = true;
	
	if(sellerDiscountPercentage == undefined || sellerDiscountPercentage == null ||
	   sellerDiscountPercentage == "" || sellerDiscountPercentage.length==0){
		$("#statusDialogBox #errorAlertMessage").text("Please enter Discount Percentage.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_discount_percentage").focus();
		},4000);
	}
	else if(sellerShopName == undefined || sellerShopName == null ||
			sellerShopName == "" || sellerShopName.length==0){
		$("#statusDialogBox #errorAlertMessage").text("Please enter Shop Name.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_shop_name").focus();
		},4000);
	}
	else if(sellerEmergencyPrice == undefined || sellerEmergencyPrice == null ||
			sellerEmergencyPrice == "" || sellerEmergencyPrice.length==0){
		$("#statusDialogBox #errorAlertMessage").text("Please enter Emergency Price.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_emergency_price").focus();
		},4000);
	}
	else if(sellerDeliveryPrice == undefined || sellerDeliveryPrice == null ||
			sellerDeliveryPrice == "" || sellerDeliveryPrice.length==0){
		$("#statusDialogBox #errorAlertMessage").text("Please enter Delivery Price.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_delivery_price").focus();
		},4000);
	}
	else if(sellerShopCloseDay == undefined || sellerShopCloseDay == null ||
			sellerShopCloseDay == "" || sellerShopCloseDay.length==0){
		$("#statusDialogBox #errorAlertMessage").text("Please enter Shop Close Day.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_shop_close_day").focus();
		},4000);
	}
	else if(sellerShopOpenTime == undefined || sellerShopOpenTime == null ||
			sellerShopOpenTime == "" || sellerShopOpenTime.length==0){
		$("#statusDialogBox #errorAlertMessage").text("Please enter Shop Open Time.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_shop_open_time").focus();
		},4000);
	}
	else if(sellerShopCloseTime == undefined || sellerShopCloseTime == null ||
			sellerShopCloseTime == "" || sellerShopCloseTime.length==0){
		$("#statusDialogBox #errorAlertMessage").text("Please enter Shop Close Time.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_shop_close_time").focus();
		},4000);
	}
	
	if(!timeRegex.test(sellerShopOpenTime)){
		$("#statusDialogBox #errorAlertMessage").text("Please enter below mentioned time format.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_shop_open_time").focus();
		},4000);
	}
	
	if(!timeRegex.test(sellerShopCloseTime)){
		$("#statusDialogBox #errorAlertMessage").text("Please enter below mentioned time format.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_shop_close_time").focus();
		},4000);
	}
	
	if(!priceRegex.test(sellerDiscountPercentage)){
		$("#statusDialogBox #errorAlertMessage").text("Please enter proper decimal format.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_discount_percentage").focus();
		},4000);
	}
	
	if(!priceRegex.test(sellerEmergencyPrice)){
		$("#statusDialogBox #errorAlertMessage").text("Please enter proper decimal format.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_emergency_price").focus();
		},4000);
	}
	
	if(!priceRegex.test(sellerDeliveryPrice)){
		$("#statusDialogBox #errorAlertMessage").text("Please enter proper decimal format.");
		$("a[href = '#statusDialogBox']").click();
		$("#statusDialogBox #errorDialog").show();
		flag = false;
		setTimeout(function () {
			$("button[class = 'close']").click();
			$("#seller_delivery_price").focus();
		},4000);
	}
	
	blockUI(" Seller info updating...");
	var updateSellerInfo = new Object();
	updateSellerInfo.sellerShopName = sellerShopName;
	updateSellerInfo.sellerDiscount = sellerDiscountPercentage;
	updateSellerInfo.sellerEmergencyPrice = sellerEmergencyPrice;
	updateSellerInfo.sellerDeliveryPrice=sellerDeliveryPrice;
	updateSellerInfo.sellerShopCloseDay=sellerShopCloseDay;
	updateSellerInfo.shopOpenTime=sellerShopOpenTime;
	updateSellerInfo.shopCloseTime=sellerShopCloseTime;
	updateSellerInfo.sellerid=sellerid;
	queryStr = JSON.stringify(updateSellerInfo);
	if(flag){
		$("#statusDialogBox #successAlertMessage").text("");
		$("#statusDialogBox #errorAlertMessage").text("");
		$("#statusDialogBox #errorDialog").css("display", "none");
		$("#statusDialogBox #successDialog").css("display", "none");
		setTimeout(function () {
			$.ajax({
				url: context+"/sellerrestservice/updatesellerinfo",
				type: "POST",
				data: queryStr,
				contentType : "application/json",
				dataType: "json",
				async: false,
				cache: false,
				success: function(data){
					if(data.hasOwnProperty("status") && data.hasOwnProperty("message")){
						if(data["status"] == "SUCCESS"){
							$("#statusDialogBox #successAlertMessage").text(data["message"]);
							$("a[href = '#statusDialogBox']").click();
							$("#statusDialogBox #successDialog").show();
							setTimeout(function () {
								$("button[class = 'close']").click();
								window.location= context+"/seller/changesellerinfo";
							},4000);
						}
						else if(data["status"] == "FAILED"){
							$("#statusDialogBox #errorAlertMessage").text(data["message"]);
							$("a[href = '#statusDialogBox']").click();
							$("#statusDialogBox #errorDialog").show();
							setTimeout(function () {
								$("button[class = 'close']").click();
							},4000);
						}
					}
					else{
						$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
				},
				error: function(){
					$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Execution!");
					$("a[href = '#statusDialogBox']").click();
					$("#statusDialogBox #errorDialog").show();
					setTimeout(function () {
						$("button[class = 'close']").click();
					},4000);
				}
			},unblockUI());
		},200);
	}
}


function loadSellerStatisticData(sellerId,fromDate,toDate){
	var dateObj = new Date();
	if(toDate == null || toDate == undefined){
		toDate = dateObj.getTime();
	}
	else{
		var dateTimeParts = toDate.split(' '),
	    timeParts = dateTimeParts[1].split(':'),
	    dateParts = dateTimeParts[0].split('-'),
	    date;
		date = new Date(dateParts[0], parseInt(dateParts[1], 10) - 1, dateParts[1], timeParts[0], timeParts[1]);
		toDate = date.getTime();
	}
	
	if(fromDate == null || fromDate == undefined){
		fromDate = dateObj.getTime()-2592000000;
	}
	else{
		var dateTimeParts = fromDate.split(' '),
	    timeParts = dateTimeParts[1].split(':'),
	    dateParts = dateTimeParts[0].split('-'),
	    date;
		date = new Date(dateParts[0], parseInt(dateParts[1], 10) - 1, dateParts[1], timeParts[0], timeParts[1]);
		fromDate = date.getTime();
	}
	
	blockUI(" Generating Seller Report...");
	google.charts.load('current', {'packages':['corechart']});
	 setTimeout(function () {
			google.charts.setOnLoadCallback(drawChart(sellerId,fromDate,toDate),unblockUI());
	},2000);
}

function drawChart(sellerId,fromDate,toDate) {
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	
	 var jsonData = $.ajax({
         url: context+"/sellerrestservice/sellerstatistic/"+sellerId+"/"+fromDate+"/"+toDate,
         dataType: "application/json",
         cache: false,
         async: false
         }).responseText;
	 var jsonResponse = JSON.parse(jsonData);
	 if(jsonResponse.length >1){
		 var data = google.visualization.arrayToDataTable(JSON.parse(jsonData));
		
		  // Optional; add a title and set the width and height of the chart
		  var options = {'title':'Sells Report', 'width':700, 'height':600, is3D: true};
		
		  // Display the chart inside the <div> element with id="piechart"
		  var chart = new google.visualization.PieChart(document.getElementById('piechart'));
		  chart.draw(data, options);
	 }
	 else{
		 $("#statusDialogBox #errorAlertMessage").text("Data not found.");
			$("a[href = '#statusDialogBox']").click();
			$("#statusDialogBox #errorDialog").show();
			setTimeout(function () {
				$("button[class = 'close']").click();
			},4000);
	 }
}

function downloadSellerReport(sellerId,fromDate,toDate){
	var dateObj = new Date();
	if(toDate == null || toDate == undefined){
		toDate = dateObj.getTime();
	}
	else{
		var dateTimeParts = toDate.split(' '),
	    timeParts = dateTimeParts[1].split(':'),
	    dateParts = dateTimeParts[0].split('-'),
	    date;
		date = new Date(dateParts[0], parseInt(dateParts[1], 10) - 1, dateParts[1], timeParts[0], timeParts[1]);
		toDate = date.getTime();
	}
	
	if(fromDate == null || fromDate == undefined){
		fromDate = dateObj.getTime()-2592000000;
	}
	else{
		var dateTimeParts = fromDate.split(' '),
	    timeParts = dateTimeParts[1].split(':'),
	    dateParts = dateTimeParts[0].split('-'),
	    date;
		date = new Date(dateParts[0], parseInt(dateParts[1], 10) - 1, dateParts[1], timeParts[0], timeParts[1]);
		fromDate = date.getTime();
	}
	
	var link = window.location.origin+context+"/sellerrestservice/downloadreport/"+sellerId+"/"+fromDate+"/"+toDate;
	//$("#seller_report_download_link").attr("href", link);
	
	blockUI(" Exporting Seller Report...");
	
	setTimeout(function () {
		download(link);
	},0);
	
}

function download(link){
	setTimeout(function () {
		unblockUI();
	},window.location.href=link);
}

/*
 Load Cancel Request List
*/
function loadCancelRequest(requester,fromDate,toDate,searchType,statusValuesArray,orderIdList){
	var blockUIString = "Loading Cancel Request...";
	blockUI(blockUIString);
	var cancelRequestObj = new Object();
	cancelRequestObj.requester = requester;
	cancelRequestObj.fromDate = fromDate;
	cancelRequestObj.toDate = toDate;
	cancelRequestObj.searchType = searchType;
	cancelRequestObj.cancelRequestIdList = orderIdList;
	cancelRequestObj.cancelRequestStatusList = statusValuesArray;
	queryStr = JSON.stringify(cancelRequestObj);
	$('#CancelRequest_Search_Details').html("");
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/raisecancelrequest",
			type: "POST",
			contentType : "application/json",
			data: queryStr,
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				$("#CancelRequest_Search_Details").html("");
				if(data.hasOwnProperty("status")){
					if(data["status"] == "FAILED"){
						$("#statusDialogBox #errorAlertMessage").text("Data not found.");
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},4000);
					}
					if(data["status"] == "SUCCESS"){
						if(data["cancelRequestList"].length>0){
								var htmlFile = '<header class="panel-heading"> Refund Orders </header><table id="cancelRequest_Search_Table" class="table table-striped table-advance table-hover"> <thead> <tr> <th><i class="fa fa-hashtag" aria-hidden="true"></i> Request ID</th><th><i class="icon_calendar"></i> Initated Date</th><th><i class="fa fa-dot-circle-o"></i> Status</th> <th><i class="fa fa-check-square"></i> Order Id</th><th><i class="icon_profile"></i> Requester</th> <th><i class="icon_cogs"></i> Action</th> </tr> </thead> <tbody>';
								var cancelRequestObjectList = data.cancelRequestList;
								for(var key = 0; key<cancelRequestObjectList.length;key++){
									var d = new Date(cancelRequestObjectList[key].requestInitiateTime);
									var formattedDate = d.getFullYear() + "-" + (((d.getMonth() + 1) <10) ? "0" + (d.getMonth() + 1):(d.getMonth() + 1)) + "-" + ((d.getDate()<10)?"0" +d.getDate():d.getDate());
									var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
									var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
									var seconds = (d.getSeconds() < 10) ? "0" + d.getSeconds() : d.getSeconds();
									var formattedTime = hours + ":" + minutes + ":" + seconds;
									formattedDate = formattedDate + " " + formattedTime;
									
									var status = cancelRequestObjectList[key].requestStatus;
									
									if(status == "Requested"){
										orderStatusLink = "<a href='#' class='label label-warning' data-toggle='modal'>"+status+"</a>";
									}
									else if(status == "Approved"){
										orderStatusLink = "<a href='#' class='label label-primary' data-toggle='modal'>"+status+"</a>";
									}
									else if(status == "Rejected"){
										orderStatusLink = "<a href='#' class='label label-danger' data-toggle='modal'>"+status+"</a>";
									}
									
									var requesterId = cancelRequestObjectList[key].initiateUser;
									var actionLink = "<a class='btn btn-primary' title='View Cancel Request' data-toggle='modal' href='#cancelrequestview"+cancelRequestObjectList[key].cancellationRequestId+"'><i class='icon_plus_alt2'></i></a>";
									actionbutton = '<button data-dismiss="modal" class="btn btn-default" type="button">Close</button>';
									
									htmlFile = htmlFile.concat('<tr><td>'+cancelRequestObjectList[key].cancellationRequestId+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+cancelRequestObjectList[key].orderid+'</td><td>'+requesterId+'</td><td><div class="btn-group">'+actionLink+'</div></td></tr>');
									var requestReason = "";
									if(cancelRequestObjectList[key].requestReason != null || cancelRequestObjectList[key].requestReason != undefined){
										requestReason = cancelRequestObjectList[key].requestReason.trim();
									}
									
									$('#CancelRequest_View_Details').append('<div class="modal fade" id="cancelrequestview'+cancelRequestObjectList[key].cancellationRequestId+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Request Id [<b>'+cancelRequestObjectList[key].cancellationRequestId+'</b>]</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">#Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+cancelRequestObjectList[key].cancellationRequestId+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Order ID:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="refund_order_view_cname">'+cancelRequestObjectList[key].orderid+'</span> </strong></div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Initiated Date:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+formattedDate+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Initiator:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+cancelRequestObjectList[key].initiateUser+'</span> </strong> </div> </div><div class="form-group"> <label for="curl" class="control-label col-lg-2">Current Status:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+orderStatusLink+'</span> </strong> </div> </div>  <div class="form-group"> <label for="cname" class="control-label col-lg-2">Approver:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+cancelRequestObjectList[key].requestApproverId+'</span> </strong> </div> </div><div class="form-group"> <label for="curl" class="control-label col-lg-2">Reason:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+requestReason+'</span> </strong> </div> </div> </form> </div> </div> <div class="modal-footer">'+actionbutton+'</div></div></div></div>');
								}
								htmlFile = htmlFile.concat('</tbody> </table>');
								$('#CancelRequest_Search_Details').html(htmlFile);
								$('#cancelRequest_Search_Table').dataTable();
						}
					}
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Data not found.");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}

/*var auto_refresh = setInterval(function () {
	$("#shopName").html();
	$("#shopRegNo").html();
	$("#errordata").html();
	$("#datavalue").html();
	$("#refreshDivCancelRequstPage").load("http://localhost:8080/OnlineMediShop/sellerrestservice/getsellerinfo/getSellerById/1000", function(response, status, xhr) {
		//$(this).fadeIn('slow');
		//$("#datavalue").html(JSON.parse(xhr.responseText));
		var jsonRespone = JSON.parse(xhr.responseText);
		if(status == "success"){
			//var data = jQuery.parseJSON(response);
			$("#shopName").html(jsonRespone.sellerShopName);
			$("#shopRegNo").html(jsonRespone.sellerRegistrationNo);
		}
		else if(status == "error"){
			var msg = "Sorry but there was an error: ";
			//alert(msg + xhr.status + " " + xhr.statusText);
			$("#errordata").html(msg + xhr.status + " " + xhr.statusText);
		}
	});
}, 25000);*/


setInterval(function () {
	$("button[id = 'load_order_status']").click();
}, 120000);

function loadOrderStatus(sellerId,loadingtype){
	var dateObj = new Date();
	var toDate = dateObj.getTime();
	var fromDate = dateObj.getTime()-2592000000;
	if(loadingtype != null){
		blockUI("Loading...");
	}
	
	$("#statusDialogBox #successAlertMessage").text("");
	$("#statusDialogBox #errorAlertMessage").text("");
	$("#statusDialogBox #errorDialog").css("display", "none");
	$("#statusDialogBox #successDialog").css("display", "none");
	
	//$("#refreshDivCancelRequstPage").load("http://localhost:8080/OnlineMediShop/sellerrestservice/getsellerinfo/getSellerById/1000", function(response, status, xhr) {
	setTimeout(function () {
		$.ajax({
			url: context+"/sellerrestservice/getordercount/"+sellerId+"/"+fromDate+"/"+toDate,
			type: "GET",
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data.hasOwnProperty("status")){
					if(data["status"] == "FAILED"){
						$("#statusDialogBox #errorAlertMessage").text(data["message"]);
						$("a[href = '#statusDialogBox']").click();
						$("#statusDialogBox #errorDialog").show();
						setTimeout(function () {
							$("button[class = 'close']").click();
						},2000);
					}
					if(data["status"] == "SUCCESS"){
						if(data.hasOwnProperty("record")){
							var record = data["record"];
							if(record.hasOwnProperty("ORDER_PENDING")){
								$("#showpendingorder").text(record["ORDER_PENDING"]);
							}
							if(record.hasOwnProperty("ORDER_CONFIRM")){
								$("#showconfirmorder").text(record["ORDER_CONFIRM"]);
							}
							if(record.hasOwnProperty("ORDER_CANCEL")){
								$("#showcancelorder").text(record["ORDER_CANCEL"]);
							}
							if(record.hasOwnProperty("ORDER_DELIVER")){
								$("#showdeliveredorder").text(record["ORDER_DELIVER"]);
							}
							
							if(record.hasOwnProperty("REFUND_PENDING")){
								$("#processrefund").text(record["REFUND_PENDING"]);
							}
							if(record.hasOwnProperty("REFUND_CONFIRM")){
								$("#confirmedrefund").text(record["REFUND_CONFIRM"]);
							}
							if(record.hasOwnProperty("REFUND_CANCEL")){
								$("#showdeliveredorder").text(record["REFUND_CANCEL"]);
							}
							if(record.hasOwnProperty("REFUND_REFUNDED")){
								$("#cancelledrefund").text(record["REFUND_REFUNDED"]);
							}
						}
					}
				}
			},
			error: function(){
				$("#statusDialogBox #errorAlertMessage").text("Error Occurs During Fetching Order!");
				$("a[href = '#statusDialogBox']").click();
				$("#statusDialogBox #errorDialog").show();
				setTimeout(function () {
					$("button[class = 'close']").click();
				},4000);
			}
		},unblockUI());
	},200);
}