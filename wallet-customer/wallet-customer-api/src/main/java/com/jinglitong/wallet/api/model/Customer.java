package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;

public class Customer implements Serializable , IAppIdModel{
    /**
     * 用户id
     */
    @Id
    private Integer id;
    
    @Column(name = "cust_id")
    private String custId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 带区域码手机号
     */
    @Column(name = "all_phone")
    private String allPhone;

    /**
     * 是否有效 1:有效 0无效
     */
    private Boolean state;

    /**
     * 邀请码
     */
    @Column(name = "invite_code")
    private String inviteCode;

    /**
     * 国家id
     */
    @Column(name = "country_id")
    private Integer countryId;

    /**
     * 盐
     */
    private String salt;

    /**
     * 语言
     */
    private String language;
    /**
     * 来源
     */
    private Integer origin;
    /**
     * 备注
     */
    private String remark;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;
    
    @Column(name = "certificate_state")
    private Boolean certificateState;

    private String email;
    @Column(name = "reg_way")
    private Integer regWay;
    
    @Column(name = "self_invite")
    private String selfInvite;

    @Column(name = "tree_level")
    private Integer treeLevel;
    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;
    
    @Transient
    private int cstate;
    
    /**
     * 昵称
     */
    @Column(name ="nick_name")
    private String nickname;
    
    /**
     * 头像
     */
    @Column(name ="favicon")
    private String favicon;
    
    
    
    public String getNickname() {
		return nickname;
	}







	public void setNickname(String nickname) {
		this.nickname = nickname;
	}







	public String getFavicon() {
		return favicon;
	}







	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}







	public Integer getTreeLevel() {
		return treeLevel;
	}







	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}







	public String getAppId() {
		return appId;
	}
    
    

 



	public int getCstate() {
		return cstate;
	}







	public void setCstate(int cstate) {
		this.cstate = cstate;
	}







	public void setAppId(String appId) {
		this.appId = appId;
	}
	
    public String getSelfInvite() {
		return selfInvite;
	}

	public void setSelfInvite(String selfInvite) {
		this.selfInvite = selfInvite;
	}

	/**
     * 获取用户id
     *
     * @return cust_id - 用户id
     */
    public String getCustId() {
        return custId;
    }

    public Boolean getCertificateState() {
		return certificateState;
	}

	public void setCertificateState(Boolean certificateState) {
		this.certificateState = certificateState;
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
     * 获取用户账号
     *
     * @return account - 用户账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置用户账号
     *
     * @param account 用户账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取用户密码
     *
     * @return password - 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     *
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取带区域码手机号
     *
     * @return all_phone - 带区域码手机号
     */
    public String getAllPhone() {
        return allPhone;
    }

    /**
     * 设置带区域码手机号
     *
     * @param allPhone 带区域码手机号
     */
    public void setAllPhone(String allPhone) {
        this.allPhone = allPhone;
    }

    /**
     * 获取是否有效 1:有效 0无效
     *
     * @return state - 是否有效 1:有效 0无效
     */
    public Boolean getState() {
        return state;
    }

    /**
     * 设置是否有效 1:有效 0无效
     *
     * @param state 是否有效 1:有效 0无效
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * 获取邀请码
     *
     * @return invite_code - 邀请码
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * 设置邀请码
     *
     * @param inviteCode 邀请码
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * 获取国家id
     *
     * @return country_id - 国家id
     */
    public Integer getCountryId() {
        return countryId;
    }



    /**
     * 设置国家id
     *
     * @param countryId 国家id
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }



    /**
     * 获取盐
     *
     * @return salt - 盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置盐
     *
     * @param salt 盐
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取语言
     *
     * @return language - 语言
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置语言
     *
     * @param language 语言
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return created_time
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRegWay() {
        return regWay;
    }

    public void setRegWay(Integer regWay) {
        this.regWay = regWay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", custId='" + custId + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", allPhone='" + allPhone + '\'' +
                ", state=" + state +
                ", inviteCode='" + inviteCode + '\'' +
                ", countryId=" + countryId +
                ", salt='" + salt + '\'' +
                ", language='" + language + '\'' +
                ", origin=" + origin +
                ", remark='" + remark + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                ", certificateState=" + certificateState +
                ", email='" + email + '\'' +
                ", regWay=" + regWay +
                ", selfInvite='" + selfInvite + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}