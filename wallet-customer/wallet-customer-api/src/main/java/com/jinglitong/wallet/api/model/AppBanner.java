package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;

public class AppBanner implements IAppIdModel {

	@Id
	private Integer id;

    @Column(name = "ban_id")
    private String banId;

    /**
     * banner名称
     */
    @Column(name = "ban_name")
    private String name;
    
    /**
     * banner图片url
     */
    @Column(name = "img_url")
    private String imgUrl;
    
    /**
     * banner轮播顺序
     */
    @Column(name = "order_id")
    private String orderId;
    
    /**
     * 点击类型
     */
    @Column(name = "click_type")
    private String clickType;
    
    /**
     * 事件详情
     */
    @Column(name = "click_id")
    private String clickId;
    
    /**
     * 描述
     */
    @Column(name = "remark")
    private String remark;
    
    
    /**
     * banner状态
     */
    @Column(name = "state")
    private String state;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    
    /**
     * 钱包app
     */
  
    private String appId;
    
    
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getClickType() {
		return clickType;
	}

	public void setClickType(String clickType) {
		this.clickType = clickType;
	}

	public String getClickId() {
		return clickId;
	}

	public void setClickId(String clickId) {
		this.clickId = clickId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

	public String getBanId() {
		return banId;
	}

	public void setBanId(String banId) {
		this.banId = banId;
	}
}
