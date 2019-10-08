package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.constants.system.order.OrderConstants;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.resthttp.system.order.response.OrderBasicResponse;
import cn.shenghui.jd.service.system.order.OrderService;
import cn.shenghui.jd.service.system.product.ProductService;
import cn.shenghui.jd.utils.ApplicationContextUtil;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/10/8 14:31
 */
public class OrderedState implements State {

    Order order;
    OrderService orderService;
    ProductService productService;

    public OrderedState(Order order){
        this.order = order;
        this.orderService = ApplicationContextUtil.getCtx().getBean(OrderService.class);
        this.productService = ApplicationContextUtil.getCtx().getBean(ProductService.class);
    }

    @Override
    public OrderBasicResponse cancelOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();

        //解冻商品库存
        String orderId = order.getOrderId();
        productService.unFreezeNum(orderService.getProductsByOrderId(orderId));

        //修改订单状态
        orderService.updateOrderStatus(orderId, OrderConstants.ORDER_STATUS_CANCELLED);

        //如果所有子订单状态都为取消，则父订单状态改为取消
        String orderPid = order.getOrderPid();
        if (orderService.ifAllThisStatus(orderPid, OrderConstants.ORDER_STATUS_CANCELLED)) {
            orderService.updateOrderStatus(orderPid, OrderConstants.ORDER_STATUS_CANCELLED);
        }

        orderBasicResponse.setStatusInfo(1, "订单取消成功");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse deliverOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();

        //修改订单状态
        orderService.updateOrderStatus(order.getOrderId(), OrderConstants.ORDER_STATUS_DELIVERED);
        orderBasicResponse.setStatusInfo(1, "发货成功");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse confirmOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "商品尚未发货，无法确认收货。");
        return orderBasicResponse;
    }
}
