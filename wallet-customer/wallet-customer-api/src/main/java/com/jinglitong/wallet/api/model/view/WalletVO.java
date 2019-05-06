package com.jinglitong.wallet.api.model.view;

import java.util.Date;

import com.jinglitong.wallet.api.model.IAppIdModel;

public class WalletVO  implements IAppIdModel{
    private String custId;
    private String chainId;
    private String passwd;
    private String secret;
    private String prompt;
    private String walletName;
    private String walletId;
    private String oldpasswd;
    private String address;
    private String mnemonic;
    private String smsCode;
    private String countryCode;
    /**
     * 数据分析
     * @return
     */
    //主键id
    private String id;
    //创建导入时间
    private String timesStamp;
    //用户名
    private String account;
    //新建类型(0新建/1导入)
    private String createType;
    //经纬度
    private String gps;
    //设备ID
    private String deviceId;
    //钱包编码
    private String appId;
    //创建时间
    private Date createTime;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getOldpasswd() {
        return oldpasswd;
    }

    public void setOldpasswd(String oldpasswd) {
        this.oldpasswd = oldpasswd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimesStamp() {
        return timesStamp;
    }

    public void setTimesStamp(String timesStamp) {
        this.timesStamp = timesStamp;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
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
