package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "ddb_chain_exchange_record")
public class DdbChainExchangeRecord {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    @Column(name = "order_id")
    private String orderId;

    /**
     * 总账ID
     */
    @Column(name = "asset_account_id")
    private String assetAccountId;

    /**
     * 用户ID
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 链ID
     */
    @Column(name = "chain_id")
    private String chainId;

    /**
     * 币ID
     */
    @Column(name = "coin_id")
    private String coinId;

    /**
     * 币名
     */
    private String currency;

    /**
     * 用户钱包地址
     */
    @Column(name = "cust_public_key")
    private String custPublicKey;

    /**
     * 转账金额
     */
    @Column(name = "coin_amount")
    private BigDecimal coinAmount;

    /**
     * 银关
     */
    @Column(name = "token_adress")
    private String tokenAdress;

    /**
     * 转账hash
     */
    @Column(name = "pay_hash")
    private String payHash;

    /**
     * 状态状态(0未开始 1待check 2完成 -1失败)
     */
    private Integer state;

    /**
     * 转账次数
     */
    private Integer count;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * @param zid
     */
    public void setZid(String zid) {
        this.zid = zid;
    }

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取总账ID
     *
     * @return asset_account_id - 总账ID
     */
    public String getAssetAccountId() {
        return assetAccountId;
    }

    /**
     * 设置总账ID
     *
     * @param assetAccountId 总账ID
     */
    public void setAssetAccountId(String assetAccountId) {
        this.assetAccountId = assetAccountId;
    }

    /**
     * 获取用户ID
     *
     * @return cust_id - 用户ID
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户ID
     *
     * @param custId 用户ID
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * 获取链ID
     *
     * @return chain_id - 链ID
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置链ID
     *
     * @param chainId 链ID
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取币ID
     *
     * @return coin_id - 币ID
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置币ID
     *
     * @param coinId 币ID
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取币名
     *
     * @return currency - 币名
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 设置币名
     *
     * @param currency 币名
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 获取用户钱包地址
     *
     * @return cust_public_key - 用户钱包地址
     */
    public String getCustPublicKey() {
        return custPublicKey;
    }

    /**
     * 设置用户钱包地址
     *
     * @param custPublicKey 用户钱包地址
     */
    public void setCustPublicKey(String custPublicKey) {
        this.custPublicKey = custPublicKey;
    }

    /**
     * 获取转账金额
     *
     * @return coin_amount - 转账金额
     */
    public BigDecimal getCoinAmount() {
        return coinAmount;
    }

    /**
     * 设置转账金额
     *
     * @param coinAmount 转账金额
     */
    public void setCoinAmount(BigDecimal coinAmount) {
        this.coinAmount = coinAmount;
    }

    /**
     * 获取银关
     *
     * @return token_adress - 银关
     */
    public String getTokenAdress() {
        return tokenAdress;
    }

    /**
     * 设置银关
     *
     * @param tokenAdress 银关
     */
    public void setTokenAdress(String tokenAdress) {
        this.tokenAdress = tokenAdress;
    }

    /**
     * 获取转账hash
     *
     * @return pay_hash - 转账hash
     */
    public String getPayHash() {
        return payHash;
    }

    /**
     * 设置转账hash
     *
     * @param payHash 转账hash
     */
    public void setPayHash(String payHash) {
        this.payHash = payHash;
    }

    /**
     * 获取状态状态(0未开始 1待check 2完成 -1失败)
     *
     * @return state - 状态状态(0未开始 1待check 2完成 -1失败)
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态状态(0未开始 1待check 2完成 -1失败)
     *
     * @param state 状态状态(0未开始 1待check 2完成 -1失败)
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取转账次数
     *
     * @return count - 转账次数
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置转账次数
     *
     * @param count 转账次数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}