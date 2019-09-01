layui.use('form', function () {
    var form = layui.form;
    var $ = layui.$;

    $.ajax({
        type: 'GET',
        url: '/warehouse/list?content=',
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $.each(data.warehouses, function (index, item) {
                $('#warehouse-id-add').append(new Option(item.warehouseName, item.warehouseId)); //往下拉菜单里添加元素
            });
            form.render();//菜单渲染 把内容加载进去
        }
    });

    $('#save').on('click', function () {
        var category = {};
        var categoryName = $('#categoryName').val();
        var warehouseId = $('#warehouse-id-add').val();
        if (categoryName === '') {
            layer.msg("商品种类名称不能为空，请填写商品种类名称。");
        } else if (warehouseId === '') {
            layer.msg("请先添加仓库。");
            setTimeout(function () {
                layer.open({
                    type: 2,
                    content: '/warehouse/addWarehouse',
                    area: ['600px', '390px'],
                    closeBtn: 2,
                    shadeClose: true,
                    title: '新增仓库'
                });
            }, 1000);
        }else {
            category.categoryName = categoryName;
            category.warehouseId = warehouseId;
            $.ajax({
                type: 'POST',
                url: '/category/add',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(category),
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