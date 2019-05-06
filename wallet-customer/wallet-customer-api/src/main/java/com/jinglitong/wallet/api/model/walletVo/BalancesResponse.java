package com.jinglitong.wallet.api.model.walletVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 余额信息
 */
public class BalancesResponse extends BaseWalletResponse{
    private Integer sequence;

    private List<WalletBalance>  balances = new ArrayList<>();

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<WalletBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<WalletBalance> balances) {
        this.balances = balances;
    }


}
