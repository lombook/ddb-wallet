package com.jinglitong.wallet.ddbapi.model.view;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.jinglitong.wallet.api.model.view.PageVO;

import java.util.Date;
/**
 * 现贝提现申请记录 vo类
 * @author Administrator
 *
 */
public class IntegralWithdrawApplyVo  extends PageVO {
	
	/**
	 * 审核，提现列表标识： 审核列表 0  false  提现列表 1  true
	 */
	private Boolean flag;

    /**
     * 业务id
     */
    private String zid;


    /**
     * 用户id
     */
    private String custId;
    
    /**
     * 用户账号
     */
    private String account;


    /**
     * 持卡人   姓名
     */
    private String cardholder;
    
    /**
     * 兑换现金数  提现数量
     */
    private Integer amount;



    /**
     * 审批状态：0 待审核  1 审核成功  2 审核失败 
     */
    private String applyStatus;

    // 申请日期
    private String applyTime;
    
    // 审批日期
    private String updateTime;

    /**
     * 申请日期    审批日期
     */
    private String beginTime;
    
    /**
     * 申请日期   审批日期
     */
    private String endTime;
    
  
	/**
	 *  审批s时间
	 */
   private String auditTime;

    
    /**
     *  处理状态  提现状态  0 待提现  1提现成功  2提现失败   
     */
    private String drawStatus;


    
	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}


	public String getCardholder() {
		return cardholder;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
    
	

}