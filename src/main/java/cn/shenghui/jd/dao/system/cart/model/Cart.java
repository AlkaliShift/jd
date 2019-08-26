package cn.shenghui.jd.dao.system.cart.model;

import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:27
 */
@Data
public class Cart {

    /**
     * 用户ID
     */
    String userId;

    /**
     * 产品ID
     */
    String productId;

    /**
     * 商品数量
     */
    int productNum;
}
