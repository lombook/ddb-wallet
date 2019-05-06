package com.jinglitong.springshop.vo.response;

public class IetOrderStatusVo {

	private String orderId;
	private int status;
	private Long nonceStr;
	private String sign;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(Long nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
