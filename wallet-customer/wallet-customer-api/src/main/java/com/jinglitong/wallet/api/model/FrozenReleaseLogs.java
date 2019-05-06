package com.jinglitong.wallet.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "frozen_release_logs")
public class FrozenReleaseLogs {
    /**
     * 主键id
     */
    @Id
    private Integer id;
    
    @Column(name = "log_id")
    private String logId;

    /**
     * 明细批次
     */
    @Column(name = "detail_id")
    private String detailId;

    /**
     * 总账地址
     */
    @Column(name = "ledger_adress")
    private String ledgerAdress;

    /**
     * 目的地址
     */
    @Column(name = "target_address")
    private String targetAddress;

    /**
     * 总释放钱数
     */
    @Column(name = "source_amount")
    private String sourceAmount;

    /**
     * 百分比
     */
    private Integer proportion;

    /**
     * 释放前应剩余钱数
     */
    @Column(name = "pre_send_amount")
    private String preSendAmount;

    /**
     * 释放后总剩余钱数
     */
    @Column(name = "suf_send_amount")
    private String sufSendAmount;

    /**
     * 本次应释放钱数
     */
    @Column(name = "send_amount")
    private String sendAmount;

    /**
     * 上次失败,填充的钱数
     */
    @Column(name = "pad_amount")
    private String padAmount;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private String payTime;

    /**
     * 确认时间
     */
    @Column(name = "confirm_time")
    private String confirmTime;

    /**
     * 释放状态 0:成功 1:失败
     */
    @Column(name = "log_status")
    private Integer logStatus;

    /**
     * 钱包id
     */
    @Column(name = "wallet_id")
    private String walletId;

    /**
     * 支付hash
     */
    @Column(name = "pay_hash")
    private String payHash;

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

    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    /**
     * 获取主键id
     *
     * @return log_id - 主键id
     */
    public String getLogId() {
        return logId;
    }

    /**
     * 设置主键id
     *
     * @param logId 主键id
     */
    public void setLogId(String logId) {
        this.logId = logId;
    }

    /**
     * 获取明细批次
     *
     * @return detail_id - 明细批次
     */
    public String getDetailId() {
        return detailId;
    }

    /**
     * 设置明细批次
     *
     * @param detailId 明细批次
     */
    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    /**
     * 获取总账地址
     *
     * @return ledger_adress - 总账地址
     */
    public String getLedgerAdress() {
        return ledgerAdress;
    }

    /**
     * 设置总账地址
     *
     * @param ledgerAdress 总账地址
     */
    public void setLedgerAdress(String ledgerAdress) {
        this.ledgerAdress = ledgerAdress;
    }

    /**
     * 获取目的地址
     *
     * @return target_address - 目的地址
     */
    public String getTargetAddress() {
        return targetAddress;
    }

    /**
     * 设置目的地址
     *
     * @param targetAddress 目的地址
     */
    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    /**
     * 获取总释放钱数
     *
     * @return source_amount - 总释放钱数
     */
    public String getSourceAmount() {
        return sourceAmount;
    }

    /**
     * 设置总释放钱数
     *
     * @param sourceAmount 总释放钱数
     */
    public void setSourceAmount(String sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    /**
     * 获取百分比
     *
     * @return proportion - 百分比
     */
    public Integer getProportion() {
        return proportion;
    }

    /**
     * 设置百分比
     *
     * @param proportion 百分比
     */
    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    /**
     * 获取释放前应剩余钱数
     *
     * @return pre_send_amount - 释放前应剩余钱数
     */
    public String getPreSendAmount() {
        return preSendAmount;
    }

    /**
     * 设置释放前应剩余钱数
     *
     * @param preSendAmount 释放前应剩余钱数
     */
    public void setPreSendAmount(String preSendAmount) {
        this.preSendAmount = preSendAmount;
    }

    /**
     * 获取释放后总剩余钱数
     *
     * @return suf_send_amount - 释放后总剩余钱数
     */
    public String getSufSendAmount() {
        return sufSendAmount;
    }

    /**
     * 设置释放后总剩余钱数
     *
     * @param sufSendAmount 释放后总剩余钱数
     */
    public void setSufSendAmount(String sufSendAmount) {
        this.sufSendAmount = sufSendAmount;
    }

    /**
     * 获取本次应释放钱数
     *
     * @return send_amount - 本次应释放钱数
     */
    public String getSendAmount() {
        return sendAmount;
    }

    /**
     * 设置本次应释放钱数
     *
     * @param sendAmount 本次应释放钱数
     */
    public void setSendAmount(String sendAmount) {
        this.sendAmount = sendAmount;
    }

    /**
     * 获取上次失败,填充的钱数
     *
     * @return pad_amount - 上次失败,填充的钱数
     */
    public String getPadAmount() {
        return padAmount;
    }

    /**
     * 设置上次失败,填充的钱数
     *
     * @param padAmount 上次失败,填充的钱数
     */
    public void setPadAmount(String padAmount) {
        this.padAmount = padAmount;
    }

    /**
     * 获取支付时间
     *
     * @return pay_time - 支付时间
     */
    public String getPayTime() {
        return payTime;
    }

    /**
     * 设置支付时间
     *
     * @param payTime 支付时间
     */
    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取确认时间
     *
     * @return confirm_time - 确认时间
     */
    public String getConfirmTime() {
        return confirmTime;
    }

    /**
     * 设置确认时间
     *
     * @param confirmTime 确认时间
     */
    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * 获取释放状态 0:成功 1:失败
     *
     * @return log_status - 释放状态 0:成功 1:失败
     */
    public Integer getLogStatus() {
        return logStatus;
    }

    /**
     * 设置释放状态 0:成功 1:失败
     *
     * @param logStatus 释放状态 0:成功 1:失败
     */
    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }

    /**
     * 获取钱包id
     *
     * @return wallet_id - 钱包id
     */
    public String getWalletId() {
        return walletId;
    }

    /**
     * 设置钱包id
     *
     * @param walletId 钱包id
     */
    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    /**
     * 获取支付hash
     *
     * @return pay_hash - 支付hash
     */
    public String getPayHash() {
        return payHash;
    }

    /**
     * 设置支付hash
     *
     * @param payHash 支付hash
     */
    public void setPayHash(String payHash) {
        this.payHash = payHash;
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