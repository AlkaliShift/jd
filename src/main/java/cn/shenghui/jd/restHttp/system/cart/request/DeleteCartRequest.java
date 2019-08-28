package cn.shenghui.jd.restHttp.system.cart.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/28 13:34
 */
@Data
@ApiModel(value = "批量移除购物车中的商品")
public class DeleteCartRequest {

    /**
     * 用户ID
     */
    String userId;

    /**
     * 商品ID集
     */
    List<String> productIds;
}
