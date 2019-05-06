package com.jinglitong.springshop.enumeration;

/**
 * @ClassName IntegralCustRecordTypeEnum
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/27 10:42
 * @Version 1.0
 **/
public enum IntegralCustRecordTypeEnum {
    TYPE_1(1,"增加"),TYPE_2(2,"减少");

    private int type;
    private String name;

    IntegralCustRecordTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
