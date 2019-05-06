package com.jinglitong.wallet.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_notice_sub")
public class AppNoticeSub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    @Column(name = "notice_id")
    private String noticeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
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
     * img ：图片\r\ntxt：文字\r\nmulti：混合
     */
    @Column(name = "notice_type")
    private String noticeType;

    /**
     * 图片
     */
    @Column(name = "notice_img")
    private String noticeImg;

    /**
     * ״̬
     */
    private Boolean state;

    /**
     * 设置显示时间
     */
    @Column(name = "view_ctime")
    private String viewCtime;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "app_id")
    private String appId;

    /**
     * 是否推送 0:不推送 1: 推送
     */
    @Column(name = "is_push")
    private Boolean isPush;

    /**
     * ͨ通知类型 1:系统升级 2:系统给个人发
     */
    private String type;

    /**
     * 通知类型 1:系统升级 2:系统给个人发
     */
    @Column(name = "sub_type")
    private String subType;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * ������
     */
    private String body;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * @param zid
     */
    public void setZid(String zid) {
        this.zid = zid;
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


    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean isTop) {
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getViewCtime() {
        return viewCtime;
    }


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
     * @return app_id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Boolean getIsPush() {
        return isPush;
    }

    public void setIsPush(Boolean isPush) {
        this.isPush = isPush;
    }

    /**
     * ��ȡ֪ͨ���� 1:ϵͳ���� 
     *
     * @return type - ֪ͨ���� 1:ϵͳ���� 
     */
    public String getType() {
        return type;
    }

    /**
     * ����֪ͨ���� 1:ϵͳ���� 
     *
     * @param type ֪ͨ���� 1:ϵͳ���� 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * ��ȡ֪ͨ���� 1:ϵͳ���� 2:ϵͳ������ת��
     *
     * @return sub_type - ֪ͨ���� 1:ϵͳ���� 2:ϵͳ������ת��
     */
    public String getSubType() {
        return subType;
    }

    /**
     * ����֪ͨ���� 1:ϵͳ���� 2:ϵͳ������ת��
     *
     * @param subType ֪ͨ���� 1:ϵͳ���� 2:ϵͳ������ת��
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    /**
     * ��ȡ�û�id
     *
     * @return cust_id - �û�id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * �����û�id
     *
     * @param custId �û�id
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * ��ȡ������
     *
     * @return body - ������
     */
    public String getBody() {
        return body;
    }

    /**
     * ����������
     *
     * @param body ������
     */
    public void setBody(String body) {
        this.body = body;
    }
}