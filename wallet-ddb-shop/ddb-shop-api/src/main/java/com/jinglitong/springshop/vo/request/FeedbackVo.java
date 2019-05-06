package com.jinglitong.springshop.vo.request;

public class FeedbackVo extends PageVo{
	
	private Integer id;
	private String zId;
	private String custId;
	private String content;//反馈内容
	private String imgs;
	private String phoneOrMail;
	private String status ;//处理状态
	private String beginTime;
	private String endTime;
	private String feedback;//处理结果	
    private String type;
    private Boolean haveseen;
    private String account;
	
	public Integer getId() {
		return id;
	}
	public String getzId() {
		return zId;
	}
	public void setzId(String zId) {
		this.zId = zId;
	}
	public void setId(Integer id) {
		this.id = id;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
    public Boolean getHaveseen() {
        return haveseen;
    }
    public void setHaveseen(Boolean haveseen) {
        this.haveseen = haveseen;
    }
    public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
}
