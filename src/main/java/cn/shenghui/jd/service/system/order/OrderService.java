package cn.shenghui.jd.service.system.order;

import cn.shenghui.jd.constants.UniversalConstants;
import cn.shenghui.jd.constants.system.order.OrderConstants;
import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.order.mapper.OrderMapper;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.dao.system.order.model.OrderDetails;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

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
     * 根据订单ID获取订单详情列表
     *
     * @param orderId 订单ID
     * @return 订单详情列表
     */
    public List<OrderDetails> getOrderDetailsList(String orderId) {
        return orderMapper.getOrderDetailsList(orderId);
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
     * 增加一个订单
     *
     * @param userId        用户ID
     * @param orderProducts 详细商品信息集
     * @param address       收货地址
     */
    public void addOrder(String userId, List<OrderProduct> orderProducts, String address) {
        //新增主订单
        Order order = new Order();
        String orderTime = DateFormatUtils.format(new Date(), UniversalConstants.PATTERN_TIME);
        String orderId = orderTime.replaceAll("[-:\\s]", "") + (orderMapper.countOrder() + 1);
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setOrderTime(orderTime);
        order.setAddress(address);

        //计算订单总价
        BigDecimal totalPrice = orderProducts.stream()
                .map(orderProduct -> orderProduct.getUnitPrice().multiply(new BigDecimal(orderProduct.getProductNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderConstants.ORDER_STATUS_ORDERED);
        orderMapper.addOrder(order);

        //分组
        Map<String, List<OrderProduct>> listMap = orderProducts.stream().collect(groupingBy(OrderProduct::getWarehouseId));

        //判断分单
        int listMapSize = listMap.size();
        if (listMapSize > 1) {
            //分单
            //增加子订单
            for (Map.Entry<String, List<OrderProduct>> entry : listMap.entrySet()) {
                String warehouseId = entry.getKey();
                List<OrderProduct> tempList = entry.getValue();
                Order childOrder = new Order();
                BeanUtils.copyProperties(order, childOrder);
                childOrder.setOrderId(orderId + warehouseId);
                childOrder.setOrderPid(orderId);
                BigDecimal total = tempList.stream()
                        .map(orderProduct -> orderProduct.getUnitPrice().multiply(new BigDecimal(orderProduct.getProductNum())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                childOrder.setTotalPrice(total);
                orderMapper.addOrder(childOrder);

                //插入子订单中的商品至订单详情表
                for (OrderProduct orderProduct : tempList) {
                    OrderDetails orderDetails = new OrderDetails();
                    BeanUtils.copyProperties(orderProduct, orderDetails);
                    orderDetails.setOrderId(childOrder.getOrderId());
                    orderMapper.addOrderDetails(orderDetails);
                }
            }
        } else {
            //不分单
            //插入订单/子订单至订单详情表
            for (OrderProduct product : orderProducts) {
                OrderDetails orderDetails = new OrderDetails();
                BeanUtils.copyProperties(product, orderDetails);
                orderDetails.setOrderId(orderId);
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
