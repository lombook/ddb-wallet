package com.jinglitong.springshop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @ClassName LoginParamVo
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/9 13:59
 * @Version 1.0
 **/

@ApiModel(value = "登录信息")
public class LoginVo {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户主键", name = "zid")
    private String zid;
    @ApiModelProperty(value = "用户名", name = "userName")
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机", name = "phone")
    private String phone;

    /**
     * 带区域码手机号
     */
    @ApiModelProperty(value = "带区域码手机号", name = "allPhone")
    private String allPhone;

    /**
     * 是否有效 1：有效 0：无效
     */
    @ApiModelProperty(value = "是否有效 1：有效 0：无效", name = "state")
    private Boolean state;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码", name = "inviteCode")
    private String inviteCode;

    /**
     * 国家id
     */
    @ApiModelProperty(value = "国家id", name = "countryId")
    private Integer countryId;


    /**
     * 语言
     */
    @ApiModelProperty(value = "语言", name = "language")
    private String language;

    /**
     * 来源 :0手机app
     */
    @ApiModelProperty(value = "来源 :0手机app", name = "origin")
    private Integer origin;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    @ApiModelProperty(value = "上次登录时间", name = "lastLoginTime")
    private Date lastLoginTime;

    /**
     * 认证状态:0 未认证；1 已认证
     */
    @ApiModelProperty(value = "认证状态:0 未认证；1 已认证", name = "certificateState")
    private Boolean certificateState;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    /**
     * 注册方式：0手机号1邮箱
     */
    @ApiModelProperty(value = "注册方式：0手机号1邮箱", name = "regWay")
    private Integer regWay;

    /**
     * 自己的的邀请码
     */
    @ApiModelProperty(value = "自己的的邀请码", name = "selfInvite")
    private String selfInvite;

    /**
     * 树形等级
     */
    @ApiModelProperty(value = "树形等级", name = "treeLevel")
    private Integer treeLevel;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickName")
    private String nickName;
    @ApiModelProperty(value = "token", name = "token")
    private String token;

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAllPhone() {
        return allPhone;
    }

    public void setAllPhone(String allPhone) {
        this.allPhone = allPhone;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getCertificateState() {
        return certificateState;
    }

    public void setCertificateState(Boolean certificateState) {
        this.certificateState = certificateState;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRegWay() {
        return regWay;
    }

    public void setRegWay(Integer regWay) {
        this.regWay = regWay;
    }

    public String getSelfInvite() {
        return selfInvite;
    }

    public void setSelfInvite(String selfInvite) {
        this.selfInvite = selfInvite;
    }

    public Integer getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
