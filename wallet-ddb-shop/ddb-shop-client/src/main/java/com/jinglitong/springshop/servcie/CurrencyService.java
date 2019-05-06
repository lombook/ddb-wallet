package com.jinglitong.springshop.servcie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.mapper.CurrencyMapper;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: CurrencyService.java
 * 作        者: yxl 2019年1月10日
 * 创建时间: 2019年1月10日
 * 功能说明:币种
 */
@Service
public class CurrencyService {

	@Autowired
	private CurrencyMapper currencyMapper;
	
	public List<Currency> getAllList(){
		
		return currencyMapper.selectAll();
	}
}
