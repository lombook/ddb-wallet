package com.jinglitong.wallet.api.model.logic;

import com.jinglitong.wallet.api.model.Admin;

/**
 * Created by fan on 2018/7/19.
 */
public class AdminVO extends Admin{
    private String walletName;

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
