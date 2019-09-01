package cn.shenghui.jd.dao.system.warehouse.mapper;

import cn.shenghui.jd.dao.system.warehouse.model.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:31
 */
@Mapper
public interface WarehouseMapper {

    /**
     * 根据仓库ID查找仓库信息
     *
     * @param warehouseId 仓库ID
     * @return 仓库信息
     */
    Warehouse getWarehouseById(@Param("warehouseId") String warehouseId);

    /**
     * 模糊查询仓库信息，若搜索内容为空，则返回所有仓库信息列表
     *
     * @param content 搜索内容
     * @return 仓库信息列表
     */
    List<Warehouse> getWarehouseList(@Param("content") String content);

    /**
     * 增加单个仓库
     *
     * @param warehouse 仓库信息
     */
    void addWarehouse(Warehouse warehouse);

    /**
     * 移除单个仓库
     *
     * @param warehouseId 仓库ID
     */
    void removeWarehouse(@Param("warehouseId") String warehouseId);

    /**
     * 更新单个仓库信息
     *
     * @param warehouse 仓库信息
     */
    void updateWarehouse(Warehouse warehouse);

    /**
     * 获得仓库表行数
     *
     * @return 行数
     */
    int countWarehouse();
}
