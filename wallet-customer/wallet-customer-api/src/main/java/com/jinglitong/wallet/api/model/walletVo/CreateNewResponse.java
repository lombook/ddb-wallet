package com.jinglitong.wallet.api.model.walletVo;

public class CreateNewResponse {

    private String success;
    private String status_code;

    private WalletResponse wallet;

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

    public WalletResponse getWallet() {
        return wallet;
    }

    public void setWallet(WalletResponse wallet) {
        this.wallet = wallet;
    }

    public class WalletResponse{
        private String secret;
        private String address;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
