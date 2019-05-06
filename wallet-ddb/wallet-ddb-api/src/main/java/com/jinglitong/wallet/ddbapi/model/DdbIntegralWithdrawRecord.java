package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "ddb_integral_withdraw_record")
public class DdbIntegralWithdrawRecord {
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 提现规则id
     */
    @Column(name = "withdraw_rule_id")
    private String withdrawRuleId;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 持卡人
     */
    private String cardholder;

    /**
     * 银行卡号
     */
    @Column(name = "card_no")
    private String cardNo;

    /**
     * 兑换现金数
     */
    private Integer amount;

    /**
     * 兑换积分数
     */
    @Column(name = "integ_amount")
    private Integer integAmount;

    /**
     * 积分名
     */
    @Column(name = "integ_name")
    private String integName;

    /**
     * 手续费
     */
    @Column(name = "integ_charge")
    private Integer integCharge;

    /**
     * 手续费积分名
     */
    @Column(name = "integ_charge_name")
    private String integChargeName;

    /**
     * 审核状态：0 待审核  1 审核成功  2 审核失败 
     */
    @Column(name = "apply_status")
    private Integer applyStatus;

    /**
     * 提现状态  0 待提现  1提现成功  2提现失败
     */
    @Column(name = "draw_status")
    private Integer drawStatus;

    /**
     * 申请日期
     */
    @Column(name = "apply_time")
    private String applyTime;

    @Column(name = "audit_time")
    private String auditTime;

    @Column(name = "draw_time")
    private  String  drawTime;
    /**
     * 审核人
     */
    private String auditor;
    
    /**
     * 操作人
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;
    
    @Column(name ="bank_name")
    private String  bankName;
    
    @Column(name ="active_address")
    private String activeAddress;

    @Column(name ="account")
    private String account;
    
    @Column(name ="integ_service")
    private Integer integService;
    
    @Column(name ="integ_service_name")
    private String integServiceName;
    
    
    
    
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取业务id
     *
     * @return zid - 业务id
     */
    public String getZid() {
        return zid;
    }

    /**
     * 设置业务id
     *
     * @param zid 业务id
     */
    public void setZid(String zid) {
        this.zid = zid;
    }

    /**
     * 获取提现规则id
     *
     * @return withdraw_rule_id - 提现规则id
     */
    public String getWithdrawRuleId() {
        return withdrawRuleId;
    }

    /**
     * 设置提现规则id
     *
     * @param withdrawRuleId 提现规则id
     */
    public void setWithdrawRuleId(String withdrawRuleId) {
        this.withdrawRuleId = withdrawRuleId;
    }

    /**
     * 获取用户id
     *
     * @return cust_id - 用户id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户id
     *
     * @param custId 用户id
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * 获取持卡人
     *
     * @return cardholder - 持卡人
     */
    public String getCardholder() {
        return cardholder;
    }

    /**
     * 设置持卡人
     *
     * @param cardholder 持卡人
     */
    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    /**
     * 获取银行卡号
     *
     * @return card_no - 银行卡号
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置银行卡号
     *
     * @param cardNo 银行卡号
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * 获取兑换现金数
     *
     * @return amount - 兑换现金数
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置兑换现金数
     *
     * @param amount 兑换现金数
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 获取兑换积分数
     *
     * @return integ_amount - 兑换积分数
     */
    public Integer getIntegAmount() {
        return integAmount;
    }

    /**
     * 设置兑换积分数
     *
     * @param integAmount 兑换积分数
     */
    public void setIntegAmount(Integer integAmount) {
        this.integAmount = integAmount;
    }

    /**
     * 获取积分名
     *
     * @return integ_name - 积分名
     */
    public String getIntegName() {
        return integName;
    }

    /**
     * 设置积分名
     *
     * @param integName 积分名
     */
    public void setIntegName(String integName) {
        this.integName = integName;
    }

    /**
     * 获取手续费
     *
     * @return integ_charge - 手续费
     */
    public Integer getIntegCharge() {
        return integCharge;
    }

    /**
     * 设置手续费
     *
     * @param integCharge 手续费
     */
    public void setIntegCharge(Integer integCharge) {
        this.integCharge = integCharge;
    }

    /**
     * 获取手续费积分名
     *
     * @return integ_charge_name - 手续费积分名
     */
    public String getIntegChargeName() {
        return integChargeName;
    }

    /**
     * 设置手续费积分名
     *
     * @param integChargeName 手续费积分名
     */
    public void setIntegChargeName(String integChargeName) {
        this.integChargeName = integChargeName;
    }

    /**
     * 获取审核状态：0 待审核  1 审核成功  2 审核失败 
     *
     * @return apply_status - 审核状态：0 待审核  1 审核成功  2 审核失败 
     */
    public Integer getApplyStatus() {
        return applyStatus;
    }

    /**
     * 设置审核状态：0 待审核  1 审核成功  2 审核失败 
     *
     * @param applyStatus 审核状态：0 待审核  1 审核成功  2 审核失败 
     */
    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    /**
     * 获取提现状态  0 待提现  1提现成功  2提现失败
     *
     * @return draw_status - 提现状态  0 待提现  1提现成功  2提现失败
     */
    public Integer getDrawStatus() {
        return drawStatus;
    }

    /**
     * 设置提现状态  0 待提现  1提现成功  2提现失败
     *
     * @param drawStatus 提现状态  0 待提现  1提现成功  2提现失败
     */
    public void setDrawStatus(Integer drawStatus) {
        this.drawStatus = drawStatus;
    }

    /**
     * 获取申请日期
     *
     * @return apply_time - 申请日期
     */
    public String getApplyTime() {
        return applyTime;
    }

    /**
     * 设置申请日期
     *
     * @param applyTime 申请日期
     */
    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取审核人
     *
     * @return auditor - 审核人
     */
    public String getAuditor() {
        return auditor;
    }

    /**
     * 设置审核人
     *
     * @param auditor 审核人
     */
    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}