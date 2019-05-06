package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;

import javax.persistence.Table;
@Table(name = "ddb_customer_team_sum")
public class DdbCustomerTeamSum {
	
    private Integer id;
    /**
     * 用户id
     */
    private String custId;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 团队业绩
     */
    private Long teamSumAmount;
    /**
     * 订单日期
     */
    private String orderDate;

    private String createTime;

    private String updateTime;
    
    private String shopTrade;
    

	public String getShopTrade() {
		return shopTrade;
	}

	public void setShopTrade(String shopTrade) {
		this.shopTrade = shopTrade;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getTeamSumAmount() {
        return teamSumAmount;
    }

    public void setTeamSumAmount(Long teamSumAmount) {
        this.teamSumAmount = teamSumAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

}