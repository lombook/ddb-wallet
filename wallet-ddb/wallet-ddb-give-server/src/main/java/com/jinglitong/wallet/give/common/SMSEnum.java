/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.give.common;


public enum SMSEnum {

    FORGET("1"),REGIST("0"),MODPASS("2"),BINDDEVICE("3"),EXPORT("4");

    private String type;

    private SMSEnum(String type){
        this.type = type;
    }

    @Override
    public  String toString(){
        return this.type.toString();
    }
}
