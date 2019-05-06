package com.jinglitong.wallet.api.model.walletVo;

import java.util.List;

public class JingtongTransaction extends BaseWalletResponse{

    private JingtongMarker marker;

    private List<PaymentInfoResponse> transactions;

    public JingtongMarker getMarker() {
        return marker;
    }

    public void setMarker(JingtongMarker marker) {
        this.marker = marker;
    }

    public List<PaymentInfoResponse> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<PaymentInfoResponse> transactions) {
        this.transactions = transactions;
    }
}
