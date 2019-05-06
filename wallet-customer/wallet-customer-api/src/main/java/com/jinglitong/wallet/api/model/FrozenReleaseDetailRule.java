package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "frozen_release_detail_rule")
public class FrozenReleaseDetailRule implements IAppIdModel{
    @Id
    private Integer id;
    
    @Column(name = "detail_id")
    private String detailId;

    /**
     * 规则id
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 本次执行百分比
     */
    private Integer proportion;

    @Column(name = "execut_time")
    private String executTime;

    /**
     * 剩余百分比
     */
    @Column(name = "left_proportion")
    private Integer leftProportion;

    /**
     * 执行状态 0:未执行 1:正在执行 2:执行完毕未检查 3:检查完毕
     */
    @Column(name = "detail_status")
    private Integer detailStatus;

    /**
     * 平均天数
     */
    @Column(name = "avg_days")
    private Integer avgDays;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;

    @Transient
    private FrozenReleaseRule rule;
    
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
    /**
     * @return detail_id
     */
    public String getDetailId() {
        return detailId;
    }

    /**
     * @param detailId
     */
    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    /**
     * 获取规则id
     *
     * @return rule_id - 规则id
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * 设置规则id
     *
     * @param ruleId 规则id
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * 获取本次执行百分比
     *
     * @return proportion - 本次执行百分比
     */
    public Integer getProportion() {
        return proportion;
    }

    /**
     * 设置本次执行百分比
     *
     * @param proportion 本次执行百分比
     */
    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    /**
     * @return execut_time
     */
    public String getExecutTime() {
        return executTime;
    }

    /**
     * @param executTime
     */
    public void setExecutTime(String executTime) {
        this.executTime = executTime;
    }

    /**
     * 获取剩余百分比
     *
     * @return left_proportion - 剩余百分比
     */
    public Integer getLeftProportion() {
        return leftProportion;
    }

    /**
     * 设置剩余百分比
     *
     * @param leftProportion 剩余百分比
     */
    public void setLeftProportion(Integer leftProportion) {
        this.leftProportion = leftProportion;
    }

    /**
     * 获取执行状态 0:未执行 1:正在执行 2:执行完毕未检查 3:检查完毕
     *
     * @return detail_status - 执行状态 0:未执行 1:正在执行 2:执行完毕未检查 3:检查完毕
     */
    public Integer getDetailStatus() {
        return detailStatus;
    }

    /**
     * 设置执行状态 0:未执行 1:正在执行 2:执行完毕未检查 3:检查完毕
     *
     * @param detailStatus 执行状态 0:未执行 1:正在执行 2:执行完毕未检查 3:检查完毕
     */
    public void setDetailStatus(Integer detailStatus) {
        this.detailStatus = detailStatus;
    }

    /**
     * 获取平均天数
     *
     * @return avg_days - 平均天数
     */
    public Integer getAvgDays() {
        return avgDays;
    }

    /**
     * 设置平均天数
     *
     * @param avgDays 平均天数
     */
    public void setAvgDays(Integer avgDays) {
        this.avgDays = avgDays;
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