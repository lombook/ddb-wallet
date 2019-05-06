package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_levelup_welfare_record")
public class DdbLevelupWelfareRecord {
    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 积分值
     */
    private Integer amount;

    /**
     * 积分id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * 积分场景名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 积分场景中文名
     */
    @Column(name = "real_cname")
    private String realCname;

    /**
     * 积分名
     */
    @Column(name = "integ_name")
    private String integName;

    /**
     * 积分中文名
     */
    @Column(name = "integ_cname")
    private String integCname;

    @Column(name = "levelup_welfare_rule_id")
    private String levelupWelfareRuleId;

    @Column(name = "order_id")
    private String orderId;

    /**
     * 备注
     */
    private String remark;

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
     * 获取积分值
     *
     * @return amount - 积分值
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置积分值
     *
     * @param amount 积分值
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 获取积分id
     *
     * @return inte_zid - 积分id
     */
    public String getInteZid() {
        return inteZid;
    }

    /**
     * 设置积分id
     *
     * @param inteZid 积分id
     */
    public void setInteZid(String inteZid) {
        this.inteZid = inteZid;
    }

    /**
     * 获取积分场景名
     *
     * @return real_name - 积分场景名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置积分场景名
     *
     * @param realName 积分场景名
     */
    public void setRealName(String realName) {
        this.realName = realName;
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
     * @return levelup_welfare_rule_id
     */
    public String getLevelupWelfareRuleId() {
        return levelupWelfareRuleId;
    }

    /**
     * @param levelupWelfareRuleId
     */
    public void setLevelupWelfareRuleId(String levelupWelfareRuleId) {
        this.levelupWelfareRuleId = levelupWelfareRuleId;
    }

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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