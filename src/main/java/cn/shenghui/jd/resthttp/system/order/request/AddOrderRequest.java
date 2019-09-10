package cn.shenghui.jd.resthttp.system.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/28 15:51
 */
@Data
@ApiModel(value = "生成订单")
public class AddOrderRequest {

    @ApiModelProperty(value = "商品ID集", required = true)
    List<String> productIds;

    @ApiModelProperty(value = "收货地址", required = true)
    String address;
}
