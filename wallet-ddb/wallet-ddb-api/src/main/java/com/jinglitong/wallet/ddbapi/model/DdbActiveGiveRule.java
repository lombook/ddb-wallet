package com.jinglitong.wallet.ddbapi.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: DdbActiveGiveRule.java
 * 作        者: yxl 2018年10月25日
 * 创建时间: 2018年10月25日
 * 功能说明:宝分赠送规则表
 */
@Table(name = "ddb_active_give_rule")
public class DdbActiveGiveRule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String zid;
	
	@Column(name ="hd_group")
	private String hdGroup;
	
	@Column(name ="inte_name")
	private String inteName;
	
	@Column(name ="inte_cname")
	private String inteCname;
	
	private BigDecimal percent;
	
	private BigDecimal rate;
	
	@Column(name ="effective_time")
	private String effectiveTime;
	
	@Column(name ="expiry_time")
	private String expiryTime;
	
	@Column(name ="create_time")
	private String createTime;

	
	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public String getHdGroup() {
		return hdGroup;
	}

	public void setHdGroup(String hdGroup) {
		this.hdGroup = hdGroup;
	}

	public String getInteName() {
		return inteName;
	}

	public void setInteName(String inteName) {
		this.inteName = inteName;
	}

	public String getInteCname() {
		return inteCname;
	}

	public void setInteCname(String inteCname) {
		this.inteCname = inteCname;
	}

 

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
