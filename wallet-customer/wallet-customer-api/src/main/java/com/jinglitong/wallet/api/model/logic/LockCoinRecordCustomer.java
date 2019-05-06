package com.jinglitong.wallet.api.model.logic;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.IAppIdModel;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.Wallet;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "lock_coin_record")
public class LockCoinRecordCustomer implements IAppIdModel{
    @Id
    private Integer id;
    
    @Column(name = "lock_record_id")
    private String lockRecordId;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    private Customer customer;
    /**
     * 用户钱包id
     */
    @Column(name = "wallet_id")
    private String walletId;

    private Wallet wallet;
    /**
     * 用户币种id
     */
    @Column(name = "coin_id")
    private String coinId;

    private SubChain subChain;
    /**
     * 用户钱包地址
     */
    @Column(name = "wallet_address")
    private String walletAddress;

    /**
     * 锁住天数
     */
    @Column(name = "lock_days")
    private Integer lockDays;

    /**
     * 利率  正确的值除以10000
     */
    @Column(name = "lock_rate")
    private Integer lockRate;

    /**
     * 锁仓开始日期
     */
    @Column(name = "lock_start_date")
    private Date lockStartDate;

    /**
     * 锁仓结束日期
     */
    @Column(name = "lock_end_date")
    private Date lockEndDate;

    /**
     * 理财规则id
     */
    @Column(name = "lock_roue_id")
    private String lockRoueId;

    /**
     * 返还利息
     */
    @Column(name = "reward_amount")
    private String rewardAmount;

    /**
     * 本金
本金
     */
    @Column(name = "lock_amount")
    private String lockAmount;

    /**
     * 转账hash
     */
    @Column(name = "pay_hash")
    private String payHash;

    /**
     * 回本金hash
     */
    @Column(name = "recevice_hash")
    private String receviceHash;

    /**
     * 回利息hash
     */
    @Column(name = "sweet_hash")
    private String sweetHash;

    /**
     * 4个状态(1正在锁,2锁成功,3解锁,4发放利息)
     */
    @Column(name = "lock_status")
    private Integer lockStatus;

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
     * 支付时间
     */
    @Column(name = "pay_time")
    private String payTime;

    /**
     * 回款时间
     */
    @Column(name = "recevice_time")
    private String receviceTime;

    /**
     * 回利息时间
     */
    @Column(name = "sweet_time")
    private String sweetTime;
    
    /**
     * 钱包app
     */
    @Column(name = "app_id")
    private String appId;
    
    @Transient 
    private String walletName;
    
    
    
    public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
     * @return lock_record_id
     */
    public String getLockRecordId() {
        return lockRecordId;
    }

    /**
     * @param lockRecordId
     */
    public void setLockRecordId(String lockRecordId) {
        this.lockRecordId = lockRecordId;
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
     * 获取用户钱包id
     *
     * @return wallet_id - 用户钱包id
     */
    public String getWalletId() {
        return walletId;
    }

    /**
     * 设置用户钱包id
     *
     * @param walletId 用户钱包id
     */
    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    /**
     * 获取用户币种id
     *
     * @return coin_id - 用户币种id
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置用户币种id
     *
     * @param coinId 用户币种id
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取用户钱包地址
     *
     * @return wallet_address - 用户钱包地址
     */
    public String getWalletAddress() {
        return walletAddress;
    }

    /**
     * 设置用户钱包地址
     *
     * @param walletAddress 用户钱包地址
     */
    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    /**
     * 获取锁住天数
     *
     * @return lock_days - 锁住天数
     */
    public Integer getLockDays() {
        return lockDays;
    }

    /**
     * 设置锁住天数
     *
     * @param lockDays 锁住天数
     */
    public void setLockDays(Integer lockDays) {
        this.lockDays = lockDays;
    }

    /**
     * 获取利率  正确的值除以10000
     *
     * @return lock_rate - 利率  正确的值除以10000
     */
    public Integer getLockRate() {
        return lockRate;
    }

    /**
     * 设置利率  正确的值除以10000
     *
     * @param lockRate 利率  正确的值除以10000
     */
    public void setLockRate(Integer lockRate) {
        this.lockRate = lockRate;
    }

    /**
     * 获取锁仓开始日期
     *
     * @return lock_start_date - 锁仓开始日期
     */
    public Date getLockStartDate() {
        return lockStartDate;
    }

    /**
     * 设置锁仓开始日期
     *
     * @param lockStartDate 锁仓开始日期
     */
    public void setLockStartDate(Date lockStartDate) {
        this.lockStartDate = lockStartDate;
    }

    /**
     * 获取锁仓结束日期
     *
     * @return lock_end_date - 锁仓结束日期
     */
    public Date getLockEndDate() {
        return lockEndDate;
    }

    /**
     * 设置锁仓结束日期
     *
     * @param lockEndDate 锁仓结束日期
     */
    public void setLockEndDate(Date lockEndDate) {
        this.lockEndDate = lockEndDate;
    }

    /**
     * 获取理财规则id
     *
     * @return lock_roue_id - 理财规则id
     */
    public String getLockRoueId() {
        return lockRoueId;
    }

    /**
     * 设置理财规则id
     *
     * @param lockRoueId 理财规则id
     */
    public void setLockRoueId(String lockRoueId) {
        this.lockRoueId = lockRoueId;
    }

    /**
     * 获取返还利息
     *
     * @return reward_amount - 返还利息
     */
    public String getRewardAmount() {
        return rewardAmount;
    }

    /**
     * 设置返还利息
     *
     * @param rewardAmount 返还利息
     */
    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    /**
     * 获取本金
本金
     *
     * @return lock_ amount - 本金
本金
     */
    public String getLockAmount() {
        return lockAmount;
    }

    /**
     * 设置本金
本金
     *
     * @param lockAmount 本金
本金
     */
    public void setLockAmount(String lockAmount) {
        this.lockAmount = lockAmount;
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
     * 获取回本金hash
     *
     * @return recevice_hash - 回本金hash
     */
    public String getReceviceHash() {
        return receviceHash;
    }

    /**
     * 设置回本金hash
     *
     * @param receviceHash 回本金hash
     */
    public void setReceviceHash(String receviceHash) {
        this.receviceHash = receviceHash;
    }

    /**
     * 获取回利息hash
     *
     * @return sweet_hash - 回利息hash
     */
    public String getSweetHash() {
        return sweetHash;
    }

    /**
     * 设置回利息hash
     *
     * @param sweetHash 回利息hash
     */
    public void setSweetHash(String sweetHash) {
        this.sweetHash = sweetHash;
    }

    /**
     * 获取4个状态(1正在锁,2锁成功,3解锁,4发放利息)
     *
     * @return lock _status - 4个状态(1正在锁,2锁成功,3解锁,4发放利息)
     */
    public Integer getLockStatus() {
        return lockStatus;
    }

    /**
     * 设置4个状态(1正在锁,2锁成功,3解锁,4发放利息)
     *
     * @param lockStatus 4个状态(1正在锁,2锁成功,3解锁,4发放利息)
     */
    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
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
     * 获取回款时间
     *
     * @return recevice_time - 回款时间
     */
    public String getReceviceTime() {
        return receviceTime;
    }

    /**
     * 设置回款时间
     *
     * @param receviceTime 回款时间
     */
    public void setReceviceTime(String receviceTime) {
        this.receviceTime = receviceTime;
    }

    /**
     * 获取回利息时间
     *
     * @return sweet_time - 回利息时间
     */
    public String getSweetTime() {
        return sweetTime;
    }

    /**
     * 设置回利息时间
     *
     * @param sweetTime 回利息时间
     */
    public void setSweetTime(String sweetTime) {
        this.sweetTime = sweetTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public SubChain getSubChain() {
        return subChain;
    }

    public void setSubChain(SubChain subChain) {
        this.subChain = subChain;
    }
}