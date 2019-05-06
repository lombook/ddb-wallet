package com.jinglitong.wallet.api.model.view;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class SmsVO {
    private String custId;

    private String phone;
    //业务场景
    private String type;
    //国家区号
    private String countryCode;

    private String appId;


    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
