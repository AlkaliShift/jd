layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form
        , layer = layui.layer
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    table.render({
        elem: '#orders'
        , url: '/order/list'
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
            , {field: 'orderStatus', width: 100, title: '订单状态'}
            , {field: 'orderTime', width: 300, title: '下单时间'}
            , {field: 'arrivalTime', width: 100, title: '到货时间'}
            , {field: 'address', width: 100, title: '用户地址'}
            , {title: '操作', align: 'center', width: 150, toolbar: '#operation'}
        ]]
        , id: 'orders'
        , page: false
    });


    var active = {
        reload: function () {
            table.reload('orders', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    content: $("#content").val()
                }
            });
        }
    };

    $('#search').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    $('#reset').on('click', function () {
        $('#content').val("");
    });

    table.on('tool(type)', function (obj) {
        var orderId = obj.data.orderId;
        var layEvent = obj.event;
        if (layEvent === 'edit') { //编辑
            layer.open({
                type: 2,
                content: '/order/updateOrderStatus?orderId=' + orderId,
                area: ['600px', '390px'],
                closeBtn: 2,
                shadeClose: true,
                title: '更新订单状态'
            });
        }
    });
});