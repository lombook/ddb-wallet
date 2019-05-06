package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ddb_shoreholder_upgrade_seq")
public class DdbShoreholderUpgradeSeq {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;
    /**
     * 订单表id
     */
    @Column(name = "order_id")
    private String orderId;
    /**
     * 升级后股东层级
     */
    @Column(name = "new_level_define")
    private String newLevelDefine;
    /**
     * 升级对应的股东升级规则zid
     */
    @Column(name = "upgrade_rule_zid")
    private String upgradeRuleZid;
    /**
     * 升级时间
     */
    @Column(name = "upgrade_time")
    private Date upgradeTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * 获得订单id
     * @return orderId - 订单id
     */
    public String getOrderId() {
		return orderId;
	}
    /**
     * 设置订单id
     * @param orderId - 订单id
     */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
     * 获取升级后股东层级
     *
     * @return new_level_define - 升级后股东层级
     */
    public String getNewLevelDefine() {
        return newLevelDefine;
    }

    /**
     * 设置升级后股东层级
     *
     * @param newLevelDefine 升级后股东层级
     */
    public void setNewLevelDefine(String newLevelDefine) {
        this.newLevelDefine = newLevelDefine;
    }
    /*
     * 获得股东升级规则id
     */
    public String getUpgradeRuleZid() {
		return upgradeRuleZid;
	}
    /*
     * 设置股东升级规则id
     */
	public void setUpgradeRuleZid(String upgradeRuleZid) {
		this.upgradeRuleZid = upgradeRuleZid;
	}

	/**
     * 获取升级时间
     *
     * @return upgrade_time - 升级时间
     */
    public Date getUpgradeTime() {
        return upgradeTime;
    }

    /**
     * 设置升级时间
     *
     * @param upgradeTime 升级时间
     */
    public void setUpgradeTime(Date upgradeTime) {
        this.upgradeTime = upgradeTime;
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
}