package com.jinglitong.wallet.api.model.walletVo;

import java.util.ArrayList;
import java.util.List;

public class JingtongPayment {

    /**
     * 发起账号
     */
    private String source;

    /**
     * 目标账号
     */
    private String destination;

    /**
     * 支付金额
     */
    private PaymentAmount amount;

    /**
     * 支付选择的key，可选
     */
    private String choice;

    /**
     * 支付的备注，String数组，可选
     */
    private List<String> memos = new ArrayList<>();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public List<String> getMemos() {
        return memos;
    }

    public void setMemos(List<String> memos) {
        this.memos = memos;
    }

    public PaymentAmount getAmount() {
        return amount;
    }

    public void setAmount(PaymentAmount amount) {
        this.amount = amount;
    }
}
