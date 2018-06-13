$(function() {
	
	$("#submit-btn").on("click",function() {
		var $userName = $("#user-name").val();
		var $password = $("#password").val();
		//判断checkbox是否选中
		var $rememberMe = $("#rememberMe").is(":checked");
		
		if ($userName == "") {
			alert("用户名不能为空!");
			return;
		}
		
		if ($password == "") {
			alert("用户名不能为空!");
			return;
		}
		
		$.ajax({
			"type": "post",
			"url": "/ajaxLogin",
			"data":{"userName":$userName,"password":$password,"rememberMe":$rememberMe},
			"success" : function(resp,status) {
                if (resp.code == 200) {
                    window.location.href = resp.obj;
                } else {
                    alert(resp.msg);
                    return;
                }
			},
			"error":function (XMLHttpRequest) {
				alert(XMLHttpRequest.status);
            }
		});
	});
	
	$(document).on("keydown",function(e) {
		if (e.keyCode == 13) {
			$("#submit-btn").trigger("click");
		}
	});
	
});