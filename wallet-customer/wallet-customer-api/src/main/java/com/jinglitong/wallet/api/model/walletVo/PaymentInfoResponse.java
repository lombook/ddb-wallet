package com.jinglitong.wallet.api.model.walletVo;

import java.util.ArrayList;
import java.util.List;

public class PaymentInfoResponse extends BaseWalletResponse{
    private String date;

    private String hash;

    /**
     * 支付类型,sent或received
     */
    private String type;


    private String fee;

    private String result;

    private List<String> memos = new ArrayList<>();
    /**
     * 交易对家
     */
    private String counterparty;

    private PaymentAmount amount;

    private List<TradeEffects> effects;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getMemos() {
        return memos;
    }

    public void setMemos(List<String> memos) {
        this.memos = memos;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public PaymentAmount getAmount() {
        return amount;
    }

    public void setAmount(PaymentAmount amount) {
        this.amount = amount;
    }

    public List<TradeEffects> getEffects() {
        return effects;
    }

    public void setEffects(List<TradeEffects> effects) {
        this.effects = effects;
    }
}
