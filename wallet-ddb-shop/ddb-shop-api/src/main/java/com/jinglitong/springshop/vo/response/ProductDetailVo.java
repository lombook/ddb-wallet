package com.jinglitong.springshop.vo.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailVo {
	private String zid;
	private String name;
	private String caption;
	private String weight;
	private String weightUnit;
	private String image;
	private String introduction;
	/*private String currencySign;
	private String price;*/
	private Integer isList;
	private BigDecimal svalue;
	private BigDecimal djbValue;
	private List<SkuPriceVo> skuPriceList;
		
	public Integer getIsList() {
		return isList;
	}
	public void setIsList(Integer isList) {
		this.isList = isList;
	}
	public String getZid() {
		return zid;
	}
	public void setZid(String zid) {
		this.zid = zid;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
}
