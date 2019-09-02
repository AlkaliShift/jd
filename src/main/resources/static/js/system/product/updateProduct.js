layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;

    $.ajax({
        type: 'GET',
        url: '/category/list?content=',
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var categoryId = $('#categoryId').val();
            $.each(data.categories, function (index, item) {
                if (categoryId === item.categoryId) {
                    $('#product-category-id-update').append("<option value=" + item.categoryId +
                        " selected = \"selected\"" + ">" + item.categoryName + "</option>");
                } else {
                    //往下拉菜单里添加元素
                    $('#product-category-id-update').append(new Option(item.categoryName, item.categoryId));
                }
            });
            //菜单渲染 把内容加载进去
            form.render();
        }
    });

    $('#save').on('click', function () {
        var product = {};
        var productName = $('#productName').val();
        if (productName === '') {
            layer.msg("商品名称不能为空，请填写商品名称。");
        } else {
            product.productId = $('#productId').val();
            product.productName = productName;
            product.categoryId = $('#product-category-id-update').val();
            product.availableNum = parseInt($('#availableNum').val());
            product.frozenNum = parseInt($('#frozenNum').val());
            product.unitPrice = $('#unitPrice').val();
            product.description = $('#description').val();
            $.ajax({
                type: 'POST',
                url: '/product/update',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(product),
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