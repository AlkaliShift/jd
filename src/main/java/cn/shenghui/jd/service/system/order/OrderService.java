package cn.shenghui.jd.service.system.order;

import cn.shenghui.jd.dao.system.cart.dto.CartProduct;
import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.order.mapper.OrderMapper;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.dao.system.order.model.OrderDetails;
import cn.shenghui.jd.dao.system.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
@Service
public class OrderService {
    private OrderMapper orderMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 根据用户ID获取所有订单
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    public List<Order> getOrderList(String userId) {
        return orderMapper.getOrderList(userId);
    }

    /**
     * 增加一个订单
     *
     * @param userId        用户ID
     * @param orderProducts 详细商品信息集
     * @param address       收货地址
     */
    public void addOrder(String userId, List<OrderProduct> orderProducts, String address) {
        Order order = new Order();
        String orderTime = new Date() + "";
        String orderId = orderTime.replaceAll(" ", "-") + "-" + (orderMapper.countOrder() + 1);
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setOrderPid("");
        order.setOrderTime(orderTime);
        order.setArrivalTime("");
        order.setAddress(address);
        order.setOrderStatus("0");
        orderMapper.addOrder(order);
    }

    public void separateOrder(List<Product> products, String orderId) {
        for (Product product : products) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderId(orderId);
            orderDetails.setProductId(product.getProductId());
            orderDetails.setProductNum(product.getAvailableNum());
            orderDetails.setUnitPrice(product.getUnitPrice());
            orderDetails.setDescription(product.getDescription());
            orderMapper.addOrderDetails(orderDetails);
        }
    }

    /**
     * 修改订单状态
     *
     * @param orderId     订单ID
     * @param orderStatus 订单状态
     */
    public void updateOrderStatus(String orderId, String orderStatus) {
        orderMapper.updateOrderStatus(orderId, orderStatus);
    }
}
