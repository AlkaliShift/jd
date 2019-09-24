package cn.shenghui.jd.controller.system.order;

import cn.shenghui.jd.constants.system.order.OrderConstants;
import cn.shenghui.jd.dao.system.order.dto.IfSufficient;
import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
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
     * 返回所有订单列表页
     *
     * @return 页面
     */
    @RequestMapping(value = "")
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
     * @param content 搜索内容
     * @return 订单列表和状态码：1
     */
    @ApiOperation(value = "获取订单信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/list")
    @ResponseBody
    public OrderResponse getOrderList(@RequestParam("content") String content) {
        OrderResponse response = new OrderResponse();
        response.setOrders(orderService.getOrderList(content, ""));
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
     * @param content 搜索内容
     * @return 订单列表和状态码：1
     */
    @ApiOperation(value = "获取订单信息", notes = "状态码1:查询成功")
    @RequestMapping(value = "/listUser")
    @ResponseBody
    public OrderResponse getOrderListUser(@RequestParam("content") String content) {
        OrderResponse response = new OrderResponse();
        response.setOrders(orderService.getOrderList(content, CurrentUserUtils.getUserName()));
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
    public OrderBasicResponse setOrderStatus(@RequestBody Order order) {
        OrderBasicResponse response = new OrderBasicResponse();
        String orderId = order.getOrderId();
        String orderStatus = order.getOrderStatus();
        Order previousOrder = orderService.getOrder(orderId);
        if (ObjectUtils.isEmpty(previousOrder)) {
            response.setStatusInfo(0, "找不到该订单。");
        } else {
            if (orderService.ifParent(orderId)) {
                response.setStatusInfo(0, "无法修改主订单状态。");
            } else {
                if (OrderConstants.ORDER_STATUS_ORDERED.equals(orderStatus) || OrderConstants.ORDER_STATUS_DELIVERED.equals(orderStatus) ||
                        OrderConstants.ORDER_STATUS_COMPLETED.equals(orderStatus) || OrderConstants.ORDER_STATUS_CANCELLED.equals(orderStatus)) {
                    String previousOrderStatus = previousOrder.getOrderStatus();
                    if (OrderConstants.ORDER_STATUS_COMPLETED.equals(orderStatus) && OrderConstants.ORDER_STATUS_ORDERED.equals(previousOrderStatus)) {
                        response.setStatusInfo(0, "订单尚未发货，请先安排发货。");
                    } else if (OrderConstants.ORDER_STATUS_ORDERED.equals(orderStatus) && OrderConstants.ORDER_STATUS_ORDERED.equals(previousOrderStatus)) {
                        response.setStatusInfo(0, "订单已下单，无法重复下单。");
                    } else if (OrderConstants.ORDER_STATUS_DELIVERED.equals(orderStatus) && OrderConstants.ORDER_STATUS_DELIVERED.equals(previousOrderStatus)) {
                        response.setStatusInfo(0, "订单已发货，无法重复发货。");
                    } else if (OrderConstants.ORDER_STATUS_COMPLETED.equals(orderStatus) && OrderConstants.ORDER_STATUS_COMPLETED.equals(previousOrderStatus)) {
                        response.setStatusInfo(0, "订单已完成，无法重复确认。");
                    } else if (OrderConstants.ORDER_STATUS_ORDERED.equals(orderStatus) && OrderConstants.ORDER_STATUS_DELIVERED.equals(previousOrderStatus)) {
                        response.setStatusInfo(0, "订单已发货，无法重新下单。");
                    } else if (OrderConstants.ORDER_STATUS_ORDERED.equals(orderStatus) && OrderConstants.ORDER_STATUS_COMPLETED.equals(previousOrderStatus)) {
                        response.setStatusInfo(0, "订单已完成，无法重新下单。");
                    } else if (OrderConstants.ORDER_STATUS_DELIVERED.equals(orderStatus) && OrderConstants.ORDER_STATUS_COMPLETED.equals(previousOrderStatus)) {
                        response.setStatusInfo(0, "订单已完成，无法重新发货。");
                    } else {
                        orderService.updateOrderStatus(orderId, orderStatus);
                        String orderPid = previousOrder.getOrderPid();
                        if (OrderConstants.ORDER_STATUS_CANCELLED.equals(orderStatus)) {
                            this.unFreezeNum(orderId);
                            if (orderService.ifAllThisStatus(orderPid, OrderConstants.ORDER_STATUS_CANCELLED)) {
                                orderService.updateOrderStatus(orderPid, orderStatus);
                            }
                        } else if (OrderConstants.ORDER_STATUS_COMPLETED.equals(orderStatus)) {
                            if (orderService.ifAllThisStatus(orderPid, OrderConstants.ORDER_STATUS_COMPLETED)) {
                                orderService.updateOrderStatus(orderPid, orderStatus);
                            }
                        }
                        response.setStatusCode(1);
                    }
                } else {
                    response.setStatusInfo(0, "前端发送的订单状态与后端不匹配。");
                }
            }
        }
        return response;
    }

    /**
     * 解冻商品库存
     *
     * @param orderId 订单ID
     */
    private void unFreezeNum(String orderId) {
        productService.unFreezeNum(orderService.getProductsByOrderId(orderId));
    }
}
