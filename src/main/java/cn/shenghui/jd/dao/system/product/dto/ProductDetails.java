package cn.shenghui.jd.dao.system.product.dto;

import cn.shenghui.jd.dao.system.product.model.Product;
import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/2 21:07
 */
@Data
public class ProductDetails extends Product {

    /**
     * 商品种类名称
     */
    String categoryName;
}
