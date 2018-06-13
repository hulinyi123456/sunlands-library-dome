$(function() {
	
	// 查询数据
	var tableObj = commonUtil.initTable({
        url : "/user/list",
        search : true,
        detailView : true,
        pagination : true,
        columns: [
            {checkbox: true},
            {field: 'userId', visible: false, title: 'ID'},
            {field: 'userName',align: 'center', title: '用户名',width:150},
            {field: 'phone',align: 'center', title: '手机号码',width:200},
            {field: 'email',align: 'center', title: '邮箱地址',width:300},
            {field : 'status', title : '状态',align: 'center',width:100, formatter : function(value) {return value == 1 ? "<span class='label label-success'>开启</span>" : "<span class='label label-danger'>禁用</span>"; } },
            {field : 'createTime', align: 'center',title : '创建时间',
				formatter:function (value) {
					return jsonDateFormat(value);
                }
			},
            {field : 'updateTime',align: 'center', title : '修改时间',
                formatter:function (value) {
                    return jsonDateFormat(value);
                }
			}
        ]
    });
	
	// 设置角色方法
	var setRole = function() {
		var list = tableObj.bootstrapTable("getSelections");
        if (list.length ==0) {
            layer.msg("请选择一条记录进行编辑",{
				offset : 't',
				anim : 6
			});
            return;
        }
        if (list.length > 1) {
             layer.msg("一次只能编辑一条记录",{
				offset : 't',
				anim : 6
			});
            return;
        }
        
     // 发送请求
	    $.ajax({
			"type" : "GET",
			"url" : "/role/getRoleListWithSelected/"+list[0].userId,
			"dataType" : "json",
			"success" : function(resp) {
				if (resp.code == 200) {
					var roleList = resp.obj;
					$('#roleIds').empty();
					var html = [];
					for(var i=0; i<roleList.length; i++) {
						var obj = roleList[i];
						if (obj.selected) {
							html.push("<option value="+obj.roleId+" selected='selected'>"+obj.name+"</option>");
						} else {
							html.push("<option value="+obj.roleId+">"+obj.name+"</option>");
						}
					}
					$('#roleIds').html(html.join(""));
					
					layer.open({
				          title: "【"+list[0].userName+'】关联角色',
				    	  type: 1,
				    	  content: $("#roleUI"),
				    	  area: ['400px', '250px'],
				    	  btn: ['保存', '取消'],
				    	  yes: function(index, layero){
				    		  	var ids = $("#roleIds").val();
				    		  	//alert(ids);
				        	    // 发送请求
				        	    $.ajax({
				    				"type" : "POST",
				    				"url" : "/user/setRole",
				    				"data":{userId:list[0].userId,roleIds:ids.join(",")},
				    				"dataType" : "json",
				    				"success" : function(resp) {
				    					if (resp.code == 200) {
				    						layer.msg("设置成功!")
				    						layer.close(index);
				    						tableObj.bootstrapTable('refresh');
				    					} else {
				    						layer.alert(resp.msg, {
				    							"icon" : 2
				    						});
				    					}
				    				}
				    			});
					         }
				        });
				} else {
					layer.alert(resp.msg, {
						"icon" : 2
					});
					return;
				}
			}
		});
        
	}
	
	// 新增方法
	var addData = function() {
		window.location.href = "/user/saveUI";
	}
	
	// 修改方法
	var updateData = function() {
		var list = tableObj.bootstrapTable("getSelections");
        if (list.length ==0) {
            layer.msg("请选择一条记录进行编辑",{
				offset : 't',
				anim : 6
			});
            return;
        }
        if (list.length > 1) {
             layer.msg("一次只能编辑一条记录",{
				offset : 't',
				anim : 6
			});
            return;
        }
		window.location.href = "/user/saveUI?id="+list[0]["userId"];
	}

	// 删除方法
	var deleteData = function() {
		var list = tableObj.bootstrapTable("getSelections");
        if (list.length == 0) {
            layer.msg("请选择至少一条记录进行删除",{
				offset : 't',
				anim : 6
			});
            return;
        }
        var ids = [];
        for(var i=0; i<list.length; i++) {
            ids[i] = list[i]["id"];
        }

		layer.confirm("确定要删除该数据吗?", {
			icon : 3,
			title : '提示'
		}, function(index) {
			$.ajax({
				"type" : "GET",
				"url" : "/user/delete/" + ids.join(","),
				"dataType" : "json",
				"success" : function(resp) {
					if (resp.code == 200) {
						layer.close(index);
						tableObj.bootstrapTable('refresh');
					} else {
						layer.alert(resp.msg, {
							"icon" : 2
						});
					}
				}
			});
		});
	}

	// 绑定按钮事件
	var btns = $("#btns").find("a");
	btns.each(function(index, domEle) {
		var btn = $(domEle);
		var code = btn.data("code");
		if (code == "user:setRole") {
			btn.on("click", setRole);
		} else if (code == "user:add") {
			btn.on("click", addData);
		} else if(code == "user:update"){
			btn.on("click", updateData);
		}else if (code == "user:delete") {
			btn.on("click", deleteData);
		}
	});

});

function jsonDateFormat(jsonDate) {
    //json日期格式转换为正常格式
    var jsonDateStr = jsonDate.toString();//此处用到toString（）是为了让传入的值为字符串类型，目的是为了避免传入的数据类型不支持.replace（）方法
    try {
        var k = parseInt(jsonDateStr.replace("/Date(", "").replace(")/", ""), 10);
        if (k < 0)
            return null;

        var date = new Date(parseInt(jsonDateStr.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        var milliseconds = date.getMilliseconds();
        return date.getFullYear() + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
    }
    catch (ex) {
        return "时间格式转换错误";
    }
}