package com.jinglitong.wallet.ddbapi.model.logic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class FRuleInfo implements Serializable {

	private static final long serialVersionUID = 2L;
	
	/** 分红规则明细 */
	private String fRuleDetailID;
	/** 分红股东级别 */
	private int fLevel;
	/** 分红比例 */
	private BigDecimal fRate;
	
    public FRuleInfo() {
    }
    
    public FRuleInfo(String fRuleDetailID, int fLevel, BigDecimal fRate) {
    	this.fRuleDetailID = fRuleDetailID;
    	this.fLevel = fLevel;
    	this.fRate = fRate;
    }

    public String toString() {
    	return "{FRuleInfo==> "
    			+ "fRuleDetailID-->" + fRuleDetailID
				+ ", fLevel-->" + fLevel
				+ ", fRate-->" + String.valueOf(fRate.doubleValue()) + "}";
    }

	public String getfRuleDetailID() {
		return fRuleDetailID;
	}
	public void setfRuleDetailID(String fRuleDetailID) {
		this.fRuleDetailID = fRuleDetailID;
	}
	public int getfLevel() {
		return fLevel;
	}
	public void setfLevel(int fLevel) {
		this.fLevel = fLevel;
	}
	public BigDecimal getfRate() {
		return fRate;
	}
	public void setfRate(BigDecimal fRate) {
		this.fRate = fRate;
	}
    
}