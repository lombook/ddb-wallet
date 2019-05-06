package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;

public class InvestAccountVO extends PageVO implements IAppIdModel {
	
	private String investId;
	
	private String investName;
	
	private String investAddress;
	
	private String chainId;
	
	private String investSecret;
	
	private Boolean state;
	
	private String currency;
	
	private String activeNum;
	
	private String appId;
	
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getInvestName() {
		return investName;
	}

	public void setInvestName(String investName) {
		this.investName = investName;
	}

	public String getInvestAddress() {
		return investAddress;
	}

	public void setInvestAddress(String investAddress) {
		this.investAddress = investAddress;
	}

	public String getChainId() {
		return chainId;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	public String getInvestSecret() {
		return investSecret;
	}

	public void setInvestSecret(String investSecret) {
		this.investSecret = investSecret;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getActiveNum() {
		return activeNum;
	}

	public void setActiveNum(String activeNum) {
		this.activeNum = activeNum;
	}

	public String getInvestId() {
		return investId;
	}

	public void setInvestId(String investId) {
		this.investId = investId;
	}
	

}
