layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;
    form.render();

    $('#addToCart').on('click', function () {
        var cart = {};
        var productNum = $('#productNum').val();
        if (isNaN(productNum)) {
            layer.msg("商品未加入购物车");
        } else if (productNum < 0) {
            layer.msg("商品库存不能小于0");
        } else if (productNum - parseInt(productNum) !== 0){
            layer.msg("请输入整数");
        } else {
            productNum = parseInt(productNum);
            cart.productId = $('#productId').val();
            cart.productNum = productNum;
            $.ajax({
                type: 'POST',
                url: '/cart/add',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(cart),
                success: function (data) {
                    if (data.statusCode === 1) {
                        layer.msg("添加成功");
                    } else {
                        layer.msg(data.msg);
                    }
                    setTimeout(function () {
                        parent.location.reload()
                    }, 1000);
                }
            });
        }
    });
});