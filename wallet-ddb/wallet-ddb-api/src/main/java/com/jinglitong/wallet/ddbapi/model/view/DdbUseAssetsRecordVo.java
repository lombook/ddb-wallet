package com.jinglitong.wallet.ddbapi.model.view;

import com.jinglitong.wallet.api.model.view.PageVO;

/**
 * 展示资产记录，创建一个Vo类
 * 
 * @author liangtf
 *
 */
public class DdbUseAssetsRecordVo extends PageVO{

	public String realname;// 对应总账表 real_name    对应记录表  integ_name   对应积分表  inte_real_name
	public String orderNum;
	public String createTime;
	public String amount;
	public Integer inteType;

	

 

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
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
