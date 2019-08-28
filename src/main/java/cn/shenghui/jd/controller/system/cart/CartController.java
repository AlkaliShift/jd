package cn.shenghui.jd.controller.system.cart;

import cn.shenghui.jd.dao.system.cart.model.Cart;
import cn.shenghui.jd.restHttp.system.cart.request.DeleteCartRequest;
import cn.shenghui.jd.restHttp.system.cart.response.CartBasicResponse;
import cn.shenghui.jd.restHttp.system.cart.response.CartResponse;
import cn.shenghui.jd.service.system.cart.CartService;
import cn.shenghui.jd.service.system.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public void setCartService(CartService cartService){
        this.cartService = cartService;
    }

    @Autowired
    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    /**
     * 根据用户ID获得其购物车中所有商品信息
     *
     * @param userId 用户ID
     * @return 商品信息列表和状态码：1
     */
    @ApiOperation(value = "根据用户ID获得其购物车中所有商品信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/list")
    @ResponseBody
    public CartResponse getCartList(@RequestParam("userId") String userId){
        CartResponse response = new CartResponse();
        response.setCartProducts(cartService.getCartList(userId));
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
    public CartBasicResponse addCategory( Cart cart) {
        CartBasicResponse response = new CartBasicResponse();
        List<String> productIds = new ArrayList<>();
        productIds.add(cart.getProductId());
        int availableNum = productService.getProductsByIds(productIds).get(0).getAvailableNum();
        boolean check = cartService.addToCart(cart.getUserId(), cart.getProductId(),
                                              cart.getProductNum(), availableNum);
        if (!check){
            response.setStatusInfo(0, "订购数量超过商品库存数量");
        }else{
            response.setStatusCode(1);
        }
        return response;
    }

    /**
     * 设置购物车中单个商品的数量
     *
     * @param cart 购物车商品
     * @return 状态码：1/状态码：0及错误信息
     */
    @ApiOperation(value = "添加单个商品进购物车", notes = "状态码1:修改成功")
    @RequestMapping(value = "/setProductNumOfCart", method = RequestMethod.POST)
    @ResponseBody
    public CartBasicResponse setProductNumOfCart(@RequestBody Cart cart){
        CartBasicResponse response = new CartBasicResponse();
        if (cart.getProductNum() == 0){
            response.setStatusInfo(0, "商品数量不能为0");
        }else{
            cartService.setProductNumOfCart(cart.getUserId(), cart.getProductId(), cart.getProductNum());
            response.setStatusCode(1);
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
    public CartBasicResponse deleteProducts(@RequestBody DeleteCartRequest deleteCartRequest){
        CartBasicResponse response = new CartBasicResponse();
        cartService.deleteProducts(deleteCartRequest.getUserId(), deleteCartRequest.getProductIds());
        response.setStatusCode(1);
        return response;
    }
}
