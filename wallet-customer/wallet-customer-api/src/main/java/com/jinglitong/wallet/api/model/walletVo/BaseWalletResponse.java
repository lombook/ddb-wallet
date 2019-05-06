package com.jinglitong.wallet.api.model.walletVo;

public class BaseWalletResponse {

    private String success;
    private String status_code;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
}
