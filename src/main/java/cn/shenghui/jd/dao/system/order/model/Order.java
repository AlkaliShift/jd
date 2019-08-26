package cn.shenghui.jd.dao.system.order.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:28
 */
@Data
public class Order {

    /**
     * 订单ID
     */
    String orderId;

    /**
     * 用户ID
     */
    String userId;

    /**
     * 订单父ID
     */
    String orderPid;

    /**
     * 订单总价
     */
    BigDecimal totalPrice;

    /**
     * 订单状态
     */
    char orderStatus;

    /**
     * 下单时间
     */
    String orderTime;

    /**
     * 到货时间
     */
    String arrivalTime;

    /**
     * 收货地址
     */
    String address;
}
