package cn.shenghui.jd.resthttp.system.order.response;

import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.resthttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/5 14:04
 */
@Data
@ApiModel(value = "添加订单，返回库存不足的商品")
public class AddOrderResponse extends AbstractResponse {

    @ApiModelProperty(value = "库存不足的商品列表", required = true)
    protected List<OrderProduct> insufficientProducts;
}
