package com.jinglitong.springshop.servcie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.entity.Brand;
import com.jinglitong.springshop.mapper.BrandMapper;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: BranchService.java
 * 作        者: yxl 2019年1月10日
 * 创建时间: 2019年1月10日
 * 功能说明:品牌
 */
@Service
public class BrandService {

	@Autowired
	private BrandMapper brandMapper;
	
	public List<Brand> getAllList(){
		
		return brandMapper.selectAll();
	}
	
	
}
