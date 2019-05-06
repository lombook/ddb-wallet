package com.jinglitong.wallet.api.model.view;

import java.util.Date;

import com.jinglitong.wallet.api.model.IAppIdModel;

/**
 * Created by fan on 2018/6/4.
 */
public class LockSelCoinRecordVO  extends PageVO implements IAppIdModel{

    private String lockRecordId;

    private String custId;

    private String walletId;

    private String coinId;

    private String walletAddress;

    private Integer lockDays;

    private Integer lockRate;

    private Date lockStartDate;

    private Date lockEndDate;

    private String lockRoueId;

    private String rewardAmount;

    private String lockAmount;

    private String payHash;


    private String receviceHash;


    private String sweetHash;


    private Integer lockStatus;


    private String createTime;


    private String updateTime;


    private String payTime;

    private String receviceTime;


    private String sweetTime;
    
    private String appId;
    
    

    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getLockRecordId() {
        return lockRecordId;
    }

    public void setLockRecordId(String lockRecordId) {
        this.lockRecordId = lockRecordId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
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

    public Date getLockStartDate() {
        return lockStartDate;
    }

    public void setLockStartDate(Date lockStartDate) {
        this.lockStartDate = lockStartDate;
    }

    public Date getLockEndDate() {
        return lockEndDate;
    }

    public void setLockEndDate(Date lockEndDate) {
        this.lockEndDate = lockEndDate;
    }

    public String getLockRoueId() {
        return lockRoueId;
    }

    public void setLockRoueId(String lockRoueId) {
        this.lockRoueId = lockRoueId;
    }

    public String getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public String getLockAmount() {
        return lockAmount;
    }

    public void setLockAmount(String lockAmount) {
        this.lockAmount = lockAmount;
    }

    public String getPayHash() {
        return payHash;
    }

    public void setPayHash(String payHash) {
        this.payHash = payHash;
    }

    public String getReceviceHash() {
        return receviceHash;
    }

    public void setReceviceHash(String receviceHash) {
        this.receviceHash = receviceHash;
    }

    public String getSweetHash() {
        return sweetHash;
    }

    public void setSweetHash(String sweetHash) {
        this.sweetHash = sweetHash;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getReceviceTime() {
        return receviceTime;
    }

    public void setReceviceTime(String receviceTime) {
        this.receviceTime = receviceTime;
    }

    public String getSweetTime() {
        return sweetTime;
    }

    public void setSweetTime(String sweetTime) {
        this.sweetTime = sweetTime;
    }
}
