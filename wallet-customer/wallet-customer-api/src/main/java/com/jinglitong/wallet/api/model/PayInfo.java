package com.jinglitong.wallet.api.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "pay_info")
public class PayInfo implements IAppIdModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="zid")
    private String zId;
    /**
     * 唯一订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 商家id
     */
    @Column(name = "seller_id")
    private String sellerId;

    /**
     * 商家订单号
     */
    @NotBlank(message = "商家订单号不能为空")
    @Column(name = "seller_order_no")
    private String sellerOrderNo;

    /**
     * 支付币种编码
     */
    @NotBlank(message = "支付币种不能为空")
    @Column(name = "coin_code")
    private String coinCode;

    /**
     * 钱包用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 链id
     */
    @Column(name = "chain_id")
    private String chainId;


    private MainChain mainChain;

    /**
     * 1: swt  2:eth 3: moac 4: eos
     */
    @Column(name = "chain_type")
    private String chainType;

    /**
     * 币id
     */
    @Column(name = "coin_id")
    private String coinId;

    private SubChain subChain;
    /**
     * 币类型
     */
    @Column(name = "coin_type")
    private String coinType;

    /**
     * 金额
     */
    @DecimalMin(value = "0.00000000001",message = "金额必须大于0.00000000001")
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    /**
     * 0: 未支付   1：申请支付成功 2：申请支付失败  3：支付成功  4：支付失败
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 通知商家次数
     */
    @Column(name = "notice_time")
    private Integer noticeTime;

    /**
     * 通知状态 0：未通知  1：成功通知   2：未收到反馈
     */
    @Column(name = "notice_status")
    private Integer noticeStatus;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Column(name = "product_name")
    private String productName;

    /**
     * 支付地址
     */
    @Column(name = "pay_address")
    private String payAddress;

    /**
     * 收款地址
     */
    @Column(name = "receive_address")
    private String receiveAddress;

    /**
     * 交易hash
     */
    @Column(name = "trade_hash")
    private String tradeHash;

    /**
     * 错误信息
     */
    @Column(name = "error_msg")
    private String errorMsg;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "issuer")
    private String issuer;

    @Column(name = "seller_name")
    private String sellerName;


    @Column(name = "wallet_id")
    private String walletId;
    
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
     * 获取唯一订单号
     *
     * @return order_no - 唯一订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置唯一订单号
     *
     * @param orderNo 唯一订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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
     * 获取商家订单号
     *
     * @return seller_order_no - 商家订单号
     */
    public String getSellerOrderNo() {
        return sellerOrderNo;
    }

    /**
     * 设置商家订单号
     *
     * @param sellerOrderNo 商家订单号
     */
    public void setSellerOrderNo(String sellerOrderNo) {
        this.sellerOrderNo = sellerOrderNo;
    }

    /**
     * 获取支付币种编码
     *
     * @return coin_code - 支付币种编码
     */
    public String getCoinCode() {
        return coinCode;
    }

    /**
     * 设置支付币种编码
     *
     * @param coinCode 支付币种编码
     */
    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    /**
     * 获取1: swt  2:eth 3: moac 4: eos
     *
     * @return chain_type - 1: swt  2:eth 3: moac 4: eos
     */
    public String getChainType() {
        return chainType;
    }

    /**
     * 设置1: swt  2:eth 3: moac 4: eos
     *
     * @param chainType 1: swt  2:eth 3: moac 4: eos
     */
    public void setChainType(String chainType) {
        this.chainType = chainType;
    }

    /**
     * 获取币类型
     *
     * @return coin_type - 币类型
     */
    public String getCoinType() {
        return coinType;
    }

    /**
     * 设置币类型
     *
     * @param coinType 币类型
     */
    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取0: 未支付   1：申请支付  2：支付成功  3：支付失败
     *
     * @return pay_status - 0: 未支付   1：申请支付  2：支付成功  3：支付失败
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * 设置0: 未支付   1：申请支付  2：支付成功  3：支付失败
     *
     * @param payStatus 0: 未支付   1：申请支付  2：支付成功  3：支付失败
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 获取通知商家次数
     *
     * @return notice_time - 通知商家次数
     */
    public Integer getNoticeTime() {
        return noticeTime;
    }

    /**
     * 设置通知商家次数
     *
     * @param noticeTime 通知商家次数
     */
    public void setNoticeTime(Integer noticeTime) {
        this.noticeTime = noticeTime;
    }

    /**
     * 获取通知状态 0：未通知  1：成功通知   2：未收到反馈
     *
     * @return notice_status - 通知状态 0：未通知  1：成功通知   2：未收到反馈
     */
    public Integer getNoticeStatus() {
        return noticeStatus;
    }

    /**
     * 设置通知状态 0：未通知  1：成功通知   2：未收到反馈
     *
     * @param noticeStatus 通知状态 0：未通知  1：成功通知   2：未收到反馈
     */
    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    /**
     * 获取商品名称
     *
     * @return product_name - 商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置商品名称
     *
     * @param productName 商品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取支付地址
     *
     * @return pay_address - 支付地址
     */
    public String getPayAddress() {
        return payAddress;
    }

    /**
     * 设置支付地址
     *
     * @param payAddress 支付地址
     */
    public void setPayAddress(String payAddress) {
        this.payAddress = payAddress;
    }

    /**
     * 获取收款地址
     *
     * @return receive_address - 收款地址
     */
    public String getReceiveAddress() {
        return receiveAddress;
    }

    /**
     * 设置收款地址
     *
     * @param receiveAddress 收款地址
     */
    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    /**
     * 获取交易hash
     *
     * @return trade_hash - 交易hash
     */
    public String getTradeHash() {
        return tradeHash;
    }

    /**
     * 设置交易hash
     *
     * @param tradeHash 交易hash
     */
    public void setTradeHash(String tradeHash) {
        this.tradeHash = tradeHash;
    }

    /**
     * 获取错误信息
     *
     * @return error_msg - 错误信息
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置错误信息
     *
     * @param errorMsg 错误信息
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
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

    public MainChain getMainChain() {
        return mainChain;
    }

    public void setMainChain(MainChain mainChain) {
        this.mainChain = mainChain;
    }

    public SubChain getSubChain() {
        return subChain;
    }

    public void setSubChain(SubChain subChain) {
        this.subChain = subChain;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }
}