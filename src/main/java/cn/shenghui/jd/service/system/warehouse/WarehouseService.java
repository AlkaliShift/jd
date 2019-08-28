package cn.shenghui.jd.service.system.warehouse;

import cn.shenghui.jd.dao.system.warehouse.mapper.WarehouseMapper;
import cn.shenghui.jd.dao.system.warehouse.model.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
@Service
public class WarehouseService {

    private WarehouseMapper warehouseMapper;
    private static int ID = 0;

    @Autowired
    public void setWarehouseMapper(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }

    /**
     * 查询仓库信息，若仓库ID为空，则返回所有仓库信息列表
     *
     * @param warehouseId 仓库ID
     * @return 仓库列表
     */
    public List<Warehouse> getWarehouseList(String warehouseId) {
        return warehouseMapper.getWarehouseList(warehouseId);
    }

    /**
     * 增加单个仓库
     *
     * @param warehouseName 仓库名称
     */
    public void addWarehouse(String warehouseName) {
        ID = ID + 1;
        String warehouseId = ID + "";
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(warehouseId);
        warehouse.setWarehouseName(warehouseName);
        warehouseMapper.addWarehouse(warehouse);
    }

    /**
     * 移除单个仓库
     *
     * @param warehouseId 仓库ID
     */
    public void removeWarehouse(String warehouseId) {
        warehouseMapper.removeWarehouse(warehouseId);
    }

    /**
     * 更新单个仓库信息
     *
     * @param warehouseId 仓库ID
     * @param warehouseName 仓库名称
     */
    public void updateWarehouse(String warehouseId, String warehouseName) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(warehouseId);
        warehouse.setWarehouseName(warehouseName);
        warehouseMapper.updateWarehouse(warehouse);
    }
}
