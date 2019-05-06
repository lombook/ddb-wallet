package com.jinglitong.springshop.vo.response;

import java.math.BigDecimal;

public class IntegerProductVo {

	private String price;
	private String integralCode;
	private BigDecimal integralPrice;
	
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getIntegralCode() {
		return integralCode;
	}
	public void setIntegralCode(String integralCode) {
		this.integralCode = integralCode;
	}
	public BigDecimal getIntegralPrice() {
		return integralPrice;
	}
	public void setIntegralPrice(BigDecimal integralPrice) {
		this.integralPrice = integralPrice;
	}
	
	
}
