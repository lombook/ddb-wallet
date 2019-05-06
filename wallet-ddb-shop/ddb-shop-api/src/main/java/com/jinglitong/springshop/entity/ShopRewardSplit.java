package com.jinglitong.springshop.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;
import java.util.function.IntToDoubleFunction;

@ApiModel(value = "订单邀请奖励拆单表")
public class ShopRewardSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "zid")
    private String zid;
    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;
    /**
     * 流水id
     */
    @Column(name = "flow_id")
    private String flowId;
    /**
     * 奖励规则类型
     * zjyq直接邀请,
     * jjyq间接邀请,
     * yjjd一级节点,
     * ddjl订单激励
     */
    @Column(name = "reward_type")
    private String rewardType;
    /**
     * 订单号
     */
    @Column(name = "order_sn")
    private String orderSn;
    /**
     * 奖励积分id
     */
    @Column(name = "integral_id")
    private String integralId;
    /**
     * 奖励积分名称
     */
    @Column(name = "real_cname")
    private String realCname;
    /**
     * 奖励积分中文名
     */
    @Column(name = "real_name")
    private String realName;
    /**
     * 奖励比例
     */
    @Column(name = "reward_percent")
    private String rewardPercent;
    /**
     * 奖励金额
     */
    @Column(name = "amount")
    private long amount;
    /**
     * 奖励状态
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getIntegralId() {
        return integralId;
    }

    public void setIntegralId(String integralId) {
        this.integralId = integralId;
    }

    public String getRealCname() {
        return realCname;
    }

    public void setRealCname(String realCname) {
        this.realCname = realCname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRewardPercent() {
        return rewardPercent;
    }

    public void setRewardPercent(String rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
