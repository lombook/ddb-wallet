package com.jinglitong.springshop.vo.response;

public class IetOrderVo {

	private String orderId;
	private String goodSn;
	private Integer quantity;
	private String areaName;
	private String address;
	private String consignee;
	private String phone;
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGoodSn() {
		return goodSn;
	}
	public void setGoodSn(String goodSn) {
		this.goodSn = goodSn;
	}
	
	
}
