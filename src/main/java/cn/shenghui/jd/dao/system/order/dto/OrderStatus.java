package cn.shenghui.jd.dao.system.order.dto;

import cn.shenghui.jd.constants.system.order.OrderConstants;
import cn.shenghui.jd.controller.system.order.*;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.resthttp.system.order.response.OrderBasicResponse;
import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/10/8 15:19
 */
@Data
public class OrderStatus {

    State orderedState;
    State cancelledState;
    State deliveredState;
    State completedState;
    State state;

    public OrderStatus(Order order){
        orderedState = new OrderedState(order);
        cancelledState = new CancelledState(order);
        deliveredState = new DeliveredState(order);
        completedState = new CompletedState(order);

        //确定当前订单状态
        String orderStatus = order.getOrderStatus();
        if (OrderConstants.ORDER_STATUS_ORDERED.equals(orderStatus)) {
            state = orderedState;
        } else if (OrderConstants.ORDER_STATUS_CANCELLED.equals(orderStatus)) {
            state = cancelledState;
        } else if (OrderConstants.ORDER_STATUS_DELIVERED.equals(orderStatus)) {
            state = deliveredState;
        } else if (OrderConstants.ORDER_STATUS_COMPLETED.equals(orderStatus)) {
            state = completedState;
        }
    }

    public OrderBasicResponse cancelOrder(){
        return state.cancelOrder();
    }

    public OrderBasicResponse deliverOrder(){
        return state.deliverOrder();
    }

    public OrderBasicResponse confirmOrder(){
        return state.confirmOrder();
    }
}
