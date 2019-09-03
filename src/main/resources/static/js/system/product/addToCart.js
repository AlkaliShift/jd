layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;
    form.render();

    $('#addToCart').on('click', function () {
        var product = {};
        var productNum = parseInt($('#productNum').val());
        if (isNaN(productNum)) {
            layer.msg("商品未加入购物车。");
            setTimeout(function () {
                parent.location.reload()
            }, 1000);
        } else if (productNum < 0){
            layer.msg("选购数量不能小于0");
            setTimeout(function () {
                parent.location.reload()
            }, 1000);
        } else {
            product.productId = $('#productId').val();
            product.productNum = productNum;
            $.ajax({
                type: 'POST',
                url: '/cart/add',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(product),
                success: function (data) {
                    if (data.statusCode === 1) {
                        layer.msg("添加成功");
                        setTimeout(function () {
                            parent.location.reload()
                        }, 1000);
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
});