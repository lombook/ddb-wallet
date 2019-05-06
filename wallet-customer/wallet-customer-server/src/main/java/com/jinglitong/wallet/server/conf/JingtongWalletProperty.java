package com.jinglitong.wallet.server.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jingtong")
@PropertySource(value = "classpath:config/jingtongwallet.properties")
@Component
public class JingtongWalletProperty {

    private String url;

    private String balances;

    private String walletnew;

    private String gotopayment;

    private String paymentinfo;

    private String paymenthistory;

    private String regTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBalances() {
        return balances;
    }

    public void setBalances(String balances) {
        this.balances = balances;
    }

    public String getWalletnew() {
        return walletnew;
    }

    public void setWalletnew(String walletnew) {
        this.walletnew = walletnew;
    }

    public String getGotopayment() {
        return gotopayment;
    }

    public void setGotopayment(String gotopayment) {
        this.gotopayment = gotopayment;
    }

    public String getPaymentinfo() {
        return paymentinfo;
    }

    public void setPaymentinfo(String paymentinfo) {
        this.paymentinfo = paymentinfo;
    }

    public String getPaymenthistory() {
        return paymenthistory;
    }

    public void setPaymenthistory(String paymenthistory) {
        this.paymenthistory = paymenthistory;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
}
