layui.use('form', function() {
    var form = layui.form;
    var $ = layui.$;
    form.render();

    $('#save').on('click',function(){
        var warehouse = {};
        warehouse.warehouseName = $('#warehouseName').val();
        $.ajax({
            type: 'POST',
            url: '/warehouse/add',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(warehouse),
            success: function (data) {
                if(data.statusCode === 1){
                    layer.msg("创建成功");
                    setTimeout(function (){parent.location.reload()},1000);
                }else{
                    layer.msg(data.msg);
                }
            }
        });
    });
});