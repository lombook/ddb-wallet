package com.jinglitong.springshop.entity;

import java.util.Date;

import javax.persistence.Table;

@Table(name = "ddb_integral_account_seq")
public class DdbIntegralAccountSeq {

    public DdbIntegralAccountSeq() {
    }

    public DdbIntegralAccountSeq(String zid, String flowId, String inteId, String realCname, String realName, String inteCname, String inteName, Long beforeAmount, Long amount, Long afterAmount, Integer type, Date createTime, Date updateTime) {
        this.zid = zid;
        this.flowId = flowId;
        this.inteId = inteId;
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
    }

    private Integer id;
    /**
	 * 业务id
	 */
    private String zid;
    /**
	 * 流水id
	 */
    private String flowId;
    /**
	 * 总账id
	 */
    private String inteId;
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
    private Date createTime;
    /**
	 * 修改时间
	 */
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

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getInteId() {
        return inteId;
    }

    public void setInteId(String inteId) {
        this.inteId = inteId;
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