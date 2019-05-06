package com.jinglitong.wallet.reportserver.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.view.DdbAssetDisplayVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWalletVo;
import com.jinglitong.wallet.reportserver.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.reportserver.mapper.CustomerMapper;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: AssetDisplayService.java
 * 作        者: yxl 2018年10月22日
 * 创建时间: 2018年10月22日
 * 功能说明:资产展示service
 */
@Service
public class AssetDisplayService {

	@Autowired
	private DdbIntegralWalletMapper ddb;
	
	@Autowired
	private CustomerMapper CustomerMapper; 
	
	@Value("${ddb_app_id}")
	private String appId;
	
	public HashMap<String, Object> getAsset(DdbAssetDisplayVo vo){
		if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
		String account = "";
		if(!StringUtils.isEmpty(vo.getAccount())) {
			account = vo.getAccount();
		}
		List<DdbAssetDisplayVo> list =ddb.getAsset(appId, account);
		List <String > test = CustomerMapper.selectTestCusts();
		for (DdbAssetDisplayVo ddbAssetDisplayVo : list) {
			ddbAssetDisplayVo.setBaofen(getValue(ddbAssetDisplayVo.getBaofen()));
			ddbAssetDisplayVo.setShubei(getValue(ddbAssetDisplayVo.getShubei()));
			ddbAssetDisplayVo.setMiaobei(getValue(ddbAssetDisplayVo.getMiaobei()));
			ddbAssetDisplayVo.setWubei(getValue(ddbAssetDisplayVo.getWubei()));
			ddbAssetDisplayVo.setXianbei(getValue(ddbAssetDisplayVo.getXianbei()));
			ddbAssetDisplayVo.setHezuo(getValue(ddbAssetDisplayVo.getHezuo()));
			ddbAssetDisplayVo.setYumiao(getValue(ddbAssetDisplayVo.getYumiao()));
			ddbAssetDisplayVo.setJinbei(getValue(ddbAssetDisplayVo.getJinbei()));
			if(test.contains(ddbAssetDisplayVo.getCid())) {
				ddbAssetDisplayVo.setIsTestAcount("1");
			}else {
				ddbAssetDisplayVo.setIsTestAcount("0");
			}
		}
		
		PageInfo pageinfo = new PageInfo<>(list);
		HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("list",list);
		return map;
	}
	
	public String getValue(String value) {
		BigDecimal de = new BigDecimal(value);
		return de.divide(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString();
	}
	
	public HashMap<String, Object> getRecord(DdbIntegralWalletVo vo){
		if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
		List<DdbCustIntegralRecord> list = ddb.getRecord(vo);
		for (DdbCustIntegralRecord ddbCustIntegralRecord : list) {
			BigDecimal de = new BigDecimal(ddbCustIntegralRecord.getAmount());
			ddbCustIntegralRecord.setAmount(de.divide(BigDecimal.valueOf(100)).intValue());
		}
		PageInfo pageinfo = new PageInfo<>(list);
		HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("list",list);
		return map;
	}
	
}
