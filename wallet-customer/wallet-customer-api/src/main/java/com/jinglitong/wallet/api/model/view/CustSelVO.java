package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;
import javax.persistence.Column;

public class CustSelVO extends PageVO implements IAppIdModel{


    private String account;
    private String phone;
    private String inviteCode;
    private String remark;
    private String countryCode;
    private Boolean state;
    private String allPhone;
    private String startTime;
    private String endTime;

    private String appId;
    private String auditState;
    private String inviteUser;// 邀请人   add by 10.18
    


    public String getInviteUser() {
		return inviteUser;
	}

	public void setInviteUser(String inviteUser) {
		this.inviteUser = inviteUser;
	}

	public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getAllPhone() {
        return allPhone;
    }

    public void setAllPhone(String allPhone) {
        this.allPhone = allPhone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
}
