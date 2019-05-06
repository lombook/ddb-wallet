package com.jinglitong.wallet.ddbkjserver.common;

public enum AppTypeEnum {


    ANDROID("android", "安卓机"),
    IOS("ios ", "苹果机"),
    ;

    private String name;//结果编码
    private String value;//结果信息

    AppTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}