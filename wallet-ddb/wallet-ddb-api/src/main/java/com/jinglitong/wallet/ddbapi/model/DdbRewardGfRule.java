package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Table(name = "ddb_reward_gf_rule")
public class DdbRewardGfRule {
    private Integer id;

    private String zid;

    /**
     * 对应商品规则表的分红规则frule_group
     */
    @Column(name = "frule_group")
    private String fruleGroup;

    /**
     * 规则类型：1:一级拓展分红 2:二级拓展分红 3:同级销售分红 4:同级加权分红
     */
    @Column(name = "rule_type")
    private Integer ruleType;

    @Column(name = "rule_name")
    private String ruleName;

    /**
     * 若值为100表示分红基数为订单金额的100%
     */
    @Column(name = "reward_rate")
    private Integer rewardRate;

    /**
     * 分红参数:格式为"real_name1:rate1,real_name2:rate2"，real_name对应的总账表的积分场景名real_name。
     */
    @Column(name = "reward_args")
    private String rewardArgs;

    /**
     * 生效时间
     */
    @Column(name = "effective_time")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @Column(name = "expiry_time")
    private Date expiryTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
    
	@Transient
    private List<DdbRewardGfRuleDetail> details;

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
     * 获取对应商品规则表的分红规则frule_group
     *
     * @return frule_group - 对应商品规则表的分红规则frule_group
     */
    public String getFruleGroup() {
        return fruleGroup;
    }

    /**
     * 设置对应商品规则表的分红规则frule_group
     *
     * @param fruleGroup 对应商品规则表的分红规则frule_group
     */
    public void setFruleGroup(String fruleGroup) {
        this.fruleGroup = fruleGroup;
    }

    /**
     * 获取规则类型：1:一级拓展分红 2:二级拓展分红 3:同级销售分红 4:同级加权分红
     *
     * @return rule_type - 规则类型：1:一级拓展分红 2:二级拓展分红 3:同级销售分红 4:同级加权分红
     */
    public Integer getRuleType() {
        return ruleType;
    }

    /**
     * 设置规则类型：1:一级拓展分红 2:二级拓展分红 3:同级销售分红 4:同级加权分红
     *
     * @param ruleType 规则类型：1:一级拓展分红 2:二级拓展分红 3:同级销售分红 4:同级加权分红
     */
    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * @return rule_name
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * @param ruleName
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * 获取若值为100表示分红基数为订单金额的100%
     *
     * @return reward_rate - 若值为100表示分红基数为订单金额的100%
     */
    public Integer getRewardRate() {
        return rewardRate;
    }

    /**
     * 设置若值为100表示分红基数为订单金额的100%
     *
     * @param rewardRate 若值为100表示分红基数为订单金额的100%
     */
    public void setRewardRate(Integer rewardRate) {
        this.rewardRate = rewardRate;
    }

    /**
     * 获取分红参数:格式为"real_name1:rate1,real_name2:rate2"，real_name对应的总账表的积分场景名real_name。
     *
     * @return reward_args - 分红参数:格式为"real_name1:rate1,real_name2:rate2"，real_name对应的总账表的积分场景名real_name。
     */
    public String getRewardArgs() {
        return rewardArgs;
    }

    /**
     * 设置分红参数:格式为"real_name1:rate1,real_name2:rate2"，real_name对应的总账表的积分场景名real_name。
     *
     * @param rewardArgs 分红参数:格式为"real_name1:rate1,real_name2:rate2"，real_name对应的总账表的积分场景名real_name。
     */
    public void setRewardArgs(String rewardArgs) {
        this.rewardArgs = rewardArgs;
    }

    /**
     * 获取生效时间
     *
     * @return effective_time - 生效时间
     */
    public Date getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * 设置生效时间
     *
     * @param effectiveTime 生效时间
     */
    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * 获取失效时间
     *
     * @return expiry_time - 失效时间
     */
    public Date getExpiryTime() {
        return expiryTime;
    }

    /**
     * 设置失效时间
     *
     * @param expiryTime 失效时间
     */
    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public List<DdbRewardGfRuleDetail> getDetails() {
		return details;
	}

	public void setDetails(List<DdbRewardGfRuleDetail> details) {
		this.details = details;
	}
}