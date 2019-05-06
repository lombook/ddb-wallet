package com.jinglitong.springshop.vo.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SkuPriceVo {
	private String zid;
	private String skuId;
	private BigDecimal price;
	private String priceCode;
	private BigDecimal integralPrice;
	private String integralCode;
	private Integer priceType;	
	private Integer isDefault;
	

}
