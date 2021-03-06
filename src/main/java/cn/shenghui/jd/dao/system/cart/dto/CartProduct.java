package cn.shenghui.jd.dao.system.cart.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/27 17:11
 */
@Data
public class CartProduct {

    /**
     * 产品ID
     */
    String productId;

    /**
     * 用户选择数量
     */
    int productNum;

    /**
     * 可用数量
     */
    int availableNum;

    /**
     * 商品名称
     */
    String productName;

    /**
     * 单位价格
     */
    BigDecimal unitPrice;

    /**
     * 商品详情
     */
    String description;
}
