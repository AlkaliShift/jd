package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.restHttp.system.order.response.OrderResponse;
import cn.shenghui.jd.service.system.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
