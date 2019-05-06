package com.jinglitong.wallet.api.model.logic;

import java.io.Serializable;

/**
 * Created by fan on 2018/8/3.
 */
public class PageViewLog implements Serializable {

    //主键id
    private Long id;

    /**
     * 页面Type
     */
    private Integer viewType;

    private String viewId;
    /**
     * 用户名
     */
    private String account;
    /**
     * qinaboa 编码
     */
    private String appId;
    /**
     * 标题
     */
    private String title;
    /**
     * 查看时间
     */
    private String viewTime;
    /**
     * 创建时间
     */
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }
}
