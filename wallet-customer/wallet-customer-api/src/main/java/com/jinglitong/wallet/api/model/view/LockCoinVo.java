package com.jinglitong.wallet.api.model.view;

public class LockCoinVo extends PaymentVO{

    private String lockRuleId;

    public String getLockRuleId() {
        return lockRuleId;
    }

    public void setLockRuleId(String lockRuleId) {
        this.lockRuleId = lockRuleId;
    }
}
