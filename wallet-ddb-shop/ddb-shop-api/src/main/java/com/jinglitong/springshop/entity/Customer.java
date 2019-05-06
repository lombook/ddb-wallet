package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cust_id")
    private String custId;

    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 带区域码手机号
     */
    @Column(name = "all_phone")
    private String allPhone;

    /**
     * 是否有效 1：有效 0：无效
     */
    private Boolean state;

    /**
     * 邀请码
     */
    @Column(name = "invite_code")
    private String inviteCode;

    /**
     * 国家id
     */
    @Column(name = "country_id")
    private Integer countryId;

    /**
     * 盐
     */
    private String salt;

    /**
     * 语言
     */
    private String language;

    /**
     * 来源 :0手机app
     */
    private Integer origin;

    /**
     * 备注
     */
    private String remark;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 认证状态:0 未认证；1 已认证
     */
    @Column(name = "certificate_state")
    private Boolean certificateState;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 注册方式：0手机号1邮箱
     */
    @Column(name = "reg_way")
    private Integer regWay;

    /**
     * 自己的的邀请码
     */
    @Column(name = "self_invite")
    private String selfInvite;

    /**
     * 树形等级
     */
    @Column(name = "tree_level")
    private Integer treeLevel;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 头像url
     */
    private String favicon;
    
    @Column(name = "app_id")
    private String appId;


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
     * @return account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * 获取用户密码
     *
     * @return password - 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     *
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取带区域码手机号
     *
     * @return all_phone - 带区域码手机号
     */
    public String getAllPhone() {
        return allPhone;
    }

    /**
     * 设置带区域码手机号
     *
     * @param allPhone 带区域码手机号
     */
    public void setAllPhone(String allPhone) {
        this.allPhone = allPhone == null ? null : allPhone.trim();
    }

    /**
     * 获取是否有效 1：有效 0：无效
     *
     * @return state - 是否有效 1：有效 0：无效
     */
    public Boolean getState() {
        return state;
    }

    /**
     * 设置是否有效 1：有效 0：无效
     *
     * @param state 是否有效 1：有效 0：无效
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * 获取邀请码
     *
     * @return invite_code - 邀请码
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * 设置邀请码
     *
     * @param inviteCode 邀请码
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode == null ? null : inviteCode.trim();
    }

    /**
     * 获取国家id
     *
     * @return country_id - 国家id
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * 设置国家id
     *
     * @param countryId 国家id
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * 获取盐
     *
     * @return salt - 盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置盐
     *
     * @param salt 盐
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * 获取语言
     *
     * @return language - 语言
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置语言
     *
     * @param language 语言
     */
    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    /**
     * 获取来源 :0手机app
     *
     * @return origin - 来源 :0手机app
     */
    public Integer getOrigin() {
        return origin;
    }

    /**
     * 设置来源 :0手机app
     *
     * @param origin 来源 :0手机app
     */
    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取认证状态:0 未认证；1 已认证
     *
     * @return certificate_state - 认证状态:0 未认证；1 已认证
     */
    public Boolean getCertificateState() {
        return certificateState;
    }

    /**
     * 设置认证状态:0 未认证；1 已认证
     *
     * @param certificateState 认证状态:0 未认证；1 已认证
     */
    public void setCertificateState(Boolean certificateState) {
        this.certificateState = certificateState;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取注册方式：0手机号1邮箱
     *
     * @return reg_way - 注册方式：0手机号1邮箱
     */
    public Integer getRegWay() {
        return regWay;
    }

    /**
     * 设置注册方式：0手机号1邮箱
     *
     * @param regWay 注册方式：0手机号1邮箱
     */
    public void setRegWay(Integer regWay) {
        this.regWay = regWay;
    }

    /**
     * 获取自己的的邀请码
     *
     * @return self_invite - 自己的的邀请码
     */
    public String getSelfInvite() {
        return selfInvite;
    }

    /**
     * 设置自己的的邀请码
     *
     * @param selfInvite 自己的的邀请码
     */
    public void setSelfInvite(String selfInvite) {
        this.selfInvite = selfInvite == null ? null : selfInvite.trim();
    }

    /**
     * 获取树形等级
     *
     * @return tree_level - 树形等级
     */
    public Integer getTreeLevel() {
        return treeLevel;
    }

    /**
     * 设置树形等级
     *
     * @param treeLevel 树形等级
     */
    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取头像url
     *
     * @return favicon - 头像url
     */
    public String getFavicon() {
        return favicon;
    }

    /**
     * 设置头像url
     *
     * @param favicon 头像url
     */
    public void setFavicon(String favicon) {
        this.favicon = favicon == null ? null : favicon.trim();
    }
}