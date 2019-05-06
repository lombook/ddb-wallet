package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "frozen_release_affirm")
public class FrozenReleaseAffirm {
    /**
     * 主键id
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * logid
     */
    @Column(name = "log_id")
    private String logId;

    /**
     * detail_id
     */
    @Column(name = "detail_id")
    private String detailId;


    /**
     * 支付hash
     */
    @Column(name = "pay_hash")
    private String payHash;

    /**
     * 钱包地址
     */
    @Column(name = "wallet_id")
    private String walletId;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;



    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getPayHash() {
        return payHash;
    }

    public void setPayHash(String payHash) {
        this.payHash = payHash;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }
}