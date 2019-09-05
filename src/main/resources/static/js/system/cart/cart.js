layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form
        , layer = layui.layer
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    table.render({
        elem: '#cart'
        , url: '/cart/list'
        , response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            , statusCode: 1 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "statusCode": res.statusCode,
                "count": res.total, //解析数据长度
                "data": res.cartProducts,
                "msg": res.msg
            }
        }
        , toolbar: '#toolbar'
        , cols: [[
            {type: 'checkbox', fixed: 'left'}
            , {field: 'productName', title: '商品名称'}
            , {field: 'unitPrice', title: '单位价格'}
            , {field: 'description', title: '商品描述'}
            , {field: 'productNum', title: '已选数量'}
            , {field: 'availableNum', title: '库存数量'}
            , {title: '操作', align: 'center', width: 250, toolbar: '#operation'}
        ]]
        , id: 'cart'
        , page: false
    });

    var active = {
        reload: function () {
            table.reload('cart', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    };

    $('#exit').on('click', function () {
        window.close();
    });

    table.on('toolbar(type)', function (obj) {
        var data = table.checkStatus(obj.config.id).data;
        var productIds = [];
        for (var i in data) {
            if (data.hasOwnProperty(i)) {
                productIds.push(data[i].productId);
            }
        }
        if (obj.event === 'del') {
            if (productIds.length > 0) {
                layer.confirm('删除选中商品,确定删除?'
                    , {icon: 0, title: '删除'}, function (index) {
                        var action = '/cart/delete';
                        $.ajax({
                            type: 'POST',
                            url: action,
                            contentType: "application/json; charset=utf-8",
                            data: JSON.stringify(productIds),
                            success: function (data) {
                                if (data.statusCode === 1) {
                                    layer.msg("删除成功");
                                    layer.close(index);
                                    active['reload'].call(this);
                                } else {
                                    layer.msg("删除失败");
                                }
                            }
                        });
                        layer.close(index);
                    });
            } else {
                layer.msg("请选择需删除的商品。");
            }
        } else if (obj.event === 'order') {
            if (productIds.length > 0) {
                layer.open({
                    type: 2,
                    content: '/order/orderCart?productIds=' + productIds,
                    area: ['800px', '500px'],
                    closeBtn: 2,
                    shadeClose: true,
                    title: '结算信息'
                });
            } else {
                layer.msg("请选择需结算的商品。");
            }
        }
    });

    table.on('tool(type)', function (obj) {
        var productId = obj.data.productId;
        var productIds = [];
        productIds.push(productId);
        var layEvent = obj.event;
        if (layEvent === 'edit') { //编辑
            layer.open({
                type: 2,
                content: '/cart/updateCart?productId=' + productId,
                area: ['600px', '500px'],
                closeBtn: 2,
                shadeClose: true,
                title: '更新购物车信息'
            });
        } else if (layEvent === 'del') {//删除
            layer.confirm('删除选中商品,确定删除?'
                , {icon: 0, title: '删除'}, function (index) {
                    var action = '/cart/delete';
                    $.ajax({
                        type: 'POST',
                        url: action,
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(productIds),
                        success: function (data) {
                            if (data.statusCode === 1) {
                                layer.msg("删除成功");
                                layer.close(index);
                                active['reload'].call(this);
                            } else {
                                layer.msg("删除失败");
                            }
                        }
                    });
                    layer.close(index);
                });
        }
    });
});