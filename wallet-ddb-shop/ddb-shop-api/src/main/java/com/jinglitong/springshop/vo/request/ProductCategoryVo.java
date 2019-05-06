package com.jinglitong.springshop.vo.request;

import java.util.Date;

import lombok.Data;

@Data
public class ProductCategoryVo extends PageVo{

	private Integer id;

	private String zid;
	/**
	 * 排序权重 0最大
	 */
	private Integer orders;

	/**
	 * 层级
	 */
	private Integer grade;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 页面描述
	 */
	private String seodescription;

	/**
	 * 关键词
	 */
	private String seokeywords;

	/**
	 * 页面标题
	 */
	private String seotitle;

	/**
	 * 树路径
	 */
	private String treepath;

	/**
	 * 上级分类
	 */
	private Integer parentId;

	private String parentName;
	
	private Date createdTime;

	private Date updatedTime;

	 
	
}
