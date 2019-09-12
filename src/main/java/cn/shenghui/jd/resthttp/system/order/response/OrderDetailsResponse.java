package cn.shenghui.jd.resthttp.system.order.response;

import cn.shenghui.jd.dao.system.order.model.OrderDetails;
import cn.shenghui.jd.resthttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/12 15:48
 */
@Data
@ApiModel(value = "订单详情列表")
public class OrderDetailsResponse extends AbstractResponse {

    @ApiModelProperty(value = "订单详情列表", required = true)
    protected List<OrderDetails> orderDetails;
}
