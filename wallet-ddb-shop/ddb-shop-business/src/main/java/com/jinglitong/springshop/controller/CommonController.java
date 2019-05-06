package com.jinglitong.springshop.controller;

import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.service.AliCloudStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: CommonController.java
 * 作        者: yxl 2019年1月9日
 * 创建时间: 2019年1月9日
 * 功能说明:
 */

@RestController
@RequestMapping("/business/common")
@Api(value = "公共API",tags = "公共 API")
@Slf4j
public class CommonController {

    @Autowired
    private AliCloudStorageService aliCloudStorageService;
    
    @ApiOperation(value = "上传图片")
    @RequestMapping(value = "/product/uploadOpionImg", method = RequestMethod.POST)
    public ShopRespose upload(@RequestParam("file") MultipartFile file) {
    	ShopRespose respose = new ShopRespose<>();
        Map map = new HashMap();
        try {
            String url = aliCloudStorageService.uploadFile(file);
            map.put("url", url);
            log.info("上传图片ok,{}",url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("ok", file.getName());
        respose.setData(map);
        respose.setCode(IConstants.SUCCESS);
        return respose;
    }
}
