package cn.shenghui.jd.dao.system.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/27 15:05
 */
@Data
public class QueryOrder {

    /**
     * 搜索内容：订单号，用户，地址
     */
    String content;

    /**
     * 价格范围
     */
    BigDecimal priceMin;

    /**
     * 价格范围
     */
    BigDecimal priceMax;

    /**
     * 订单状态
     */
    String orderStatus;

    /**
     * 开始时间
     */
    String start;

    /**
     * 结束时间
     */
    String end;

    /**
     * 用户
     */
    String userId;
}
