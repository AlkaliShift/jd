layui.use(['form', 'table'], function () {
    var form = layui.form
        , $ = layui.$
        , table = layui.table
    ;
    form.render();

    table.render({
        elem: '#orderDetails'
        , url: '/order/listOrderDetails'
        , where: {orderId: $("#orderId").val()}
        , width: 600
        , response: {
            statusName: 'statusCode' //规定数据状态的字段名称，默认：code
            , statusCode: 1 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "statusCode": res.statusCode,
                "data": res.orderDetails,
                "msg": res.msg
            }
        }
        , cols: [[
            {field: 'orderId', width: 200, title: '订单ID'}
            , {field: 'productName', title: '商品名称'}
            , {field: 'productNum', title: '购买数量'}
            , {field: 'unitPrice', title: '单位价格'}
            , {field: 'description', title: '商品描述'}
        ]]
        , id: 'orderDetails'
        , page: false
    });
});