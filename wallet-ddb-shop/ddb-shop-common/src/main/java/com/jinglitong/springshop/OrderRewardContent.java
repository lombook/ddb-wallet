package com.jinglitong.springshop;

public class OrderRewardContent {

    //zjyq直接邀请
    public static String REWARD_ZJYQ = "zjyq";
    //jjyq直接邀请
    public static String REWARD_JJYQ = "jjyq";
    //yjjd一级节点
    public static String REWARD_YJYQ = "yjyq";
    //ddjl订单激励 2019-03-01新版本舍弃，不奖励自己
    public static String REWARD_DDYQ = "ddjl";
    //已发放奖励
    public static Integer REWARD_ISSUED = 1;
    //未发放奖励
    public static Integer REWARD_UNISSUED = 0;
    //总账到个人转账
    public static Integer TRANSFER_TO_ONE = 1;
    //个人到总账转账
    public static Integer TRANSFER_TO_ALL = 2;
    /**
     * 总账积分的积分英文名（现金贷邀请订单奖励）
     */
    public static String INTEGRAL_TYPE_YQ = "xjd_yqdd";
    /**
     * 总账积分的积分英文名（订单奖励）
     */
    public static String INTEGRAL_TYPE_DD = "djb_ddjl";
}
