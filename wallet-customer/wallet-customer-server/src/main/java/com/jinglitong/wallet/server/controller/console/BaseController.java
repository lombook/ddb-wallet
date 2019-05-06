package com.jinglitong.wallet.server.controller.console;


import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.IAppIdModel;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;

public class BaseController {


    @Value("${app.id}")
    private String appId;





    /**
     * 设置 后台 appId
     * @param appIdModel
     */
    public  void setGlobalAdminAppId(IAppIdModel appIdModel){
        setGlobalAdminAppId(appIdModel,false);
    }


    /**
     * 设置 后台 appId
     * @param appIdModel
     * @param needAllDataIfAdmin 是否需要全部数据,仅仅超级管理员
     */
    public  void setGlobalAdminAppId(IAppIdModel appIdModel,boolean needAllDataIfAdmin){
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        if(!(admin instanceof Admin)){
            throw new RuntimeException("用户没有登录");
        }


        if(admin.getAppId().equals(appId) && admin.getIsSystem()) { //超级管理员
            if(needAllDataIfAdmin){
                appIdModel.setAppId(null);
            }
        }else{  //普通用户
            appIdModel.setAppId(admin.getAppId());
        }
    }



}
