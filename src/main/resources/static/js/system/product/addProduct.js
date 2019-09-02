layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;
    form.render();

    $.ajax({
        type: 'GET',
        url: '/category/list?content=',
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $.each(data.categories, function (index, item) {
                //往下拉菜单里添加元素
                $('#product-category-id-add').append(new Option(item.categoryName, item.categoryId));
            });
            //菜单渲染 把内容加载进去
            form.render();
        }
    });

    $('#save').on('click', function () {
        var product = {};
        var productName = $('#productName').val();
        var categoryId = $('#product-category-id-add').val();
        if (productName === '') {
            layer.msg("商品名称不能为空，请填写商品名称。");
        } else if (categoryId === '') {
            layer.msg("请先添加商品种类。");
            setTimeout(function () {
                layer.open({
                    type: 2,
                    content: '/category/addCategory',
                    area: ['600px', '390px'],
                    closeBtn: 2,
                    shadeClose: true,
                    title: '新增商品种类'
                });
            }, 1000);
        } else {
            product.productName = productName;
            product.categoryId = categoryId;
            product.availableNum = parseInt($('#availableNum').val());
            product.unitPrice = $('#unitPrice').val();
            product.description = $('#description').val();
            $.ajax({
                type: 'POST',
                url: '/product/add',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(product),
                success: function (data) {
                    if (data.statusCode === 1) {
                        layer.msg("商品添加成功");
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