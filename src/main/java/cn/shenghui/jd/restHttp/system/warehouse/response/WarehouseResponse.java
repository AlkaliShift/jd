package cn.shenghui.jd.restHttp.system.warehouse.response;

import cn.shenghui.jd.dao.system.warehouse.model.Warehouse;
import cn.shenghui.jd.restHttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/27 11:17
 */
@Data
@ApiModel(value = "仓库列表")
public class WarehouseResponse extends AbstractResponse {
    protected List<Warehouse> warehouses;
}
