package com.jinglitong.springshop.controller;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Customer;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.md5.Hash;
import com.jinglitong.springshop.servcie.CustomerService;
import com.jinglitong.springshop.servcie.IntegralCustRecordService;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerController
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 17:00
 * @Version 1.0
 **/
@Api(value = "消费者信息",tags = "消费者信息 API")
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private IntegralCustRecordService  integralCustRecordService;


    @ApiOperation(value = " 获取用户信息")
    @RequestMapping(value = "/getCustomer", method = RequestMethod.POST)
    public ShopRespose getCustomer(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token){
        LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
        Customer customer = customerService.getCustomer(info.getZid());
        return new ShopRespose(IConstants.SUCCESS,"查询成功",customer);
    }

    


}
