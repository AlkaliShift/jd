package cn.shenghui.jd.service.system.cart;

import cn.shenghui.jd.dao.system.cart.mapper.CartMapper;
import cn.shenghui.jd.dao.system.cart.model.Cart;
import cn.shenghui.jd.dao.system.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
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
    public List<Cart> getCartList(String userId) {
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
    public int addToCart(String userId, String productId, int num, int availableNum) {
        int previousNum = cartMapper.getFromCart(userId, productId).getProductNum();
        if (previousNum != 0) {
            int currentNum = previousNum + num;
            this.setProductNum(userId, productId, currentNum, availableNum);
        } else {
            if (num > availableNum) {
                return -1;
            } else {
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(productId);
                cart.setProductNum(num);
                cartMapper.addToCart(cart);
                return 0;
            }
        }
    }

    /**
     * 修改购物车中的指定商品数量
     *
     * @param userId       用户ID
     * @param productId    商品ID
     * @param num          修改的商品数量
     * @param availableNum 商品可用库存数量
     */
    public int setProductNum(String userId, String productId, int num, int availableNum) {
        if (num > availableNum) {
            return -1;
        } else {
            num = availableNum - num;
            cartMapper.setProductNum(num);
            return 0;
        }
    }

    /**
     * 从购物车中批量移除商品
     *
     * @param userId   用户ID
     * @param products 商品集
     */
    public void deleteProducts(String userId, List<Product> products) {
        cartMapper.deleteProducts(userId, products);
    }
}
