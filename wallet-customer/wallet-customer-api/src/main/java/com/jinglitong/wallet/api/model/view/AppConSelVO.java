package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;
import lombok.Data;

@Data
public class AppConSelVO extends PageVO implements IAppIdModel{
    private Integer id;

    private String zId;

    private String appType;

    private String appName;

    private String appVersion;

    private String appUrl;

    private Boolean appFource;

    private Integer appVersionCode;

    private String appRemark;

    private String createdTime;

    private String updatedTime;

    private String appId;

    private String walletName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}