package com.jinglitong.springshop.controller;


import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.servcie.CustomerService;
import com.jinglitong.springshop.servcie.ReceiverService;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.ReceiverVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api(value = "收货地址信息",tags = "收货地址信息API")
@RestController
@RequestMapping("/receiver")
public class ReceiverController {
	
	@Autowired
    private ReceiverService receiverService;
	
	@Autowired
    private CustomerService customerService;
	
	//获得某个用户的收货列表
	@ResponseBody
    @RequestMapping(value = "/getReceivers",method = RequestMethod.POST)
    public ShopRespose<?> getReceivers(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
    		@ApiParam(name = "clientId", value = "登录clientId", required = false)@RequestHeader(name = "clientId", required = false, defaultValue = "") String clientId){
		if(StringUtils.isNotEmpty(token)){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            if(info == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            return receiverService.getList(info.getZid());
        }
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
		
	}
	//添加收货地址
	@ResponseBody
    @RequestMapping(value = "/addReceiver",method = RequestMethod.POST)
    public ShopRespose<?> addReceiver(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
    		@RequestBody @Valid ReceiverVo receiver){
		/* public ShopRespose<?> addReceiver(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
		    		@ApiParam(name = "areaId", value = "地区id", required = true)@RequestParam(name = "areaID", required = true, defaultValue = "")Integer areaID,
		    		@ApiParam(name = "consignee", value = "收货人姓名", required = true)@RequestParam(name = "consignee", required = true, defaultValue = "")String consignee,
		    		@ApiParam(name = "address", value = "详细地址", required = true)@RequestParam(name = "address", required = true, defaultValue = "")String address,
		    		@ApiParam(name = "zipCode", value = "邮编", required = true)@RequestParam(name = "zipCode", required = true, defaultValue = "")String zipCode,
		    		@ApiParam(name = "phone", value = "手机号", required = true)@RequestParam(name = "phone", required = true, defaultValue = "")String phone,
		    		@ApiParam(name = "isdefault", value = "是否默认", required = true)@RequestParam(name = "isdefault", required = true, defaultValue = "")String isdefault){*/
		
		if(StringUtils.isNotEmpty(token)){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            if(info == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            String b = "0";
            if(receiver.getIsdefault().equals(1)){
            	b = "1";
            }
           return receiverService.add(info.getZid(),receiver.getAreaId(),receiver.getConsignee(),receiver.getAddress(),receiver.getZipcode(),receiver.getPhone(),b);   
        }
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}

	}
	
	//更新收货地址
	@ResponseBody
    @RequestMapping(value = "/updateReceiver",method = RequestMethod.POST)
    public ShopRespose<?> updateReceiver(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
    		@RequestBody @Valid ReceiverVo receiver){
		if(StringUtils.isNotEmpty(token)){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            if(info == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            String b = "0";
            if(receiver.getIsdefault().equals(1)){
            	b = "1";
            }
            return receiverService.update(receiver.getZid(),receiver.getAreaId(),receiver.getConsignee(),receiver.getAddress(),receiver.getZipcode(),receiver.getPhone(),b);
        }else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
	}
	
	//删除收货地址
	@ResponseBody
    @RequestMapping(value = "/delReceiver",method = RequestMethod.POST)
    public ShopRespose<?> delReceiver(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
    		@RequestBody @Valid ReceiverVo receiver){
		if(StringUtils.isNotEmpty(token)){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            if(info == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            return receiverService.del(info.getZid(),receiver.getZid());
        }else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
	}
}
