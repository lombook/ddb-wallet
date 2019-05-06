package com.jinglitong.wallet.api.model.walletVo;

public class JingtongUserInfo {
    private String userName;
    private String phone;
    private String address;
    private String remark;

    private String swtTotal;
    private String swtFreeze;
    private String swtCanUse;

    private String cntTotal;
    private String cntFreeze;
    private String cntCanUse;
    private String regTime;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSwtTotal() {
        return swtTotal;
    }

    public void setSwtTotal(String swtTotal) {
        this.swtTotal = swtTotal;
    }

    public String getSwtFreeze() {
        return swtFreeze;
    }

    public void setSwtFreeze(String swtFreeze) {
        this.swtFreeze = swtFreeze;
    }

    public String getSwtCanUse() {
        return swtCanUse;
    }

    public void setSwtCanUse(String swtCanUse) {
        this.swtCanUse = swtCanUse;
    }

    public String getCntTotal() {
        return cntTotal;
    }

    public void setCntTotal(String cntTotal) {
        this.cntTotal = cntTotal;
    }

    public String getCntFreeze() {
        return cntFreeze;
    }

    public void setCntFreeze(String cntFreeze) {
        this.cntFreeze = cntFreeze;
    }

    public String getCntCanUse() {
        return cntCanUse;
    }

    public void setCntCanUse(String cntCanUse) {
        this.cntCanUse = cntCanUse;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
}
