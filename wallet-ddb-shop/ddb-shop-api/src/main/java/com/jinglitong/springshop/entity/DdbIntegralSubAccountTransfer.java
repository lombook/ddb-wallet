package com.jinglitong.springshop.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "integral_sub_account_transfer")
public class DdbIntegralSubAccountTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 相关业务id
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * 转账金额
     */
    private Long amount;

    /**
     * 积分总账id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * 积分名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 积分中文名
     */
    @Column(name = "real_cname")
    private String realCname;

    /**
     * 积分名称
     */
    @Column(name = "integ_name")
    private String integName;

    /**
     * 积分中文名
     */
    @Column(name = "integ_cname")
    private String integCname;

    /**
     * 操作类型:操作类型：1、增加 2、减少
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    @Column(name = "create_time")
    private Date createTime;

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
        this.zid = zid == null ? null : zid.trim();
    }

    /**
     * 获取相关业务id
     *
     * @return flow_id - 相关业务id
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * 设置相关业务id
     *
     * @param flowId 相关业务id
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    /**
     * 获取转账金额
     *
     * @return amount - 转账金额
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置转账金额
     *
     * @param amount 转账金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取积分总账id
     *
     * @return inte_zid - 积分总账id
     */
    public String getInteZid() {
        return inteZid;
    }

    /**
     * 设置积分总账id
     *
     * @param inteZid 积分总账id
     */
    public void setInteZid(String inteZid) {
        this.inteZid = inteZid == null ? null : inteZid.trim();
    }

    /**
     * 获取积分名
     *
     * @return real_name - 积分名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置积分名
     *
     * @param realName 积分名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取积分中文名
     *
     * @return real_cname - 积分中文名
     */
    public String getRealCname() {
        return realCname;
    }

    /**
     * 设置积分中文名
     *
     * @param realCname 积分中文名
     */
    public void setRealCname(String realCname) {
        this.realCname = realCname == null ? null : realCname.trim();
    }

    /**
     * 获取积分名称
     *
     * @return integ_name - 积分名称
     */
    public String getIntegName() {
        return integName;
    }

    /**
     * 设置积分名称
     *
     * @param integName 积分名称
     */
    public void setIntegName(String integName) {
        this.integName = integName == null ? null : integName.trim();
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
        this.integCname = integCname == null ? null : integCname.trim();
    }

    /**
     * 获取操作类型:操作类型：1、增加 2、减少
     *
     * @return type - 操作类型:操作类型：1、增加 2、减少
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置操作类型:操作类型：1、增加 2、减少
     *
     * @param type 操作类型:操作类型：1、增加 2、减少
     */
    public void setType(Integer type) {
        this.type = type;
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
        this.remark = remark == null ? null : remark.trim();
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

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}