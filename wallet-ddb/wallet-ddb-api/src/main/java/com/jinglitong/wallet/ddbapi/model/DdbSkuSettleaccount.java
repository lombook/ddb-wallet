package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "ddb_sku_settleaccount")
public class DdbSkuSettleaccount {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 规则id
     */
    @Column(name = "zid")
    private String zid;

    /**
     * 规格id
     */
    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "cust_id")
    private String custId;
    
    @Column(name = "amount")
    private Integer amount;

    @Column(name = "inte_id")
    private String inteId;
     
    @Column(name = "effective_time")
    private Date effectiveTime;
    
    @Column(name = "expiry_time")
    private Date expiryTime;
    
    @Column(name = "create_time")
    private Date createTime;

}