package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.resthttp.system.order.response.OrderBasicResponse;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/10/8 14:31
 */
public class CancelledState implements State {

    Order order;

    public CancelledState(Order order){
        this.order = order;
    }

    @Override
    public OrderBasicResponse cancelOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "订单已取消，无法重复取消。");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse deliverOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "订单已取消，无法发货。");
        return orderBasicResponse;
    }

    @Override
    public OrderBasicResponse confirmOrder(){
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        orderBasicResponse.setStatusInfo(0, "订单已取消，无法确认收货。");
        return orderBasicResponse;
    }
}
