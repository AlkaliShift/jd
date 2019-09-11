layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form
        , layer = layui.layer
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    table.render({
        elem: '#categories'
        , url: '/category/list'
        , width: 1000
        , where: {content: $("#content").val()}
        , response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            , statusCode: 1 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "statusCode": res.statusCode,
                "data": res.categories,
                "msg": res.msg
            }
        }
        , cols: [[
            {field: 'categoryId', title: '商品种类ID'}
            , {field: 'categoryName', title: '商品种类'}
            , {field: 'warehouseName', title: '仓库名称'}
            , {title: '操作', align: 'center', width: 250, toolbar: '#operation'}
        ]]
        , id: 'categories'
        , page: false
    });


    var active = {
        reload: function () {
            table.reload('categories', {
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
            content: '/category/addCategory',
            area: ['600px', '390px'],
            closeBtn: 2,
            shadeClose: true,
            title: '新增商品种类'
        });
    });

    table.on('tool(type)', function (obj) {
        var categoryId = obj.data.categoryId;
        var layEvent = obj.event;
        if (layEvent === 'edit') { //编辑
            layer.open({
                type: 2,
                content: '/category/updateCategory?categoryId=' + categoryId,
                area: ['600px', '390px'],
                closeBtn: 2,
                shadeClose: true,
                title: '更新商品种类信息'
            });
        } else if (layEvent === 'del') {//删除
            layer.confirm('删除该商品分类,确定删除?'
                , {icon: 0, title: '删除'}, function (index) {
                    var action = '/category/remove?categoryId=' + categoryId;
                    $.ajax({
                        type: 'POST',
                        url: action,
                        success: function (data) {
                            if (data.statusCode === 1) {
                                layer.msg("删除成功");
                                layer.close(index);
                                $('#search').click();
                            } else {
                                layer.msg(data.msg);
                            }
                        }
                    });
                    layer.close(index);
                });
        }
    });
});