package com.jinglitong.wallet.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "notice_custmer_record")
public class NoticeCustmerCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    @Column(name = "app_notice_id")
    private String appNoticeId;

    /**
     * 用户cust_id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
    
    /**
     * 钱包app
     */
    @Column(name = "app_id")
    private String appId;
    
    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

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
     * @return app_notice_id
     */
    public String getAppNoticeId() {
        return appNoticeId;
    }

    /**
     * @param appNoticeId
     */
    public void setAppNoticeId(String appNoticeId) {
        this.appNoticeId = appNoticeId;
    }

    /**
     * 获得用户cust_id
     *
     * @return cust_id - 用户cust_id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置cust_id
     *
     * @param custId 用户cust_id
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * 获得创建时间
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