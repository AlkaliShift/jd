package cn.shenghui.jd.controller.system.warehouse;

import cn.shenghui.jd.restHttp.system.warehouse.response.WarehouseBasicResponse;
import cn.shenghui.jd.restHttp.system.warehouse.response.WarehouseResponse;
import cn.shenghui.jd.service.system.warehouse.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:27
 */
@Controller
@Api(value = "Warehouse")
@RequestMapping("/warehouse")
public class WarehouseController {

    private WarehouseService warehouseService;

    @Autowired
    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * 获取单个仓库信息，若仓库ID为空则获取全部仓库列表
     *
     * @param warehouseId 仓库ID
     * @return 仓库列表和状态码：1
     */
    @ApiOperation(value = "获取仓库列表", notes = "状态码1:查询成功")
    @RequestMapping(value = "/getWarehouseList")
    public WarehouseResponse getWarehouseList(@RequestParam(name = "warehouseId") String warehouseId) {
        WarehouseResponse response = new WarehouseResponse();
        response.setWarehouses(warehouseService.getWarehouseList(warehouseId));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 增加单个仓库
     *
     * @param warehouseName 仓库名称
     * @return 状态码：1
     */
    @ApiOperation(value = "增加单个仓库", notes = "状态码1:创建成功")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public WarehouseBasicResponse addWarehouse(@RequestParam(name = "warehouseName") String warehouseName) {
        WarehouseBasicResponse response = new WarehouseBasicResponse();
        warehouseService.addWarehouse(warehouseName);
        response.setStatusCode(1);
        return response;
    }

    /**
     * 逻辑删除单个仓库
     *
     * @param warehouseId 仓库ID
     * @return 状态码：1
     */
    @ApiOperation(value = "逻辑删除单个仓库", notes = "状态码1:删除成功")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public WarehouseBasicResponse removeWarehouse(@RequestParam(name = "warehouseId") String warehouseId) {
        WarehouseBasicResponse response = new WarehouseBasicResponse();
        warehouseService.removeWarehouse(warehouseId);
        response.setStatusCode(1);
        return response;
    }

    /**
     * 修改单个仓库信息
     *
     * @param warehouseId   仓库ID
     * @param warehouseName 仓库名称
     * @return 状态码：1
     */
    @ApiOperation(value = "修改单个仓库信息", notes = "状态码1:修改成功")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public WarehouseBasicResponse updateWarehouse(@RequestParam(name = "warehouseId") String warehouseId,
                                                  @RequestParam(name = "warehouseName") String warehouseName) {
        WarehouseBasicResponse response = new WarehouseBasicResponse();
        warehouseService.updateWarehouse(warehouseId, warehouseName);
        response.setStatusCode(1);
        return response;
    }
}
