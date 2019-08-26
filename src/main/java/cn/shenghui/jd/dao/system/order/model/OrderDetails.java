package cn.shenghui.jd.dao.system.order.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/26 16:07
 */
@Data
public class OrderDetails {

    /**
     * 订单ID
     */
    String orderId;

    /**
     * 商品ID
     */
    String productId;

    /**
     * 商品名称
     */
    String productName;

    /**
     * 商品数量
     */
    int productNum;

    /**
     * 单位价格
     */
    BigDecimal unitPrice;

    /**
     * 商品描述
     */
    String description;
}
