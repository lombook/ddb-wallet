package com.jinglitong.wallet.api.model;

import javax.persistence.*;

@Table(name = "wallet_coin")
public class WalletCoin implements IAppIdModel {
    /**
     * 钱包子币id
     */
    @Id
    private Integer id;
    
    @Column(name = "wallet_coin_id")
    private String walletCoinId;

    /**
     * 钱包id
     */
    @Column(name = "wallet_id")
    private String walletId;

    /**
     * 链id
     */
    @Column(name = "coin_id")
    private String coinId;

    @Column(name = "create_time")
    private String createTime;

    /**
     * 是否显示1显示0不显示
    用于主币不可被删除
     */
    @Column(name = "is_show")
    private Boolean isShow;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;
    
    
    
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
     * 获取钱包子币id
     *
     * @return wallet_coin_id - 钱包子币id
     */
    public String getWalletCoinId() {
        return walletCoinId;
    }

    /**
     * 设置钱包子币id
     *
     * @param walletCoinId 钱包子币id
     */
    public void setWalletCoinId(String walletCoinId) {
        this.walletCoinId = walletCoinId;
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
     * @return created_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createdTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否显示1显示0不显示
用于主币不可被删除
     *
     * @return is_show - 是否显示1显示0不显示
用于主币不可被删除
     */
    public Boolean getIsShow() {
        return isShow;
    }

    /**
     * 设置是否显示1显示0不显示
用于主币不可被删除
     *
     * @param isShow 是否显示1显示0不显示
用于主币不可被删除
     */
    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
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
}