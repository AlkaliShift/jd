package cn.shenghui.jd.dao.system.order.mapper;

import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.order.model.Order;
import cn.shenghui.jd.dao.system.order.model.OrderDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:30
 */
@Mapper
public interface OrderMapper {

    /**
     * 根据用户ID查找订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> getOrderList(@Param("userId") String userId);

    /**
     * 增加单个订单
     *
     * @param order 订单信息
     */
    void addOrder(Order order);

    /**
     * 修改订单状态
     *
     * @param orderId     订单ID
     * @param orderStatus 订单状态
     */
    void updateOrderStatus(@Param("orderId") String orderId, @Param("orderStatus") String orderStatus);

    /**
     * 添加订单详细信息
     *
     * @param orderDetails 订单详细信息
     */
    void addOrderDetails(OrderDetails orderDetails);

    /**
     * 获得订单表行数
     *
     * @return 行数
     */
    int countOrder();

    /**
     * 获得详细订单表行数
     * @return 行数
     */
    int countOrderDetails();
}
