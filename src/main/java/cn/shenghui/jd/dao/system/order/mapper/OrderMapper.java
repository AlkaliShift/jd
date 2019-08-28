package cn.shenghui.jd.dao.system.order.mapper;

import cn.shenghui.jd.dao.system.order.model.Order;
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
}
