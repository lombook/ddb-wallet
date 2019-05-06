package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;

import javax.persistence.Table;

import lombok.Data;
@Table(name = "ddb_integral_wallet_seq")
public class DdbIntegralWalletSeq {
    public DdbIntegralWalletSeq() {
    }

    public DdbIntegralWalletSeq(String zid, String custId, String realCname, String realName, String inteCname, String inteName, Long beforeAmount, Long amount, Long afterAmount, Integer type, String createTime, String updateTime, String flowId) {
        this.zid = zid;
        this.custId = custId;
        this.realCname = realCname;
        this.realName = realName;
        this.inteCname = inteCname;
        this.inteName = inteName;
        this.beforeAmount = beforeAmount;
        this.amount = amount;
        this.afterAmount = afterAmount;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.flowId = flowId;
    }

    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 用户id
     */
    private String custId;
    /**
     * 积分场景中文名
     */
    private String realCname;
    /**
     * 积分场景名
     */
    private String realName;
    /**
     * 积分中文名
     */
    private String inteCname;
    /**
     * 积分名
     */
    private String inteName;
    /**
     * 操作前数值
     */
    private Long beforeAmount;
    /**
     * 操作金额
     */
    private Long amount;
    /**
     * 操作后数值
     */
    private Long afterAmount;
    /**
     * 操作类型：1、增加 2、减少
     */
    private Integer type;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 流水ID
     */
    private String flowId;

    public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

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

    public String getInteCname() {
        return inteCname;
    }

    public void setInteCname(String inteCname) {
        this.inteCname = inteCname;
    }

    public String getInteName() {
        return inteName;
    }

    public void setInteName(String inteName) {
        this.inteName = inteName;
    }

    public Long getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(Long beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(Long afterAmount) {
        this.afterAmount = afterAmount;
    }

 

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}