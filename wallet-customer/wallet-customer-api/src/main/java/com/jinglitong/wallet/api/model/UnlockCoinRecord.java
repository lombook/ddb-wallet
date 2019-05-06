package com.jinglitong.wallet.api.model;

import java.sql.Timestamp;

public class UnlockCoinRecord {
	
	private Integer id;
	
    private String unlockRecordId;

    private String lockRecordId;

    private String walletId;

    private String coinId;
    
    private String chainId;
    
    private String txHash;

    private String destAddress;

    private String fromAddress;

    private String amount;

    private Integer status;
    
    private Integer operateCount;

    private Timestamp createTime;

    private Timestamp updateTime;

    /**
     * 钱包app
     */
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
	 * @return the chainId
	 */
	public String getChainId() {
		return chainId;
	}

	/**
	 * @param chainId the chainId to set
	 */
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	/**
	 * @return the txHash
	 */
	public String getTxHash() {
		return txHash;
	}

	/**
	 * @param txHash the txHash to set
	 */
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	/**
	 * @return the operateCount
	 */
	public Integer getOperateCount() {
		return operateCount;
	}

	/**
	 * @param operateCount the operateCount to set
	 */
	public void setOperateCount(Integer operateCount) {
		this.operateCount = operateCount;
	}

	/**
	 * @return the unlockRecordId
	 */
	public String getUnlockRecordId() {
		return unlockRecordId;
	}

	/**
	 * @param unlockRecordId the unlockRecordId to set
	 */
	public void setUnlockRecordId(String unlockRecordId) {
		this.unlockRecordId = unlockRecordId;
	}

	/**
	 * @return the lockRecordId
	 */
	public String getLockRecordId() {
		return lockRecordId;
	}

	/**
	 * @param lockRecordId the lockRecordId to set
	 */
	public void setLockRecordId(String lockRecordId) {
		this.lockRecordId = lockRecordId;
	}

	/**
	 * @return the walletId
	 */
	public String getWalletId() {
		return walletId;
	}

	/**
	 * @param walletId the walletId to set
	 */
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	/**
	 * @return the coinId
	 */
	public String getCoinId() {
		return coinId;
	}

	/**
	 * @param coinId the coinId to set
	 */
	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	/**
	 * @return the destAddress
	 */
	public String getDestAddress() {
		return destAddress;
	}

	/**
	 * @param destAddress the destAddress to set
	 */
	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}

	/**
	 * @return the fromAddress
	 */
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * @param fromAddress the fromAddress to set
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

     
    
}