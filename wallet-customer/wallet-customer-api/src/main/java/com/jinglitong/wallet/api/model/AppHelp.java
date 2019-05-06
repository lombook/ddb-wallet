package com.jinglitong.wallet.api.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "app_help")
public class AppHelp implements IAppIdModel{
    /**
     * 帮助文档id
     */
    @Id
    private Integer id;
    
    @Column(name = "help_id")
    private String helpId;

    /**
     * 标题
     */
    private String title;

    /**
     * 标题下描述
     */
    private String remark;

    /**
     * 帮助适用机型
     */
    @Column(name = "app_type")
    private String appType;

    /**
     * 是否有效
     */
    private Boolean state;

    @Column(name = "show_order")
    private Integer showOrder;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name ="update_time")
    private Timestamp updateTime;
    /**
     * 主文体
     */
    private String body;

    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;
    
    
   

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
    /**
     * 获取帮助文档id
     *
     * @return help_id - 帮助文档id
     */
    public String getHelpId() {
        return helpId;
    }

    /**
     * 设置帮助文档id
     *
     * @param helpId 帮助文档id
     */
    public void setHelpId(String helpId) {
        this.helpId = helpId;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取标题下描述
     *
     * @return remark - 标题下描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置标题下描述
     *
     * @param remark 标题下描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取帮助适用机型
     *
     * @return app_type - 帮助适用机型
     */
    public String getAppType() {
        return appType;
    }

    /**
     * 设置帮助适用机型
     *
     * @param appType 帮助适用机型
     */
    public void setAppType(String appType) {
        this.appType = appType;
    }

    /**
     * 获取是否有效
     *
     * @return state - 是否有效
     */
    public Boolean getState() {
        return state;
    }

    /**
     * 设置是否有效
     *
     * @param state 是否有效
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @return show_order
     */
    public Integer getShowOrder() {
        return showOrder;
    }

    /**
     * @param showOrder
     */
    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

 

    public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
     * 获取主文体
     *
     * @return body - 主文体
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置主文体
     *
     * @param body 主文体
     */
    public void setBody(String body) {
        this.body = body;
    }
}