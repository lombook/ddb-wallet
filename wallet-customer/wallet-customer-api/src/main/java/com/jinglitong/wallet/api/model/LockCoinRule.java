package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "lock_coin_rule")
public class LockCoinRule implements IAppIdModel{
	
    @Id
    private Integer id;
    
    @Column(name = "lock_rule_id")
    private String lockRuleId;

    /**
     * 链id
     */
    @Column(name = "chain_id")
    private String chainId;

    /**
     * 用户币种id
     */
    @Column(name = "coin_id")
    private String coinId;

    /**
     * 锁住天数
     */
    @Column(name = "lock_days")
    private Integer lockDays;

    /**
     * 锁住费率(100表示10%)
     */
    @Column(name = "lock_rate")
    private Integer lockRate;

    /**
     * 锁仓地址
     */
    @Column(name = "lock_address")
    private String lockAddress;

    /**
     * 最小金额
     */
    @Column(name = "min_lock_amout")
    private String minLockAmout;

    /**
     * 前端是否展示(1展示,0不展示)
     */
    @Column(name = "rule_status")
    private Boolean ruleStatus;
    
    /**
     * 总账地址
     */
    private String address;
    
    /**
     * 总账秘钥
     */
    private String secret;
    
    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    @Transient
    private String chainName;

    @Transient
    private String coinName;

    @Transient
    private String issuer;

    @Transient
    private String currency;

    /**
     * 钱包app
     */
    @Column(name = "app_id")
    private String appId;
    
    
    
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
	 * @return the custId
	 */
	public String getCustId() {
		return custId;
	}

	/**
	 * @param custId the custId to set
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
     * @return lock_rule_id
     */
    public String getLockRuleId() {
        return lockRuleId;
    }

    /**
     * @param lockRuleId
     */
    public void setLockRuleId(String lockRuleId) {
        this.lockRuleId = lockRuleId;
    }

    /**
     * 获取链id
     *
     * @return chain_id - 链id
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置链id
     *
     * @param chainId 链id
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取用户币种id
     *
     * @return coin_id - 用户币种id
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置用户币种id
     *
     * @param coinId 用户币种id
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取锁住天数
     *
     * @return lock_days - 锁住天数
     */
    public Integer getLockDays() {
        return lockDays;
    }

    /**
     * 设置锁住天数
     *
     * @param lockDays 锁住天数
     */
    public void setLockDays(Integer lockDays) {
        this.lockDays = lockDays;
    }

    /**
     * 获取锁住费率(100表示10%)
     *
     * @return lock_rate - 锁住费率(100表示10%)
     */
    public Integer getLockRate() {
        return lockRate;
    }

    /**
     * 设置锁住费率(100表示10%)
     *
     * @param lockRate 锁住费率(100表示10%)
     */
    public void setLockRate(Integer lockRate) {
        this.lockRate = lockRate;
    }

    /**
     * 获取锁仓地址
     *
     * @return lock_address - 锁仓地址
     */
    public String getLockAddress() {
        return lockAddress;
    }

    /**
     * 设置锁仓地址
     *
     * @param lockAddress 锁仓地址
     */
    public void setLockAddress(String lockAddress) {
        this.lockAddress = lockAddress;
    }

    /**
     * 获取最小金额
     *
     * @return min_lock_amout - 最小金额
     */
    public String getMinLockAmout() {
        return minLockAmout;
    }

    /**
     * 设置最小金额
     *
     * @param minLockAmout 最小金额
     */
    public void setMinLockAmout(String minLockAmout) {
        this.minLockAmout = minLockAmout;
    }

    /**
     * 获取前端是否展示(1展示,0不展示)
     *
     * @return rule_status - 前端是否展示(1展示,0不展示)
     */
    public Boolean getRuleStatus() {
        return ruleStatus;
    }

    /**
     * 设置前端是否展示(1展示,0不展示)
     *
     * @param ruleStatus 前端是否展示(1展示,0不展示)
     */
    public void setRuleStatus(Boolean ruleStatus) {
        this.ruleStatus = ruleStatus;
    }

    
    /**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}