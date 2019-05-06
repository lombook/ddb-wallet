package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;

public class Feedback implements IAppIdModel{

	@Id
	@Column(name="id")
	private Integer id;
//	@Column(name="zid")
//	private String zId;
	@Column(name="feedback_id")
	private String feedbackid;
	@Column(name="cust_id")
	private String custId;
	
	@Column(name="type")
	private String type;
	/**
     * 是否查看过
     */
    private Boolean haveseen;
    
    /**
     * 获得用户是否查看过
     *
     * @return haveseen 1：查看过 0：未查看
     */
    public Boolean getHaveseen() {
        return haveseen;
    }

    /**
     * 设置用户是否查看过
     *
     * @param haveseen 1：查看过 0：未查看
     */
    public void setHaveseen(Boolean haveseen) {
        this.haveseen = haveseen;
    }
	
	public String getFeedbackid() {
		return feedbackid;
	}

	public void setFeedbackid(String feedbackid) {
		this.feedbackid = feedbackid;
	}

	@Column(name="content")
	private String content;
	
	@Column(name="imgs")
	private String imgs;
	
	@Column(name="phone_mail")
	private String phoneOrMail;
	
	@Column(name="create_time")
	private String createTime;
	
	@Column(name="status")
	private String status;
	
	@Column(name="dealer")
	private String dealer;
	
    /**
     * 钱包app
     */
	@Column(name="app_id")
    private String appId;
    
    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public String getzId() {
//		return zId;
//	}
//
//	public void setzId(String zId) {
//		this.zId = zId;
//	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getPhoneOrMail() {
		return phoneOrMail;
	}

	public void setPhoneOrMail(String phoneOrMail) {
		this.phoneOrMail = phoneOrMail;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	@Column(name="update_time")
	private String updateTime;
	
	@Column(name="feedback_result")
	private String feedback;
}
