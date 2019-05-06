package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Table(name = "ddb_seplit_order_reward_gf")
public class DdbSeplitOrderRewardGf {
    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 逻辑id
     */
    private String zid;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 订单表flowId
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * 规则id
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 分红id
     */
    @Column(name = "frule_id")
    private String fruleId;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 总账id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * 积分场景中文名
     */
    @Column(name = "real_cname")
    private String realCname;

    /**
     * 积分场景名称
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 股东层级定义
     */
    @Column(name = "level_define")
    private String levelDefine;

    /**
     * 分红比例
     */
    @Column(name = "reward_percent")
    private String rewardPercent;

    /**
     * 总金额
     */
    private Integer amount;

    /**
     * 状态（0未处理1：已处理）
     */
    private Integer state;

    /**
     * 类型 0:默认值(级差、同星、一级拓展分红)；1:二级分红；2：福利；
     */
    private Integer type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取逻辑id
     *
     * @return zid - 逻辑id
     */
    public String getZid() {
        return zid;
    }

    /**
     * 设置逻辑id
     *
     * @param zid 逻辑id
     */
    public void setZid(String zid) {
        this.zid = zid;
    }

    /**
     * 获取用户id
     *
     * @return cust_id - 用户id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户id
     *
     * @param custId 用户id
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * 获取订单表flowId
     *
     * @return flow_id - 订单表flowId
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * 设置订单表flowId
     *
     * @param flowId 订单表flowId
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId;
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
     * 获取分红id
     *
     * @return frule_id - 分红id
     */
    public String getFruleId() {
        return fruleId;
    }

    /**
     * 设置分红id
     *
     * @param fruleId 分红id
     */
    public void setFruleId(String fruleId) {
        this.fruleId = fruleId;
    }

    /**
     * 获取订单号
     *
     * @return order_id - 订单号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单号
     *
     * @param orderId 订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取总账id
     *
     * @return inte_zid - 总账id
     */
    public String getInteZid() {
        return inteZid;
    }

    /**
     * 设置总账id
     *
     * @param inteZid 总账id
     */
    public void setInteZid(String inteZid) {
        this.inteZid = inteZid;
    }

    /**
     * 获取积分场景中文名
     *
     * @return real_cname - 积分场景中文名
     */
    public String getRealCname() {
        return realCname;
    }

    /**
     * 设置积分场景中文名
     *
     * @param realCname 积分场景中文名
     */
    public void setRealCname(String realCname) {
        this.realCname = realCname;
    }

    /**
     * 获取积分场景名称
     *
     * @return real_namereal_name - 积分场景名称
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置积分场景名称
     *
     * @param realName 积分场景名称
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取股东层级定义
     *
     * @return level_define - 股东层级定义
     */
    public String getLevelDefine() {
        return levelDefine;
    }

    /**
     * 设置股东层级定义
     *
     * @param levelDefine 股东层级定义
     */
    public void setLevelDefine(String levelDefine) {
        this.levelDefine = levelDefine;
    }

    /**
     * 获取分红比例
     *
     * @return reward_percent - 分红比例
     */
    public String getRewardPercent() {
        return rewardPercent;
    }

    /**
     * 设置分红比例
     *
     * @param rewardPercent 分红比例
     */
    public void setRewardPercent(String rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    /**
     * 获取总金额
     *
     * @return amount - 总金额
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置总金额
     *
     * @param amount 总金额
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 获取状态（0未处理1：已处理）
     *
     * @return state - 状态（0未处理1：已处理）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态（0未处理1：已处理）
     *
     * @param state 状态（0未处理1：已处理）
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}