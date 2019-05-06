package com.jinglitong.wallet.reportserver.controller.report;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.view.DdbAssetDisplayVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWalletVo;
import com.jinglitong.wallet.reportserver.service.AssetDisplayService;

import com.jinglitong.wallet.reportserver.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: AssetDisplayController.java
 * 作        者: yxl 2018年10月22日
 * 创建时间: 2018年10月22日
 * 功能说明:资产展示
 */
@RestController
@Slf4j
@RequestMapping("ddb/asset")
public class AssetDisplayController {

	@Autowired
    AssetDisplayService AssetDisplayService;
	@ResponseBody
	@RequestMapping("/getAsset.json")
	public Map getAsset(@RequestBody DdbAssetDisplayVo vo) {
		return JsonUtil.toJsonSuccess("查询成功", AssetDisplayService.getAsset(vo));
	}
	
	@RequestMapping("/getRecord.json")
	public Map getRecord(@RequestBody DdbIntegralWalletVo wallet) {
		
		return JsonUtil.toJsonSuccess("查询成功", AssetDisplayService.getRecord(wallet));
	}
}
