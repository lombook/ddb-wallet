package com.jinglitong.springshop.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Paymenttransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    private BigDecimal amount;

    private Date expire;

    @Column(name = "isSuccess")
    private Boolean issuccess;

    @Column(name = "paymentPluginId")
    private String paymentpluginid;

    @Column(name = "paymentPluginName")
    private String paymentpluginname;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "cust_id")
    private String custId;

    /**
     * 0未支付,1支付成功,2待确认
     */
    @Column(name = "payStatus")
    private Integer paystatus;

    /**
     * 币种
     */
    @Column(name = "currency_id")
    private String currencyId;

    @Column(name = "real_amount")
    private BigDecimal realAmount;

    @Column(name = "real_currency_id")
    private String realCurrencyId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

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
     * @return amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return expire
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * @param expire
     */
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    /**
     * @return isSuccess
     */
    public Boolean getIssuccess() {
        return issuccess;
    }

    /**
     * @param issuccess
     */
    public void setIssuccess(Boolean issuccess) {
        this.issuccess = issuccess;
    }

    /**
     * @return paymentPluginId
     */
    public String getPaymentpluginid() {
        return paymentpluginid;
    }

    /**
     * @param paymentpluginid
     */
    public void setPaymentpluginid(String paymentpluginid) {
        this.paymentpluginid = paymentpluginid == null ? null : paymentpluginid.trim();
    }

    /**
     * @return paymentPluginName
     */
    public String getPaymentpluginname() {
        return paymentpluginname;
    }

    /**
     * @param paymentpluginname
     */
    public void setPaymentpluginname(String paymentpluginname) {
        this.paymentpluginname = paymentpluginname == null ? null : paymentpluginname.trim();
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
     * @return store_id
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * @param storeId
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
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
     * 获取0未支付,1支付成功,2待确认
     *
     * @return payStatus - 0未支付,1支付成功,2待确认
     */
    public Integer getPaystatus() {
        return paystatus;
    }

    /**
     * 设置0未支付,1支付成功,2待确认
     *
     * @param paystatus 0未支付,1支付成功,2待确认
     */
    public void setPaystatus(Integer paystatus) {
        this.paystatus = paystatus;
    }

    /**
     * 获取币种
     *
     * @return currency_id - 币种
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * 设置币种
     *
     * @param currencyId 币种
     */
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId == null ? null : currencyId.trim();
    }

    /**
     * @return real_amount
     */
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    /**
     * @param realAmount
     */
    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    /**
     * @return real_currency_id
     */
    public String getRealCurrencyId() {
        return realCurrencyId;
    }

    /**
     * @param realCurrencyId
     */
    public void setRealCurrencyId(String realCurrencyId) {
        this.realCurrencyId = realCurrencyId == null ? null : realCurrencyId.trim();
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
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}