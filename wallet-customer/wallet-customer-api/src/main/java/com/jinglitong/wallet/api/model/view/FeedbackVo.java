package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;

public class FeedbackVo extends PageVO implements IAppIdModel{
	private Integer id;
//	private String zId;
	private String feedbackid;
	private String custId;
	private String feedbackType;
	private String content;
	private String imgs;
	private String phoneOrMail;
	private String status ;
	private String beginTime;
	private String endTime;
	private String account;
	private String feedback;
	private String appId;
	private String wallet_name;
	
	
	public String getWallet_name() {
		return wallet_name;
	}
	public void setWallet_name(String wallet_name) {
		this.wallet_name = wallet_name;
	}
	public Integer getId() {
		return id;
	}
	public String getFeedbackid() {
		return feedbackid;
	}
	public void setFeedbackid(String feedbackid) {
		this.feedbackid = feedbackid;
	}
//	public String getzId() {
//		return zId;
//	}
//	public void setzId(String zId) {
//		this.zId = zId;
//	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
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
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
