package cn.shenghui.jd.dao.system.cart.mapper;

import cn.shenghui.jd.dao.system.cart.dto.CartProduct;
import cn.shenghui.jd.dao.system.cart.model.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:30
 */
@Mapper
public interface CartMapper {

    /**
     * 根据用户ID获得其购物车中商品信息
     *
     * @param userId 用户ID
     * @return 购物车中商品具体信息
     */
    List<CartProduct> getCartList(@Param("userId") String userId);

    /**
     * 根据商品ID获得该用户选购的商品数量
     *
     * @param userId     用户ID
     * @param productIds 商品ID集
     * @return 商品信息
     */
    List<CartProduct> getProductFromCart(@Param("userId") String userId, @Param("productIds") List<String> productIds);

    /**
     * 修改购物车中某种商品的数量
     *
     * @param userId     用户ID
     * @param productId  商品ID
     * @param productNum 要修改的商品数量
     */
    void setProductNumOfCart(@Param("userId") String userId, @Param("productId") String productId,
                             @Param("productNum") int productNum);

    /**
     * 将商品加入购物车
     *
     * @param cart 购物车信息
     */
    void addToCart(Cart cart);

    /**
     * 批量删除购物车中的商品
     *
     * @param userId     用户ID
     * @param productIds 商品ID集
     */
    void deleteProducts(@Param("userId") String userId, List<String> productIds);
}
