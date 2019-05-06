package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "invite_system")
public class InviteSystem implements IAppIdModel{

	/**
     * id
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name ="zid")
	private String zId;
	/**
     * 一级被邀请用户id
     */
	@Column(name = "cust_id")
	private String custId;
	
	/**
     * 一级被邀请用户账号
     */
	@Column(name = "cust_account")
	private String custAccount;
	
	/**
     * 二级被邀请用户id
     */
	@Column(name = "level_one_cust_id")
	private String levelOneCustId;
	
	/**
     * 二级被邀请用户账号
     */
	@Column(name = "level_one_cust_account")
	private String levelOneCustAccount;
	
	/**
     * 三级被邀请用户id
     */
	@Column(name = "level_two_cust_id")
	private String levelTwoCustId;
	
	/**
     * 三级被邀请用户账号
     */
	@Column(name = "level_two_cust_account")
	private String levelTwoCustAccount;
	
	/**
     * 四级被邀请用户id
     */
	@Column(name = "level_three_cust_id")
	private String levelThreeCustId;
	
    /**
     * 钱包app
     */
	@Column(name ="app_id")
    private String appId;
    
    
    
    public String getzId() {
		return zId;
	}

	public void setzId(String zId) {
		this.zId = zId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustAccount() {
		return custAccount;
	}

	public void setCustAccount(String custAccount) {
		this.custAccount = custAccount;
	}

	public String getLevelOneCustId() {
		return levelOneCustId;
	}

	public void setLevelOneCustId(String levelOneCustId) {
		this.levelOneCustId = levelOneCustId;
	}

	public String getLevelOneCustAccount() {
		return levelOneCustAccount;
	}

	public void setLevelOneCustAccount(String levelOneCustAccount) {
		this.levelOneCustAccount = levelOneCustAccount;
	}

	public String getLevelTwoCustId() {
		return levelTwoCustId;
	}

	public void setLevelTwoCustId(String levelTwoCustId) {
		this.levelTwoCustId = levelTwoCustId;
	}

	public String getLevelTwoCustAccount() {
		return levelTwoCustAccount;
	}

	public void setLevelTwoCustAccount(String levelTwoCustAccount) {
		this.levelTwoCustAccount = levelTwoCustAccount;
	}

	public String getLevelThreeCustId() {
		return levelThreeCustId;
	}

	public void setLevelThreeCustId(String levelThreeCustId) {
		this.levelThreeCustId = levelThreeCustId;
	}

	public String getLevelThreeCustAccount() {
		return levelThreeCustAccount;
	}

	public void setLevelThreeCustAccount(String levelThreeCustAccount) {
		this.levelThreeCustAccount = levelThreeCustAccount;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	/**
     * 四级级被邀请用户账号
     */
	@Column(name = "level_three_cust_account")
	private String levelThreeCustAccount;
	
	/**
     * 创建时间
     */
	@Column(name = "created_time")
	private String createdTime;
	
	
}
