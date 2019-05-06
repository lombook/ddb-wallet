package com.jinglitong.wallet.api.model.view;

import javax.persistence.Column;

import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.IAppIdModel;

public class AppNoticeVO  implements IAppIdModel{
    private Integer id;
    private String noticeId;
    private String title;
    private String subTitle;
    private String isTop;
    private Integer orderTop;
    private String noticeType;
    private String noticeImg;
    private String state;
    private String viewCtime;
    private String body;
    private String appId;
    private String walletName;
    private Boolean isPush;
    private String type;
    private String subType;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public Integer getOrderTop() {
        return orderTop;
    }

    public void setOrderTop(Integer orderTop) {
        this.orderTop = orderTop;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeImg() {
        return noticeImg;
    }

    public void setNoticeImg(String noticeImg) {
        this.noticeImg = noticeImg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getViewCtime() {
        return viewCtime;
    }

    public void setViewCtime(String viewCtime) {
        this.viewCtime = viewCtime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public Boolean getIsPush() {
		return isPush;
	}

	public void setIsPush(Boolean isPush) {
		this.isPush = isPush;
	}
	
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
}
