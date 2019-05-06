package com.jinglitong.springshop.vo.request;

import java.math.BigDecimal;

public class PriceCurrencyVo {

	private String pzid;
	
	private BigDecimal price;
	
	private String priceCode;
	
	private BigDecimal integralPrice;
	
	private String integralCode;
	
	private int isDefault;

	private BigDecimal svalue;
	
	private BigDecimal djbValue;
 

	
	public BigDecimal getSvalue() {
		return svalue;
	}
	public void setSvalue(BigDecimal svalue) {
		this.svalue = svalue;
	}
	public BigDecimal getDjbValue() {
		return djbValue;
	}
	public void setDjbValue(BigDecimal djbValue) {
		this.djbValue = djbValue;
	}

	public String getPzid() {
		return pzid;
	}

	public void setPzid(String pzid) {
		this.pzid = pzid;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}

	public BigDecimal getIntegralPrice() {
		return integralPrice;
	}

	public void setIntegralPrice(BigDecimal integralPrice) {
		this.integralPrice = integralPrice;
	}

	public String getIntegralCode() {
		return integralCode;
	}

	public void setIntegralCode(String integralCode) {
		this.integralCode = integralCode;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	
	
	
	
	
}
