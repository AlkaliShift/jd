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
     * 库存充足
     */
    public static final String AVAILABLE_SUFFICIENT = "sufficient";

    /**
     * 库存不充足
     */
    public static final String AVAILABLE_INSUFFICIENT = "insufficient";

    /**
     * 订单状态：已下单
     */
    public static final String ORDER_STATUS_ORDERED = "ordered" ;

    /**
     * 订单状态：已发货
     */
    public static final String ORDER_STATUS_DELIVERED = "delivered" ;

    /**
     * 订单状态：已完成
     */
    public static final String ORDER_STATUS_COMPLETED = "completed" ;

    /**
     * 订单状态：已取消
     */
    public static final String ORDER_STATUS_CANCELLED = "cancelled" ;
}
