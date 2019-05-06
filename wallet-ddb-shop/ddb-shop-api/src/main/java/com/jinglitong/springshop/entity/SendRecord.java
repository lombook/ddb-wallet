package com.jinglitong.springshop.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "send_record")
public class SendRecord {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * flowid
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * 发送地址
     */
    private String address;

    /**
     * 类型：1、成功 2、失败
     */
    private Integer type;

    /**
     * 执行时间
     */
    @Column(name = "retry_excuse_time")
    private Date retryExcuseTime;

    /**
     * 重试次数
     */
    @Column(name = "trtry_times")
    private Integer trtryTimes;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 发送数据
     */
    private String body;

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
     * 获取flowid
     *
     * @return flow_id - flowid
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * 设置flowid
     *
     * @param flowId flowid
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    /**
     * 获取发送地址
     *
     * @return address - 发送地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置发送地址
     *
     * @param address 发送地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取类型：1、成功 2、失败
     *
     * @return type - 类型：1、成功 2、失败
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型：1、成功 2、失败
     *
     * @param type 类型：1、成功 2、失败
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取执行时间
     *
     * @return retry_excuse_time - 执行时间
     */
    public Date getRetryExcuseTime() {
        return retryExcuseTime;
    }

    /**
     * 设置执行时间
     *
     * @param retryExcuseTime 执行时间
     */
    public void setRetryExcuseTime(Date retryExcuseTime) {
        this.retryExcuseTime = retryExcuseTime;
    }

    /**
     * 获取重试次数
     *
     * @return trtry_times - 重试次数
     */
    public Integer getTrtryTimes() {
        return trtryTimes;
    }

    /**
     * 设置重试次数
     *
     * @param trtryTimes 重试次数
     */
    public void setTrtryTimes(Integer trtryTimes) {
        this.trtryTimes = trtryTimes;
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
     * 获取发送数据
     *
     * @return body - 发送数据
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置发送数据
     *
     * @param body 发送数据
     */
    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }
}