package com.jinglitong.springshop.enumeration;

/**
 * @ClassName RealNameEnum
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/25 18:28
 * @Version 1.0
 **/
public enum RealNameEnum {

    TYPE_XJD_PAY("xjd_ddzf","现金贷订单支付"),
    TYPE_XJD_INCOME("xjd_ddch","现金贷订单退回"),
    TYPE_DJB_PAY("djb_ddzf","代金币订单支付"),
    TYPE_DJB_INCOME("djb_ddch","代金币订单退回"),
    TYPE_WB_INCOME("wb_ddch","物贝订单退回"),
    TYPE_WB_PAY("wb_ddzf","物贝订单支付"),
    TYPE_XB_INCOME("xb_ddch","现贝订单退回"),
    TYPE_XB_PAY("xb_ddzf","现贝订单支付"),
    TYPE_BF_INCOME("bf_ddch","宝分订单退回"),
    TYPE_BF_PAY("bf_ddzf","宝分订单支付");

    private String type;//类型
    private String name;//名称

    RealNameEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
