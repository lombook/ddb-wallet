package com.jinglitong.springshop.controller;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Brand;
import com.jinglitong.springshop.entity.Business;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.entity.admin.ShopAdmin;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.service.BrandService;
import com.jinglitong.springshop.service.BusinessLoginService;
import com.jinglitong.springshop.service.CurrencyService;
import com.jinglitong.springshop.service.ProductService;
import com.jinglitong.springshop.vo.request.CateTreeVo;
import com.jinglitong.springshop.vo.request.ProductAddParam;
import com.jinglitong.springshop.vo.request.ProductToUpdateParam;
import com.jinglitong.springshop.vo.response.ProductListVo;
import com.jinglitong.springshop.vo.response.ProductSeeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: ProductUpperShelfController.java
 * 作        者: yxl 2019年1月10日
 * 创建时间: 2019年1月10日
 * 功能说明:后端商品列表、上架
 */
@RestController
@RequestMapping("/business/product/upper")
@Api(value = "web商品列表",tags = "web商品 API")
public class ProductUpperShelfController {

	@Autowired
	private ProductService ProductService;
	
	@Autowired
	private BusinessLoginService businessLoginService;
	
	@Autowired
	private com.jinglitong.springshop.service.ProductCategoryService ProductCategoryService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private BrandService brandService;
	
	@ApiOperation(value = "商品列表")
    @RequestMapping(value ="/getProductList" , method = RequestMethod.POST)
	public ShopRespose getProductList(@RequestBody ProductListVo vo) {
		ShopRespose respose = new ShopRespose<>();
		PageInfo<ProductListVo> info =ProductService.getProductList(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}
	
	@ApiOperation(value = "添加商品")
    @RequestMapping(value ="/addProduct" , method = RequestMethod.POST)
	public ShopRespose getCategoryList() {
		ShopRespose respose = new ShopRespose<>();
		Map<String, Object> map = new HashMap<>();
		List<CateTreeVo> caList = ProductCategoryService.selectProductCategoryList();
		List<Currency> cuList = currencyService.getAllList();
		List<Brand> braList = brandService.getAllList();
		map.put("category", caList);
		map.put("currency", cuList);
		map.put("brand", braList);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(map);
        respose.setCode(IConstants.SUCCESS);
        return respose;
	}
	@ApiOperation(value = "保存商品")
    @RequestMapping(value ="/saveProduct" , method = RequestMethod.POST)
	public ShopRespose saveProduct(@ApiParam(name = "X-Auth-Token", value = "登录token", required = true)@RequestHeader(name = "X-Auth-Token", required = true, defaultValue = "") String token,@RequestBody ProductAddParam param) {
		ShopRespose respose = new ShopRespose<>();
		ShopAdmin business = businessLoginService.getBusinessInfo(JwtTokenUtils.getUsername(token));
		if(business == null) {
			respose = new ShopRespose<>(ErrorEnum.ERROR_401);
			return respose;
		}
		//business.setUsername("test");
		try {
			respose =ProductService.productSave(param,business);
		} catch (Exception e) {
			e.printStackTrace();
			respose.setCode(ErrorEnum.ERROR_4002.getCode());
    		respose.setMessage(ErrorEnum.ERROR_4002.getMsg());
		}
		 
		 return respose;
	}
	
	@ApiOperation(value = "下架商品")
    @RequestMapping(value ="/lowerShelf" , method = RequestMethod.POST)
	public ShopRespose  lowerShelf(@RequestBody HashMap<String, String> map) {
		ShopRespose respose;
		if(ProductService.lowerShelf(map.get("id"))) {
			 respose = new ShopRespose<>(IConstants.SUCCESS, IConstants.SUCCESS_MSG);
		}else{
			 respose = new ShopRespose<>(IConstants.FAILED, IConstants.FAILED_MSG);
		}
		return respose;
	}
	@ApiOperation(value = "查看商品")
    @RequestMapping(value ="/seeProduct" , method = RequestMethod.POST)
	public ShopRespose seeProduct(@RequestBody ProductSeeVo vo) {
		ShopRespose respose = new ShopRespose<>();
		ProductSeeVo info = ProductService.seeProduct(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
        return respose;
	}
	@ApiOperation(value = "去修改商品")
    @RequestMapping(value ="/toUpdateProduct" , method = RequestMethod.POST)
	public ShopRespose toUpdateProduct(@RequestBody ProductAddParam vo ) {
		ShopRespose respose = new ShopRespose<>();
		ProductToUpdateParam info = ProductService.toUpdateProduct(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
        return respose;
	}
	@ApiOperation(value = "修改商品")
    @RequestMapping(value ="/updateProduct" , method = RequestMethod.POST)
	public ShopRespose updateProduct(@ApiParam(name = "X-Auth-Token", value = "登录token", required = true)@RequestHeader(name = "X-Auth-Token", required = true, defaultValue = "") String token,@RequestBody ProductAddParam vo) {
		ShopRespose respose = new ShopRespose<>();
		ShopAdmin business = businessLoginService.getBusinessInfo(JwtTokenUtils.getUsername(token));
		if(business == null) {
			respose = new ShopRespose<>(ErrorEnum.ERROR_401);
			return respose;
		}
		//business.setUsername("test");
		try {
			respose =ProductService.updateProduct(vo,business);
		} catch (Exception e) {
			e.printStackTrace();
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
}
