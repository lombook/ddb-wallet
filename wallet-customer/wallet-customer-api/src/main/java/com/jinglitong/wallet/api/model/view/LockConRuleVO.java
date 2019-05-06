package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;

/**
 * Created by fan on 2018/6/4.
 */
public class LockConRuleVO extends PageVO implements IAppIdModel{

    private String lockRuleId;

    private String chainId;

    private String coinId;

    private Integer lockDays;

    private Integer lockRate;

    private String lockAddress;

    private String minLockAmout;

    private Boolean ruleStatus;

    private String createTime;

    private String updateTime;
    /**
     * 钱包app
     */
    private String appId;
    
    

    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getLockRuleId() {
        return lockRuleId;
    }

    public void setLockRuleId(String lockRuleId) {
        this.lockRuleId = lockRuleId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public Integer getLockDays() {
        return lockDays;
    }

    public void setLockDays(Integer lockDays) {
        this.lockDays = lockDays;
    }

    public Integer getLockRate() {
        return lockRate;
    }

    public void setLockRate(Integer lockRate) {
        this.lockRate = lockRate;
    }

    public String getLockAddress() {
        return lockAddress;
    }

    public void setLockAddress(String lockAddress) {
        this.lockAddress = lockAddress;
    }

    public String getMinLockAmout() {
        return minLockAmout;
    }

    public void setMinLockAmout(String minLockAmout) {
        this.minLockAmout = minLockAmout;
    }

    public Boolean getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(Boolean ruleStatus) {
        this.ruleStatus = ruleStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
