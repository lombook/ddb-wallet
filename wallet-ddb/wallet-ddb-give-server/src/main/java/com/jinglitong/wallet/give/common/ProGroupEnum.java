/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.give.common;


public enum ProGroupEnum {

    IMG("about","关于"),TXT("devicedata","数据"),ASC("handleName","类名称");

    ;
    private String name;
    private String value;
    private ProGroupEnum(String name , String value ){
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
