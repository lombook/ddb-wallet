package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import lombok.Data;


public class Wallet implements IAppIdModel{
    /**
     * 钱包id
     */
    @Id
    private Integer id;
    
    @Column(name = "wallet_id")
    private String walletId;

    /**
     * 钱包名
     */
    @Column(name = "wallet_name")
    private String walletName;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 用户地址
     */
    @Column(name = "public_key")
    private String publicKey;

    /**
     * 钱包所在链
     */
    @Column(name = "chain_id")
    private String chainId;

    private String prompt;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    /**
     * 用户支付密码
     */
    @Column(name = "pay_passwd")
    private String payPasswd;

    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;


    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
     * 获取钱包名
     *
     * @return wallet_name - 钱包名
     */
    public String getWalletName() {
        return walletName;
    }

    /**
     * 设置钱包名
     *
     * @param walletName 钱包名
     */
    public void setWalletName(String walletName) {
        this.walletName = walletName;
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
     * 获取用户地址
     *
     * @return public_key - 用户地址
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 设置用户地址
     *
     * @param publicKey 用户地址
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 获取钱包所在链
     *
     * @return chain_id - 钱包所在链
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置钱包所在链
     *
     * @param chainId 钱包所在链
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * @return prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @param prompt
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @return created_time
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取用户支付密码
     *
     * @return pay_passwd - 用户支付密码
     */
    public String getPayPasswd() {
        return payPasswd;
    }

    /**
     * 设置用户支付密码
     *
     * @param payPasswd 用户支付密码
     */
    public void setPayPasswd(String payPasswd) {
        this.payPasswd = payPasswd;
    }


}