/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.ddbkjserver.common;


public enum CustLevelTypeEnum {

    NEWCREATE(1,"新创股东"),PARTNERSHIP(2,"合伙股东"),STARTING(3,"创业股东"),STARTUP(4,"初创股东");

    private int type;

    private String value;

    CustLevelTypeEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustLevelTypeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return NEWCREATE;
            case 2:
                return PARTNERSHIP;
            case 3:
                return STARTING;
            case 4:
                return STARTUP;
            default:
                return null;
        }
    }
}
