package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "frozen_release_excel_source")
public class FrozenReleaseExcelSource implements IAppIdModel{
    /**
     * 主键id
     */
    @Id
    private Integer id;
    
    @Column(name = "source_id")
    private String sourceId;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 金额
     */
    private String amount;

    /**
     * 规则id
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 导入时间
     */
    @Column(name = "import_time")
    private String importTime;

    /**
     * 用户id
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 钱包id
     */
    @Column(name = "wallet_id")
    private String walletId;

    /**
     * 账户地址
     */
    private String address;

    @Column(name = "chain_id")
    private String chainId;

    /**
     * 链id
     */
    @Column(name = "coin_id")
    private String coinId;

    /**
     * 状态 0:导入 1:已开始进入 2:已完成 3:已取消
     */
    @Column(name = "source_status")
    private Integer sourceStatus;

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

    private MainChain mainChain;
    
    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;
    
    @Transient
    private String walletName;
    
    @Transient
    private String rest;
    
    @Transient
    private String relea;
    
    
    public String getRelea() {
		return relea;
	}

	public void setRelea(String relea) {
		this.relea = relea;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

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

    public MainChain getMainChain() {
        return mainChain;
    }

    public void setMainChain(MainChain mainChain) {
        this.mainChain = mainChain;
    }

    /**
     * 获取主键id
     *
     * @return source_id - 主键id
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * 设置主键id
     *
     * @param sourceId 主键id
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 获取登录账号
     *
     * @return account - 登录账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置登录账号
     *
     * @param account 登录账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    public String getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * 获取规则id
     *
     * @return rule_id - 规则id
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * 设置规则id
     *
     * @param ruleId 规则id
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * 获取导入时间
     *
     * @return import_time - 导入时间
     */
    public String getImportTime() {
        return importTime;
    }

    /**
     * 设置导入时间
     *
     * @param importTime 导入时间
     */
    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    /**
     * 获取用户id
     *
     * @return customer_id - 用户id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * 设置用户id
     *
     * @param customerId 用户id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
     * 获取账户地址
     *
     * @return address - 账户地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置账户地址
     *
     * @param address 账户地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return chain_id
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * @param chainId
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取链id
     *
     * @return coin_id - 链id
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置链id
     *
     * @param coinId 链id
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取状态 0:导入 1:已开始进入 2:已完成 3:已取消
     *
     * @return source_status - 状态 0:导入 1:已开始进入 2:已完成 3:已取消
     */
    public Integer getSourceStatus() {
        return sourceStatus;
    }

    /**
     * 设置状态 0:导入 1:已开始进入 2:已完成 3:已取消
     *
     * @param sourceStatus 状态 0:导入 1:已开始进入 2:已完成 3:已取消
     */
    public void setSourceStatus(Integer sourceStatus) {
        this.sourceStatus = sourceStatus;
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