package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.util.Date;

public class Seller implements IAppIdModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="seller_id")
    private String sellerId;
    /**
     * 商家简称
     */
    @Column(name = "short_name")
    private String shortName;

    /**
     * 商家全称
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 公司地址
     */
    @Column(name = "company_address")
    private String companyAddress;

    /**
     * 商家唯一标识
     */
    @Column(name = "access_key")
    private String accessKey;

    /**
     * 公钥
     */
    @Column(name = "public_key")
    private String publicKey;

    /**
     * 同步回调地址
     */
    @Column(name = "syn_notify")
    private String synNotify;

    /**
     * 异步回调地址
     */
    @Column(name = "asyn_notify")
    private String asynNotify;

    /**
     * 0: 待确认 1：确认可用   2：停用
     */
    private Integer status;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * app 访问信息
     */
    @Column(name = "app_access_info")
    private String appAccessInfo;

    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;
    
    @Transient 
    private String walletName;
    
    
    
    public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
    
    public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

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
     * 获取商家简称
     *
     * @return short_name - 商家简称
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 设置商家简称
     *
     * @param shortName 商家简称
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 获取商家全称
     *
     * @return full_name - 商家全称
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 设置商家全称
     *
     * @param fullName 商家全称
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
     * 获取公司地址
     *
     * @return company_address - 公司地址
     */
    public String getCompanyAddress() {
        return companyAddress;
    }

    /**
     * 设置公司地址
     *
     * @param companyAddress 公司地址
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    /**
     * 获取商家唯一标识
     *
     * @return access_key - 商家唯一标识
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * 设置商家唯一标识
     *
     * @param accessKey 商家唯一标识
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * 获取公钥
     *
     * @return public_key - 公钥
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 设置公钥
     *
     * @param publicKey 公钥
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 获取同步回调地址
     *
     * @return syn_notify - 同步回调地址
     */
    public String getSynNotify() {
        return synNotify;
    }

    /**
     * 设置同步回调地址
     *
     * @param synNotify 同步回调地址
     */
    public void setSynNotify(String synNotify) {
        this.synNotify = synNotify;
    }

    /**
     * 获取异步回调地址
     *
     * @return asyn_notify - 异步回调地址
     */
    public String getAsynNotify() {
        return asynNotify;
    }

    /**
     * 设置异步回调地址
     *
     * @param asynNotify 异步回调地址
     */
    public void setAsynNotify(String asynNotify) {
        this.asynNotify = asynNotify;
    }

    /**
     * 获取0: 待确认 1：确认可用   2：停用
     *
     * @return status - 0: 待确认 1：确认可用   2：停用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0: 待确认 1：确认可用   2：停用
     *
     * @param status 0: 待确认 1：确认可用   2：停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
     * 获取修改人
     *
     * @return update_by - 修改人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置修改人
     *
     * @param updateBy 修改人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAppAccessInfo() {
        return appAccessInfo;
    }

    public void setAppAccessInfo(String appAccessInfo) {
        this.appAccessInfo = appAccessInfo;
    }
}