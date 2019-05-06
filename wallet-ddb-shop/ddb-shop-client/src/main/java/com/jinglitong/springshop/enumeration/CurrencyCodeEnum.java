package com.jinglitong.springshop.enumeration;

/**
 * @ClassName CurrencyCodeEnum
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/27 10:52
 * @Version 1.0
 **/
public enum CurrencyCodeEnum {
    CODE_RMB("CNY","人民币"),
    CODE_AY("AUD","澳元"),
    CODE_XJD("xjd_all","现金袋"),
    CODE_DJB("djb_all","代金币"),
    CODE_WB("wubei_all","代金币"),
    CODE_XB("xianbei_all","代金币"),
    CODE_BF("baofen_all","代金币");
    private String code;
    private String name;

    CurrencyCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
