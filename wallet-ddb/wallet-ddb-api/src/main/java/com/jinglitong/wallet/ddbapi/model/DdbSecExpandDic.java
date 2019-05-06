package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_sec_expand_dic")
public class DdbSecExpandDic {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 商品规则id
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 积分数量
     */
    @Column(name = "inte_amount")
    private Integer inteAmount;

    /**
     * 积分id
     */
    @Column(name = "inte_id")
    private String inteId;

    @Column(name = "effective_time")
    private Date effectiveTime;

    @Column(name = "expiry_time")
    private Date expiryTime;

    @Column(name = "create_time")
    private Date createTime;

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
     * 获取商品规则id
     *
     * @return rule_id - 商品规则id
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * 设置商品规则id
     *
     * @param ruleId 商品规则id
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * 获取积分数量
     *
     * @return inte_amount - 积分数量
     */
    public Integer getInteAmount() {
        return inteAmount;
    }

    /**
     * 设置积分数量
     *
     * @param inteAmount 积分数量
     */
    public void setInteAmount(Integer inteAmount) {
        this.inteAmount = inteAmount;
    }

    /**
     * 获取积分id
     *
     * @return inte_id - 积分id
     */
    public String getInteId() {
        return inteId;
    }

    /**
     * 设置积分id
     *
     * @param inteId 积分id
     */
    public void setInteId(String inteId) {
        this.inteId = inteId;
    }

    /**
     * @return effective_time
     */
    public Date getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * @param effectiveTime
     */
    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * @return expiry_time
     */
    public Date getExpiryTime() {
        return expiryTime;
    }

    /**
     * @param expiryTime
     */
    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
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