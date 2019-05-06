package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "ddb_chain_exchange_order")
public class DdbChainExchangeOrder {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 规则ID
     */
    @Column(name = "exchange_rule_id")
    private String exchangeRuleId;

    /**
     * 兑换系数
     */
    private BigDecimal percent;

    /**
     * 兑换积分数(以分为单位)
     */
    @Column(name = "integ_amount")
    private Integer integAmount;

    /**
     * 积分中文名
     */
    @Column(name = "integ_cname")
    private String integCname;

    /**
     * 积分名
     */
    @Column(name = "integ_name")
    private String integName;

    /**
     * 兑换币数(以元为单位)
     */
    @Column(name = "coin_amount")
    private BigDecimal coinAmount;

    /**
     * 链ID
     */
    @Column(name = "chain_id")
    private String chainId;

    /**
     * 币ID
     */
    @Column(name = "coin_id")
    private String coinId;

    /**
     * 币名
     */
    private String currency;

    /**
     * 用户ID
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 用户钱包ID
     */
    @Column(name = "wallet_id")
    private String walletId;

    /**
     * 状态(0:未完成 1:完成)
     */
    private Integer state;

    /**
     * 生成时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

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
        this.zid = zid;
    }

    /**
     * 获取规则ID
     *
     * @return exchange_rule_id - 规则ID
     */
    public String getExchangeRuleId() {
        return exchangeRuleId;
    }

    /**
     * 设置规则ID
     *
     * @param exchangeRuleId 规则ID
     */
    public void setExchangeRuleId(String exchangeRuleId) {
        this.exchangeRuleId = exchangeRuleId;
    }

    /**
     * 获取兑换系数
     *
     * @return percent - 兑换系数
     */
    public BigDecimal getPercent() {
        return percent;
    }

    /**
     * 设置兑换系数
     *
     * @param percent 兑换系数
     */
    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    /**
     * 获取兑换积分数(以分为单位)
     *
     * @return integ_amount - 兑换积分数(以分为单位)
     */
    public Integer getIntegAmount() {
        return integAmount;
    }

    /**
     * 设置兑换积分数(以分为单位)
     *
     * @param integAmount 兑换积分数(以分为单位)
     */
    public void setIntegAmount(Integer integAmount) {
        this.integAmount = integAmount;
    }

    /**
     * 获取积分中文名
     *
     * @return integ_cname - 积分中文名
     */
    public String getIntegCname() {
        return integCname;
    }

    /**
     * 设置积分中文名
     *
     * @param integCname 积分中文名
     */
    public void setIntegCname(String integCname) {
        this.integCname = integCname;
    }

    /**
     * 获取积分名
     *
     * @return integ_name - 积分名
     */
    public String getIntegName() {
        return integName;
    }

    /**
     * 设置积分名
     *
     * @param integName 积分名
     */
    public void setIntegName(String integName) {
        this.integName = integName;
    }

    /**
     * 获取兑换币数(以元为单位)
     *
     * @return coin_amount - 兑换币数(以元为单位)
     */
    public BigDecimal getCoinAmount() {
        return coinAmount;
    }

    /**
     * 设置兑换币数(以元为单位)
     *
     * @param coinAmount 兑换币数(以元为单位)
     */
    public void setCoinAmount(BigDecimal coinAmount) {
        this.coinAmount = coinAmount;
    }

    /**
     * 获取链ID
     *
     * @return chain_id - 链ID
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置链ID
     *
     * @param chainId 链ID
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取币ID
     *
     * @return coin_id - 币ID
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置币ID
     *
     * @param coinId 币ID
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取币名
     *
     * @return currency - 币名
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 设置币名
     *
     * @param currency 币名
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 获取用户ID
     *
     * @return cust_id - 用户ID
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户ID
     *
     * @param custId 用户ID
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * 获取用户钱包ID
     *
     * @return wallet_id - 用户钱包ID
     */
    public String getWalletId() {
        return walletId;
    }

    /**
     * 设置用户钱包ID
     *
     * @param walletId 用户钱包ID
     */
    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    /**
     * 获取状态(0:未完成 1:完成)
     *
     * @return state - 状态(0:未完成 1:完成)
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态(0:未完成 1:完成)
     *
     * @param state 状态(0:未完成 1:完成)
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取生成时间
     *
     * @return create_time - 生成时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置生成时间
     *
     * @param createTime 生成时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}