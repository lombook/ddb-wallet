package com.jinglitong.springshop.entity;

import java.util.List;

/**
 * @ClassName SysUser
 * @Description TODO
 * @Author zili.zong
 * @Date 2018/11/22 11:37
 * @Version 1.0
 **/
public class SysUser {
    private long userId;
    private String account;
    private String pwd;
    private List<String> roles;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
