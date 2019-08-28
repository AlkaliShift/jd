package cn.shenghui.jd.service.system.order;

import cn.shenghui.jd.dao.system.order.mapper.OrderMapper;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.dao.system.order.model.OrderDetails;
import cn.shenghui.jd.dao.system.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
@Service
public class OrderService {
    private OrderMapper orderMapper;
    private static int ID = 0;

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
     * @param userId     用户ID
     * @param products   商品集
     * @param totalPrice 订单总价
     * @param address    收货地址
     */
    public void addOrder(String userId, List<Product> products, BigDecimal totalPrice, String address) {
        Order order = new Order();
        long orderTime = System.currentTimeMillis();
        ID = ID + 1;
        String orderId = orderTime + "-" + ID;
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderTime(orderTime + "");
        order.setAddress(address);
        order.setOrderStatus((char)0);
        orderMapper.addOrder(order);
        for (Product product : products) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderId(orderId);
            orderDetails.setProductId(product.getProductId());
            orderDetails.setProductNum(product.getAvailableNum());
            orderDetails.setUnitPrice(product.getUnitPrice());
            orderDetails.setDescription(product.getDescription());
            orderMapper.addOrderDetails(orderDetails);
        }
        //TODO id & details
    }

    public void mainOrder(){

    }

    public void separateOrder() {
    }

    /**
     * 修改订单状态
     *
     * @param orderId     订单ID
     * @param orderStatus 订单状态
     */
    public void setOrderStatus(String orderId, char orderStatus) {
        orderMapper.setOrderStatus(orderId, orderStatus);
    }
}
