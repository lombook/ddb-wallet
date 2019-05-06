package com.jinglitong.springshop;

/**
 * @ClassName IConstants
 * @Description 常量
 * @Author zili.zong
 * @Date 2018/11/19 10:10
 * @Version 1.0
 **/
public interface IConstants {
    public static final Integer SUCCESS = 0;//请求成功
    public static final Integer FAILED = -1; //请求失败

    public static final String SUCCESS_MSG = "success";
    public static final String FAILED_MSG = "failed";
    public static final Integer HOMEORDER_EXPIR_TIME = 2;//订单自动过期时间 年
    public static final Integer ABROADORDER_EXPIR_TIME = 12;//订单自动过期时间 小时
    public static final Integer ORDER_AUTOCOMPLETE_TIME = 25;//订单自动收货时间 天
    public static final Integer SHARE_NOTICE_NUM = 5;//公告分享奖励次数限制
    //********常量********************************************************************************************
    public static final Integer CART_LITTLE_NUM = 1;//购物车单个商品加减的最小数量
    public static final Integer CART_BIG_NUM = 99;//购物车单个商品最大数量
    public static final Integer CART_CartBIG_NUM = 99;//购物车商品最大数量
    //*********************用户登录redis存储key固定头******************************************************************
    public static final String LOGIN_KEY_HEADER_FIXED="shop_business_login_header@";

    public static final String BUSINESS_LOGIN_KEY_HEADER_FIXED="BUSINESS@";

    public static final String LOGIN_ENCRYPT_SALT="!En@1crYPt23_";
    //*********************IOS请求头固定APPID******************************************************************
    public static final String IOS_APP_ID="com.jlt.ASMall";
}
