layui.use('form', function() {
    var form = layui.form;
    var $ = layui.$;
    form.render();

    $('#save').on('click',function(){
        var warehouse = {};
        warehouse.warehouseId = $('#warehouseId').val();
        warehouse.warehouseName = $('#warehouseName').val();
        $.ajax({
            type: 'POST',
            url: '/warehouse/update',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(warehouse),
            success: function (data) {
                if(data.statusCode === 1){
                    layer.msg("修改成功");
                    setTimeout(function (){parent.location.reload()},1000);
                }else{
                    layer.msg(data.msg);
                }
            }
        });
    });
});