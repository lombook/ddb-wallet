package com.jinglitong.wallet.api.model.walletVo;

public class PaymentRequest {
    /**
     * 支付方私钥
     */
    private String secret;

    /**
     * 此次请求的交易单号，交易单需要唯一
     */
    private String client_id;

    private JingtongPayment payment;

    public JingtongPayment getPayment() {
        return payment;
    }

    public void setPayment(JingtongPayment payment) {
        this.payment = payment;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
}
