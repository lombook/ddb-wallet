package com.jinglitong.springshop.vo.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class InteCustRecordVo extends PageVo {

	private String realname;// 对应总账表 real_name    对应记录表  integ_name   对应积分表  inte_real_name
	private String orderNum;
	/**
     * 设置显示时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String amount;
    private Integer inteType; // 1:增加 2：减少


	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Integer getInteType() {
		return inteType;
	}

	public void setInteType(Integer inteType) {
		this.inteType = inteType;
	}
	
}
