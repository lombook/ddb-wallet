package com.jinglitong.springshop.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pay_message")
public class PayMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payaction_id")
    private String payactionId;

    /**
     * 1直接请求获得,2异步获得
     */
    private Byte type;

    @Column(name = "create_time")
    private Date createTime;

    private String body;

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
     * @return payaction_id
     */
    public String getPayactionId() {
        return payactionId;
    }

    /**
     * @param payactionId
     */
    public void setPayactionId(String payactionId) {
        this.payactionId = payactionId == null ? null : payactionId.trim();
    }

    /**
     * 获取1直接请求获得,2异步获得
     *
     * @return type - 1直接请求获得,2异步获得
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置1直接请求获得,2异步获得
     *
     * @param type 1直接请求获得,2异步获得
     */
    public void setType(Byte type) {
        this.type = type;
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
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     */
    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }
}