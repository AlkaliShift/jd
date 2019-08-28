package cn.shenghui.jd.service.system.cart;

import cn.shenghui.jd.dao.system.cart.dto.CartProduct;
import cn.shenghui.jd.dao.system.cart.mapper.CartMapper;
import cn.shenghui.jd.dao.system.cart.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
@Service
public class CartService {

    private CartMapper cartMapper;

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    /**
     * 根据用户ID获取购物车中所有商品
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    public List<CartProduct> getCartList(String userId) {
        return cartMapper.getCartList(userId);
    }

    /**
     * 将商品加入购物车
     *
     * @param userId       用户ID
     * @param productId    商品ID
     * @param num          用户选择的数量
     * @param availableNum 商品可用库存数量
     */
    public boolean addToCart(String userId, String productId, int num, int availableNum) {
        int productNum = cartMapper.getProductNumFromCart(userId, productId);
        if (productNum != 0) {
            num = productNum + num;
        }

        if (num > availableNum) {
            return false;
        } else {
            if (productNum != 0) {
                this.setProductNumOfCart(userId, productId, num);
            } else {
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(productId);
                cart.setProductNum(num);
                cartMapper.addToCart(cart);
            }
            return true;
        }
    }

    /**
     * 设置购物车中单个商品的数量
     *
     * @param userId     用户ID
     * @param productId  商品ID
     * @param productNum 选购的商品数量
     */
    public void setProductNumOfCart(String userId, String productId, int productNum) {
        cartMapper.setProductNumOfCart(userId, productId, productNum);
    }

    /**
     * 从购物车中批量移除商品
     *
     * @param userId     用户ID
     * @param productIds 商品集
     */
    public void deleteProducts(String userId, List<String> productIds) {
        cartMapper.deleteProducts(userId, productIds);
    }
}
