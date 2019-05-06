package com.jinglitong.springshop.vo.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: ProductAddParam.java
 * 作        者: yxl 2019年1月10日
 * 创建时间: 2019年1月10日
 * 功能说明:上架商品
 */
public class ProductAddParam {
	private String zid;//skuId
	private String categoryId;
	private String sn;
	private String name;
	private String caption;
	private String image;
	private String weight;
	private String weightUnit;
	private String brandId;
	private Boolean isList;
	private String memo;
	private String keyword;
	private String introduction;
	private String price;
	private String currencyId;
	private String storeId;
	private CurrencyGroup [] currencyGroup;//币种组合[{"code":"AUD","value":"100"},{"code":"AUD-DJB","value":"90-20"}] 
	
	
	
	
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public CurrencyGroup[] getCurrencyGroup() {
		return currencyGroup;
	}
	public void setCurrencyGroup(CurrencyGroup[] currencyGroup) {
		this.currencyGroup = currencyGroup;
	}
	public String getZid() {
		return zid;
	}
	public void setZid(String zid) {
		this.zid = zid;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getWeightUnit() {
		return weightUnit;
	}
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public Boolean getIsList() {
		return isList;
	}
	public void setIsList(Boolean isList) {
		this.isList = isList;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
	
}
