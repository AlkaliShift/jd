package cn.shenghui.jd.resthttp.system.cart.response;

import cn.shenghui.jd.dao.system.cart.dto.CartProduct;
import cn.shenghui.jd.resthttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/28 10:06
 */
@Data
@ApiModel(value = "购物车列表")
public class CartResponse extends AbstractResponse {

    @ApiModelProperty(value = "购物车中商品列表", required = true)
    protected List<CartProduct> cartProducts;
}
