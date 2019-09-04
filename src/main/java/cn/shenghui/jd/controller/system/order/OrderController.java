package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.restHttp.system.order.request.AddOrderRequest;
import cn.shenghui.jd.restHttp.system.order.response.OrderBasicResponse;
import cn.shenghui.jd.restHttp.system.order.response.OrderResponse;
import cn.shenghui.jd.service.system.cart.CartService;
import cn.shenghui.jd.service.system.order.OrderService;
import cn.shenghui.jd.service.system.product.ProductService;
import cn.shenghui.jd.utils.CurrentUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:26
 */
@Controller
@Api(value = "Order")
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;
    private ProductService productService;
    private CartService cartService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * 订单列表页
     *
     * @param productIds 商品ID集
     * @return 页面
     */
    @RequestMapping(value = "")
    public ModelAndView orderPage(@RequestParam("productIds") List<String> productIds) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("productIds", productIds);
        mv.setViewName("system/order/orderCart");
        return mv;
    }

    /**
     * 根据用户ID查找订单列表
     *
     * @param userId 用户ID
     * @return 状态码：1
     */
    @ApiOperation(value = "根据用户ID查找订单列表", notes = "状态码1:查询成功")
    @RequestMapping(value = "/list")
    @ResponseBody
    public OrderResponse getOrderList(@RequestParam("userId") String userId) {
        OrderResponse response = new OrderResponse();
        response.setOrders(orderService.getOrderList(userId));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 增加单个订单
     *
     * @param addOrderRequest 用户ID，商品ID集，订单总价，收货地址
     * @return 状态码：1
     */
    @ApiOperation(value = "增加单个订单", notes = "状态码1:创建成功")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public OrderBasicResponse addOrder(@RequestBody AddOrderRequest addOrderRequest) {
        OrderBasicResponse response = new OrderBasicResponse();
        List<OrderProduct> orderProducts = productService.getProductDetails(CurrentUserUtils.getUserName(), addOrderRequest.getProductIds());
        orderService.addOrder(CurrentUserUtils.getUserName(), orderProducts, addOrderRequest.getAddress());
        response.setStatusCode(1);
        return response;
    }

    /**
     * 修改订单状态
     *
     * @param orderId     订单ID
     * @param orderStatus 订单状态
     * @return 状态码：1
     */
    @ApiOperation(value = "修改订单状态", notes = "状态码1:修改成功")
    @RequestMapping(value = "/updateOrderStatus", method = RequestMethod.POST)
    @ResponseBody
    public OrderBasicResponse updateOrderStatus(@RequestParam("orderId") String orderId,
                                                @RequestParam("orderStatus") String orderStatus) {
        OrderBasicResponse response = new OrderBasicResponse();
        orderService.updateOrderStatus(orderId, orderStatus);
        response.setStatusCode(1);
        return response;
    }
}
