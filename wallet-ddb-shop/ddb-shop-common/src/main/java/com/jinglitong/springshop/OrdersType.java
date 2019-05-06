package com.jinglitong.springshop;

public class OrdersType {
    /**
     * 子订单状态审核通过
     */
    public static int ORDER_APPROVED = 1;
    /**
     * 子订单状态审核不通过
     */
    public static int ORDER_NOAPPROVED = 2;
    /**
     * 0 待支付
     */
    public static int ORDER_STATUS_UNPAY = 0;
    /**
     * 1 发货中
     */
    public static int ORDER_STATUS_IN_DELIVERY  = 1;
    /**
     * 2 已取消
     */
    public static int ORDER_STATUS_CANCEL = 2;
    /**
     * 3 已完成
     */
    public static int ORDER_STATUS_FINISH = 3;
    /**
     * 4 待发货
     */
    public static int ORDER_STATUS_FOR_DELIVERY = 4;
    /**
     * 5 已发货
     */
    public static int ORDER_STATUS_DELIVERED = 5;
    /**
     * 6 审核中
     */
    public static int ORDER_IN_AUDIT = 6;
    /**
     * 修改中
     */
    public static int ORDER_MODYING = 1;
    /**
     * 非修改中
     */
    public static int ORDER_UNMODYING = 0;
    /**
     * 订单为主订单时order_parent字段的值
     */
    public static String ORDER_PARENT = "P";
}
