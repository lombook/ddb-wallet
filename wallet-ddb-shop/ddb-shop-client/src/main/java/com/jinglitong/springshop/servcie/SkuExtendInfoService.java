package com.jinglitong.springshop.servcie;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.entity.SkuExtendInfo;
import com.jinglitong.springshop.mapper.SkuExtendInfoMapper;

@Service
public class SkuExtendInfoService {
	@Autowired
    private SkuExtendInfoMapper skuExtendInfoMapper;
	
	public SkuExtendInfo getBySkuPriceId(String skuPriceId){
		SkuExtendInfo SkuExtendInfo = new SkuExtendInfo();
		SkuExtendInfo.setSkuPriceId(skuPriceId);
		return skuExtendInfoMapper.selectOne(SkuExtendInfo);		
	}
}
