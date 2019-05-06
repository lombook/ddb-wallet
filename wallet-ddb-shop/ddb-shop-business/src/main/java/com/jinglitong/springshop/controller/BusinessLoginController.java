package com.jinglitong.springshop.controller;

import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.service.BusinessLoginService;
import com.jinglitong.springshop.vo.request.LoginParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName BusinessLoginController
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/14 13:44
 * @Version 1.0
 **/
@Api(value = "商铺登录接口",tags = "商铺登录接口 API")
@RestController
@RequestMapping("/business")
public class BusinessLoginController {

    @Autowired
    private BusinessLoginService loginService;

    @ApiOperation(value = " 商铺管理员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ShopRespose login(@ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                             @RequestBody @Valid LoginParamVo vo){
        if(vo==null){
            return new ShopRespose(IConstants.FAILED,"登录名和密码为空");
        }
        return loginService.login(vo.getUserName(), vo.getPassword(),clientId);
    }

}
