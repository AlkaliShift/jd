package cn.shenghui.jd.restHttp.system.product.response;

import cn.shenghui.jd.dao.system.product.model.Product;
import cn.shenghui.jd.restHttp.base.AbstractResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/27 16:03
 */
@Data
@ApiModel(value = "商品列表")
public class ProductResponse extends AbstractResponse {

    @ApiModelProperty(value = "产品列表", required = true)
    protected List<Product> products;

    @ApiModelProperty(value = "总个数", required = true)
    long total;
}
