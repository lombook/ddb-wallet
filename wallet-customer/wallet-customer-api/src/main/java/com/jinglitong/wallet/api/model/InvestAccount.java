package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "invest_account")
public class InvestAccount implements IAppIdModel{
    @Id
    private Integer id;
    
    @Column(name = "invest_id")
    private String investId;

    /**
     * 总账名
     */
    @Column(name = "invest_name")
    private String investName;

    /**
     * 链ID
     */
    @Column(name = "chain_id")
    private String chainId;

    /**
     * 总账地址
     */
    @Column(name = "invest_address")
    private String investAddress;

    /**
     * 状态:1有效 0无效
     */
    private Boolean status;

    /**
     * 币种
     */
    private String currency;

    /**
     * 激活数
     */
    @Column(name = "active_num")
    private String activeNum;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    /**
     * 总账私钥
     */
    @Column(name = "invest_secret")
    private String investSecret;

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
	
    /**
     * @return invest_id
     */
    public String getInvestId() {
        return investId;
    }

    /**
     * @param investId
     */
    public void setInvestId(String investId) {
        this.investId = investId;
    }

    /**
     * 获取总账名
     *
     * @return invest_name - 总账名
     */
    public String getInvestName() {
        return investName;
    }

    /**
     * 设置总账名
     *
     * @param investName 总账名
     */
    public void setInvestName(String investName) {
        this.investName = investName;
    }

    /**
     * 获取链ID
     *
     * @return chain_id - 链ID
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置链ID
     *
     * @param chainId 链ID
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取总账地址
     *
     * @return invest_address - 总账地址
     */
    public String getInvestAddress() {
        return investAddress;
    }

    /**
     * 设置总账地址
     *
     * @param investAddress 总账地址
     */
    public void setInvestAddress(String investAddress) {
        this.investAddress = investAddress;
    }

    /**
     * 获取状态:1有效 0无效
     *
     * @return status - 状态:1有效 0无效
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置状态:1有效 0无效
     *
     * @param status 状态:1有效 0无效
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取币种
     *
     * @return currency - 币种
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 设置币种
     *
     * @param currency 币种
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 获取激活数
     *
     * @return active_num - 激活数
     */
    public String getActiveNum() {
        return activeNum;
    }

    /**
     * 设置激活数
     *
     * @param activeNum 激活数
     */
    public void setActiveNum(String activeNum) {
        this.activeNum = activeNum;
    }

    /**
     * @return create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取总账私钥
     *
     * @return invest_secret - 总账私钥
     */
    public String getInvestSecret() {
        return investSecret;
    }

    /**
     * 设置总账私钥
     *
     * @param investSecret 总账私钥
     */
    public void setInvestSecret(String investSecret) {
        this.investSecret = investSecret;
    }
}