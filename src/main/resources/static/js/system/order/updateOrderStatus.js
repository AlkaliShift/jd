layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;

    form.render();

    $('#save').on('click', function () {
        var order = {};
        order.orderId = $('#orderId').val();
        order.orderStatus = $('#orderStatus').val();
        $.ajax({
            type: 'POST',
            url: '/order/setOrderStatus',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(order),
            success: function (data) {
                if (data.statusCode === 1) {
                    layer.msg("修改成功");
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