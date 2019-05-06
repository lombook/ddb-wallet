package com.jinglitong.wallet.api.model.view;

/**
 * Created by fan on 2018/6/4.
 */
public class Rule {
    private Integer lockDays;

    private Integer lockRate;

    private Boolean ruleStatus;

    public Integer getLockDays() {
        return lockDays;
    }

    public void setLockDays(Integer lockDays) {
        this.lockDays = lockDays;
    }

    public Integer getLockRate() {
        return lockRate;
    }

    public void setLockRate(Integer lockRate) {
        this.lockRate = lockRate;
    }

    public Boolean getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(Boolean ruleStatus) {
        this.ruleStatus = ruleStatus;
    }
}
