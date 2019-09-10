package cn.shenghui.jd.dao.system.category.dto;

import cn.shenghui.jd.dao.system.category.model.Category;
import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/10 14:27
 */
@Data
public class CategoryDetails extends Category {

    /**
     * 仓库名称
     */
    String warehouseName;
}
