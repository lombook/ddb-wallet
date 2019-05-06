package com.jinglitong.wallet.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_control")
public class AppControl implements IAppIdModel{
    @Id
    private Integer id;

    @Column(name = "zid")
    private String zId;

    /**
     * app适用类型
     */
    @Column(name = "app_type")
    private String appType;

    /**
     * app名字
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * app版本号
     */
    @Column(name = "app_version")
    private String appVersion;

    /**
     * app下载url
     */
    @Column(name = "app_url")
    private String appUrl;

    /**
     * app是否强制更新
     */
    @Column(name = "app_fource")
    private Boolean appFource;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;
    @Column(name = "app_id")
    private String appId;
    /**
     * app升级次数
     */
    @Column(name = "app_version_code")
    private Integer appVersionCode;

    /**
     * 版本介绍
     */
    @Column(name = "app_remark")
    private String appRemark;



    /**
     * 获取app适用类型
     *
     * @return app_type - app适用类型
     */
    public String getAppType() {
        return appType;
    }

    /**
     * 设置app适用类型
     *
     * @param appType app适用类型
     */
    public void setAppType(String appType) {
        this.appType = appType;
    }

    /**
     * 获取app名字
     *
     * @return app_name - app名字
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置app名字
     *
     * @param appName app名字
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取app版本号
     *
     * @return app_version - app版本号
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * 设置app版本号
     *
     * @param appVersion app版本号
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * 获取app下载url
     *
     * @return app_url - app下载url
     */
    public String getAppUrl() {
        return appUrl;
    }

    /**
     * 设置app下载url
     *
     * @param appUrl app下载url
     */
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    /**
     * 获取app是否强制更新
     *
     * @return app_fource - app是否强制更新
     */
    public Boolean getAppFource() {
        return appFource;
    }

    /**
     * 设置app是否强制更新
     *
     * @param appFource app是否强制更新
     */
    public void setAppFource(Boolean appFource) {
        this.appFource = appFource;
    }

    /**
     * @return created_time
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取app升级次数
     *
     * @return app_version_code - app升级次数
     */
    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    /**
     * 设置app升级次数
     *
     * @param appVersionCode app升级次数
     */
    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    /**
     * 获取版本介绍
     *
     * @return app_remark - 版本介绍
     */
    public String getAppRemark() {
        return appRemark;
    }

    /**
     * 设置版本介绍
     *
     * @param appRemark 版本介绍
     */
    public void setAppRemark(String appRemark) {
        this.appRemark = appRemark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getzId() {
        return zId;
    }

    public void setzId(String zId) {
        this.zId = zId;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }
}