layui.use(['form', 'table'], function () {
    var form = layui.form
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    var productIds = $("#productIds").val().toString();
    productIds = productIds.substring(1, productIds.length - 1);
    var totalPrice = 0;

    table.render({
        elem: '#orderProduct'
        , url: '/cart/listProduct'
        , where: {productIds: productIds}
        , response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            , statusCode: 1 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "statusCode": res.statusCode,
                "data": res.cartProducts,
                "msg": res.msg
            }
        }
        , cols: [[
            {field: 'productName', title: '商品名称'}
            , {
                field: 'unitPrice', title: '单位价格', templet: function (data) {
                    var unitPrice = data.unitPrice;
                    var productNum = data.productNum;
                    var price = unitPrice * productNum;
                    totalPrice = totalPrice + price;
                    $('#totalPrice').val(totalPrice.toFixed(2));
                    return parseFloat(unitPrice).toFixed(2);
                }
            }
            , {field: 'description', title: '商品描述'}
            , {field: 'productNum', title: '已选数量'}
            , {field: 'availableNum', title: '库存数量'}
            , {title: '操作', align: 'center', width: 250, toolbar: '#operation'}
        ]]
        , id: 'orderProduct'
        , page: false
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
        }
    });

    $('#save').on('click', function () {
        var order = {};
        var productIds = $("#productIds").val().toString();
        order.productIds = (productIds.substring(1, productIds.length - 1)).split(", ");
        order.address = $('#address').val();
        $.ajax({
            type: 'POST',
            url: '/order/add',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(order),
            success: function (data) {
                if (data.statusCode === 1) {
                    var insufficientProducts = data.insufficientProducts;
                    var flag = false;
                    var productNameList = [];
                    for (var i in insufficientProducts) {
                        if (insufficientProducts.hasOwnProperty(i)) {
                            productNameList.push(insufficientProducts[i].productName);
                            flag = true;
                        }
                    }
                    if (flag) {
                        layer.msg("以下商品库存不足： " + productNameList);
                    } else {
                        layer.msg("下单成功");
                    }
                    setTimeout(function () {
                        parent.location.reload()
                    }, 1000);
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    });
});