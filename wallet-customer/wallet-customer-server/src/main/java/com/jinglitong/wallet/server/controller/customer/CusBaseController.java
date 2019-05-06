package com.jinglitong.wallet.server.controller.customer;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.IAppIdModel;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;

public class CusBaseController {

    @Value("${app.id}")
    private String appId;

    /**
     * 设置android ios appId
     * @param appIdModel
     */
    public  void setGlobalCustomAppId(IAppIdModel appIdModel){
        Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
        if(!(customer instanceof Customer)){
            throw new RuntimeException("用户没有登录");
        }
        appIdModel.setAppId(customer.getAppId());

    }

    public  void setGlobalFromAppId(IAppIdModel appIdModel){
        Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
        if(!(customer instanceof Customer)){
            throw new RuntimeException("用户没有登录");
        }
        appIdModel.setAppId(appIdModel.getAppId());

    }
}
