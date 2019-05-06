package com.jinglitong.wallet.api.model.view;

import javax.validation.constraints.NotEmpty;

import com.jinglitong.wallet.api.model.IAppIdModel;

import java.util.Date;

public class CustRegVO implements IAppIdModel{

    private Integer countryId;
    private String account;
    private String passwd;
    private String InviteCode;
    private String smsCode;
    private String countryCode;
    /**
     * 数据收集

     */
    //主键id
    private Long id;
    //登陆时间
    private String timesTamp;
    //经纬度
    private String gps;
    //运营商(电信，联通，移动 )
    private String mno;
    //设备操作系统及版本号
    private String deviceos;
    //设备生产厂商
    private String firm;
    //设备ID
    private String deviceId;
    //钱包编码
    @NotEmpty(message ="appId为空")
    private String appId;
    //创建时间
    private Date createTime;




    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getInviteCode() {
        return InviteCode;
    }

    public void setInviteCode(String inviteCode) {
        InviteCode = inviteCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    /**
     * 数据收集
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(String timesTamp) {
        this.timesTamp = timesTamp;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getDeviceos() {
        return deviceos;
    }

    public void setDeviceos(String deviceos) {
        this.deviceos = deviceos;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
