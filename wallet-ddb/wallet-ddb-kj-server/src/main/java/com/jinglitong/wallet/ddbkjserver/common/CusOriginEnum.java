package com.jinglitong.wallet.ddbkjserver.common;

public enum CusOriginEnum {


    FORMAPP("app", 0),
    FORMADMIN("后台 ", 1),
    ;

    private String name;//结果编码
    private Integer value;//结果信息

    CusOriginEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}