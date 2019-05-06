package com.jinglitong.wallet.ddbapi.model.logic;

import java.io.Serializable;

/**
 * @ClassName SecExpandDicInfo
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/28 17:43
 * @Version 1.0
 **/
public class SecExpandDicInfo implements Serializable {
    private static final long serialVersionUID = 199L;
    private String zid;
    private String ruleId;
    private int inteAmount;
    private String inteId;
    private String realName;
    private String realCname;

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public int getInteAmount() {
        return inteAmount;
    }

    public void setInteAmount(int inteAmount) {
        this.inteAmount = inteAmount;
    }

    public String getInteId() {
        return inteId;
    }

    public void setInteId(String inteId) {
        this.inteId = inteId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealCname() {
        return realCname;
    }

    public void setRealCname(String realCname) {
        this.realCname = realCname;
    }

    @Override
    public String toString() {
        return "SecExpandDicInfo{" +
                "zid='" + zid + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", inteAmount=" + inteAmount +
                ", inteId='" + inteId + '\'' +
                ", realName='" + realName + '\'' +
                ", realCname='" + realCname + '\'' +
                '}';
    }
}
