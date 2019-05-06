/*
 * Copyright 2018 Zhongan.com All right reserved. This software is the
 * confidential and proprietary information of Zhongan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Zhongan.com.
 */
package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;

/**
 * Copyright (c) 2018, 北京井立通科技有限公司
 * All rights reserved.
 * 文件名称: LockCoinToltalVo.java
 * 作        者: yxl 2018年6月25日
 * 创建时间: 2018年6月25日
 * 功能说明: 锁仓详情页面解仓数据总计
 */
public class LockCoinToltalVo extends PageVO implements IAppIdModel{
	private String chainId;
	
	private String chainName;
	
	private String coinId;
	
	private String coinName;
	
	private String num;//到期个数
	
	private String toltalAmount;//到期总金额
	
	private String address;//总账地址

	private String endDate;//到期时间
	
	private String balance;//余额
	
	private String appId;
	
	private String walletName;
	
	
	
	
	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	 * @return the chainName
	 */
	public String getChainName() {
		return chainName;
	}

	/**
	 * @param chainName the chainName to set
	 */
	public void setChainName(String chainName) {
		this.chainName = chainName;
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
	 * @return the coinName
	 */
	public String getCoinName() {
		return coinName;
	}

	/**
	 * @param coinName the coinName to set
	 */
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	/**
	 * @return the num
	 */
	public String getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(String num) {
		this.num = num;
	}

	/**
	 * @return the toltalAmount
	 */
	public String getToltalAmount() {
		return toltalAmount;
	}

	/**
	 * @param toltalAmount the toltalAmount to set
	 */
	public void setToltalAmount(String toltalAmount) {
		this.toltalAmount = toltalAmount;
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
	
	

}
