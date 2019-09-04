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
     * 商品ID集
     */
    List<String> productIds;

    /**
     * 收货地址
     */
    String address;
}
