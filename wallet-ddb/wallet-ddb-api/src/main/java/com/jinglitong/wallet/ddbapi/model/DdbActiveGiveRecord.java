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
 * 文件名称: ddbActiveGiveRecord.java
 * 作        者: yxl 2018年10月25日
 * 创建时间: 2018年10月25日
 * 功能说明:宝分赠送规则表
 */
@Table(name ="ddb_active_give_record")
public class DdbActiveGiveRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String zid;
	
	@Column(name ="flow_id")
	private String flowId;
	
	@Column(name ="shop_trade")
	private String shopTrade;
	
	@Column(name ="active_rule_id")
	private String activeRuleId;
	
	@Column(name ="cust_id")
	private String custId;
	
	@Column(name ="product_price")
	private Long productPrice;
	
	private BigDecimal percent;
	
	@Column(name ="mei_price")
	private String meiPrice;
	
	@Column(name ="inte_name")
	private String inteName;
	
	@Column(name ="inte_amount")
	private String inteAmount;

	private String rate;

	private Integer status;

	@Column(name = "create_time")
	private String createTime;
	
	@Column(name ="update_time")
	private String updateTime;

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

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getShopTrade() {
		return shopTrade;
	}

	public void setShopTrade(String shopTrade) {
		this.shopTrade = shopTrade;
	}

	public String getActiveRuleId() {
		return activeRuleId;
	}

	public void setActiveRuleId(String activeRuleId) {
		this.activeRuleId = activeRuleId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public Long getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Long productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public String getMeiPrice() {
		return meiPrice;
	}

	public void setMeiPrice(String meiPrice) {
		this.meiPrice = meiPrice;
	}

	public String getInteName() {
		return inteName;
	}

	public void setInteName(String inteName) {
		this.inteName = inteName;
	}

	public String getInteAmount() {
		return inteAmount;
	}

	public void setInteAmount(String inteAmount) {
		this.inteAmount = inteAmount;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
