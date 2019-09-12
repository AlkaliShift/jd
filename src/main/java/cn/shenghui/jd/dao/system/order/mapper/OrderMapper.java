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
     * 模糊查询订单信息，若搜索内容为空，用户为空，则返回所有用户订单
     *
     * @param content 搜索内容
     * @param userId  用户ID
     * @return 订单列表
     */
    List<Order> getOrderList(@Param("content") String content, @Param("userId") String userId);

    /**
     * 根据订单ID获取订单详情列表
     *
     * @param orderId 订单ID
     * @return 订单详情列表
     */
    List<OrderDetails> getOrderDetailsList(@Param("orderId") String orderId);

    /**
     * 根据订单ID查找对应的商品信息
     *
     * @param orderId 订单ID
     * @return 商品ID和数量
     */
    List<OrderProduct> getProductsByOrderId(@Param("orderId") String orderId);

    /**
     * 根据订单ID查找订单
     *
     * @param orderId 订单ID
     * @return 订单信息
     */
    Order getOrder(@Param("orderId") String orderId);

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
     *
     * @return 行数
     */
    int countOrderDetails();

    /**
     * 判断该订单是否有子订单
     *
     * @param orderId 订单ID
     * @return 子订单数
     */
    int ifParent(@Param("orderId") String orderId);

    /**
     * 判断该订单的子订单是否全部处于给定状态
     *
     * @param orderPid    主订单ID
     * @param orderStatus 订单状态
     * @return 未完成子订单数
     */
    int ifAllThisStatus(@Param("orderPid") String orderPid, @Param("orderStatus") String orderStatus);
}
