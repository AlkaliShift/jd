package cn.shenghui.jd.restHttp.system.order.response;

import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.restHttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/28 14:43
 */
@Data
@ApiModel(value = "订单列表")
public class OrderResponse extends AbstractResponse {
    protected List<Order> orders;
}
