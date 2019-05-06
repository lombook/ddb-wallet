package com.jinglitong.wallet.ddbserver.controller.customer;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.IAppIdModel;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;

public class CusBaseController {

    @Value("${ddb_app_id}")
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
}
