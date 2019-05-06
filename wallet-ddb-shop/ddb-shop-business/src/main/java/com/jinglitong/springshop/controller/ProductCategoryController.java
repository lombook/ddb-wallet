package com.jinglitong.springshop.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Brand;
import com.jinglitong.springshop.entity.ProductCategory;
import com.jinglitong.springshop.service.ProductCategoryService;
import com.jinglitong.springshop.vo.request.BrandVo;
import com.jinglitong.springshop.vo.request.ProductCategoryVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/business/category")
@Api(value = "分类API", tags = "分类API")
public class ProductCategoryController {

	@Resource
	private ProductCategoryService productCategoryService;
	
	@ApiOperation(value = "分类列表")
	@RequestMapping(value = "/getCategoryList", method = RequestMethod.POST)
	public ShopRespose getCategoryList(@RequestBody ProductCategoryVo vo) {
		ShopRespose respose = new ShopRespose<>();
		PageInfo<ProductCategoryVo> info = productCategoryService.getCategoryList(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}
	
	@ApiOperation(value = "新增")
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public ShopRespose addCategory(@RequestBody ProductCategory vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			productCategoryService.addCategory(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
	
	@ApiOperation(value = "查看")
	@RequestMapping(value = "/getCategoryDetail", method = RequestMethod.POST)
	public ShopRespose getCategoryDetail(@RequestBody ProductCategory vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			ProductCategory info = productCategoryService.getCategoryById(vo);
			respose.setMessage(IConstants.SUCCESS_MSG);
			respose.setData(info);
			respose.setCode(IConstants.SUCCESS);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
			respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
	
	
	@ApiOperation(value = "删除")
	@RequestMapping(value = "/delCategory", method = RequestMethod.POST)
	public ShopRespose delCategory(@RequestBody ProductCategory vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			productCategoryService.delCategory(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
	
	@ApiOperation(value = "修改")
	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	public ShopRespose updateCategory(@RequestBody ProductCategory vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			productCategoryService.updateCategory(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
}
