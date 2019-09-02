package cn.shenghui.jd.restHttp.system.category.response;

import cn.shenghui.jd.dao.system.category.model.Category;
import cn.shenghui.jd.restHttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/27 11:10
 */
@Data
@ApiModel(value = "商品种类列表")
public class CategoryResponse extends AbstractResponse {

    @ApiModelProperty(value = "商品种类列表", required = true)
    protected List<Category> categories;
}
