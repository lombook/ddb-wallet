package com.jinglitong.wallet.api.model;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "app_notice")
public class AppNotice extends BaseEntity implements IAppIdModel {
    @Id
    private Integer id;

    @Column(name = "notice_id")
    private String noticeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 标题下描述
     */
    @Column(name = "sub_title")
    private String subTitle;

    /**
     * 是否置顶
     */
    @Column(name = "is_top")
    private Boolean isTop;

    /**
     * 置顶顺序
     */
    @Column(name = "order_top")
    private Integer orderTop;

    /**
     * img ：图片
txt：文字
multi：混合
     */
    @Column(name = "notice_type")
    private String noticeType;

    /**
     * 公告图片
     */
    @Column(name = "notice_img")
    private String noticeImg;

    /**
     * 状态
     */

    private Boolean state;
    
    /**
     * 是否推送
     * 1：推送 0：不推送
     */

    @Column(name = "is_push")
    private Boolean isPush;
    
    /**
     * 通知类型 
     * 1：系统升级
     */

    @Column(name = "type")
    private String type;
    

	/**
     * 设置显示时间
     */
    @Column(name = "view_ctime")
    private String viewCtime;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    /**
     * 主文体
     */
    private String body;

    /**
     * 钱包app
     */
    @Column(name = "app_id")
    private String appId;
    
    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
    /**
     * @return notice_id
     */
    public String getNoticeId() {
        return noticeId;
    }

    /**
     * @param noticeId
     */
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
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
     * @return sub_title - 标题下描述
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * 设置标题下描述
     *
     * @param subTitle 标题下描述
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * 获取是否置顶
     *
     * @return is_top - 是否置顶
     */
    public Boolean getIsTop() {
        return isTop;
    }

    /**
     * 设置是否置顶
     *
     * @param isTop 是否置顶
     */
    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    /**
     * 获取置顶顺序
     *
     * @return order_top - 置顶顺序
     */
    public Integer getOrderTop() {
        return orderTop;
    }

    /**
     * 设置置顶顺序
     *
     * @param orderTop 置顶顺序
     */
    public void setOrderTop(Integer orderTop) {
        this.orderTop = orderTop;
    }
    
    /**
     * 获得是否推送
     * @return
     */
    public Boolean getIsPush() {
		return isPush;
	}

    /**
     * 设置是否推送
     * @param isPush
     */
	public void setIsPush(Boolean isPush) {
		this.isPush = isPush;
	}
	
	/**
     * 获得通知类型
     * @return
     */
    public String getType() {
		return type;
	}

    /**
     * 设置通知类型
     * @param String
     */
	public void setType(String type) {
		this.type = type;
	}

    /**
     * 获取img ：图片
txt：文字
multi：混合
     *
     * @return notice_type - img ：图片
txt：文字
multi：混合
     */
    public String getNoticeType() {
        return noticeType;
    }

    /**
     * 设置img ：图片
txt：文字
multi：混合
     *
     * @param noticeType img ：图片
txt：文字
multi：混合
     */
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * 获取公告图片
     *
     * @return notice_img - 公告图片
     */
    public String getNoticeImg() {
        return noticeImg;
    }

    /**
     * 设置公告图片
     *
     * @param noticeImg 公告图片
     */
    public void setNoticeImg(String noticeImg) {
        this.noticeImg = noticeImg;
    }

    /**
     * 获取状态
     *
     * @return state - 状态
     */
    public Boolean getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * 获取设置显示时间
     *
     * @return view_ctime - 设置显示时间
     */
    public String getViewCtime() {
        return viewCtime;
    }

    /**
     * 设置设置显示时间
     *
     * @param viewCtime 设置显示时间
     */
    public void setViewCtime(String viewCtime) {
        this.viewCtime = viewCtime;
    }

    /**
     * @return create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(String updateTime) {
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