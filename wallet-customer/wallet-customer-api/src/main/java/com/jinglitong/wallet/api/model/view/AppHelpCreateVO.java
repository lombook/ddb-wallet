package com.jinglitong.wallet.api.model.view;

import java.sql.Timestamp;

import com.jinglitong.wallet.api.model.IAppIdModel;

public class AppHelpCreateVO implements IAppIdModel{
    private String appHelpId;
    private String title;
    private String remark;
    private String appType;
    private Boolean state;
    private Integer showOrder;
    private String createTime;
    private String updateTime;
    private String body;
    private String appId;


    
    public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }


    public String getAppHelpId() {
        return appHelpId;
    }

    public void setAppHelpId(String appHelpId) {
        this.appHelpId = appHelpId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
