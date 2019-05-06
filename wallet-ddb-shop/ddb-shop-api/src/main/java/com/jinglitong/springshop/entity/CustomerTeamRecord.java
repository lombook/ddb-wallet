package com.jinglitong.springshop.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "customer_team_record")
public class CustomerTeamRecord {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * zid
     */
    private String zid;

    /**
     * 用户
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 账号
     */
    private String account;

    /**
     * 订单标价人民币
     */
    @Column(name = "price_cny")
    private BigDecimal priceCny;

    @Column(name = "price_aud")
    private BigDecimal priceAud;

    /**
     * 订单标价代金币
     */
    @Column(name = "price_djb")
    private BigDecimal priceDjb;

    /**
     * 订单号
     */
    @Column(name = "order_sn")
    private String orderSn;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单创建时间
     */
    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取zid
     *
     * @return zid - zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * 设置zid
     *
     * @param zid zid
     */
    public void setZid(String zid) {
        this.zid = zid == null ? null : zid.trim();
    }

    /**
     * 获取用户
     *
     * @return cust_id - 用户
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户
     *
     * @param custId 用户
     */
    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    /**
     * 获取账号
     *
     * @return account - 账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置账号
     *
     * @param account 账号
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * 获取订单标价人民币
     *
     * @return price_cny - 订单标价人民币
     */
    public BigDecimal getPriceCny() {
        return priceCny;
    }

    /**
     * 设置订单标价人民币
     *
     * @param priceCny 订单标价人民币
     */
    public void setPriceCny(BigDecimal priceCny) {
        this.priceCny = priceCny;
    }

    /**
     * @return price_aud
     */
    public BigDecimal getPriceAud() {
        return priceAud;
    }

    /**
     * @param priceAud
     */
    public void setPriceAud(BigDecimal priceAud) {
        this.priceAud = priceAud;
    }

    /**
     * 获取订单标价代金币
     *
     * @return price_djb - 订单标价代金币
     */
    public BigDecimal getPriceDjb() {
        return priceDjb;
    }

    /**
     * 设置订单标价代金币
     *
     * @param priceDjb 订单标价代金币
     */
    public void setPriceDjb(BigDecimal priceDjb) {
        this.priceDjb = priceDjb;
    }

    /**
     * 获取订单号
     *
     * @return order_sn - 订单号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单号
     *
     * @param orderSn 订单号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * 获取订单id
     *
     * @return order_id - 订单id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id
     *
     * @param orderId 订单id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * 获取订单创建时间
     *
     * @return order_date - 订单创建时间
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * 设置订单创建时间
     *
     * @param orderDate 订单创建时间
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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
}