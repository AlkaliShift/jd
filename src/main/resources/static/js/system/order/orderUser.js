layui.use(['form', 'layer', 'treeGrid'], function () {
    var form = layui.form
        , layer = layui.layer
        , treeGrid = layui.treeGrid
        , $ = layui.$;
    form.render();

    loadTreeGrid($("#content").val());

    function loadTreeGrid(content) {
        $.ajax({
            type: 'GET',
            url: '/order/listUser?content=' + content,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (data.statusCode === 1) {
                    var dataTreeGrid = data.orders;
                    treeGridFunction(dataTreeGrid);
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    }

    function treeGridFunction(dataTreeGrid){
        treeGrid.render({
            elem: '#treeGrid'
            , data: dataTreeGrid
            , treeId: 'orderId'//树形id字段名称
            , treeUpId: 'orderPid'//树形父id字段名称
            , treeShowName: 'orderId'//以树形式显示的字段
            , cols: [[
                {field: 'orderId', width: 200, title: '订单ID'}
                , {field: 'userId', width: 100, title: '用户ID'}
                , {field: 'orderPid', width: 150, title: '父订单ID'}
                , {
                    field: 'totalPrice', width: 100, title: '订单总价', templet: function (data) {
                        return parseFloat(data.totalPrice).toFixed(2);
                    }
                }
                , {
                    field: 'orderStatus', width: 100, title: '订单状态', templet: function (data) {
                        var orderPid = data.orderPid;
                        var orderStatus = data.orderStatus;
                        if (orderPid === "" || orderStatus === "cancelled" || orderStatus === "completed") {
                            $('#operation').html("");
                        } else if (orderStatus === "delivered") {
                            $('#operation').html("<a class=\"layui-btn layui-btn-xs layui-btn-danger\" " +
                                "lay-event=\"completed\">确认收货</a>" +
                                "<a class=\"layui-btn layui-btn-xs layui-btn\" lay-event=\"details\">订单详情</a>");
                        } else {
                            $('#operation').html("<a class=\"layui-btn layui-btn-xs layui-btn-danger\" " +
                                "lay-event=\"completed\">确认收货</a>\n" +
                                "<a class=\"layui-btn layui-btn-xs layui-btn-danger\" lay-event=\"cancel\">取消订单</a>" +
                                "<a class=\"layui-btn layui-btn-xs layui-btn\" lay-event=\"details\">订单详情</a>");
                        }
                        return orderStatus;
                    }
                }
                , {field: 'orderTime', width: 160, title: '下单时间'}
                , {field: 'arrivalTime', width: 160, title: '到货时间'}
                , {field: 'address', width: 100, title: '用户地址'}
                , {title: '操作', align: 'center', width: 260, toolbar: '#operation'}
            ]]
            , id: 'treeGrid'
            , page: false
        });

        treeGrid.on('tool(treeGridType)', function (obj) {
            var order = {};
            var orderId = obj.data.orderId;
            order.orderId = orderId;
            var layEvent = obj.event;
            var action = '/order/setOrderStatus';
            if (layEvent === 'completed') {//确认收货
                order.orderStatus = "completed";
                $.ajax({
                    type: 'POST',
                    url: action,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(order),
                    success: function (data) {
                        if (data.statusCode === 1) {
                            layer.msg("确认收货成功");
                            reloadTable($('#content').val());
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                });
            } else if (layEvent === 'cancel') {//取消
                layer.confirm('取消选中订单,确定取消?'
                    , {icon: 0, title: '取消'}, function (index) {
                        order.orderStatus = "cancelled";
                        $.ajax({
                            type: 'POST',
                            url: action,
                            contentType: "application/json; charset=utf-8",
                            data: JSON.stringify(order),
                            success: function (data) {
                                if (data.statusCode === 1) {
                                    layer.msg("取消成功");
                                    layer.close(index);
                                    reloadTable($('#content').val());
                                } else {
                                    layer.msg(data.msg);
                                }
                            }
                        });
                        layer.close(index);
                    });
            } else if (layEvent === 'details') {//详情
                layer.open({
                    type: 2,
                    content: '/order/orderDetails?orderId=' + orderId,
                    area: ['600px', '300px'],
                    closeBtn: 2,
                    shadeClose: true,
                    title: '订单详情'
                });
            }
        });
    }

    $('#search').on('click', function () {
        loadTreeGrid($('#content').val());
    });

    $('#reset').on('click', function () {
        $('#content').val("");
    });

    $('#ordered').on('click', function () {
        loadTreeGrid('ordered');
    });

    $('#delivered').on('click', function () {
        loadTreeGrid('delivered');
    });

    $('#cancelled').on('click', function () {
        loadTreeGrid('cancelled');
    });
});