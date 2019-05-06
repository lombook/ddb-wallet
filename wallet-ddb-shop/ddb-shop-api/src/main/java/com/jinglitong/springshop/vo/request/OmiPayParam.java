package com.jinglitong.springshop.vo.request;

public class OmiPayParam {
	
	/**
	 * 订单sn
	 */
	private String orderNo;
	
	/**
	 * 支付插件zid
	 */
	private String paymentPluginId;
	
	/**
	 * 支付平台
	 */
	private String platform;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPaymentPluginId() {
		return paymentPluginId;
	}
	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	

}
