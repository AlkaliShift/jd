package cn.shenghui.jd.service.system.cart;

import cn.shenghui.jd.dao.system.cart.mapper.CartMapper;
import cn.shenghui.jd.dao.system.cart.model.Cart;
import cn.shenghui.jd.dao.system.product.mapper.ProductMapper;
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
    private ProductMapper productMapper;

    @Autowired
    public void setCartMapper(CartMapper cartMapper){
        this.cartMapper = cartMapper;
    }

    public void setProductMapper(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    /**
     * 根据用户ID获取购物车中所有商品
     * @param userId 用户ID
     * @return 购物车列表
     */
    public List<Cart> getCartList(String userId){
        return cartMapper.getCartList(userId);
    }

    /**
     * 将商品加入购物车
     * @param userId 用户ID
     * @param productId 商品ID
     * @param num 用户选择的数量
     */
    public void addProductToCart(String userId, String productId, int num){
        int currentNum = productMapper.getPro
        cartMapper.addProductToCart(userId, productId, num);
    }

    /**
     * 修改购物车中的指定商品数量
     * @param userId 用户ID
     * @param productId 商品ID
     * @param num 修改的商品数量
     */
    public void setProductNum(String userId, String productId, int num){
        cartMapper.setProductNum(userId, productId, num);
    }

    /**
     * 从购物车中批量移除商品
     * @param userId 用户ID
     * @param products 商品
     */
    public void removeProducts(String userId, List<Product> products){
        cartMapper.removeProducts(userId, products);
    }
}
