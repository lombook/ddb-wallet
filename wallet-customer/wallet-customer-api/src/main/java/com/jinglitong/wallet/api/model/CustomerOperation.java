package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "customer_operation")
public class CustomerOperation implements IAppIdModel{
    @Id
    private Integer id;
    
    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "cust_id")
    private String custId;

    private Boolean status;

    @Column(name = "chain_id")
    private String chainId;

    @Column(name = "send_address")
    private String sendAddress;

    @Column(name = "rev_address")
    private String revAddress;

    private String currency;

    private String amount;

    private String tx;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;
    
    /**
     * 钱包app
     */
    @Column(name="app_id")
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
     * @return operation_id
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * @param operationId
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return cust_id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * @param custId
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * @return status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Boolean status) {
        this.status = status;
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
     * @return send_address
     */
    public String getSendAddress() {
        return sendAddress;
    }

    /**
     * @param sendAddress
     */
    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    /**
     * @return rev_address
     */
    public String getRevAddress() {
        return revAddress;
    }

    /**
     * @param revAddress
     */
    public void setRevAddress(String revAddress) {
        this.revAddress = revAddress;
    }

    /**
     * @return currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return tx
     */
    public String getTx() {
        return tx;
    }

    /**
     * @param tx
     */
    public void setTx(String tx) {
        this.tx = tx;
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
}