package cn.shenghui.jd.resthttp.system.order.response;

import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.resthttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "订单列表", required = true)
    protected List<Order> orders;
}
