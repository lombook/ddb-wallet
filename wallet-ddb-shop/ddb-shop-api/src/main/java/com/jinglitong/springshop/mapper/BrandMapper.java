package com.jinglitong.springshop.mapper;

import java.util.List;

import com.jinglitong.springshop.entity.Brand;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.request.BrandVo;

public interface BrandMapper extends MyMapper<Brand> {
	
	List<Brand> getBrandList(BrandVo vo);
}