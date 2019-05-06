package com.jinglitong.springshop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @ClassName BusinessVo
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/14 15:19
 * @Version 1.0
 **/
@ApiModel(value = "商铺管理员登录信息")
public class BusinessVo {
    @ApiModelProperty(value = "管理员主键", name = "zid")
    private String zid;
    @ApiModelProperty(value = "balance", name = "balance")
    private BigDecimal balance;
    @ApiModelProperty(value = "email", name = "email")
    private String email;
    @ApiModelProperty(value = "frozenfund", name = "frozenfund")
    private BigDecimal frozenfund;
    @ApiModelProperty(value = "手机", name = "mobile")
    private String mobile;
    @ApiModelProperty(value = "name", name = "name")
    private String name;
    @ApiModelProperty(value = "phone", name = "phone")
    private String phone;
    @ApiModelProperty(value = "username", name = "username")
    private String username;
    @ApiModelProperty(value = "token", name = "token")
    private String token;

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getFrozenfund() {
        return frozenfund;
    }

    public void setFrozenfund(BigDecimal frozenfund) {
        this.frozenfund = frozenfund;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
