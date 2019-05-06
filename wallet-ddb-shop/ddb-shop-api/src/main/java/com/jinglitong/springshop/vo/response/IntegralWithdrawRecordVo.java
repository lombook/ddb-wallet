package com.jinglitong.springshop.vo.response;

import com.jinglitong.springshop.vo.request.PageVo;

public class IntegralWithdrawRecordVo extends PageVo{
	/**
     * id
     */
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 提现规则id
     */
    private String withdrawRuleId;

    /**
     * 用户id
     */
    private String custId;

    /**
     * 持卡人
     */
    private String cardholder;

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 兑换现金数
     */
    private Long amount;
    
    private String amountStr;

    /**
     * 兑换积分数
     */
    private Long integralAmount;

    /**
     * 积分名
     */
    private String integralName;

    /**
     * 手续费
     */
    private Long integralCharge;

    /**
     * 手续费积分名
     */
    private String integralChargeName;

    /**
     * 审核状态：0 待审核  1 审核成功  2 审核失败 
     */
    private Integer applyStatus;

    /**
     * 提现状态  0 待提现  1提现成功  2提现失败
     */
    private Integer drawStatus;

    /**
     * 申请日期
     */
    private String applyTime;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public String getWithdrawRuleId() {
		return withdrawRuleId;
	}

	public void setWithdrawRuleId(String withdrawRuleId) {
		this.withdrawRuleId = withdrawRuleId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCardholder() {
		return cardholder;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getIntegralAmount() {
		return integralAmount;
	}

	public void setIntegralAmount(Long integralAmount) {
		this.integralAmount = integralAmount;
	}

	public String getIntegralName() {
		return integralName;
	}

	public void setIntegralName(String integralName) {
		this.integralName = integralName;
	}

	public Long getIntegralCharge() {
		return integralCharge;
	}

	public void setIntegralCharge(Long integralCharge) {
		this.integralCharge = integralCharge;
	}

	public String getIntegralChargeName() {
		return integralChargeName;
	}

	public void setIntegralChargeName(String integralChargeName) {
		this.integralChargeName = integralChargeName;
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}

	public Integer getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(Integer drawStatus) {
		this.drawStatus = drawStatus;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getAmountStr() {
		return amountStr;
	}

	public void setAmountStr(String amountStr) {
		this.amountStr = amountStr;
	}

	 
    
}
