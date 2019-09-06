layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;

    $.ajax({
        type: 'GET',
        url: '/warehouse/list?content=',
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var warehouseId = $('#warehouseId').val();
            $.each(data.warehouses, function (index, item) {
                if (warehouseId === item.warehouseId) {
                    $('#category-warehouse-id-update').append("<option value=" + item.warehouseId +
                        " selected = \"selected\"" + ">" + item.warehouseName + "</option>");
                } else {
                    //往下拉菜单里添加元素
                    $('#category-warehouse-id-update').append(new Option(item.warehouseName, item.warehouseId));
                }
            });
            //菜单渲染 把内容加载进去
            form.render();
        }
    });

    $('#save').on('click', function () {
        var category = {};
        var categoryName = $('#categoryName').val();
        if (categoryName === '') {
            layer.msg("商品种类名称不能为空，请填写商品种类名称。");
        } else {
            category.categoryId = $('#categoryId').val();
            category.categoryName = categoryName;
            category.warehouseId = $('#category-warehouse-id-update').val();
            $.ajax({
                type: 'POST',
                url: '/category/update',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(category),
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