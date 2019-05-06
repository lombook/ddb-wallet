package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 付款金额
     */
    private BigDecimal amount;

    /**
     * 收货详细地址
     */
    private String address;

    /**
     * 地区地址
     */
    @Column(name = "areaName")
    private String areaname;

    /**
     * 订单完成时间
     */
    @Column(name = "completeDate")
    private Date completedate;

    /**
     * 收货人名称
     */
    private String consignee;

    /**
     * 过期时间
     */
    private Date expire;

    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 发票
     */
    @Column(name = "invoiceContent")
    private String invoicecontent;

    /**
     * 发票抬头
     */
    @Column(name = "invoiceTitle")
    private String invoicetitle;

    /**
     * 是否已分配库存 1：是 0 否
     */
    @Column(name = "isAllocatedStock")
    private Integer isallocatedstock;

    /**
     * 备注
     */
    private String memo;

    /**
     * 调整金额
     */
    @Column(name = "offsetAmount")
    private BigDecimal offsetamount;

    /**
     * 电话
     */
    private String phone;

    /**
     * 订单价格
     */
    private BigDecimal price;

    /**
     * 配送方式名
     */
    @Column(name = "shippingMethodName")
    private String shippingmethodname;

    /**
     * 订单sn
     */
    private String sn;

    /**
     * 订单状态 0待支付,1待发货,2已发货,3已完成
     */
    private Integer status;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 邮编
     */
    @Column(name = "zipCode")
    private String zipcode;

    /**
     * 地区
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 城市id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 配送方式id
     */
    @Column(name = "shippingMethod_id")
    private String shippingmethodId;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 币种id
     */
    @Column(name = "currency_id")
    private String currencyId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "order_hash")
    private String orderHash;
    
    @Column(name = "order_parent")
    private String orderParent;
    
    @Column(name = "integral_c_id")
    private String integralCId;
    
    @Column(name = "price_code")
    private String priceCode;
    
    @Column(name = "integral_code")
    private String integralCode;
    
    @Column(name = "integral_price")
    private BigDecimal integralPrice;
    
    /**
     * 销售激励额度
     */
    @Column(name = "s_value")
    private BigDecimal sValue;

    /**
     * 袋金币积分
     */
    @Column(name = "djb_value")
    private BigDecimal djbValue;

}