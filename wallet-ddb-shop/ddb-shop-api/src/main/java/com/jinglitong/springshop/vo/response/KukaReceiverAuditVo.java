package com.jinglitong.springshop.vo.response;

import java.util.Date;

import com.jinglitong.springshop.vo.request.PageVo;


public class KukaReceiverAuditVo extends PageVo {

	
	private String zid;
	
    private String orderId;
    
    private String phone;
    
    private String sn;
    
    private Integer mainStatus;

    private Integer subStatus;
    
    private String consignee;
    
    private String areaName;
    
    private String address;

    private String zipCode;
    
    private Date auditTime;
    
    private Date createTime;

    private String auditCustomer;

    private Date editEndTime;

    private String custName;
    
    
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getMainStatus() {
		return mainStatus;
	}

	public void setMainStatus(Integer mainStatus) {
		this.mainStatus = mainStatus;
	}

	public Integer getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(Integer subStatus) {
		this.subStatus = subStatus;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditCustomer() {
		return auditCustomer;
	}

	public void setAuditCustomer(String auditCustomer) {
		this.auditCustomer = auditCustomer;
	}

	public Date getEditEndTime() {
		return editEndTime;
	}

	public void setEditEndTime(Date editEndTime) {
		this.editEndTime = editEndTime;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}
}
