package com.jinglitong.wallet.api.model;

public class MiniSellerCoin {
    /**
     * 币种编号
     */
    private String coinCode;




    /**
     * 币
     */
    private String coinCurrency;

    /**
     * 支付地址
     */
    private String sellerAddress;

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }


    public String getCoinCurrency() {
        return coinCurrency;
    }

    public void setCoinCurrency(String coinCurrency) {
        this.coinCurrency = coinCurrency;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }
}
