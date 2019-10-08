layui.use(['form', 'layer', 'laydate', 'treeGrid'], function () {
    var form = layui.form
        , layer = layui.layer
        , $ = layui.$
        , laydate = layui.laydate
        , treeGrid = layui.treeGrid;

    form.render();

    var start = "";
    var end = "";

    laydate.render({
        elem: '#time'
        , type: 'datetime'
        , max: 0
        , range: true
        , done: function (value) {
            var temp = value.toString().split(" - ");
            start = temp[0];
            end = temp[1];
        }
    });

    loadTreeGrid();
    function loadTreeGrid() {
        var queryOrder = {};
        queryOrder.content = $("#content").val();
        queryOrder.priceMin = $('#priceMin').val();
        queryOrder.priceMax = $('#priceMax').val();
        queryOrder.orderStatus = $('#orderStatus').val();
        queryOrder.start = start;
        queryOrder.end = end;
        $.ajax({
            type: 'POST',
            url: '/order/list',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(queryOrder),
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

    function treeGridFunction(dataTreeGrid) {
        treeGrid.render({
            elem: '#orders'
            , data: dataTreeGrid
            , treeId: 'orderId'//树形id字段名称
            , treeUpId: 'orderPid'//树形父id字段名称
            , treeShowName: 'orderId'//以树形式显示的字段
            , cols: [[
                {field: 'orderId', width: 250, title: '订单ID'}
                , {field: 'userId', width: 100, title: '用户ID'}
                , {
                    field: 'totalPrice', width: 100, title: '订单总价', templet: function (data) {
                        return parseFloat(data.totalPrice).toFixed(2);
                    }
                }
                , {field: 'orderStatus', width: 100, title: '订单状态'}
                , {field: 'orderTime', width: 160, title: '下单时间'}
                , {field: 'arrivalTime', width: 160, title: '到货时间'}
                , {field: 'address', width: 200, title: '用户地址'}
                , {title: '操作', align: 'center', width: 260, toolbar: '#operation'}
            ]]
            , id: 'treeGrid'
            , page: false
        });

        treeGrid.on('tool(orderType)',function (obj) {
            var orderId = obj.data.orderId;
            var layEvent = obj.event;
            if (layEvent === 'edit') { //编辑
                layer.open({
                    type: 2,
                    content: '/order/updateOrderStatus?orderId=' + orderId,
                    area: ['400px', '300px'],
                    closeBtn: 2,
                    shadeClose: true,
                    title: '更新订单状态'
                });
            }
        });
    }

    $('#search').on('click', function () {
        loadTreeGrid();
    });

    $('#reset').on('click', function () {
        $('#content').val("");
        $('#priceMin').val("");
        $('#priceMax').val("");
        $('#time').val("");
    });
});