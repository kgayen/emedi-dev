$( document ).ready(function() {
	
	$('#refund_order_Search_Table').dataTable();
	
	$('#datetimepicker5').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$('#datetimepicker6').datetimepicker({
		format: 'YYYY-MM-DD hh:mm A'
	});
	
	$("#refundByDateOtion").click(function(){
		$('#refunddateRange').show();
		$('#refundorderid').hide();
	});
	
	$("#refundByOrderIdOtion").click(function(){
		$('#refunddateRange').hide();
		$('#refundorderid').show();
	});
	
	$("#returnOrderClick").click(function(){
		
		var orderId = $("#returnOrderId").val();
		var billNo = $("#returnBillNo").val();
		var flag = true;
		if(orderId == "" || orderId == undefined){
			$("#returnErrorMessage").html();
			$("#returnErrorMessage").html("Please provide Order Id. Order Id should not be blank.");
			$('#returnErrorDiv').show();
			setTimeout(function () {
				$('#returnErrorDiv').hide();
			},3000);
			flag = false;
			return false;
		}
		if(billNo == "" || billNo == undefined){
			$("#returnErrorMessage").html();
			$("#returnErrorMessage").html("Please provide Bill No. Bill No should not be blank.");
			$('#returnErrorDiv').show();
			setTimeout(function () {
				$('#returnErrorDiv').hide();
			},3000);
			flag = false;
			return false;
		}
		if(flag){
			var context = $("#contextValue").val();
			blockUI("Verifying Order...");
			var postObject = new Object();
			postObject["orderid"] = orderId;
			postObject["billNo"] = billNo;
			queryStr = JSON.stringify(postObject);
			setTimeout(function () {
				$.ajax({
					url: context+"/orderservice/refundordervalidation",
					type: "POST",
					data: queryStr,
					contentType : "application/json",
					dataType: "json",
					async: false,
					cache: false,
					success: function(data){
						if(data["status"] == undefined && data["error"].length>0){
							$("#errorAlertMessage").html();
							$("#errorAlertMessage").html(data.error[0].errorMsg);
							$('#successModifiedErrorAlert').show();
							setTimeout(function () {
								$('#successModifiedErrorAlert').hide();
							},5000);
						}
						else{
							if(data != null){
								if(data["status"] == "success"){
									/*alert(data["orderid"]);
									alert(data["cashmemono"]);
									alert(data["price"]);*/
									$("#refundRequestOrderId").val(data["orderid"]);
									$("#refundRequestBillNo").val(data["cashmemono"]);
									$("#refundRequestAmount").val(data["price"]);
									$("#refundRequestSellerId").val(data["sellerid"]);
									$("a[href = '#returnRequest']").click();
								}
							}
							else{
								$("#errorAlertMessage").html();
								$("#errorAlertMessage").html("No Order Available!");
								$('#successModifiedErrorAlert').show();
								setTimeout(function () {
									$('#successModifiedErrorAlert').hide();
								},5000);
							}
						}
					},
					error: function(){
						$("#returnErrorMessage").html();
						$("#returnErrorMessage").html("Error Occurs During Fetching Order!");
						$('#returnErrorDiv').show();
						setTimeout(function () {
							$('#returnErrorDiv').hide();
						},3000);
					}
				},unblockUI());
			},200);
		}
	});
	
	
	$("#refundRequestClick").click(function(){
		//var termsAndCondition = $("#refundTermsCondition").val();
		var termsAndCondition = $("input[id='refundTermsCondition']:checked").val();
		var refundConditionFlag = true;
		if(termsAndCondition == undefined){
			$('#refundRequestErrorMessage').html();
			$('#refundRequestErrorMessage').html("Please, check the Terms and Conditions. Read before check.");
			$('#refundRequestErrorDiv').show();
			setTimeout(function () {
				$('#refundRequestErrorDiv').hide();
				$('#refundTermsCondition').focus();
			},5000);
			refundConditionFlag = false;
			return false;
		}
		
		var validReasonText = $("#refundRequestReason").val();
		if(validReasonText == undefined || validReasonText == ""){
			$('#refundRequestErrorMessage').html();
			$('#refundRequestErrorMessage').html("Please, provide valid refund reason.");
			$('#refundRequestErrorDiv').show();
			setTimeout(function () {
				$('#refundRequestErrorDiv').hide();
				$('#refundRequestReason').focus();
			},5000);
			refundConditionFlag = false;
			return false;
		}
		
		if(refundConditionFlag){
			var refundRequestPostObject = new Object();
			refundRequestPostObject.refundOrderid = $("#refundRequestOrderId").val();
			refundRequestPostObject.refundOrderStatus = "Pending";
			refundRequestPostObject.reundReason = $("#refundRequestReason").val();;
			refundRequestPostObject.refundCashmemono = $("#refundRequestBillNo").val();
			refundRequestPostObject.refundAmount = $("#refundRequestAmount").val();
			refundRequestPostObject.refundSellerCommt = "";
			refundRequestPostObject.sellerID = $("#refundRequestSellerId").val();
			//refundRequestPostObject.refundRequestId = "RFO"+$("#refundRequestOrderId").val()+$("#refundRequestBillNo").val();
			queryStr = JSON.stringify(refundRequestPostObject);
			refundRequestCall(queryStr);
		}
	});
	
	
	$("#refundorderSearchByAdvance").click(function(){
		var optionValue = $("input[name='option']:checked").val();
		if(optionValue == undefined || optionValue == "" || optionValue == null){
			$('#refundadvanceErrorMessageSpan').html("Please, choose one option.").show();
			$('#refundadvanceErrorMessage').show();
			setTimeout(function () {
				$('#refundadvanceErrorMessage').hide();
			},10000);
			return false;
		}
		else{
			var statusValues = $("input[name='refundStatusOption']:checked");
			if(statusValues.length == 0){
				$('#refundadvanceErrorMessageSpan').html("Please, choose atleast one status option.").show();
				$('#refundadvanceErrorMessage').show();
				setTimeout(function () {
					$('#refundadvanceErrorMessage').hide();
				},10000);
				return false;
			}
			else{
				var statusValuesArray = new Array();
				for(var i = 0;i<statusValues.length;i++){
					statusValuesArray[i] = statusValues[i].value;
				}
				if(optionValue == "refundByDateOtion"){
					var fromDate = $("input[name='refundstartDate']").val();
					var toDate = $("input[name='refundendDate']").val();
					fromDate = new Date(fromDate);
					fromDate = fromDate.getTime();
					toDate = new Date(toDate);
					toDate = toDate.getTime();
					if(fromDate>=toDate){
						$("#refundadvanceErrorMessageSpan").text("From date should not be greater than or equal to todate");
						$('#refundadvanceErrorMessage').show();
						setTimeout(function () {
							$('#refundadvanceErrorMessage').hide();
						},3000);
						return false;
					}
					else{
						//loadRefundOrders("ByAdvanceSearch",fromDate,toDate,null,statusValuesArray,new Array("refundByDateOtion"));
						loadRefundOrders("",optionValue,fromDate,toDate,null,statusValuesArray);
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},3000);
					}
				}
				else if(optionValue == "refundByOrderIdOtion"){
					var orderIdList = $("textarea[name='refundadvanceSearchorderID']").val();
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
						$('#refundadvanceErrorMessageSpan').html("<b>"+eventValue + " </b>is invalid character to search. Please use comma as a separator.");
						$('#refundadvanceErrorMessage').show();
						setTimeout(function () {
							$('#refundadvanceErrorMessage').hide();
						},10000);
						return false;
					}
					if(checkCharFlag){
						orderIdList = orderIdList.split(",");
						//loadRefundOrders("ByAdvanceSearch",null,null,orderIdList,statusValuesArray,new Array("refundByOrderIdOtion"));
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},3000);
					}
				}
			}
		}		
	});
	
});

var context = $("#contextValue").val();

function blockUIForMessage(loadingString){
	top.$.blockUI({
		baseZ:"9999",
		overlayCSS:{
			backgroundColor:"#B8B8B8"
		},
		message: "<i class='fa fa-check-square-o'></i> <strong id='successAlertMessage'>"+loadingString+"</strong>"
	});
}

function unblockUIForMessage(){
	top.$.unblockUI();
	$(".blockUIForMessage").fadeOut("slow"); 
}


function refundRequestCall(queryStr){
		blockUI("Initiate Refund Request...");
		setTimeout(function () {
			$.ajax({
				url: context+"/orderservice/refundorderrequest",
				type: "POST",
				data: queryStr,
				contentType : "application/json",
				dataType: "json",
				async: false,
				cache: false,
				success: function(data){
					if(data["status"] == undefined && data["error"].length>0){
						$("#errorAlertMessage").html();
						$("#errorAlertMessage").html(data.error[0].errorMsg);
						$('#successModifiedErrorAlert').show();
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},5000);
					}
					else{
						if(data != null){
							if(data["status"] == "success"){
								alert(data["status"]);
							}
						}
						else{
							$("#errorAlertMessage").html();
							$("#errorAlertMessage").html("No Order Available!");
							$('#successModifiedErrorAlert').show();
							setTimeout(function () {
								$('#successModifiedErrorAlert').hide();
							},5000);
						}
					}
				},
				error: function(){
					$("#returnErrorMessage").html();
					$("#returnErrorMessage").html("Error Occurs During Fetching Order!");
					$('#returnErrorDiv').show();
					setTimeout(function () {
						$('#returnErrorDiv').hide();
					},3000);
				}
			},unblockUI());
		},200);
}

function loadRefundOrders(userid,loadRefundOrders,fromDate,toDate,refundOrderIdList,refundOrderStatusList){
	blockUI("Loading Refund Orders...");
	var confirmBar = "<div class='progress-bar progress-bar-info'  style='width: 20.4%;padding-right: 50px;'></div>";
	var pendingBar = "<div class='progress-bar progress-bar-warning'  style='width: 20.4%;padding-right: 50px;'></div>";
	var cancelBar = "<div class='progress-bar progress-bar-danger'  style='width: 20.4%;padding-right: 50px;'></div>";
	var deliverBar = "<div class='progress-bar progress-bar-success'  style='width: 20.4%;padding-right: 50px;'></div>";
	$('#Order_Search_Details').html("");
	var refundSearchObject = new Object();
	refundSearchObject.userId = userid;
	refundSearchObject.refundSearchType = loadRefundOrders;
	refundSearchObject.fromDate = fromDate;
	refundSearchObject.toDate = toDate;
	refundSearchObject.refundOrderIdList = refundOrderIdList;
	refundSearchObject.refundOrderStatusList = refundOrderStatusList;
	queryStr = JSON.stringify(refundSearchObject);
	setTimeout(function () {
		$.ajax({
			url: context+"/orderservice/getRefundOrders",
			type: "GET",
			data: "searchRefundOrder="+queryStr,
			contentType : "application/json",
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
								htmlFile = htmlFile.concat('<tr><td>'+refundOrderObjectList[key].refunRequestNo+'</td><td>'+formattedDate+'</td><td>'+orderStatusLink+'</td><td>'+price+'</td><td>'+refundOrderObjectList[key].refundOrderid+'</td><td>'+refundOrderObjectList[key].cashmemoNo+'</td><td><div class="btn-group"><a class="btn btn-primary" title="View Refund Order" data-toggle="modal" href="#refundorderview'+refundOrderObjectList[key].refunRequestNo+'"><i class="icon_plus_alt2"></i></a><a class="btn btn-danger" title="Cancel Refund Order" data-toggle="modal" href="#refundOrderCancel'+refundOrderObjectList[key].refunRequestNo+'"><i class="icon_close_alt2"></i></a></div></td></tr>');
								$('#Refund_Order_View_Details').append('<div class="modal fade" id="refundorderview'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Refund Order [<b>'+refundOrderObjectList[key].refunRequestNo+'</b>]</h4> </div> <div class="modal-body"> <div class="form"> <form class="form-horizontal"> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Request Id:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderid">'+refundOrderObjectList[key].refunRequestNo+'</span> </strong> </div> </div><div class="progress progress-sm">'+orderStatusBar+'</div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Order ID:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="refund_order_view_cname">'+refundOrderObjectList[key].refundOrderid+'</span> </strong><input id="refund_seller_id" type="hidden" value="'+sellerId+'"/> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Cashmemo:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+refundOrderObjectList[key].cashmemoNo+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Initiator:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_cname">'+refundOrderObjectList[key].refundInitiateUser+'</span> </strong> </div> </div><div class="form-group"> <label for="curl" class="control-label col-lg-2">Mobile:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+refundOrderObjectList[key].initiatorMobileNo+'</span> </strong> </div> </div> <div class="form-group"> <label for="curl" class="control-label col-lg-2">Email:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_mobile">'+refundOrderObjectList[key].initiatorEmailAddress+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Address:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_address">'+refundOrderObjectList[key].initiatorAddress+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Refund Amount:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+price+'</span> </strong> </div> </div><div class="form-group"> <label for="cname" class="control-label col-lg-2">Reason:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_orderdetails">'+refundOrderObjectList[key].refundReason+'</span> </strong> </div> </div> <div class="form-group"> <label for="cname" class="control-label col-lg-2">Seller Comment:</label> <div class="col-lg-10"> <strong> <span class="form-control" id="order_view_price">'+refundOrderObjectList[key].refundOrderSellerComt+'</span> </strong> </div> </div></form> </div> </div> <div class="modal-footer"><button class="btn btn-danger" type="button" data-toggle="modal" data-dismiss="modal" href="#refundOrderCancel'+refundOrderObjectList[key].refunRequestNo+'" title="Cancel Refund Order">Cancel Refund Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
								var refundOrderCancelHtml  = '<div class="modal fade" id="refundOrderCancel'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Cancel Refund Order[<b>'+refundOrderObjectList[key].refunRequestNo+'</b>]</h4> </div> <div class="modal-body"><div class="form"> <form class="form-horizontal" id="cust_refundorder_cancel_form_'+refundOrderObjectList[key].refunRequestNo+'"><div class="alert alert-info fade in"><strong>Are you really want to cancel '+refundOrderObjectList[key].refunRequestNo+' refund order?</strong></div></form></div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-danger" id="cust_refundorder_cancel_button_'+refundOrderObjectList[key].refunRequestNo+'" type="button" onclick="cancelRefundOrder(\''+refundOrderObjectList[key].refunRequestNo+'\')">Cancel Refund Order</button><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>';
								$('#Refund_Order_Cancel_View').append(refundOrderCancelHtml);
								$('#Refund_Order_Status_View').append('<div class="modal fade" id="orderStatus'+refundOrderObjectList[key].refunRequestNo+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> <h4 class="modal-title">Refund Order Status['+refundOrderObjectList[key].refunRequestNo+']</h4> </div> <div class="modal-body"><div class="progress progress-sm">'+orderStatusBar+'</div><div><b>Note</b>: '+refundOrderObjectList[key].refundOrderSellerComt.trim()+'</div></div><div class="modal-footer"><button data-dismiss="modal" class="btn btn-default" type="button">Close</button></div></div></div></div>');
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


function cancelRefundOrder(requestid){
	blockUI("Cancel Refund Order...");
	var refundOrderid = $("#refundorderview"+requestid+" span[id='refund_order_view_cname']").text();
	var refundSellerid = $("#refundorderview"+requestid+" input[id='refund_seller_id']").val();
	var refundSearchObject = new Object();
	refundSearchObject.refundOrderid = refundOrderid;
	refundSearchObject.refundOrderStatus = "Cancel";
	refundSearchObject.refundRequestId = requestid;
	refundSearchObject.sellerID = refundSellerid;
	queryStr = JSON.stringify(refundSearchObject);
	setTimeout(function () {
		$.ajax({
			url: context+"/orderservice/changerefundorderstatus",
			type: "POST",
			data: queryStr,
			contentType : "application/json",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data){
				if(data["status"] == undefined && data["error"].length>0){
					$("#errorAlertMessage").html();
					$("#errorAlertMessage").html(data.error[0].errorMsg);
					$('#successModifiedErrorAlert').show();
					setTimeout(function () {
						$('#successModifiedErrorAlert').hide();
					},5000);
				}
				else{
					if(data != null){
						if(data["status"] == "success"){
							//$("#successAlertMessage").html();
							//$("#successAlertMessage").html(data["message"]);
							//$('#successModifiedAlert').show();
							
							blockUIForMessage(data["message"]);
							setTimeout(function () {
								unblockUIForMessage();
								window.location.reload(true);
							},5000);
						}
					}
					else{
						$("#errorAlertMessage").html();
						$("#errorAlertMessage").html("No Order Available!");
						$('#successModifiedErrorAlert').show();
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},5000);
					}
				}
			},
			error: function(){
				$("#returnErrorMessage").html();
				$("#returnErrorMessage").html("Error Occurs During Fetching Order!");
				$('#returnErrorDiv').show();
				setTimeout(function () {
					$('#returnErrorDiv').hide();
				},3000);
			}
		},unblockUI());
	},200);
}