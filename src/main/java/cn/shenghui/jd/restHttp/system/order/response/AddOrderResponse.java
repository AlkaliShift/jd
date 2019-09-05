package cn.shenghui.jd.restHttp.system.order.response;

import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.restHttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
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

    protected List<OrderProduct> insufficientProducts;
}
