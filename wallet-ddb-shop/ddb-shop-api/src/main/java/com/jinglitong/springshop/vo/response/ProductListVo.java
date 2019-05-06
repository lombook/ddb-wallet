package com.jinglitong.springshop.vo.response;

import com.jinglitong.springshop.vo.request.PageVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: ProductListVo.java
 * 作        者: yxl 2019年1月10日
 * 创建时间: 2019年1月10日
 * 功能说明:web列表查询
 */
@Data
public class ProductListVo extends PageVo{
	private String id;
	private String zid;
	private String sn;
	private String name;
	private String categName;
	private String currencySign;
	private String currencyName;
	private String currencyId;
	private String isList;
	private String createdTime;
	private String updatedTime;
	private BigDecimal svalue;
	private BigDecimal djbValue;
	private String supportIntegral;//是否支持积分支付
	List<IntegerProductVo> integerProductVo;

	
	
	
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZid() {
		return zid;
	}
	public void setZid(String zid) {
		this.zid = zid;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategName() {
		return categName;
	}
	public void setCategName(String categName) {
		this.categName = categName;
	}
	public String getCurrencySign() {
		return currencySign;
	}
	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}
	public String getIsList() {
		return isList;
	}
	public void setIsList(String isList) {
		this.isList = isList;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
