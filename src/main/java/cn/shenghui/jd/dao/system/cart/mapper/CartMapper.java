package cn.shenghui.jd.dao.system.cart.mapper;

import cn.shenghui.jd.dao.system.cart.dto.CartProduct;
import cn.shenghui.jd.dao.system.cart.model.Cart;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:30
 */
public interface CartMapper {

    /**
     * 根据用户ID获得其购物车中商品信息
     * @param userId 用户ID
     * @return
     */
    List<CartProduct> getCartList(String userId);
}
