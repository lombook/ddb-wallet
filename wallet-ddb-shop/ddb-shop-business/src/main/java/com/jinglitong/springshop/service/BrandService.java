package com.jinglitong.springshop.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.entity.Brand;
import com.jinglitong.springshop.mapper.BrandMapper;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.request.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
	
	@Autowired
	private AliCloudStorageService aliCloudStorageService;
	
	/**
	 * 
	 * 功能说明:上架用到的
	 * @return
	 */
	public List<Brand> getAllList(){
		
		return brandMapper.selectAll();
	}
	/**
	 * 
	 * 功能说明:列表
	 * @param vo
	 * @return
	 */
	public PageInfo<Brand> getBrandList(BrandVo vo){
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<Brand> list = brandMapper.getBrandList(vo);
		PageInfo<Brand> info = new PageInfo<Brand>(list);
		return info;
	}
	
	@Transactional
	public void addBrand(Brand vo) {
		if (vo == null) {
			throw new RuntimeException("参数异常");
		}
		/*InputStream htmlIs = new ByteArrayInputStream(vo.getIntroduction().getBytes());
		String dateTimeStr = " brand_" + DateUtils.getDateTimeStr() + ".html";
		String resultOos = aliCloudStorageService.uploadProtocolHtml(htmlIs, dateTimeStr);

		if (resultOos == null) {
			throw new RuntimeException("文本上传失败");
		}*/
		vo.setZid(UuidUtil.getUUID());
		//vo.setIntroduction(vo.getIntroduction());
		vo.setCreatedTime(new Date());
		vo.setUpdatedTime(new Date());
		int insert = brandMapper.insert(vo);
		if (insert != 1) {
			throw new RuntimeException("入库异常");
		}

	}
	public void delBrand(BrandVo vo) {
		Integer id = vo.getId();
		if(id == null) {
			throw new RuntimeException("参数异常");
		}
		int count = brandMapper.deleteByPrimaryKey(id);
		if(count != 1) {
			throw new RuntimeException("删除异常");
		}
	}
	
	public Brand getBrandById(Brand vo) {

		Integer id = vo.getId();
		if(id == null) {
			throw new RuntimeException("参数异常");
		}
		Brand b = brandMapper.selectByPrimaryKey(id);
		
		return b;
	}
	
	@Transactional
	public void upBrand(Brand brand) {
		if (brand == null) {
			throw new RuntimeException("参数异常");
		}
		/*InputStream htmlIs = new ByteArrayInputStream(brand.getIntroduction().getBytes());
		String dateTimeStr = "brand" + DateUtils.getDateTimeStr() + ".html";
		String resultOos = aliCloudStorageService.uploadProtocolHtml(htmlIs, dateTimeStr);
		if (resultOos == null) {
			throw new RuntimeException("文本上传失败");
		}
		brand.setIntroduction(resultOos);*/
		brand.setUpdatedTime(new Date());
		int count = brandMapper.updateByPrimaryKeySelective(brand);
		if (count != 1) {
			throw new RuntimeException("修改异常");
		}
	}
}
