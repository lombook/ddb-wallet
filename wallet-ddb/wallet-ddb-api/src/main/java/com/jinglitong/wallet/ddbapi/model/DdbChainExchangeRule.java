package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "ddb_chain_exchange_rule")
public class DdbChainExchangeRule {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 规则名
     */
    @Column(name = "rule_name")
    private String ruleName;

    /**
     * 兑换系数
     */
    private BigDecimal percent;

    /**
     * 最小兑换数
     */
    @Column(name = "min_amount")
    private Integer minAmount;

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
     * 兑换积分回收总账中文名
     */
    @Column(name = "resave_integ_real_cname")
    private String resaveIntegRealCname;

    /**
     * 兑换积分回收总账名
     */
    @Column(name = "resave_integ_real_name")
    private String resaveIntegRealName;

    @Column(name = "exchange_chain_id")
    private String exchangeChainId;

    @Column(name = "exchange_coin_id")
    private String exchangeCoinId;

    @Column(name = "effective_time")
    private String effectiveTime;

    @Column(name = "expiry_time")
    private String expiryTime;

    @Column(name = "create_time")
    private String createTime;

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
     * 获取规则名
     *
     * @return rule_name - 规则名
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * 设置规则名
     *
     * @param ruleName 规则名
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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
     * 获取最小兑换数
     *
     * @return min_amount - 最小兑换数
     */
    public Integer getMinAmount() {
        return minAmount;
    }

    /**
     * 设置最小兑换数
     *
     * @param minAmount 最小兑换数
     */
    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
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
     * 获取兑换积分回收总账中文名
     *
     * @return resave_integ_real_cname - 兑换积分回收总账中文名
     */
    public String getResaveIntegRealCname() {
        return resaveIntegRealCname;
    }

    /**
     * 设置兑换积分回收总账中文名
     *
     * @param resaveIntegRealCname 兑换积分回收总账中文名
     */
    public void setResaveIntegRealCname(String resaveIntegRealCname) {
        this.resaveIntegRealCname = resaveIntegRealCname;
    }

    /**
     * 获取兑换积分回收总账名
     *
     * @return resave_integ_real_name - 兑换积分回收总账名
     */
    public String getResaveIntegRealName() {
        return resaveIntegRealName;
    }

    /**
     * 设置兑换积分回收总账名
     *
     * @param resaveIntegRealName 兑换积分回收总账名
     */
    public void setResaveIntegRealName(String resaveIntegRealName) {
        this.resaveIntegRealName = resaveIntegRealName;
    }

    /**
     * @return exchange_chain_id
     */
    public String getExchangeChainId() {
        return exchangeChainId;
    }

    /**
     * @param exchangeChainId
     */
    public void setExchangeChainId(String exchangeChainId) {
        this.exchangeChainId = exchangeChainId;
    }

    /**
     * @return exchange_coin_id
     */
    public String getExchangeCoinId() {
        return exchangeCoinId;
    }

    /**
     * @param exchangeCoinId
     */
    public void setExchangeCoinId(String exchangeCoinId) {
        this.exchangeCoinId = exchangeCoinId;
    }

    /**
     * @return effective_time
     */
    public String getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * @param effectiveTime
     */
    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * @return expiry_time
     */
    public String getExpiryTime() {
        return expiryTime;
    }

    /**
     * @param expiryTime
     */
    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
     * @return create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}