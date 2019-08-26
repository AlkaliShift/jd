package cn.shenghui.jd.dao.system.category.model;

import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:28
 */
@Data
public class Category {

    /**
     * 商品种类ID
     */
    String categoryId;

    /**
     * 商品种类名称
     */
    String categoryName;

    /**
     * 仓库ID
     */
    String warehouseId;
}
