package com.jinglitong.springshop.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "deed_order_award_split")
public class DeedOrderAwardSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    @Column(name = "cust_id")
    private String custId;

    @Column(name = "integral_id")
    private String integralId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "sku_num")
    private Integer skuNum;

    private Long amount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

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
     * @return cust_id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * @param custId
     */
    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    /**
     * @return integral_id
     */
    public String getIntegralId() {
        return integralId;
    }

    /**
     * @param integralId
     */
    public void setIntegralId(String integralId) {
        this.integralId = integralId == null ? null : integralId.trim();
    }

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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
     * @return sku_num
     */
    public Integer getSkuNum() {
        return skuNum;
    }

    /**
     * @param skuNum
     */
    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    /**
     * @return amount
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}