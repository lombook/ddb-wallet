package com.jinglitong.wallet.ddbapi.model.view;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.jinglitong.wallet.api.model.view.PageVO;

import java.util.Date;
/**
 * 现贝提现  用户申请信息 vo类
 * @author Administrator
 *
 */
public class IntegralwithdrawCustomerApplyVo {
	
	/**
     *    姓名  申请人  持卡人  ；认证信息  认证人
     */
    private String cardholder;

    /**
     *申请信息  多少钱
     */
    private Integer amount;
    
    // 申请日期
    private String applyTime;
    
    // 身份证号  或者 护照号
    private String identity_no;
    
    //证件类型  00 身份证    01 其他，比如护照
    private String identity_type;
    
    
    
    
    /**
     * 银行名
     */
    private String bankName;
    /**
     * 开户行
     */
    private String activeAddress;
    /**
     * 卡号
     */
    private String cardNo;
    
    // 当前现贝数量
    private String xianBayAccount;
    
    // 备注说明
    private String remark;
 // 账号
    private String account;
    
 // 审批时间
    private String auditTime;
    
    
    
	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getIdentity_no() {
		return identity_no;
	}

	public void setIdentity_no(String identity_no) {
		this.identity_no = identity_no;
	}

	public String getIdentity_type() {
		return identity_type;
	}

	public void setIdentity_type(String identity_type) {
		this.identity_type = identity_type;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getActiveAddress() {
		return activeAddress;
	}

	public void setActiveAddress(String activeAddress) {
		this.activeAddress = activeAddress;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getXianBayAccount() {
		return xianBayAccount;
	}

	public void setXianBayAccount(String xianBayAccount) {
		this.xianBayAccount = xianBayAccount;
	}
    


}