package cn.shenghui.jd.restHttp.system.cart.response;

import cn.shenghui.jd.dao.system.cart.dto.CartProduct;
import cn.shenghui.jd.restHttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
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
    protected List<CartProduct> cartProducts;
}
