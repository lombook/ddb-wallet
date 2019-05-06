/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.server.common.shiro;


import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.server.service.AdminService;
import com.jinglitong.wallet.server.service.MenuService;
import com.jinglitong.wallet.server.service.ShiroUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Set;

public class AdminShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ShiroUserService shiroUserService;


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.info("后台登录：AdminShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String username = (String)token.getPrincipal();
        String appId = ((CustomerAuthenticationToken)token).getAppId();
        Admin admin = shiroUserService.findByUsername(username,appId);
        if(admin == null){
            throw new UnknownAccountException();
        }
        if(null == admin.getState() || false == admin.getState()) {
            throw new LockedAccountException(); //帐号锁定
        }


        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                admin, //用户名
                admin.getPassword(), //密码
                ByteSource.Util.bytes(admin.getSalt()),
                admin.getUsername()  //realm name
        );

        return authenticationInfo;
    }



    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthenticationException{

        logger.info("后台权限校验-->AdminShiroRealm.doGetAuthorizationInfo()");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Admin admin  = (Admin)principals.getPrimaryPrincipal();
        Set<String> menus = null;
        if(admin.getIsSystem().equals(true)) {
            menus = shiroUserService.getAllMenuCode();
        }else{
            menus = shiroUserService.findMenuCodeByUserId(admin.getAdminId());
        }
        authorizationInfo.setStringPermissions(menus);
        return authorizationInfo;
    }


    public void clearCachedAuthorizationInfo() {
        PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }


    public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }
}
