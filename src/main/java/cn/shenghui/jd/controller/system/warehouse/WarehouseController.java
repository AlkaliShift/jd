package cn.shenghui.jd.controller.system.warehouse;

import cn.shenghui.jd.dao.system.warehouse.model.Warehouse;
import cn.shenghui.jd.resthttp.system.warehouse.response.WarehouseBasicResponse;
import cn.shenghui.jd.resthttp.system.warehouse.response.WarehouseResponse;
import cn.shenghui.jd.service.system.category.CategoryService;
import cn.shenghui.jd.service.system.warehouse.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:27
 */
@Controller
@Api(value = "Warehouse")
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    CategoryService categoryService;

    /**
     * 仓库列表页
     *
     * @return 页面
     */
    @RequestMapping("")
    public ModelAndView warehousePage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/warehouse/warehouse");
        return mv;
    }

    /**
     * 增加仓库页
     *
     * @return 页面
     */
    @RequestMapping("/addWarehouse")
    public ModelAndView addWarehousePage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/warehouse/addWarehouse");
        return mv;
    }

    /**
     * 更新仓库页
     *
     * @return 页面
     */
    @RequestMapping("/updateWarehouse")
    public ModelAndView updateWarehousePage(@RequestParam(name = "warehouseId") String warehouseId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/warehouse/updateWarehouse");
        mv.addObject("warehouse", warehouseService.getWarehouseById(warehouseId));
        return mv;
    }

    /**
     * 模糊查询仓库信息，若搜索内容为空，则返回所有仓库信息列表
     *
     * @param content 搜索内容
     * @return 仓库列表和状态码：1
     */
    @ApiOperation(value = "获取仓库列表", notes = "状态码1:查询成功")
    @RequestMapping(value = "/list")
    @ResponseBody
    public WarehouseResponse getWarehouseList(@RequestParam(name = "content") String content) {
        WarehouseResponse response = new WarehouseResponse();
        response.setWarehouses(warehouseService.getWarehouseList(content));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 增加单个仓库
     *
     * @param warehouse 仓库信息
     * @return 状态码：1
     */
    @ApiOperation(value = "增加单个仓库", notes = "状态码1:创建成功")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public WarehouseBasicResponse addWarehouse(@RequestBody Warehouse warehouse) {
        WarehouseBasicResponse response = new WarehouseBasicResponse();
        warehouseService.addWarehouse(warehouse);
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
        if (categoryService.ifExistCategory(warehouseId)) {
            response.setStatusInfo(0, "请先删除改仓库下所有分类。");
        } else {
            warehouseService.removeWarehouse(warehouseId);
            response.setStatusCode(1);
        }
        return response;
    }

    /**
     * 修改单个仓库信息
     *
     * @param warehouse 仓库ID
     * @return 状态码：1
     */
    @ApiOperation(value = "修改单个仓库信息", notes = "状态码1:修改成功")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public WarehouseBasicResponse updateWarehouse(@RequestBody Warehouse warehouse) {
        WarehouseBasicResponse response = new WarehouseBasicResponse();
        warehouseService.updateWarehouse(warehouse);
        response.setStatusCode(1);
        return response;
    }
}
