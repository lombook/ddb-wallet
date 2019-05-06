package com.jinglitong.wallet.ddbapi.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_integral_withdraw_rule")
public class DdbIntegralWithdrawRule {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 兑换系数
     */
    private BigDecimal percent;

    /**
     * 最小提现金额
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
     * 手续费
     */
    @Column(name = "integ_charge")
    private Integer integCharge;

    /**
     * 手续费积分中文名
     */
    @Column(name = "charge_integ_cname")
    private String chargeIntegCname;

    /**
     * 手续费积分名
     */
    @Column(name = "charge_integ_name")
    private String chargeIntegName;

    /**
     * 提现积分回收总账中文名
     */
    @Column(name = "resave_integ_real_cname")
    private String resaveIntegRealCname;

    /**
     * 提现积分回收总账名
     */
    @Column(name = "resave_integ_real_name")
    private String resaveIntegRealName;

    /**
     * 手续费回收总账中文名
     */
    @Column(name = "resave_charge_real_cname")
    private String resaveChargeRealCname;

    /**
     * 手续费回收总账名
     */
    @Column(name = "resave_charge_real_name")
    private String resaveChargeRealName;

    /**
     * 有效时间开始时间
     */
    @Column(name = "effective_time")
    private String effectiveTime;

    /**
     * 失效时间
     */
    @Column(name = "expiry_time")
    private String expiryTime;

    /**
     * 规则名
     */
    @Column(name = "rule_name")
    private String ruleName;

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
     * 获取业务id
     *
     * @return zid - 业务id
     */
    public String getZid() {
        return zid;
    }

    /**
     * 设置业务id
     *
     * @param zid 业务id
     */
    public void setZid(String zid) {
        this.zid = zid;
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
     * 获取最小提现金额
     *
     * @return min_amount - 最小提现金额
     */
    public Integer getMinAmount() {
        return minAmount;
    }

    /**
     * 设置最小提现金额
     *
     * @param minAmount 最小提现金额
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
     * 获取手续费
     *
     * @return integ_charge - 手续费
     */
    public Integer getIntegCharge() {
        return integCharge;
    }

    /**
     * 设置手续费
     *
     * @param integCharge 手续费
     */
    public void setIntegCharge(Integer integCharge) {
        this.integCharge = integCharge;
    }

    /**
     * 获取手续费积分中文名
     *
     * @return charge_integ_cname - 手续费积分中文名
     */
    public String getChargeIntegCname() {
        return chargeIntegCname;
    }

    /**
     * 设置手续费积分中文名
     *
     * @param chargeIntegCname 手续费积分中文名
     */
    public void setChargeIntegCname(String chargeIntegCname) {
        this.chargeIntegCname = chargeIntegCname;
    }

    /**
     * 获取手续费积分名
     *
     * @return charge_integ_name - 手续费积分名
     */
    public String getChargeIntegName() {
        return chargeIntegName;
    }

    /**
     * 设置手续费积分名
     *
     * @param chargeIntegName 手续费积分名
     */
    public void setChargeIntegName(String chargeIntegName) {
        this.chargeIntegName = chargeIntegName;
    }

    /**
     * 获取提现积分回收总账中文名
     *
     * @return resave_integ_real_cname - 提现积分回收总账中文名
     */
    public String getResaveIntegRealCname() {
        return resaveIntegRealCname;
    }

    /**
     * 设置提现积分回收总账中文名
     *
     * @param resaveIntegRealCname 提现积分回收总账中文名
     */
    public void setResaveIntegRealCname(String resaveIntegRealCname) {
        this.resaveIntegRealCname = resaveIntegRealCname;
    }

    /**
     * 获取提现积分回收总账名
     *
     * @return resave_integ_real_name - 提现积分回收总账名
     */
    public String getResaveIntegRealName() {
        return resaveIntegRealName;
    }

    /**
     * 设置提现积分回收总账名
     *
     * @param resaveIntegRealName 提现积分回收总账名
     */
    public void setResaveIntegRealName(String resaveIntegRealName) {
        this.resaveIntegRealName = resaveIntegRealName;
    }

    /**
     * 获取手续费回收总账中文名
     *
     * @return resave_charge_real_cname - 手续费回收总账中文名
     */
    public String getResaveChargeRealCname() {
        return resaveChargeRealCname;
    }

    /**
     * 设置手续费回收总账中文名
     *
     * @param resaveChargeRealCname 手续费回收总账中文名
     */
    public void setResaveChargeRealCname(String resaveChargeRealCname) {
        this.resaveChargeRealCname = resaveChargeRealCname;
    }

    /**
     * 获取手续费回收总账名
     *
     * @return resave_charge_real_name - 手续费回收总账名
     */
    public String getResaveChargeRealName() {
        return resaveChargeRealName;
    }

    /**
     * 设置手续费回收总账名
     *
     * @param resaveChargeRealName 手续费回收总账名
     */
    public void setResaveChargeRealName(String resaveChargeRealName) {
        this.resaveChargeRealName = resaveChargeRealName;
    }

    /**
     * 获取有效时间开始时间
     *
     * @return effective_time - 有效时间开始时间
     */
    public String getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * 设置有效时间开始时间
     *
     * @param effectiveTime 有效时间开始时间
     */
    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * 获取失效时间
     *
     * @return expiry_time - 失效时间
     */
    public String getExpiryTime() {
        return expiryTime;
    }

    /**
     * 设置失效时间
     *
     * @param expiryTime 失效时间
     */
    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
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
}