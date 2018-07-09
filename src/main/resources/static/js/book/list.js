
$(function() {
    // 查询数据
    var tableObj = commonUtil.initTable({
        url: "/book/list",
        search: true,
        detailView: true,
        pagination: true,
        columns: [
            {checkbox: true},
            {field: 'bId', visible: false, title: 'ID'},
            {field: 'name', align: 'center', title: '书名', width: 150},
            {field: 'author', align: 'center', title: '作者', width: 220},
            {field: 'press', align: 'center', title: '出版社', width: 220},
            {field: 'typeName', align: 'center', title: '类别', width: 220},
            {
                field: 'status', align: 'center', title: '借阅状态', width: 100,
                formatter: function (value) {
                    if (value === 0) {
                        return '<span class="label label-success">可借</span>';
                    }
                    if (value === 1) {
                        return '<span class="label label-primary">借出</span>';
                    }
                    if (value === 2) {
                        return '<span class="label label-warning">续期</span>';
                    }
                    if (value === 3) {
                        return '<span class="label label-danger">超时</span>';
                    }
                }
            },
            {
                field: 'currentDetail', align: 'center', title: '借阅人', width: 120,
                formatter: function (value) {
                    if (value != null) {
                        return value.borrower;
                    } else {
                        return "";
                    }
                }
            },
            {
                field: 'currentDetail', align: 'center', title: '借出时间', width: 150,
                formatter: function (value) {
                    if (value != null)
                        return jsonDateFormat(value.borrowTime);
                    else
                        return "";
                }
            },
            {
                field: 'currentDetail', align: 'center', title: '续期时间', width: 150,
                formatter: function (value) {
                    if (value != null) {
                        if (value.status === 2 || value.status === 4) {
                            return value.renewal;
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                }
            },
            {
                field: 'operation', align: 'center', title: '操作', width: 100, events: {
                    'click .borrow': function (e, value, row, index) {
                        borrow2(row);
                    },
                    'click .renewal': function (e, value, row, index) {
                        renewal(row);
                    },
                    'click .return': function (e, value, row, index) {
                        returnBook(row);
                    }
                },
                formatter: function (value, row, index) {
                    if (row.status === 0) {
                        return "<a href=\"javascript:void(0)\" class=\"borrow\" style='padding: 0px 5px'>" +
                            "借阅</a>"
                    } else {
                        var html = "";
                        if (row.status === 1) {
                            html = "<a href=\"javascript:void(0)\" class=\"renewal\" style='padding: 0px 5px'>" +
                                "续期</a>"
                        }
                        html = html + "<a href=\"javascript:void(0)\" class=\"return\" style='padding: 0px 5px'>" +
                            "归还</a>"
                        return html;
                    }
                }
            }
        ]
    });

    // 图书借阅方法
    var borrow = function () {
        var list = tableObj.bootstrapTable("getSelections");

        if (list.length == 0) {
            layer.msg("请选择一本图书借阅", {
                offset: 't',
                anim: 6
            });
            return;
        }
        if (list.length > 1) {
            layer.msg("一次只能借阅一本图书", {
                offset: 't',
                anim: 6
            });
            return;
        }
        if (list[0].status != 0) {
            layer.msg("图书已借出", {
                offset: 't',
                anim: 6
            });
            return;
        }
        $.ajax({
            "type": "GET",
            "url": "/user/getList/",
            "dataType": "json",
            "success": function (resp) {
                if (resp.code == 200) {
                    var userList = resp.obj;
                    $('#userId').empty();
                    var html = [];
                    for (var i = 0; i < userList.length; i++) {
                        var obj = userList[i];
                        html.push("<option value=" + obj.userId + ">" + obj.userName + "</option>");
                    }
                    $('#userId').html(html.join(""));

                    layer.open({
                        title: "《" + list[0].name + '》借阅',
                        type: 1,
                        content: $("#bookUI"),
                        area: ['400px', '200px'],
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            var id = $("#userId").val();
                            // 发送请求
                            $.ajax({
                                "type": "POST",
                                "url": "/book/borrow",
                                "data": {bId: list[0].bId, userId: id},
                                "dataType": "json",
                                "success": function (resp) {
                                    if (resp.code == 200) {
                                        layer.msg("借阅成功!")
                                        layer.close(index);
                                        tableObj.bootstrapTable('refresh');
                                    } else {
                                        layer.alert(resp.msg, {
                                            "icon": 2
                                        });
                                    }
                                }
                            });
                        }
                    });
                } else {
                    layer.alert(resp.msg, {
                        "icon": 2
                    });
                    return;
                }
            }
        });

    }

    var borrow2 = function (row) {
        $.ajax({
            "type": "GET",
            "url": "/user/getUserWithoutBook",
            "dataType": "json",
            "success": function (resp) {
                if (resp.code == 200) {
                    var userList = resp.obj;
                    $('#userId').empty();
                    var html = [];
                    for (var i = 0; i < userList.length; i++) {
                        var obj = userList[i];
                        html.push("<option value=" + obj.userId + ">" + obj.userName + "</option>");
                    }
                    $('#userId').html(html.join(""));

                    layer.open({
                        title: "《" + row.name + '》借阅',
                        type: 1,
                        content: $("#bookUI"),
                        area: ['400px', '200px'],
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            var id = $("#userId").val();
                            // 发送请求
                            $.ajax({
                                "type": "POST",
                                "url": "/book/borrow",
                                "data": {bId: row.bId, userId: id},
                                "dataType": "json",
                                "success": function (resp) {
                                    if (resp.code == 200) {
                                        layer.msg("借阅成功!")
                                        layer.close(index);
                                        tableObj.bootstrapTable('refresh');
                                    } else {
                                        layer.alert(resp.msg, {
                                            "icon": 2
                                        });
                                    }
                                }
                            });
                        }
                    });
                } else {
                    layer.alert(resp.msg, {
                        "icon": 2
                    });
                    return;
                }
            }
        });

    }

    var renewal = function (row) {
        layer.open({
            title: "《" + row.name + '》续期',
            type: 1,
            content: $("#bookUI3"),
            area: ['400px', '120px'],
            btn: ['确定', '取消'],
            yes: function (index, layero) {
                // 发送请求
                $.ajax({
                    "type": "POST",
                    "url": "/book/return",
                    "data": {bId: row.bId, id: row.currentDetail.id,operation:2},
                    "dataType": "json",
                    "success": function (resp) {
                        if (resp.code == 200) {
                            layer.msg("续期成功!")
                            layer.close(index);
                            tableObj.bootstrapTable('refresh');
                        } else {
                            layer.alert(resp.msg, {
                                "icon": 2
                            });
                        }
                    }
                });
            }
        });
    }

    var returnBook = function (row) {

        layer.open({
            title: "《" + row.name + '》归还',
            type: 1,
            content: $("#bookUI2"),
            area: ['400px', '120px'],
            btn: ['确定', '取消'],
            yes: function (index, layero) {
                // 发送请求
                $.ajax({
                    "type": "POST",
                    "url": "/book/return",
                    "data": {bId: row.bId, id: row.currentDetail.id,operation:1},
                    "dataType": "json",
                    "success": function (resp) {
                        if (resp.code == 200) {
                            layer.msg("归还成功!")
                            layer.close(index);
                            tableObj.bootstrapTable('refresh');
                        } else {
                            layer.alert(resp.msg, {
                                "icon": 2
                            });
                        }
                    }
                });
            }
        });
    };
    // 新增方法
    var addData = function () {
        window.location.href = "/book/saveUI";
    }

    // 修改方法
    var updateData = function () {
        var list = tableObj.bootstrapTable("getSelections");
        if (list.length == 0) {
            layer.msg("请选择一条记录进行编辑", {
                offset: 't',
                anim: 6
            });
            return;
        }
        if (list.length > 1) {
            layer.msg("一次只能编辑一条记录", {
                offset: 't',
                anim: 6
            });
            return;
        }
        window.location.href = "/book/saveUI?bId=" + list[0]["bId"];
    }

    // 删除方法
    var deleteData = function () {
        var list = tableObj.bootstrapTable("getSelections");
        if (list.length == 0) {
            layer.msg("请选择至少一条记录进行删除", {
                offset: 't',
                anim: 6
            });
            return;
        }
        var ids = [];
        for (var i = 0; i < list.length; i++) {
            ids[i] = list[i]["id"];
        }

        layer.confirm("确定要删除该数据吗?", {
            icon: 3,
            title: '提示'
        }, function (index) {
            $.ajax({
                "type": "GET",
                "url": "/role/delete/" + ids.join(","),
                "dataType": "json",
                "success": function (resp) {
                    if (resp.code == 200) {
                        layer.close(index);
                        tableObj.bootstrapTable('refresh');
                    } else {
                        layer.alert(resp.msg, {
                            "icon": 2
                        });
                    }
                }
            });
        });
    }

// 绑定按钮事件
    var btns = $("#btns").find("a");
    btns.each(function (index, domEle) {
        var btn = $(domEle);
        var code = btn.data("code");
        if (code == "book:borrow") {
            btn.on("click", borrow);
        } else if (code == "book:add") {
            btn.on("click", addData);
        } else if (code == "book:update") {
            btn.on("click", updateData);
        } else if (code == "role:delete") {
            btn.on("click", deleteData);
        }
    });
})

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