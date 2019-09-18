package cn.shenghui.jd.constants.system.order;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/9/9 09:40
 */
public class OrderConstants {

    private OrderConstants(){

    }

    /**
     * 订单状态：已下单
     */
    public static String ORDER_STATUS_ORDERED = "ordered" ;

    /**
     * 订单状态：已发货
     */
    public static String ORDER_STATUS_DELIVERED = "delivered" ;

    /**
     * 订单状态：已完成
     */
    public static String ORDER_STATUS_COMPLETED = "completed" ;

    /**
     * 订单状态：已取消
     */
    public static String ORDER_STATUS_CANCELLED = "cancelled" ;
}
