package com.jinglitong.wallet.api.model.view;

import lombok.Data;

@Data
public class AppControlVO {
    private Integer id;

    private String zId;

    private String appId;

    /**
     * app适用类型
     */
    private String appType;

    /**
     * app名字
     */
    private String appName;

    /**
     * app版本号
     */
    private String appVersion;

    /**
     * app下载url
     */
    private String appUrl;

    /**
     * app是否强制更新
     */
    private Boolean appFource;


    /**
     * app升级次数
     */
    private Integer appVersionCode;

    /**
     * 版本介绍
     */
    private String appRemark;

    private String  updatedTime;

}