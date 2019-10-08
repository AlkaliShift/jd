package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.resthttp.system.order.response.OrderBasicResponse;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/10/8 14:32
 */
public interface State {

    /**
     * 取消订单
     *
     * @return 状态码和信息
     */
    OrderBasicResponse cancelOrder();

    /**
     * 发货
     *
     * @return 状态码和信息
     */
    OrderBasicResponse deliverOrder();

    /**
     * 确认订单
     *
     * @return 状态码和信息
     */
    OrderBasicResponse confirmOrder();
}
