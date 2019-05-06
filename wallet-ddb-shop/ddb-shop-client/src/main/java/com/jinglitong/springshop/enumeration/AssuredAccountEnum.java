package com.jinglitong.springshop.enumeration;

/**
 * @ClassName AssuredAccountEnum
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/27 10:31
 * @Version 1.0
 **/
public enum AssuredAccountEnum {
    XJD("virtual_wallet","现金袋指定转入账户"),
    DJB("virtual_wallet","袋金币指定转入账户"),
    BF("virtual_wallet","宝分指定转入账户"),
    XB("virtual_wallet","现贝币指定转入账户"),
    WB("virtual_wallet","物贝指定转入账户");

    private String account;
    private String desc;

    AssuredAccountEnum(String account, String desc) {
        this.account = account;
        this.desc = desc;
    }

    public String getAccount() {
        return account;
    }

    public String getDesc() {
        return desc;
    }
}
