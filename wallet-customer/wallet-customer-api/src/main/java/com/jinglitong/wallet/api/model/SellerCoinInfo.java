package com.jinglitong.wallet.api.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name = "seller_coin_info")
public class SellerCoinInfo implements IAppIdModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="zid")
    private String zId;
    /**
     * 币种编号
     */
    @Column(name = "coin_code")
    private String coinCode;

    /**
     * 链id
     */
    @NotBlank(message = "链id不能为空")
    @Column(name = "chain_id")
    private String chainId;

    /**
     * 链
     */
    @NotBlank(message = "链不能为空")
    @Column(name = "chain_currency")
    private String chainCurrency;

    /**
     * 币id
     */
    @NotBlank(message = "币id不能为空")
    @Column(name = "coin_id")
    private String coinId;

    /**
     * 币
     */
    @NotBlank(message = "币不能为空")
    @Column(name = "coin_currency")
    private String coinCurrency;

    /**
     * 支付地址
     */
    @Column(name = "seller_address")
    private String sellerAddress;

    /**
     * 商家id
     */
    @NotBlank(message = "商家id不能为空")
    @Column(name = "seller_id")
    private String sellerId;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 钱包app
     */
    @Column(name ="app_id")
    private String appId;
    
    @Transient 
    private String walletName;
    
    
    
    public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
    
    public String getzId() {
		return zId;
	}

	public void setzId(String zId) {
		this.zId = zId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
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
     * 获取币种编号
     *
     * @return coin_code - 币种编号
     */
    public String getCoinCode() {
        return coinCode;
    }

    /**
     * 设置币种编号
     *
     * @param coinCode 币种编号
     */
    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    /**
     * 获取链id
     *
     * @return chain_id - 链id
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * 设置链id
     *
     * @param chainId 链id
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取链
     *
     * @return chain_currency - 链
     */
    public String getChainCurrency() {
        return chainCurrency;
    }

    /**
     * 设置链
     *
     * @param chainCurrency 链
     */
    public void setChainCurrency(String chainCurrency) {
        this.chainCurrency = chainCurrency;
    }

    /**
     * 获取币id
     *
     * @return coin_id - 币id
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置币id
     *
     * @param coinId 币id
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取币
     *
     * @return coin_currency - 币
     */
    public String getCoinCurrency() {
        return coinCurrency;
    }

    /**
     * 设置币
     *
     * @param coinCurrency 币
     */
    public void setCoinCurrency(String coinCurrency) {
        this.coinCurrency = coinCurrency;
    }

    /**
     * 获取支付地址
     *
     * @return seller_address - 支付地址
     */
    public String getSellerAddress() {
        return sellerAddress;
    }

    /**
     * 设置支付地址
     *
     * @param sellerAddress 支付地址
     */
    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    /**
     * 获取商家id
     *
     * @return seller_id - 商家id
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * 设置商家id
     *
     * @param sellerId 商家id
     */
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改人
     *
     * @return update_by - 修改人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置修改人
     *
     * @param updateBy 修改人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public static MiniSellerCoin getMiniSellerCoin(SellerCoinInfo sellerCoinInfo){
        MiniSellerCoin miniSellerCoin = new MiniSellerCoin();
        miniSellerCoin.setCoinCurrency(sellerCoinInfo.getCoinCurrency());
        miniSellerCoin.setSellerAddress(sellerCoinInfo.getSellerAddress());
        miniSellerCoin.setCoinCode(sellerCoinInfo.getCoinCode());
        return miniSellerCoin;
    }
}