package com.jinglitong.springshop.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
public class Orderitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 是否需要物流
     */
    @Column(name = "isDelivery")
    private Boolean isdelivery;

    /**
     * 商品名
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 已发货数量
     */
    @Column(name = "shippedQuantity")
    private Integer shippedquantity;

    /**
     * 商品sn
     */
    private String sn;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 商品类型
     */
    private Integer type;

    /**
     * 商品重量
     */
    private Integer weight;

    /**
     * 订单id
     */
    @Column(name = "orders_id")
    private String ordersId;

    /**
     * skuId
     */
    @Column(name = "sku_id")
    private String skuId;

    /**
     * 币种id
     */
    @Column(name = "currency_id")
    private String currencyId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 规格
     */
    private String specifications;
    
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