package com.jinglitong.wallet.api.model;

import javax.persistence.*;

@Table(name = "sub_chain")
public class SubChain implements IAppIdModel{
    /**
     * 主键
     */
    @Id
    private  Integer  id;
    /**
     * 币种ID
     */

    @Column(name = "coin_id")
    private String coinId;

    /**
     * 主链ID
     */
    @Column(name = "chain_id")
    private String chainId;

    /**
     * 币种名
     */
    @Column(name = "coin_name")
    private String coinName;

    /**
     * 币种
     */
    private String currency;

    /**
     * 币种图片
     */
    @Column(name = "coin_img")
    private String coinImg;

    /**
     * 币种描述
     */
    @Column(name = "coin_remark")
    private String coinRemark;

    /**
     * 合约地址
     */
    @Column(name = "token_address")
    private String tokenAddress;

    /**
     * 状态1可用0不可用
     */
    private Boolean state;

    /**
     * 是否主币1是0不是
是否主币1是0不是
是否主币1是0不是
     */
    @Column(name = "base_chain")
    private Boolean baseChain;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 修改日期
     */
    @Column(name = "update_time")
    private String updateTime;

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
     * 获取币种ID
     *
     * @return coin_id - 币种ID
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置币种ID
     *
     * @param coinId 币种ID
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取主链ID
     *
     * @return chain_id - 主链ID
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置主链ID
     *
     * @param chainId 主链ID
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取币种名
     *
     * @return coin_name - 币种名
     */
    public String getCoinName() {
        return coinName;
    }

    /**
     * 设置币种名
     *
     * @param coinName 币种名
     */
    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    /**
     * 获取币种
     *
     * @return currency - 币种
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 设置币种
     *
     * @param currency 币种
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 获取币种图片
     *
     * @return coin_img - 币种图片
     */
    public String getCoinImg() {
        return coinImg;
    }

    /**
     * 设置币种图片
     *
     * @param coinImg 币种图片
     */
    public void setCoinImg(String coinImg) {
        this.coinImg = coinImg;
    }

    /**
     * 获取币种描述
     *
     * @return coin_remark - 币种描述
     */
    public String getCoinRemark() {
        return coinRemark;
    }

    /**
     * 设置币种描述
     *
     * @param coinRemark 币种描述
     */
    public void setCoinRemark(String coinRemark) {
        this.coinRemark = coinRemark;
    }

    /**
     * 获取合约地址
     *
     * @return token_address - 合约地址
     */
    public String getTokenAddress() {
        return tokenAddress;
    }

    /**
     * 设置合约地址
     *
     * @param tokenAddress 合约地址
     */
    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    /**
     * 获取状态1可用0不可用
     *
     * @return state - 状态1可用0不可用
     */
    public Boolean getState() {
        return state;
    }

    /**
     * 设置状态1可用0不可用
     *
     * @param state 状态1可用0不可用
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * 获取是否主币1是0不是
是否主币1是0不是
是否主币1是0不是
     *
     * @return base_chain - 是否主币1是0不是
是否主币1是0不是
是否主币1是0不是
     */
    public Boolean getBaseChain() {
        return baseChain;
    }

    /**
     * 设置是否主币1是0不是
是否主币1是0不是
是否主币1是0不是
     *
     * @param baseChain 是否主币1是0不是
是否主币1是0不是
是否主币1是0不是
     */
    public void setBaseChain(Boolean baseChain) {
        this.baseChain = baseChain;
    }

    /**
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改日期
     *
     * @return update_time - 修改日期
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改日期
     *
     * @param updateTime 修改日期
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


}