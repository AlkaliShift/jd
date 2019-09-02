package cn.shenghui.jd.dao.system.product.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:28
 */
@Data
public class Product {

    /**
     * 商品ID
     */
    String productId;

    /**
     * 商品名称
     */
    String productName;

    /**
     * 商品种类ID
     */
    String categoryId;

    /**
     * 可用数量
     */
    int availableNum;

    /**
     * 冻结数量
     */
    int frozenNum;

    /**
     * 商品单位价格
     */
    BigDecimal unitPrice;

    /**
     * 商品状态
     */
    String productStatus;

    /**
     * 上架时间
     */
    String startTime;

    /**
     * 下架时间
     */
    String endTime;

    /**
     * 商品描述
     */
    String description;
}
