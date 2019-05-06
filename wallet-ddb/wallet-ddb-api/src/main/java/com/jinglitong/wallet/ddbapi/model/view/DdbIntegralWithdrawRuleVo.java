package com.jinglitong.wallet.ddbapi.model.view;

import java.math.BigDecimal;

import javax.persistence.Column;

import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRule;

public class DdbIntegralWithdrawRuleVo {

	private String servicecName;
	
	private String serviceName;
	
	private BigDecimal servicePercent;
	
	private String resaveServiceTempCname;
	
	private String resaveServiceTempName;
	
	private String resaveServiceRealCname;
	
	private String resaveServiceRealName;
	
	/**
     * 业务id
     */
    private String zid;

    /**
     * 兑换系数
     */
    private BigDecimal percent;

    /**
     * 最小提现金额
     */
    private Integer minAmount;

    /**
     * 积分中文名
     */
    private String integCname;

    /**
     * 积分名
     */
    private String integName;

    /**
     * 手续费
     */
    private Integer integCharge;

    /**
     * 手续费积分中文名
     */
    private String chargeIntegCname;

    /**
     * 手续费积分名
     */
    private String chargeIntegName;

    /**
     * 提现积分回收总账中文名
     */
    private String resaveIntegRealCname;

    /**
     * 提现积分回收总账名
     */
    private String resaveIntegRealName;

    /**
     * 手续费回收总账中文名
     */
    private String resaveChargeRealCname;

    /**
     * 手续费回收总账名
     */
    private String resaveChargeRealName;

    /**
     * 有效时间开始时间
     */
    private String effectiveTime;

    /**
     * 失效时间
     */
    private String expiryTime;

    /**
     * 规则名
     */
    private String ruleName;

	

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public Integer getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}

	public String getIntegCname() {
		return integCname;
	}

	public void setIntegCname(String integCname) {
		this.integCname = integCname;
	}

	public String getIntegName() {
		return integName;
	}

	public void setIntegName(String integName) {
		this.integName = integName;
	}

	public Integer getIntegCharge() {
		return integCharge;
	}

	public void setIntegCharge(Integer integCharge) {
		this.integCharge = integCharge;
	}

	public String getChargeIntegCname() {
		return chargeIntegCname;
	}

	public void setChargeIntegCname(String chargeIntegCname) {
		this.chargeIntegCname = chargeIntegCname;
	}

	public String getChargeIntegName() {
		return chargeIntegName;
	}

	public void setChargeIntegName(String chargeIntegName) {
		this.chargeIntegName = chargeIntegName;
	}

	public String getResaveIntegRealCname() {
		return resaveIntegRealCname;
	}

	public void setResaveIntegRealCname(String resaveIntegRealCname) {
		this.resaveIntegRealCname = resaveIntegRealCname;
	}

	public String getResaveIntegRealName() {
		return resaveIntegRealName;
	}

	public void setResaveIntegRealName(String resaveIntegRealName) {
		this.resaveIntegRealName = resaveIntegRealName;
	}

	public String getResaveChargeRealCname() {
		return resaveChargeRealCname;
	}

	public void setResaveChargeRealCname(String resaveChargeRealCname) {
		this.resaveChargeRealCname = resaveChargeRealCname;
	}

	public String getResaveChargeRealName() {
		return resaveChargeRealName;
	}

	public void setResaveChargeRealName(String resaveChargeRealName) {
		this.resaveChargeRealName = resaveChargeRealName;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getResaveServiceTempCname() {
		return resaveServiceTempCname;
	}

	public void setResaveServiceTempCname(String resaveServiceTempCname) {
		this.resaveServiceTempCname = resaveServiceTempCname;
	}

	public String getResaveServiceTempName() {
		return resaveServiceTempName;
	}

	public void setResaveServiceTempName(String resaveServiceTempName) {
		this.resaveServiceTempName = resaveServiceTempName;
	}

	public String getResaveServiceRealCname() {
		return resaveServiceRealCname;
	}

	public void setResaveServiceRealCname(String resaveServiceRealCname) {
		this.resaveServiceRealCname = resaveServiceRealCname;
	}

	public String getResaveServiceRealName() {
		return resaveServiceRealName;
	}

	public void setResaveServiceRealName(String resaveServiceRealName) {
		this.resaveServiceRealName = resaveServiceRealName;
	}

	public String getServicecName() {
		return servicecName;
	}

	public void setServicecName(String servicecName) {
		this.servicecName = servicecName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public BigDecimal getServicePercent() {
		return servicePercent;
	}

	public void setServicePercent(BigDecimal servicePercent) {
		this.servicePercent = servicePercent;
	}
	
	
}
