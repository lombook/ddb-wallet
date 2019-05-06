package com.jinglitong.springshop.vo.response;

import java.math.BigDecimal;

public class UserAssetVo {

//	袋金币
	public String djbType = "djb_all";
	public BigDecimal djbAmount = BigDecimal.ZERO;
	// 现金贷
	public String xjdType = "xjd_all";
	public BigDecimal xjdNum = BigDecimal.ZERO;
	
	//swtc
	public String swtcType = "swtc_all";
	public BigDecimal swtcAmount = BigDecimal.ZERO;
	public String getDjbType() {
		return djbType;
	}
	public void setDjbType(String djbType) {
		this.djbType = djbType;
	}
	public BigDecimal getDjbAmount() {
		return djbAmount;
	}
	public void setDjbAmount(BigDecimal djbAmount) {
		this.djbAmount = djbAmount;
	}
	public String getXjdType() {
		return xjdType;
	}
	public void setXjdType(String xjdType) {
		this.xjdType = xjdType;
	}
	public BigDecimal getXjdNum() {
		return xjdNum;
	}
	public void setXjdNum(BigDecimal xjdNum) {
		this.xjdNum = xjdNum;
	}
	public String getSwtcType() {
		return swtcType;
	}
	public void setSwtcType(String swtcType) {
		this.swtcType = swtcType;
	}
	public BigDecimal getSwtcAmount() {
		return swtcAmount;
	}
	public void setSwtcAmount(BigDecimal swtcAmount) {
		this.swtcAmount = swtcAmount;
	}
	
	

}
