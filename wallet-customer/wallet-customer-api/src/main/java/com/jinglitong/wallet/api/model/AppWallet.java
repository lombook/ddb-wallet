package com.jinglitong.wallet.api.model;

import javax.persistence.*;
import java.util.Date;

import lombok.Data;

@Data
@Table(name = "app_wallet")
public class AppWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * app唯一标识
     */
    private String zid;
    /**
     * 钱包名称
     */
    @Column(name = "wallet_name")
    private String walletName;

    /**
     * 公司id
     */
    @Column(name = "company_zid")
    private String companyZid;


    /**
     * 注册验证码
     */
    @Column(name = "sms_reg_code")
    private String smsRegCode;


    /**
     * 短信验证码
     */
    @Column(name = "sms_validation_code")
    private String smsValidationCode;

    /**
     * 必填邀请码
     */
    @Column(name = "must_invite")
    private Boolean mustInvite;

    /**
     * 邮件签名
     */
    @Column(name = "mail_title")
    private String mailTitle;

    /**
     * 理财显示
     */
    @Column(name = "show_financing")
    private Boolean showFinancing;

    /**
     * 自动激活显示
     */
    @Column(name = "show_auto_invest")
    private Boolean showAutoInvest;

    /**
     * 状态
     */
    @Column(name = "state")
    private Boolean state;
    /**
     * 状态
     */
    @Column(name = "protocl_zid")
    private String protoclZid;

    @Column(name = "download_page")
    private String downloadPage;


    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @Column(name = "created_user")
    private String createdUser;

    /**
     * 修改人
     */
    @Column(name = "updated_user")
    private String updatedUser;

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

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getCompanyZid() {
        return companyZid;
    }

    public void setCompanyZid(String companyZid) {
        this.companyZid = companyZid;
    }

    public String getSmsRegCode() {
        return smsRegCode;
    }

    public void setSmsRegCode(String smsRegCode) {
        this.smsRegCode = smsRegCode;
    }

    public String getSmsValidationCode() {
        return smsValidationCode;
    }

    public void setSmsValidationCode(String smsValidationCode) {
        this.smsValidationCode = smsValidationCode;
    }

    public Boolean getMustInvite() {
        return mustInvite;
    }

    public void setMustInvite(Boolean mustInvite) {
        this.mustInvite = mustInvite;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public Boolean getShowFinancing() {
        return showFinancing;
    }

    public void setShowFinancing(Boolean showFinancing) {
        this.showFinancing = showFinancing;
    }

    public Boolean getShowAutoInvest() {
        return showAutoInvest;
    }

    public void setShowAutoInvest(Boolean showAutoInvest) {
        this.showAutoInvest = showAutoInvest;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getProtoclZid() {
        return protoclZid;
    }

    public void setProtoclZid(String protoclZid) {
        this.protoclZid = protoclZid;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }
}