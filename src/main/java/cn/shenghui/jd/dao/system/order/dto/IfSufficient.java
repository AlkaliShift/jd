package cn.shenghui.jd.dao.system.order.dto;

import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/5 13:58
 */
@Data
public class IfSufficient {

    /**
     * 商品库存数量充足的商品
     */
    List<OrderProduct> sufficientProducts;

    /**
     * 商品库存数量不足的商品
     */
    List<OrderProduct> insufficientProducts;
}
