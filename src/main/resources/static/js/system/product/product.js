layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form
        , layer = layui.layer
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    table.render({
        elem: '#products'
        , url: '/product/list'
        , where: {content: $("#content").val()}
        , response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            , statusCode: 1 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "statusCode": res.statusCode,
                "count": res.total, //解析数据长度
                "data": res.products,
                "msg": res.msg
            }
        }
        , toolbar: '#toolbar'
        , cols: [[
            {type: 'checkbox'}
            , {field: 'productId', width: 80, title: '商品ID'}
            , {field: 'productName', width: 90, title: '商品名称'}
            , {field: 'categoryName', width: 90, title: '商品种类'}
            , {field: 'availableNum', width: 90, title: '可用数量'}
            , {field: 'frozenNum', width: 90, title: '冻结数量'}
            , {
                field: 'unitPrice', width: 90, title: '单位价格', templet: function (data) {
                    return parseFloat(data.unitPrice).toFixed(2);
                }
            }
            , {field: 'productStatus', width: 90, title: '商品状态'}
            , {field: 'startTime', width: 160, title: '上架时间'}
            , {field: 'endTime', width: 160, title: '下架时间'}
            , {
                field: 'path', title: '商品图片', width: 150, style: 'height:60px;', templet: function (data) {
                    return "<img src=\"/product/downloadImage?productId=" + data.productId + "\" alt=\"\"/>";
                }
            }
            , {field: 'description', width: 100, title: '商品描述'}
            , {title: '操作', align: 'center', width: 200, toolbar: '#operation'}
        ]]
        , id: 'products'
        , page: true
        , limit: 10
    });

    var active = {
        reload: function () {
            table.reload('products', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    content: $("#content").val()
                }
            });
        }
    };

    $('#search').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    $('#reset').on('click', function () {
        $('#content').val("");
    });

    $('#add').on('click', function () {
        layer.open({
            type: 2,
            content: '/product/addProduct',
            area: ['600px', '390px'],
            closeBtn: 2,
            shadeClose: true,
            title: '新增商品'
        });
    });

    table.on('toolbar(type)', function (obj) {
        var data = table.checkStatus(obj.config.id).data;
        var productIds = [];
        var productStatusUp = false;
        var productStatusDown = false;
        for (var i in data) {
            if (data.hasOwnProperty(i)) {
                productIds.push(data[i].productId);
                if ("up" === data[i].productStatus) {
                    productStatusUp = true;
                } else if (null === data[i].productStatus || "down" === data[i].productStatus) {
                    productStatusDown = true;
                }
            }
        }
        if (obj.event === 'productUp') {
            layer.confirm('上架选中商品,确定上架?'
                , {icon: 0, title: '上架'}, function (index) {
                    if (productStatusUp) {
                        layer.msg("部分选中的商品已上架，请重新勾选。");
                    } else {
                        var action = '/product/setProductStatus?productStatus=up';
                        $.ajax({
                            type: 'POST',
                            url: action,
                            contentType: "application/json; charset=utf-8",
                            data: JSON.stringify(productIds),
                            success: function (data) {
                                if (data.statusCode === 1) {
                                    layer.msg("上架成功");
                                    layer.close(index);
                                    $('#search').click();
                                } else {
                                    layer.msg("上架失败");
                                }
                            }
                        });
                        layer.close(index);
                    }
                });
        } else if (obj.event === 'productDown') {
            layer.confirm('下架选中商品,确定下架?'
                , {icon: 0, title: '下架'}, function (index) {
                    if (productStatusDown) {
                        layer.msg("部分选中的商品未上架或已下架，请重新勾选。");
                    } else {
                        var action = '/product/setProductStatus?productStatus=down';
                        $.ajax({
                            type: 'POST',
                            url: action,
                            contentType: "application/json; charset=utf-8",
                            data: JSON.stringify(productIds),
                            success: function (data) {
                                if (data.statusCode === 1) {
                                    layer.msg("下架成功");
                                    layer.close(index);
                                    $('#search').click();
                                } else {
                                    layer.msg("下架失败");
                                }
                            }
                        });
                        layer.close(index);
                    }

                });
        }
    });

    table.on('tool(type)', function (obj) {
        var productId = obj.data.productId;
        var layEvent = obj.event;
        if (layEvent === 'edit') { //编辑
            layer.open({
                type: 2,
                content: '/product/updateProduct?productId=' + productId,
                area: ['600px', '500px'],
                closeBtn: 2,
                shadeClose: true,
                title: '更新商品信息'
            });
        } else if (layEvent === 'del') {//删除
            layer.confirm('删除该账户,确定删除?'
                , {icon: 0, title: '删除'}, function (index) {
                    var action = '/product/removeProduct?productId=' + productId;
                    $.ajax({
                        type: 'POST',
                        url: action,
                        success: function (data) {
                            if (data.statusCode === 1) {
                                layer.msg("删除成功");
                                layer.close(index);
                                $('#search').click();
                            } else {
                                layer.msg("删除失败");
                            }
                        }
                    });
                    layer.close(index);
                });
        } else if (layEvent === 'up') {
            layer.open({
                type: 2,
                content: '/product/uploadImage?productId=' + productId,
                area: ['400px', '260px'],
                closeBtn: 2,
                shadeClose: true,
                title: '上传图片'
            });
        }
    });
});