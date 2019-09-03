layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form
        , layer = layui.layer
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    table.render({
        elem: '#products'
        , url: '/product/listUser'
        , where: {content: $("#content").val()}
        , response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            , statusCode: 1 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "statusCode": res.statusCode,
                "count": res.total, //解析数据长度
                "data": res.products,
                "msg": res.msg
            }
        }
        , cols: [[
            {field: 'productName', title: '商品名称'}
            , {field: 'categoryName', title: '商品种类'}
            , {field: 'availableNum', title: '可用数量'}
            , {field: 'unitPrice', title: '单位价格'}
            , {title: '操作', align: 'center', width: 250, toolbar: '#operation'}
        ]]
        , id: 'products'
        , page: true
        , limit: 10
    });

    var active = {
        reload: function () {
            table.reload('products', {
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

    $('#cart').on('click', function () {
        window.open('/cart');
    });

    table.on('tool(type)', function (obj) {
        var productId = obj.data.productId;
        var layEvent = obj.event;
        if (layEvent === 'productCart') { //加入购物车页面
            layer.open({
                type: 2,
                content: '/product/productCart?productId=' + productId,
                area: ['600px', '500px'],
                closeBtn: 2,
                shadeClose: true,
                title: '加入购物车'
            });
        }
    });
});