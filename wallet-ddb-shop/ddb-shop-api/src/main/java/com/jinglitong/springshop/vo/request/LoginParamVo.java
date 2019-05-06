package com.jinglitong.springshop.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jetbrains.annotations.NotNull;

/**
 * @ClassName LoginParamVo
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/15 14:43
 * @Version 1.0
 **/
@ApiModel(value = "登录body里的参数")
public class LoginParamVo {
    @NotNull
    @ApiModelProperty(value = "用户名")
    private String userName;
    @NotNull
    @ApiModelProperty(value = "密码，加密方式先encode再AES的加密")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
