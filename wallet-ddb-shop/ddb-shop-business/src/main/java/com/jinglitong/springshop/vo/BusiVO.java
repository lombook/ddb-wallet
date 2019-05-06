package com.jinglitong.springshop.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName BusiVO
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/4/8 14:49
 * @Version 1.0
 **/
@Data
public class BusiVO {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String password2;
    private String email;
    private String name;
    private String phone;
    @NotNull
    private String storeId;
    private String remark;
    private Integer state;

}
