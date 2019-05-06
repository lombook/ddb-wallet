package com.jinglitong.springshop.vo.request;



public class AppNSelVO extends PageVo{
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

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
