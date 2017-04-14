/**
 * 
 */

$( document ).ready(function() {
	
	$("#retrievePasswordButton").click(function(){
		var mobileNoFieldValue = $("#forgetUserIdField").val();
		$('#forgetPasswordErrorMessageSpan').html("");
		if(mobileNoFieldValue == "" || mobileNoFieldValue == undefined){
			$('#forgetPasswordErrorMessageSpan').html("Mobile number should not be blank.").show();
			$('#forgetPasswordErrorMessage').show();
			setTimeout(function () {
				$('#forgetPasswordErrorMessage').hide();
			},6000);
			return false;
		}
		else if(mobileNoFieldValue.length <10){
			$('#forgetPasswordErrorMessageSpan').html("Please enter 10 digit mobile number.").show();
			$('#forgetPasswordErrorMessage').show();
			setTimeout(function () {
				$('#forgetPasswordErrorMessage').hide();
			},6000);
			return false;
		}
		else{
			sendPassword(mobileNoFieldValue);
		}
		
		//return false;
	});
});

var contextPath = $("#contextValue").val();

function sendPassword(mobileNoFieldValue){
	//alert("sendPassword ::"+mobileNoFieldValue);
	blockUI("Processing...");
	var postObject = new Object();
	//postObject["mobilenumber"] = 
	queryStr = JSON.stringify(postObject);
	setTimeout(function () {
		$.ajax({
			url: contextPath+"/authenticationservice/retrive/forgetpassword",
			type: "POST",
			data: "mobilenumber="+btoa(mobileNoFieldValue),
			contentType : "application/x-www-form-urlencoded",
			dataType: "json",
			async: false,
			cache: false,
			success: function(data, textStatus, xhr){
				console.log("message:"+data["message"]);
				//console.log(textStatus);
				if(data["message"].length>0){
					var message = data["message"];
					var statusMessage = message.split(":")[0].trim();
					message = message.split(":")[1].trim();
					if(statusMessage == "FAILED"){
						$("#errorAlertMessage").html();
						$("#errorAlertMessage").html(message);
						$('#successModifiedErrorAlert').show();
						$('#successModifiedErrorAlert').focus();
						setTimeout(function () {
							$('#successModifiedErrorAlert').hide();
						},5000);
					}
					else if(statusMessage == "SUCCESS"){
						$("#successAlertMessage").html();
						$("#successAlertMessage").html(message);
						$('#successModifiedAlert').show();
						$('#successModifiedAlert').focus();
						setTimeout(function () {
							$('#successModifiedAlert').hide();
						},5000);
					}
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
    //return false;
}