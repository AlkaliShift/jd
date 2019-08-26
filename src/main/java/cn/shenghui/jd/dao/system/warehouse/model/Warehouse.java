package cn.shenghui.jd.dao.system.warehouse.model;

import lombok.Data;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:28
 */
@Data
public class Warehouse {

    /**
     * 仓库ID
     */
    String warehouseId;

    /**
     * 仓库名称
     */
    String warehouseName;

    /**
     * 逻辑删除
     */
    char delFlag;
}
