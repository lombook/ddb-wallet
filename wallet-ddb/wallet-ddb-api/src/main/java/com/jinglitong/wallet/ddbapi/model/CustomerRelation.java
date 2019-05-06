package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;

public class CustomerRelation {
	
    private Integer id;

    /**
     * 用户id
     */
    private String custId;
	/**
	 * 用户账号
	 */
    private String account;
    /**
     * 朋友用户id
     */
    private String friendId;
    /**
     * 朋友账号
     */
    private String friendAccount;
    /**
     * 创建时间
     */
    private String createTime;

    private String appId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}