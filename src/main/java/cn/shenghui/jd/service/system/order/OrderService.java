package cn.shenghui.jd.service.system.order;

import cn.shenghui.jd.dao.system.order.dto.IfSufficient;
import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.order.mapper.OrderMapper;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.dao.system.order.model.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
     * 判断商品库存是否充足
     *
     * @param orderProducts 商品详细信息列表
     * @return 充足：true/不足:false
     */
    public IfSufficient ifSufficient(List<OrderProduct> orderProducts) {
        IfSufficient ifSufficient = new IfSufficient();
        int productNum;
        int availableNum;
        List<OrderProduct> sufficientProducts = new ArrayList<>();
        List<OrderProduct> insufficientProducts = new ArrayList<>();
        for (OrderProduct product : orderProducts) {
            productNum = product.getProductNum();
            availableNum = product.getAvailableNum();
            if (productNum > availableNum) {
                insufficientProducts.add(product);
            } else {
                sufficientProducts.add(product);
            }
        }
        ifSufficient.setSufficientProducts(sufficientProducts);
        ifSufficient.setInsufficientProducts(insufficientProducts);
        return ifSufficient;
    }

    /**
     * 增加一个订单
     *
     * @param userId        用户ID
     * @param orderProducts 详细商品信息集
     * @param address       收货地址
     */
    public void addOrder(String userId, List<OrderProduct> orderProducts, String address) {
        int productNum;
        BigDecimal unitPrice;
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderProduct product : orderProducts) {
            productNum = product.getProductNum();
            unitPrice = product.getUnitPrice();
            totalPrice = totalPrice.add(unitPrice.multiply(new BigDecimal(productNum)));
        }
        Order order = new Order();
        String orderTime = new Date() + "";
        String orderId = orderTime.replaceAll(" ", "-") + "-" + (orderMapper.countOrder() + 1);
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setOrderPid("");
        order.setTotalPrice(totalPrice);
        order.setOrderTime(orderTime);
        order.setArrivalTime("");
        order.setAddress(address);
        order.setOrderStatus("0");
        orderMapper.addOrder(order);
        separateOrder(orderProducts, order);
    }

    /**
     * 分单
     *
     * @param orderProducts 商品详细信息
     * @param order       主订单
     */
    private void separateOrder(List<OrderProduct> orderProducts, Order order) {
        String warehouseId = orderProducts.get(0).getWarehouseId();
        boolean ifNeedSeparate = false;
        for (OrderProduct product : orderProducts) {
            String currentWarehouseId = product.getWarehouseId();
            if (!warehouseId.equals(currentWarehouseId)) {
                ifNeedSeparate = true;
            }
        }

        if (ifNeedSeparate) {
            String mainOrderId = order.getOrderId();
            for (OrderProduct product : orderProducts) {
                order.setOrderPid(mainOrderId);
                String orderId = mainOrderId + "-" + (orderMapper.countOrderDetails() + 1);
                order.setOrderId(orderId);
                int productNum = product.getProductNum();
                BigDecimal unitPrice = product.getUnitPrice();
                BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(productNum));
                order.setTotalPrice(totalPrice);
                orderMapper.addOrder(order);
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderId(orderId);
                orderDetails.setProductId(product.getProductId());
                orderDetails.setProductName(product.getProductName());
                orderDetails.setProductNum(productNum);
                orderDetails.setUnitPrice(unitPrice);
                orderDetails.setDescription(product.getDescription());
                orderMapper.addOrderDetails(orderDetails);
            }
        } else {
            for (OrderProduct product : orderProducts) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderId(order.getOrderId());
                orderDetails.setProductId(product.getProductId());
                orderDetails.setProductName(product.getProductName());
                orderDetails.setProductNum(product.getProductNum());
                orderDetails.setUnitPrice(product.getUnitPrice());
                orderDetails.setDescription(product.getDescription());
                orderMapper.addOrderDetails(orderDetails);
            }
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
