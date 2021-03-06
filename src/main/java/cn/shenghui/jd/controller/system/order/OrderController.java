package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.constants.system.order.OrderConstants;
import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.order.dto.OrderStatus;
import cn.shenghui.jd.dao.system.order.dto.QueryOrder;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.resthttp.system.order.request.AddOrderRequest;
import cn.shenghui.jd.resthttp.system.order.response.AddOrderResponse;
import cn.shenghui.jd.resthttp.system.order.response.OrderBasicResponse;
import cn.shenghui.jd.resthttp.system.order.response.OrderDetailsResponse;
import cn.shenghui.jd.resthttp.system.order.response.OrderResponse;
import cn.shenghui.jd.service.system.cart.CartService;
import cn.shenghui.jd.service.system.order.OrderService;
import cn.shenghui.jd.service.system.product.ProductService;
import cn.shenghui.jd.utils.CurrentUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:26
 */
@Controller
@Api(value = "Order")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    /**
     * 返回所有订单列表页
     *
     * @return 页面
     */
    @RequestMapping(value = "")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView orderPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/order/order");
        return mv;
    }

    /**
     * 返回订单详情页
     *
     * @return 页面
     */
    @RequestMapping(value = "/orderDetails")
    public ModelAndView orderDetailsPage(@RequestParam("orderId") String orderId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("orderId", orderId);
        mv.setViewName("system/order/orderDetails");
        return mv;
    }

    /**
     * 购物车中订单列表页
     *
     * @param productIds 商品ID集
     * @return 页面
     */
    @RequestMapping(value = "/orderCart")
    public ModelAndView orderCartPage(@RequestParam("productIds") List<String> productIds) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("productIds", productIds);
        mv.setViewName("system/order/orderCart");
        return mv;
    }

    /**
     * 修改订单状态页
     *
     * @param orderId 订单ID
     * @return 页面
     */
    @RequestMapping(value = "/updateOrderStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView updateOrderStatusPage(@RequestParam("orderId") String orderId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("orderId", orderId);
        mv.setViewName("system/order/updateOrderStatus");
        return mv;
    }

    /**
     * 订单页面（用户）
     *
     * @return 页面
     */
    @RequestMapping(value = "/orderUser")
    public ModelAndView orderUserPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/order/orderUser");
        return mv;
    }

    /**
     * 模糊查询订单信息，若搜索内容为空，则返回所有用户订单
     *
     * @param queryOrder 搜索内容包
     * @return 订单列表和状态码：1
     */
    @ApiOperation(value = "获取订单信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public OrderResponse getOrderList(@RequestBody QueryOrder queryOrder) {
        OrderResponse response = new OrderResponse();
        queryOrder.setUserId("");
        response.setOrders(orderService.getOrderList(queryOrder));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 根据订单ID获取订单详情列表
     *
     * @param orderId 订单ID
     * @return 订单详细信息和状态码：1
     */
    @ApiOperation(value = "获取订单详细信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/listOrderDetails")
    @ResponseBody
    public OrderDetailsResponse getOrderDetailsList(@RequestParam("orderId") String orderId) {
        OrderDetailsResponse response = new OrderDetailsResponse();
        response.setOrderDetails(orderService.getOrderDetailsList(orderId));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 模糊查询订单信息，若搜索内容为空，则返回当前用户所有订单
     *
     * @param queryOrder 搜索内容包
     * @return 订单列表和状态码：1
     */
    @ApiOperation(value = "获取订单信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/listUser")
    @ResponseBody
    public OrderResponse getOrderListUser(@RequestBody QueryOrder queryOrder) {
        OrderResponse response = new OrderResponse();
        queryOrder.setUserId(CurrentUserUtils.getUserName());
        response.setOrders(orderService.getOrderList(queryOrder));
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
    public AddOrderResponse addOrder(@RequestBody AddOrderRequest addOrderRequest) {
        AddOrderResponse response = new AddOrderResponse();
        List<OrderProduct> orderProducts = productService.getProductDetails(CurrentUserUtils.getUserName(),
                addOrderRequest.getProductIds());
        if (ObjectUtils.isEmpty(orderProducts)) {
            response.setStatusInfo(0, "查找不到该商品。");
        } else {
            Map<String, List<OrderProduct>> ifSufficient = orderProducts.stream()
                    .collect(Collectors.groupingBy(orderProduct -> {
                        if (orderProduct.getAvailableNum() > 0) {
                            return OrderConstants.AVAILABLE_SUFFICIENT;
                        } else {
                            return OrderConstants.AVAILABLE_INSUFFICIENT;
                        }
                    }));
            if (!ObjectUtils.isEmpty(ifSufficient.get(OrderConstants.AVAILABLE_INSUFFICIENT))) {
                response.setInsufficientProducts(ifSufficient.get(OrderConstants.AVAILABLE_INSUFFICIENT));
            }

            if (!ObjectUtils.isEmpty(ifSufficient.get(OrderConstants.AVAILABLE_SUFFICIENT))) {
                productService.freezeNum(ifSufficient.get(OrderConstants.AVAILABLE_SUFFICIENT));
                orderService.addOrder(CurrentUserUtils.getUserName(),
                        ifSufficient.get(OrderConstants.AVAILABLE_SUFFICIENT), addOrderRequest.getAddress());
                List<String> productIds = new ArrayList<>();
                for (OrderProduct product : ifSufficient.get(OrderConstants.AVAILABLE_SUFFICIENT)) {
                    productIds.add(product.getProductId());
                }
                cartService.deleteProducts(CurrentUserUtils.getUserName(), productIds);
            }
            response.setStatusCode(1);
        }
        return response;
    }

    /**
     * 修改订单状态
     *
     * @param order 订单
     * @return 状态码：1
     */
    @ApiOperation(value = "修改订单状态", notes = "状态码1:修改成功")
    @RequestMapping(value = "/setOrderStatus", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public OrderBasicResponse setOrderStatus(@RequestBody Order order) {
        OrderBasicResponse orderBasicResponse = new OrderBasicResponse();
        String orderId = order.getOrderId();

        Order previousOrder = orderService.getOrder(orderId);
        if (ObjectUtils.isEmpty(previousOrder)) {
            orderBasicResponse.setStatusInfo(0, "找不到该订单。");
        } else {
            if (orderService.ifParent(orderId)) {
                orderBasicResponse.setStatusInfo(0, "无法修改主订单状态。");
            } else {
                //建立状态对象
                OrderStatus newOrder = new OrderStatus(previousOrder);
                String orderStatus = order.getOrderStatus();

                //操作行为
                if (OrderConstants.ORDER_STATUS_CANCELLED.equals(orderStatus)) {
                    return newOrder.cancelOrder();
                } else if (OrderConstants.ORDER_STATUS_DELIVERED.equals(orderStatus)) {
                    return newOrder.deliverOrder();
                } else if (OrderConstants.ORDER_STATUS_COMPLETED.equals(orderStatus)) {
                    return newOrder.confirmOrder();
                } else {
                    orderBasicResponse.setStatusInfo(0, "前端发送的订单状态与后端不匹配。");
                }
            }
        }
        return orderBasicResponse;
    }
}
