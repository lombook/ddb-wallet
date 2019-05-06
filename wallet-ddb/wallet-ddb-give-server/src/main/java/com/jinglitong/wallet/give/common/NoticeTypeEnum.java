/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.give.common;


public enum NoticeTypeEnum {

    IMG("img","图片型"),TXT("txt","文本类型"),MULTI("multi","多类型");

    ;
    private String name;
    private String value;
    private NoticeTypeEnum( String name , String value ){
        this.name = name ;
        this.value = value ;
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
