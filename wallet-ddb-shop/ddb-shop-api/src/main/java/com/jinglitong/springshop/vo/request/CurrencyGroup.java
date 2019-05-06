package com.jinglitong.springshop.vo.request;

import java.math.BigDecimal;

public class CurrencyGroup {

	private String zid;
	
	private String code;
	
	private String value;
	
	private int isDefault;
	
	private String svalue;
	
	private String djbValue;
	

 

 


	public String getSvalue() {
		return svalue;
	}


	public void setSvalue(String svalue) {
		this.svalue = svalue;
	}


	public String getDjbValue() {
		return djbValue;
	}


	public void setDjbValue(String djbValue) {
		this.djbValue = djbValue;
	}


	public CurrencyGroup() {
		
	}
	
	
	public String getZid() {
		return zid;
	}


	public void setZid(String zid) {
		this.zid = zid;
	}


	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
