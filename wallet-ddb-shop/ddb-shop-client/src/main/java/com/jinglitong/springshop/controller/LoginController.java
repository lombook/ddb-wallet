package com.jinglitong.springshop.controller;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.servcie.CartService;
import com.jinglitong.springshop.servcie.LoginService;
import com.jinglitong.springshop.vo.request.CustRegVO;
import com.jinglitong.springshop.vo.request.LoginParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

import javax.validation.Valid;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/9 14:18
 * @Version 1.0
 **/
@Api(value = "用登录接口",tags = "用登录接口 API")
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private CartService cartService;


    @ApiOperation(value = " 用户登录")
    @RequestMapping( value= "/login", method = RequestMethod.POST)
    public ShopRespose login(@ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                             @RequestBody @Valid LoginParamVo vo,@ApiParam(name = "appId", value = "IOS参数", required = false)@RequestHeader(name = "appId", required = false,  defaultValue = "")String appId){
        if(vo==null){
            return new ShopRespose(IConstants.FAILED,"登录名和密码为空");
        }
        ShopRespose login = loginService.login(vo.getUserName(), vo.getPassword(), clientId,appId);
        //用户购物车同步
        //if(IConstants.SUCCESS.equals(login.getCode())){
        //    Integer integer = cartService.synsClientIdToCustomerCart(vo.getUserName(), clientId);
        //}
        return login;
    }
    
	@ApiOperation(value = " 用户登出")
	@RequestMapping(value = "/loginOut", method = RequestMethod.POST)
	public ShopRespose loginOut(
			@ApiParam(name = "clientId", value = "客户端Id", required = true) @RequestHeader(name = "clientId", required = true, defaultValue = "") String clientId,
			@RequestBody @Valid LoginParamVo vo) {
		ShopRespose login = loginService.loginOut(vo.getUserName(), clientId);
		return login;
	}

}
