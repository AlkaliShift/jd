package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.restHttp.system.order.request.AddOrderRequest;
import cn.shenghui.jd.restHttp.system.order.response.OrderBasicResponse;
import cn.shenghui.jd.restHttp.system.order.response.OrderResponse;
import cn.shenghui.jd.service.system.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 订单列表页
     *
     * @return 页面
     */
    @RequestMapping("")
    public ModelAndView orderPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/order/order");
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
        orderService.addOrder(addOrderRequest.getUserId(), addOrderRequest.getProducts(),
                addOrderRequest.getTotalPrice(), addOrderRequest.getAddress());
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
                                                @RequestParam("orderStatus") char orderStatus) {
        OrderBasicResponse response = new OrderBasicResponse();
        orderService.updateOrderStatus(orderId, orderStatus);
        response.setStatusCode(1);
        return response;
    }
}
