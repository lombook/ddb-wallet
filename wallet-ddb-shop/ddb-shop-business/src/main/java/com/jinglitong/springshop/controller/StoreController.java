package com.jinglitong.springshop.controller;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.service.StoreService;
import com.jinglitong.springshop.vo.request.PageVo;
import com.jinglitong.springshop.vo.request.StoreAddVo;
import com.jinglitong.springshop.vo.response.StoreVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/business/store")
@Api(value = "商铺API", tags = "商铺API")
public class StoreController {
	
	@Autowired
	private StoreService storeService;
	
	@ApiOperation(value = "可用商铺列表")
	@ResponseBody
	@RequestMapping(value = "/getStoreList", method = RequestMethod.POST)
	public ShopRespose<Object> getCustomerList(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
			@RequestBody PageVo vo) 
	{
		if(StringUtils.isNotEmpty(token)){
			String userName = JwtTokenUtils.getUsername(token);
            if(userName == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            return new ShopRespose(IConstants.SUCCESS,"成功",storeService.getEnabledStores(vo));
        }
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
	}
	
	@ApiOperation(value = "可用商铺列表")
	@ResponseBody
	@RequestMapping(value = "/getStores", method = RequestMethod.POST)
	public ShopRespose<Object> getStoreList(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token) 
	{
		if(StringUtils.isNotEmpty(token)){
			String userName = JwtTokenUtils.getUsername(token);
            if(userName == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            return new ShopRespose(IConstants.SUCCESS,"成功",storeService.getEnabledStores());
        }
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
	}
	
	@ApiOperation(value = "添加商家")
	@ResponseBody
	@RequestMapping(value = "/addStore", method = RequestMethod.POST)
	public ShopRespose<Object> addStore(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
			@RequestBody StoreAddVo vo) 
	{
		if(StringUtils.isNotEmpty(token)){
			String userName = JwtTokenUtils.getUsername(token);
            if(userName == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            try{
            	return storeService.addStore(vo);
            }catch(DataAccessException   e){
            	SQLException exception = (SQLException)e.getCause();     	
            	if(exception.getErrorCode() == 1062){
            		return new ShopRespose<Object>(ErrorEnum.ERROR_5602);
            	}
            	return new ShopRespose<Object>(ErrorEnum.ERROR_5601);
            }
              
        }
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
	}
	
	@ApiOperation(value = "获得商家信息")
	@ResponseBody
	@RequestMapping(value = "/getStore", method = RequestMethod.POST)
	public ShopRespose<Object> getStore(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
			@RequestBody StoreAddVo vo) 
	{
		if(StringUtils.isNotEmpty(token)){
			String userName = JwtTokenUtils.getUsername(token);
            if(userName == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            StoreVo StoreVo = storeService.getStore(vo.getZid());
            return new ShopRespose(IConstants.SUCCESS,"成功",StoreVo);     
        }
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
	}
	
	@ApiOperation(value = "修改商家信息")
	@ResponseBody
	@RequestMapping(value = "/updateStore", method = RequestMethod.POST)
	public ShopRespose<Object> updateStore(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
			@RequestBody StoreAddVo vo) 
	{
		if(StringUtils.isNotEmpty(token)){
			String userName = JwtTokenUtils.getUsername(token);
            if(userName == null){
            	//未登录
            	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
            }
            try{
            	int res = storeService.updateStore(vo);
                if(res == 1){
                	return new ShopRespose(IConstants.SUCCESS,"成功");
                }
                else{
                	return new ShopRespose<Object>(ErrorEnum.ERROR_5601);
                } 
            }catch(DataAccessException   e){
            	SQLException exception = (SQLException)e.getCause();     	
            	if(exception.getErrorCode() == 1062){
            		return new ShopRespose<Object>(ErrorEnum.ERROR_5602);
            	}
            	return new ShopRespose<Object>(ErrorEnum.ERROR_5606);
            }
        }
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
	}
	
	
	
	

}
