package com.jinglitong.springshop.controller;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.PropertieTab;
import com.jinglitong.springshop.service.PropertieTabService;
import com.jinglitong.springshop.vo.request.PropertieTabVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/propertieTab")
@Api(value = "关于管理API", tags = "关于管理API")
@Slf4j
public class PropertieTabController {

	@Autowired
	private PropertieTabService propertieTabService;

	@ApiOperation(value = "关于管理列表")
	@RequestMapping(value = "/getPropertieTabList", method = RequestMethod.POST)
	public ShopRespose getAppPList(@RequestBody PropertieTabVO vo) {
		ShopRespose respose = new ShopRespose<>();
		PageInfo<PropertieTab> info = propertieTabService.getAppPList(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}

	@ApiOperation(value = "新增")
	@RequestMapping(value = "/addPropertieTab", method = RequestMethod.POST)
	public ShopRespose addPropertieTab(@RequestBody PropertieTabVO vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			propertieTabService.addAboutManagement(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}

	@ApiOperation(value = "查看")
	@RequestMapping(value = "/getPropertieTabDetail", method = RequestMethod.POST)
	public ShopRespose getPropertieTabDetail(@RequestBody PropertieTabVO vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			PropertieTab info = propertieTabService.getAboutById(vo);
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
	@RequestMapping(value = "/updatePropertieTab", method = RequestMethod.POST)
	public ShopRespose updatePropertieTab(@RequestBody PropertieTab vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			propertieTabService.updateAboutManagement(vo);
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}

	@ApiOperation(value = "删除")
	@RequestMapping(value = "/delProP", method = RequestMethod.POST)
	public ShopRespose delProP(@RequestBody PropertieTabVO vo) {
		ShopRespose respose = new ShopRespose<>();
		try {
			propertieTabService.delAboutManagement(vo);;
			respose.setCode(IConstants.SUCCESS);
			respose.setMessage(IConstants.SUCCESS_MSG);
		} catch (Exception e) {
			respose.setCode(IConstants.FAILED);
    		respose.setMessage(IConstants.FAILED_MSG);
		}
		return respose;
	}
}
