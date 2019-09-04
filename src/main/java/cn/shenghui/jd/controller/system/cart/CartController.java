package cn.shenghui.jd.controller.system.cart;

import cn.shenghui.jd.dao.system.cart.dto.CartProduct;
import cn.shenghui.jd.dao.system.cart.model.Cart;
import cn.shenghui.jd.dao.system.product.model.Product;
import cn.shenghui.jd.restHttp.system.cart.response.CartBasicResponse;
import cn.shenghui.jd.restHttp.system.cart.response.CartResponse;
import cn.shenghui.jd.service.system.cart.CartService;
import cn.shenghui.jd.service.system.product.ProductService;
import cn.shenghui.jd.utils.CurrentUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:26
 */
@Controller
@Api(value = "Cart")
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private ProductService productService;

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 购物车页面
     *
     * @return 页面
     */
    @RequestMapping("")
    public ModelAndView cartPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/cart/cart");
        return mv;
    }

    /**
     * 根据商品ID更新购物车中商品数量页面
     *
     * @param productId 商品ID
     * @return 页面
     */
    @RequestMapping("/updateCart")
    public ModelAndView setProductNumPage(@RequestParam("productId") String productId) {
        ModelAndView mv = new ModelAndView();
        List<String> productIds = new ArrayList<>();
        productIds.add(productId);

        List<CartProduct> products = cartService.getProductFromCart(CurrentUserUtils.getUserName(), productIds);
        if (!ObjectUtils.isEmpty(products)){
            mv.addObject("product", products.get(0));
        }
        mv.setViewName("system/cart/updateCart");
        return mv;
    }

    /**
     * 根据用户ID获得其购物车中所有商品信息
     *
     * @return 商品信息列表和状态码：1
     */
    @ApiOperation(value = "根据用户ID获得其购物车中所有商品信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/list")
    @ResponseBody
    public CartResponse getCartList() {
        CartResponse response = new CartResponse();
        response.setCartProducts(cartService.getCartList(CurrentUserUtils.getUserName()));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 根据用户ID和商品ID获得商品信息
     *
     * @return 商品信息列表和状态码：1
     */
    @ApiOperation(value = "根据用户ID和商品ID获得商品信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/listProduct")
    @ResponseBody
    public CartResponse getCartListProduct(@RequestParam("productIds") List<String> productIds) {
        CartResponse response = new CartResponse();
        response.setCartProducts(cartService.getProductFromCart(CurrentUserUtils.getUserName(), productIds));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 添加单个商品进购物车
     *
     * @param cart 购物车商品
     * @return 状态码：1/状态码：0及错误信息
     */
    @ApiOperation(value = "添加单个商品进购物车", notes = "状态码1:添加成功")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public CartBasicResponse addCartProduct(@RequestBody Cart cart, @RequestParam("action") String action) {
        CartBasicResponse response = new CartBasicResponse();
        List<String> productIds = new ArrayList<>();
        productIds.add(cart.getProductId());
        List<Product> products = productService.getProductsByIds(productIds);
        if (!ObjectUtils.isEmpty(products)) {
            int availableNum = productService.getProductsByIds(productIds).get(0).getAvailableNum();
            cart.setUserId(CurrentUserUtils.getUserName());
            boolean check = cartService.addToCart(cart.getUserId(), cart.getProductId(),
                    cart.getProductNum(), availableNum, action);
            if (!check) {
                response.setStatusInfo(0, "订购数量超过商品库存数量");
            } else {
                response.setStatusCode(1);
            }
        } else {
            response.setStatusInfo(0, "找不到该商品");
        }
        return response;
    }

    /**
     * 批量删除购物车中的商品
     *
     * @return 状态码：1
     */
    @ApiOperation(value = "批量删除购物车中的商品", notes = "状态码1:删除成功")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CartBasicResponse deleteProducts(@RequestBody List<String> productIds) {
        CartBasicResponse response = new CartBasicResponse();
        cartService.deleteProducts(CurrentUserUtils.getUserName(), productIds);
        response.setStatusCode(1);
        return response;
    }
}
