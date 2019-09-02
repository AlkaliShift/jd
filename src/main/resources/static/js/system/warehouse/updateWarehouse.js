layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;
    form.render();

    $('#save').on('click', function () {
        var warehouse = {};
        var warehouseName = $('#warehouseName').val();
        if (warehouseName === '') {
            layer.msg("仓库名称不能为空，请填写仓库名称。");
        } else {
            warehouse.warehouseId = $('#warehouseId').val();
            warehouse.warehouseName = warehouseName;
            $.ajax({
                type: 'POST',
                url: '/warehouse/update',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(warehouse),
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
        }
    });
});