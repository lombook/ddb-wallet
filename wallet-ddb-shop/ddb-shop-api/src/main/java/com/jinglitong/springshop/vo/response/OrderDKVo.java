package com.jinglitong.springshop.vo.response;

import java.math.BigDecimal;

public class OrderDKVo {

    private BigDecimal dkPrice;
    private String dkCode;
	public BigDecimal getDkPrice() {
		return dkPrice;
	}
	public void setDkPrice(BigDecimal dkPrice) {
		this.dkPrice = dkPrice;
	}
	public String getDkCode() {
		return dkCode;
	}
	public void setDkCode(String dkCode) {
		this.dkCode = dkCode;
	}
    
    
}
