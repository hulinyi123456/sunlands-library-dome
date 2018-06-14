$(function() {
	
	// 查询数据
	var tableObj = commonUtil.initTable({
        url : "/book/list",
        search : true,
        detailView : true,
        pagination : true,
        columns: [
            {checkbox: true},
            {field: 'bId', visible: false, title: 'ID'},
            {field: 'name',align: 'center', title: '书名',width:150},
            {field: 'author',align: 'center', title: '作者',width:220},
            {field: 'press',align: 'center', title: '出版社',width:220},
            {field: 'typeName',align: 'center', title: '类别',width:220},
            {field: 'status',align: 'center', title: '借阅状态',width:220,
                formatter:function (value) {
                    if (value === 0) {
                        return '<span class="label label-primary">可借</span>';
                    }
                    if (value === 1) {
                        return '<span class="label label-success">借出</span>';
                    }
                    if (value === 2) {
                        return '<span class="label label-warning">延期</span>';
                    }
                    if (value === 3) {
                        return '<span class="label label-warning">超时</span>';
                    }
                }
                },
            {field: 'currentDetail',align: 'center', title: '借阅人',width:220,
                formatter:function (value) {
            		if(value!=null){
                        return value.borrower;
                    }else {
            			return "";
					}
                }
			},
            {field: 'currentDetail',align: 'center', title: '借出时间',width:220,
                formatter:function (value) {
            		if (value!=null)
                    return jsonDateFormat(value.borrowTime);
            		else
            			return"";
                }
			},
            {field: 'currentDetail',align: 'center', title: '续期',width:220,
                formatter:function (value) {
                    if (value!=null)
                        return value.renewal;
                    else
                        return"";
                }
            }
        ]
    });

	var ztreeObj = null;
	// 设置权限方法
	var setPermission = function() {
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
        $.ajax({
			"type" : "GET",
			"url" : "/permission/getPermissionListWithChecked/"+list[0].roleId,
			"dataType" : "json",
			"success" : function(resp) {
				if (resp.code == 200) {
					// 生成树
					ztreeObj = $.fn.zTree.init($("#permissionTree"), getZTreeSetting(), resp.obj);

					layer.open({
			          title: "【"+list[0].name+'】关联权限',
			    	  type: 1,
			    	  content: $("#permissionUI"),
			    	  area: ['400px', '350px'],
			    	  btn: ['保存', '取消'],
			    	  yes: function(index, layero){
			    		  var nodes = ztreeObj.getCheckedNodes(true);
			              var permissionIds = [];
			              if (nodes.length > 0) {
			                  for (var i = 0; i < nodes.length; i++) {
			                	  permissionIds.push(nodes[i].permissionId);
			                  }
			              }
			        	    // 发送请求
			        	    $.ajax({
			    				"type" : "POST",
			    				"url" : "/role/setPermission",
			    				"data":{roleId:list[0].roleId,permissionIds:permissionIds.join(",")},
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
		window.location.href = "/role/saveUI";
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
		window.location.href = "/role/saveUI?id="+list[0]["id"];
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
				"url" : "/role/delete/" + ids.join(","),
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
	
	//ztree设置
    function getZTreeSetting() {
        return {
            check: {
                enable: true,
                chkboxType: { "Y": "p", "N": "s" }
            },
            view: {
                dblClickExpand: false,
                showLine: true,
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable: true,
					//设置权限的主键
                    idKey: "permissionId",
                    pIdKey: "pid",
                    rootPId: ""
                }
            },
            callback: {
                beforeClick: function(treeId, treeNode) {
                    var zTree = $.fn.zTree.getZTreeObj("permissionTree");
                    if (treeNode.isParent) {
                        zTree.expandNode(treeNode);
                    }
                    return false;
                }
            }
        };
    }

	// 绑定按钮事件
	var btns = $("#btns").find("a");
	btns.each(function(index, domEle) {
		var btn = $(domEle);
		var code = btn.data("code");
		if (code == "role:setPermission") {
			btn.on("click", setPermission);
		} else if (code == "role:add") {
			btn.on("click", addData);
		} else if(code == "role:update"){
			btn.on("click", updateData);
		}else if (code == "role:delete") {
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