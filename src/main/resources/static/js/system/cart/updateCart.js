layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;
    form.render();

    $('#save').on('click', function () {
        var cart = {};
        var productNum = $('#productNum').val();
        if (isNaN(productNum)) {
            layer.msg("商品未加入购物车。");
        } else if (productNum < 0){
            layer.msg("选购数量不能小于0");
        } else if (productNum - parseInt(productNum) !== 0){
            layer.msg("请输入整数");
        } else {
            productNum = parseInt(productNum);
            cart.productId = $('#productId').val();
            cart.productNum = productNum;
            $.ajax({
                type: 'POST',
                url: '/cart/add?action=set',
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