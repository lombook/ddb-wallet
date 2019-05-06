package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ddb_reward_rule_detail")
public class DdbRewardRuleDetail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 所属规则ZID
     */
    @Column(name = "rule_zid")
    private String ruleZid;

    /**
     * 规则类型：1:一级拓展分红 2:二级拓展分红 3:同级销售分红 4:同级加权分红
     */
    @Column(name = "rule_type")
    private Integer ruleType;

    /**
     * 股东等级名称:规则类型=1or2时空白，规则类型=3or4时不能为空，值为股东升级规则表的level_define
     */
    @Column(name = "shoreholder_level_define")
    private String shoreholderLevelDefine;

    /**
     * 分红比例，若值为4表示分红比例为分红基数的4%
     */
    @Column(name = "reward_percent")
    private Integer rewardPercent;

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
     * 获取所属规则ZID
     *
     * @return rule_zid - 所属规则ZID
     */
    public String getRuleZid() {
        return ruleZid;
    }

    /**
     * 设置所属规则ZID
     *
     * @param ruleZid 所属规则ZID
     */
    public void setRuleZid(String ruleZid) {
        this.ruleZid = ruleZid;
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
     * 获取股东等级名称:规则类型=1or2时空白，规则类型=3or4时不能为空，值为股东升级规则表的level_define
     *
     * @return shoreholder_level_define - 股东等级名称:规则类型=1or2时空白，规则类型=3or4时不能为空，值为股东升级规则表的level_define
     */
    public String getShoreholderLevelDefine() {
        return shoreholderLevelDefine;
    }

    /**
     * 设置股东等级名称:规则类型=1or2时空白，规则类型=3or4时不能为空，值为股东升级规则表的level_define
     *
     * @param shoreholderLevelDefine 股东等级名称:规则类型=1or2时空白，规则类型=3or4时不能为空，值为股东升级规则表的level_define
     */
    public void setShoreholderLevelDefine(String shoreholderLevelDefine) {
        this.shoreholderLevelDefine = shoreholderLevelDefine;
    }

    /**
     * 获取分红比例，若值为4表示分红比例为分红基数的4%
     *
     * @return reward_percent - 分红比例，若值为4表示分红比例为分红基数的4%
     */
    public Integer getRewardPercent() {
        return rewardPercent;
    }

    /**
     * 设置分红比例，若值为4表示分红比例为分红基数的4%
     *
     * @param rewardPercent 分红比例，若值为4表示分红比例为分红基数的4%
     */
    public void setRewardPercent(Integer rewardPercent) {
        this.rewardPercent = rewardPercent;
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
}