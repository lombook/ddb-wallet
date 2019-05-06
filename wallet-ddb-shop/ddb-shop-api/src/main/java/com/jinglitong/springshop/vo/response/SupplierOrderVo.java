package com.jinglitong.springshop.vo.response;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinglitong.springshop.vo.request.PageVo;

import lombok.Data;

@Data
public class SupplierOrderVo extends PageVo{

	private String zid;
	private String account;
	private String sn;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;
	private String name;
	private String quantity;
	private BigDecimal price;
	private String priceCode;
	private BigDecimal integralPrice;
	private String integralCode;
	private String thumbnail;
	private String consignee;
	private String phone;
	private String address;
	private String areaName;
	private String status;
	private String deliveryCorp;
	private String trackingNo;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date deliveryTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date completeDate;
	
	private String beginTime;
	private String endTime;
	private String businessZid;
}
