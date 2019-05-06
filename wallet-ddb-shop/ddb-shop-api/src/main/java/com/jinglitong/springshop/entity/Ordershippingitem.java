package com.jinglitong.springshop.entity;

import java.util.Date;
import javax.persistence.*;

public class Ordershippingitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    @Column(name = "isDelivery")
    private Byte isdelivery;

    private String name;

    private Integer quantity;

    private String sn;

    @Column(name = "orderShipping_id")
    private String ordershippingId;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "update_time")
    private Date updateTime;

    private String specifications;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * @param zid
     */
    public void setZid(String zid) {
        this.zid = zid == null ? null : zid.trim();
    }

    /**
     * @return isDelivery
     */
    public Byte getIsdelivery() {
        return isdelivery;
    }

    /**
     * @param isdelivery
     */
    public void setIsdelivery(Byte isdelivery) {
        this.isdelivery = isdelivery;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * @param sn
     */
    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    /**
     * @return orderShipping_id
     */
    public String getOrdershippingId() {
        return ordershippingId;
    }

    /**
     * @param ordershippingId
     */
    public void setOrdershippingId(String ordershippingId) {
        this.ordershippingId = ordershippingId == null ? null : ordershippingId.trim();
    }

    /**
     * @return sku_id
     */
    public String getSkuId() {
        return skuId;
    }

    /**
     * @param skuId
     */
    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return specifications
     */
    public String getSpecifications() {
        return specifications;
    }

    /**
     * @param specifications
     */
    public void setSpecifications(String specifications) {
        this.specifications = specifications == null ? null : specifications.trim();
    }
}