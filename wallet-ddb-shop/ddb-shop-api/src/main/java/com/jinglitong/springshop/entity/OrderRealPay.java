package com.jinglitong.springshop.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "order_real_pay")
public class OrderRealPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 主订单id
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 币种id
     */
    @Column(name = "currency_id")
    private String currencyId;

    /**
     * 价格
     */
    private BigDecimal amount;

    /**
     * 币种code
     */
    @Column(name = "currency_code")
    private String currencyCode;

    /**
     * 0:法币 1:非法币
     */
    @Column(name = "currency_type")
    private Integer currencyType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
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
     * 获取主订单id
     *
     * @return order_id - 主订单id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置主订单id
     *
     * @param orderId 主订单id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * 获取币种id
     *
     * @return currency_id - 币种id
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * 设置币种id
     *
     * @param currencyId 币种id
     */
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId == null ? null : currencyId.trim();
    }

    /**
     * 获取价格
     *
     * @return amount - 价格
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置价格
     *
     * @param amount 价格
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取币种code
     *
     * @return currency_code - 币种code
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * 设置币种code
     *
     * @param currencyCode 币种code
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode == null ? null : currencyCode.trim();
    }

    /**
     * 获取0:法币 1:非法币
     *
     * @return currency_type - 0:法币 1:非法币
     */
    public Integer getCurrencyType() {
        return currencyType;
    }

    /**
     * 设置0:法币 1:非法币
     *
     * @param currencyType 0:法币 1:非法币
     */
    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}