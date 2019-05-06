package com.jinglitong.wallet.api.model;

import javax.persistence.*;

@Table(name = "main_chain")
public class MainChain implements IAppIdModel{
    /**
     * 主链id
     */
    @Id
    private Integer id;
    
    @Column(name = "chain_id")
    private String chainId;

    /**
     * 主链名字
     */
    @Column(name = "chain_name")
    private String chainName;

    /**
     * 主链币种
     */
    @Column(name = "chain_currency")
    private String chainCurrency;

    /**
     * 主链对外IP接口
     */
    @Column(name = "chain_interface")
    private String chainInterface;

    /**
     * 主链图片
     */
    @Column(name = "chain_currency_img")
    private String chainCurrencyImg;

    /**
     * 描述
     */
    @Column(name = "chain_desc")
    private String chainDesc;

    /**
     * 处理类
     */
    @Column(name = "handle_name")
    private String handleName;

    /**
     * 状态1可用0不可用
     */
    private Boolean state;

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
     * 链是否自动激活
     */
    @Column(name = "active_flag")
    private Boolean activeFlag;

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
     * 获取主链id
     *
     * @return chain_id - 主链id
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置主链id
     *
     * @param chainId 主链id
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取主链名字
     *
     * @return chain_name - 主链名字
     */
    public String getChainName() {
        return chainName;
    }

    /**
     * 设置主链名字
     *
     * @param chainName 主链名字
     */
    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    /**
     * 获取主链币种
     *
     * @return chain_currency - 主链币种
     */
    public String getChainCurrency() {
        return chainCurrency;
    }

    /**
     * 设置主链币种
     *
     * @param chainCurrency 主链币种
     */
    public void setChainCurrency(String chainCurrency) {
        this.chainCurrency = chainCurrency;
    }

    /**
     * 获取主链对外IP接口
     *
     * @return chain_interface - 主链对外IP接口
     */
    public String getChainInterface() {
        return chainInterface;
    }

    /**
     * 设置主链对外IP接口
     *
     * @param chainInterface 主链对外IP接口
     */
    public void setChainInterface(String chainInterface) {
        this.chainInterface = chainInterface;
    }

    /**
     * 获取主链图片
     *
     * @return chain_currency_img - 主链图片
     */
    public String getChainCurrencyImg() {
        return chainCurrencyImg;
    }

    /**
     * 设置主链图片
     *
     * @param chainCurrencyImg 主链图片
     */
    public void setChainCurrencyImg(String chainCurrencyImg) {
        this.chainCurrencyImg = chainCurrencyImg;
    }

    /**
     * 获取描述
     *
     * @return chain_desc - 描述
     */
    public String getChainDesc() {
        return chainDesc;
    }

    /**
     * 设置描述
     *
     * @param chainDesc 描述
     */
    public void setChainDesc(String chainDesc) {
        this.chainDesc = chainDesc;
    }

    /**
     * 获取处理类
     *
     * @return handle_name - 处理类
     */
    public String getHandleName() {
        return handleName;
    }

    /**
     * 设置处理类
     *
     * @param handleName 处理类
     */
    public void setHandleName(String handleName) {
        this.handleName = handleName;
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

    /**
     * 获取链是否自动激活
     *
     * @return active_flag - 链是否自动激活
     */
    public Boolean getActiveFlag() {
        return activeFlag;
    }

    /**
     * 设置链是否自动激活
     *
     * @param activeFlag 链是否自动激活
     */
    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }
}