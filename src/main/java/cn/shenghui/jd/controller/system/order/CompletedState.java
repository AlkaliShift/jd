package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.resthttp.system.order.response.OrderBasicResponse;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/10/8 14:32
 */
public class CompletedState implements State {

    Order order;

    public CompletedState(Order order){
        this.order = order;
    }

    @Override
    public OrderBasicResponse cancelOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "订单已完成，无法取消订单。");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse deliverOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "订单已完成，无法重复发货。");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse confirmOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "订单已完成，无法重复确认。");
        return orderBasicResponse;
    }
}
