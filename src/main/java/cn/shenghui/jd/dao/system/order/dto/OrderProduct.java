package cn.shenghui.jd.dao.system.order.dto;

import cn.shenghui.jd.dao.system.product.model.Product;
import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/4 17:07
 */
@Data
public class OrderProduct extends Product {

    /**
     * 产品数量
     */
    int productNum;

    /**
     * 仓库ID
     */
    String warehouseId;
}