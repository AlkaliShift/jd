layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form
        , layer = layui.layer
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    table.render({
        elem: '#orders'
        , url: '/order/listUser'
        , where: {content: $("#content").val()}
        , response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            , statusCode: 1 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "statusCode": res.statusCode,
                "data": res.orders,
                "msg": res.msg
            }
        }
        , cols: [[
            {field: 'orderId', width: 300, title: '订单ID'}
            , {field: 'userId', width: 100, title: '用户ID'}
            , {field: 'orderPid', width: 300, title: '父订单'}
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
                            "lay-event=\"completed\">确认收货</a>");
                    } else {
                        $('#operation').html("<a class=\"layui-btn layui-btn-xs layui-btn-danger\" " +
                            "lay-event=\"completed\">确认收货</a>\n" +
                            "<a class=\"layui-btn layui-btn-xs layui-btn-danger\" lay-event=\"cancel\">取消订单</a>");
                    }
                    return orderStatus;
                }
            }
            , {field: 'orderTime', width: 300, title: '下单时间'}
            , {field: 'arrivalTime', width: 300, title: '到货时间'}
            , {field: 'address', width: 100, title: '用户地址'}
            , {title: '操作', align: 'center', width: 200, toolbar: '#operation'}
        ]]
        , id: 'orders'
        , page: false
    });

    function reloadTable(content) {
        table.reload('orders', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {
                content: content
            }
        });
    }

    $('#search').on('click', function () {
        reloadTable($('#content').val());
    });

    $('#reset').on('click', function () {
        $('#content').val("");
    });

    $('#ordered').on('click', function () {
        reloadTable('ordered');
    });

    $('#delivered').on('click', function () {
        reloadTable('delivered');
    });

    $('#cancelled').on('click', function () {
        reloadTable('cancelled');
    });

    table.on('tool(type)', function (obj) {
        var order = {};
        order.orderId = obj.data.orderId;
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
        }
    });
});