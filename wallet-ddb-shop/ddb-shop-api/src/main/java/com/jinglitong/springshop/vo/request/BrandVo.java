package com.jinglitong.springshop.vo.request;

import java.util.Date;


public class BrandVo  extends PageVo{

	private Integer id;

    private String zid;
    
    /**
     * 显示优先级 0最高
     */
    private Byte orders;

    /**
     * logo图标
     */
    private String logo;

    /**
     * 名称
     */
    private String name;

    /**
     * 网址
     */
    private String url;

    private Date createdTime;

    private Date updatedTime;

    /**
     * 介绍
     */
    private String introduction;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public Byte getOrders() {
		return orders;
	}

	public void setOrders(Byte orders) {
		this.orders = orders;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
    
    
}
