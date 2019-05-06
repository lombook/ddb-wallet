package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "frozen_release_log_fail")
public class FrozenReleaseLogFail {
    /**
     * 主键id
     */
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "zid")
    private String zid;
    /**
     * 日志Id
     */
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
     * 本次应释放钱数
     */
    @Column(name = "send_amount")
    private String sendAmount;

    /**
     * 失败状态
     */
    @Column(name = "fail_state")
    private int failState;

    /**
     * 失败状态
     */
    @Column(name = "count")
    private int count=0;



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
    
    /**
     * 成功状态
     */
    @Column(name = "success_state")
    private int successState;
    
    /**
     * 成功时间
     */
    @Column(name = "success_time")
    private String successTime;

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


    public int getFailState() {
        return failState;
    }

    public void setFailState(int failState) {
        this.failState = failState;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}