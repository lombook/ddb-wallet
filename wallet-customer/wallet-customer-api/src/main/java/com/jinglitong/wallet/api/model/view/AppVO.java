package com.jinglitong.wallet.api.model.view;

import javax.persistence.Column;

import com.jinglitong.wallet.api.model.IAppIdModel;

public class AppVO implements IAppIdModel{
    private String type;
    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
