package com.jinglitong.springshop.vo.request;

public class CustRegVO {

	private String account;
	private String passwd;
	private String InviteCode;
	private String smsCode;
	
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getInviteCode() {
		return InviteCode;
	}
	public void setInviteCode(String inviteCode) {
		InviteCode = inviteCode;
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	
}
