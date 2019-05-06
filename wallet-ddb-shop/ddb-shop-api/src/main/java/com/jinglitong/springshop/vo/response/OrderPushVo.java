package com.jinglitong.springshop.vo.response;

import java.util.List;

public class OrderPushVo {

	private String consignee;
	private String actualPayment;
	private String orderSn;
	private String appId;
	private String consigneeArea;
	private String consigneeMobile;
	private String consigneeAddress;
	private Long orderTime;
	private String sign;
	private List<OrderPushChildVo> childOrder;
	
	
	
 
	public Long getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Long orderTime) {
		this.orderTime = orderTime;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getActualPayment() {
		return actualPayment;
	}
	public void setActualPayment(String actualPayment) {
		this.actualPayment = actualPayment;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getConsigneeArea() {
		return consigneeArea;
	}
	public void setConsigneeArea(String consigneeArea) {
		this.consigneeArea = consigneeArea;
	}
	public String getConsigneeMobile() {
		return consigneeMobile;
	}
	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public List<OrderPushChildVo> getChildOrder() {
		return childOrder;
	}
	public void setChildOrder(List<OrderPushChildVo> childOrder) {
		this.childOrder = childOrder;
	}
	
	
	
	
}
