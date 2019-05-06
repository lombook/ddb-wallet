/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.reportserver.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;


public class CustomerAuthenticationToken extends UsernamePasswordToken {

    private String captcha;

    private String loginType;

    private String loginForm;

    private String appId;

    public CustomerAuthenticationToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }
    public CustomerAuthenticationToken(String username, String password, String appId,boolean rememberMe) {
        super(username, password, rememberMe);
        this.appId = appId;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginForm() {
        return loginForm;
    }

    public void setLoginForm(String loginForm) {
        this.loginForm = loginForm;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
