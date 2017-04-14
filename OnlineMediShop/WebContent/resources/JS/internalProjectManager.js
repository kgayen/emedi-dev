function blockUI(){
	top.$.blockUI({
		baseZ:"9999",
		overlayCSS:{
			backgroundColor:"#B8B8B8"
		},
		message: "<i class='fa fa-spinner fa-spin fa-5x fa-fw margin-bottom'></i> <span class='search-icon-text'>Searching...</span>"
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

$(document).keypress(function(e){
    if (e.which == 13){
        $("#search-button").click();
    }
});


function loadSearchResults(){
	searchUser(getUrlParameter('query'));
}

function searchUser(key){
	$(".search-results div").remove();
	$("#successErrorMessage").css("display","none");
	blockUI();
	/*$.ajax({
		url: "searchJson",
		type: "GET",
		data : "query="+key,//$("#search-key").val(),
		contentType : "application/json",
		dataType: "json",
		success: function(data){
//			console.log(data);
			for(var i=0;i<data.length;i++){
//				console.log(data[i].empName);
				var $tempDiv=$("<div>",{class: 'row user-details'});
				$(".search-results").append($tempDiv);
				var $imgDiv=$("<div>",{class:'col-md-3 col-sm-12 col-xs-12'});
				if(data[i].empProfilePicEncoded!=null && data[i].empProfilePicEncoded!="null" && data[i].empProfilePicEncoded.length!=0){
					$imgDiv.append("<img alt='' src='data:image/png;base64,"+data[i].empProfilePicEncoded+"'"+
													" class='img-rounded img-responsive'>");
				} else{
					$imgDiv.append("<img alt='' src='../resources/img/default.gif'"+
						" class='img-rounded img-responsive'>");
				}
				$tempDiv.append($imgDiv);
				var $contentDiv=$("<div>",{class: 'col-md-9 col-sm-12 col-xs-12'});
				$contentDiv.append("<p class='lead'><a href='#'>"+data[i].empName+"</a></p>");
				$contentDiv.append("<p><b>Employee ID: </b>"+data[i].empId+"</p>");
				$contentDiv.append("<p><b>Sex: </b>"+data[i].empGender+"</p>");
				$contentDiv.append("<p><b>Email ID: </b>"+data[i].empTcsEmail+"</p>");
				$contentDiv.append("<p><b>Project: </b>"+data[i].empProjName+"</p>");
				$tempDiv.append($contentDiv);
			}
			unblockUI();
		},
		error: function(){
			$("#successErrorMessage").html("<span class='glyphicon glyphicon-remove-circle'><b> Failure: </b>" +
					"</span> Oops something's not right here!");
			$("#successErrorMessage").css("display","block");
			$("#successErrorMessage").addClass("alert alert-danger");
			unblockUI();
		}
	});*/
}

function loadDashboardStats(){
	//$("#successErrorMessage").css("display","none");
	blockUI();
	$.ajax({
		url: contextPath+"/getDashboardStats",
		type: "GET",
		contentType : "application/json",
		dataType: "json",
		success: function(data){
//			console.log(data);
			$("#userCount").html(data.userRegistrationCount);
			$("#assetCount").html(data.assetRegistrationCount);
			$("#searchCount").html(data.searchCount);
			$("#pageHits").html(data.pageHitCounter);
			unblockUI();
		},
		error: function(){
			$("#successErrorMessage").html("<span class='glyphicon glyphicon-remove-circle'><b> Failure: </b>" +
					"</span> Oops something's not right here!");
			$("#successErrorMessage").css("display","block");
			$("#successErrorMessage").addClass("alert alert-danger");
			unblockUI();
		}
	});
}