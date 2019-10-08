package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.constants.system.order.OrderConstants;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.resthttp.system.order.response.OrderBasicResponse;
import cn.shenghui.jd.service.system.order.OrderService;
import cn.shenghui.jd.utils.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/10/8 14:32
 */
public class DeliveredState implements State {

    Order order;
    OrderService orderService;

    public DeliveredState(Order order){
        this.order = order;
        this.orderService = ApplicationContextUtil.getCtx().getBean(OrderService.class);
    }

    @Override
    public OrderBasicResponse cancelOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "商品已发货，无法取消订单。");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse deliverOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "商品已发货，无法重复发货。");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse confirmOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();

        //修改订单状态
        orderService.updateOrderStatus(order.getOrderId(), OrderConstants.ORDER_STATUS_COMPLETED);

        //如果所有子订单状态都为取消，则父订单状态改为取消
        String orderPid = order.getOrderPid();
        if (orderService.ifAllThisStatus(orderPid, OrderConstants.ORDER_STATUS_COMPLETED)) {
            orderService.updateOrderStatus(orderPid, OrderConstants.ORDER_STATUS_COMPLETED);
        }

        orderBasicResponse.setStatusInfo(1, "订单已完成");
        return orderBasicResponse;
    }
}
