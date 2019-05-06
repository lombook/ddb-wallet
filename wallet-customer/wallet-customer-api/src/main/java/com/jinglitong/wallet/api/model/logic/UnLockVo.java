/*
 * Copyright 2018 Zhongan.com All right reserved. This software is the
 * confidential and proprietary information of Zhongan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Zhongan.com.
 */
package com.jinglitong.wallet.api.model.logic;

/**
 * Copyright (c) 2018, 北京井立通科技有限公司
 * All rights reserved.
 * 文件名称: UnLockVo.java
 * 作        者: yxl 2018年6月27日
 * 创建时间: 2018年6月27日
 * 功能说明: 解仓需要的bean
 */
public class UnLockVo extends LPaymentVO {

	private String chainId;
	
	private String lockRecordId;
	
	private String coinId;
	
	private String walletId;
	
	

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
	
	
}
