package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
@Data
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主键
     */
    private String zid;

    /**
     * 副标题
     */
    private String caption;

    /**
     * 图片
     */
    private String image;

    /**
     * 是否上架
     */
    @Column(name = "isList")
    private Boolean islist;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 备注
     */
    private String memo;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商户id
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 产品编码
     */
    private String sn;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 重量单位 kg/g
     */
    @Column(name = "weight_unit")
    private String weightUnit;

    /**
     * 品牌
     */
    @Column(name = "brand_id")
    private String brandId;

    /**
     * 产品分类
     */
    @Column(name = "product_category_id")
    private String productCategoryId;

    /**
     * 卖出个数
     */
    @Column(name = "sell_num")
    private Integer sellNum;

    /**
     * 基准价，人民币的标的价格
     */
    @Column(name = "benchmarkPrice")
    private BigDecimal benchmarkprice;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 介绍
     */
    private String introduction;

    /**
     * 商品图片组
     */
    @Column(name = "productImages")
    private String productimages;

    /**
     * 规格key
     */
    @Column(name = "specificationItems")
    private String specificationitems;
}