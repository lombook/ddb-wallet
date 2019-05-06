package com.jinglitong.springshop.vo.request;


import lombok.Data;

@Data
public class SmsVO {

    private String phone;
    //业务场景
    private String type;
    
}
