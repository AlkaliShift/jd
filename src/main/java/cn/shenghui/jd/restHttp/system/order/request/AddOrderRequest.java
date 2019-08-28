package cn.shenghui.jd.restHttp.system.order.request;

import cn.shenghui.jd.dao.system.product.model.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/28 15:51
 */
@Data
public class AddOrderRequest {

    /**
     * 用户ID
     */
    String userId;

    /**
     * 商品集
     */
    List<Product> products;

    /**
     * 总价
     */
    BigDecimal totalPrice;

    /**
     * 收货地址
     */
    String address;
}
