package com.jinglitong.wallet.api.model.logic;

public class LWalletVO extends ErrorCode {
    //钱包地址
    private String publicAddress;

    //私钥
    private String secretKey;

    //支付密码
    private String payPasswd;

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPayPasswd() {
        return payPasswd;
    }

    public void setPayPasswd(String payPasswd) {
        this.payPasswd = payPasswd;
    }
}
