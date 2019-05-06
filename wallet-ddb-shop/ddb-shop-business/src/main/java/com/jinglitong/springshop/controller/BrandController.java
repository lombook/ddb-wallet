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
import com.jinglitong.springshop.service.BrandService;
import com.jinglitong.springshop.vo.request.BrandVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/business/brand")
@Api(value = "品牌API", tags = "品牌API")
public class BrandController {

	@Resource
	private BrandService brandService; 
	
	@ApiOperation(value = "品牌列表")
	@RequestMapping(value = "/getBrandList", method = RequestMethod.POST)
	public ShopRespose getBrandList(@RequestBody BrandVo vo) {
		ShopRespose respose = new ShopRespose<>();
		PageInfo<Brand> info = brandService.getBrandList(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}
	
	@ApiOperation(value = "新增")
	@RequestMapping(value = "/addBrand", method = RequestMethod.POST)
	public ShopRespose addBrand(@RequestBody Brand vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			brandService.addBrand(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
	
	@ApiOperation(value = "删除")
	@RequestMapping(value = "/delBrand", method = RequestMethod.POST)
	public ShopRespose delBrand(@RequestBody BrandVo vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			brandService.delBrand(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
	
	@ApiOperation(value = "查看")
	@RequestMapping(value = "/getBranDetail", method = RequestMethod.POST)
	public ShopRespose getBranDetail(@RequestBody Brand vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			Brand info = brandService.getBrandById(vo);
			respose.setMessage(IConstants.SUCCESS_MSG);
			respose.setData(info);
			respose.setCode(IConstants.SUCCESS);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
			respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
	
	@ApiOperation(value = "修改")
	@RequestMapping(value = "/updateBrand", method = RequestMethod.POST)
	public ShopRespose updateAppProtocol(@RequestBody Brand vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			brandService.upBrand(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
	
}
