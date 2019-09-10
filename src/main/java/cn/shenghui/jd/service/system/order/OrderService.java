package cn.shenghui.jd.service.system.order;

import cn.shenghui.jd.dao.system.order.dto.IfSufficient;
import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.order.mapper.OrderMapper;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.dao.system.order.model.OrderDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.shenghui.jd.constants.system.order.OrderConstants.ORDER_STATUS_ORDERED;

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
     * 模糊查询订单信息，若搜索内容为空，用户为空，则返回所有用户订单
     *
     * @param content 搜索内容
     * @param userId  用户ID
     * @return 订单列表
     */
    public List<Order> getOrderList(String content, String userId) {
        return orderMapper.getOrderList(content, userId);
    }

    /**
     * 根据订单ID查找对应的商品ID
     *
     * @param orderId 订单ID
     * @return 商品ID和数量
     */
    public List<OrderProduct> getProductsByOrderId(String orderId) {
        return orderMapper.getProductsByOrderId(orderId);
    }

    /**
     * 根据订单ID查找订单
     *
     * @param orderId 订单ID
     * @return 订单信息
     */
    public Order getOrder(String orderId) {
        return orderMapper.getOrder(orderId);
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
        order.setOrderStatus(ORDER_STATUS_ORDERED);
        orderMapper.addOrder(order);
        separateOrder(orderProducts, order);
    }

    /**
     * 分单
     *
     * @param orderProducts 商品详细信息
     * @param order         主订单
     */
    private void separateOrder(List<OrderProduct> orderProducts, Order order) {
        String mainOrderId = order.getOrderId();
        String warehouseId = orderProducts.get(0).getWarehouseId();
        String currentWarehouseId;
        boolean ifNeedSeparate = false;
        for (OrderProduct product : orderProducts) {
            currentWarehouseId = product.getWarehouseId();
            if (!warehouseId.equals(currentWarehouseId)) {
                ifNeedSeparate = true;
            }
        }

        if (ifNeedSeparate) {
            // 分单
            List<Order> tempOrders = new ArrayList<>();
            String childOrderId;
            int productNum;
            BigDecimal unitPrice;
            BigDecimal totalPrice;

            for (OrderProduct product : orderProducts) {
                // 创建子订单
                Order childOrder = new Order();
                BeanUtils.copyProperties(order, childOrder);
                childOrder.setOrderPid(mainOrderId);
                childOrderId = mainOrderId + "-" + product.getWarehouseId();
                childOrder.setOrderId(childOrderId);
                productNum = product.getProductNum();
                unitPrice = product.getUnitPrice();
                totalPrice = unitPrice.multiply(new BigDecimal(productNum));
                childOrder.setTotalPrice(totalPrice);

                // 插入子订单至订单详情表
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderId(childOrderId);
                orderDetails.setProductId(product.getProductId());
                orderDetails.setProductName(product.getProductName());
                orderDetails.setUnitPrice(unitPrice);
                orderDetails.setProductNum(productNum);
                orderDetails.setDescription(product.getDescription());
                orderMapper.addOrderDetails(orderDetails);

                // 查找同一仓库的子订单，若为同一仓库，将商品总价相加后记录在tempOrders中
                int len = tempOrders.size();
                boolean flag = true;
                if (len > 0) {
                    for (Order tempOrder : tempOrders) {
                        String tempOrderId = tempOrder.getOrderId();
                        BigDecimal tempTotalPrice = tempOrder.getTotalPrice();
                        if (childOrderId.equals(tempOrderId)) {
                            tempTotalPrice = tempTotalPrice.add(totalPrice);
                            tempOrder.setTotalPrice(tempTotalPrice);
                            flag = false;
                        }
                    }
                    if (flag) {
                        tempOrders.add(childOrder);
                    }
                } else {
                    tempOrders.add(childOrder);
                }
            }

            // 插入子订单至订单列表中
            for (Order tempOrder : tempOrders) {
                orderMapper.addOrder(tempOrder);
            }
        } else {
            //不分单
            for (OrderProduct product : orderProducts) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderId(mainOrderId);
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

    /**
     * 判断该订单是否有子订单
     *
     * @param orderId 订单ID
     * @return 有子订单：true/无子订单：false
     */
    public boolean ifParent(String orderId) {
        int childNum = orderMapper.ifParent(orderId);
        return childNum != 0;
    }


    /**
     * 判断该订单的子订单是否全部处于给定状态
     *
     * @param orderPid    主订单ID
     * @param orderStatus 订单状态
     * @return 子订单全部为给定状态：true/反之：false
     */
    public boolean ifAllThisStatus(String orderPid, String orderStatus) {
        int completed = orderMapper.ifAllThisStatus(orderPid, orderStatus);
        return completed == 0;
    }
}
