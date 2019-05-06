package com.jinglitong.springshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.servcie.CustomerService;
import com.jinglitong.springshop.servcie.OrdershippingService;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.response.OrderShopingVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/ordershipping")
@Api(value = "订单物流 API", tags = "订单物流 API")
public class OrdershippingController {
	
	@Autowired
    private CustomerService customerService;
	
	@Autowired
    private OrdershippingService orderShippingService;
	
	@ApiOperation(value = "获取物流详情")
	@RequestMapping(value = "/getShippingDetail", method = RequestMethod.POST)
	public ShopRespose<Object> cancelOrder(
			@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
			@RequestBody OrderShopingVo vo) {
		LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
		if(info == null || info.getZid() == null){
        	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
        }
		return new ShopRespose<Object>(IConstants.SUCCESS, "成功", orderShippingService.getOrdershippingDetail(vo.getZid()));
	}
}
