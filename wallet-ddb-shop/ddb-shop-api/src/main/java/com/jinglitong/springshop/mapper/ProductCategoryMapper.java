package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.ProductCategory;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.request.CateTreeVo;
import com.jinglitong.springshop.vo.request.ProductCategoryVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryMapper extends MyMapper<ProductCategory> {
	
	List<CateTreeVo> selectTree(@Param("id") Integer id);
	
	List<ProductCategoryVo>getCategoryList(ProductCategoryVo vo);

    List<ProductCategory> selectByGrade(@Param("grade") int grade);
}