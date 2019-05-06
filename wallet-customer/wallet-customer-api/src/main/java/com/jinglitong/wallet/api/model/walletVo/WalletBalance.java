package com.jinglitong.wallet.api.model.walletVo;

public class WalletBalance {

    /**
     * 余额
     */
    private String value;
    /**
     * 货币名称，三个字母或20字节的货币
     */
    private String currency;
    /**
     * 货币发行方
     */
    private String issuer;
    /**
     * 冻结的金额
     */
    private String freezed;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getFreezed() {
        return freezed;
    }

    public void setFreezed(String freezed) {
        this.freezed = freezed;
    }
}
