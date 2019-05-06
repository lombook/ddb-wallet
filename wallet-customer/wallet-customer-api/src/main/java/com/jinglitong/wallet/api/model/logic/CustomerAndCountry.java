package com.jinglitong.wallet.api.model.logic;

import com.jinglitong.wallet.api.model.AppWallet;
import com.jinglitong.wallet.api.model.Country;

import com.jinglitong.wallet.api.model.Customer;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public class CustomerAndCountry extends Customer implements Serializable {

	private Country country;

	private AppWallet appWallet;

	private String walletName;

	private String auditState;

	private String inviteUser;//邀请人（上级）

	public String getInviteUser() {
		return inviteUser;
	}

	public void setInviteUser(String inviteUser) {
		this.inviteUser = inviteUser;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public AppWallet getAppWallet() {
		return appWallet;
	}

	public void setAppWallet(AppWallet appWallet) {
		this.appWallet = appWallet;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
}