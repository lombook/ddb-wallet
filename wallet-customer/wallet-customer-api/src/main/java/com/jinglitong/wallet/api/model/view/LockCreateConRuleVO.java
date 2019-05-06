package com.jinglitong.wallet.api.model.view;

import java.util.List;

import com.jinglitong.wallet.api.model.IAppIdModel;

/**
 * Created by fan on 2018/6/4.
 */
public class LockCreateConRuleVO implements IAppIdModel{


    private String lockRuleId;

    private String chainId;

    private String coinId;

    private String lockAddress;

    private String minLockAmout;

    private List<Rule> rulelist = null;

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

    public List<Rule> getRulelist() {
        return rulelist;
    }

    public void setRulelist(List<Rule> rulelist) {
        this.rulelist = rulelist;
    }
}
