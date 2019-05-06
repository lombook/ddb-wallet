package com.jinglitong.wallet.ddbapi.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_integ_exchange_rule")
public class DdbIntegExchangeRule {
    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * zid
     */
    private String zid;

    /**
     * ��Ȩ����
     */
    @Column(name = "ex_name")
    private String exName;

    /**
     * ��������(1�������һ� 2���籴�һ�)
     */
    @Column(name = "rule_type")
    private Integer ruleType;

    /**
     * ʹ�õĻ�������������
     */
    @Column(name = "integ_cname")
    private String integCname;

    /**
     * ʹ�õĻ�������
     */
    @Column(name = "integ_name")
    private String integName;

    /**
     * ʹ�õĻ������͵�λ����
     */
    @Column(name = "integ_percent")
    private BigDecimal integPercent;

    
    /**
     * �һ��Ļ�������������
     */
    @Column(name = "ex_integ_cname")
    private String exIntegCname;

    /**
     * �һ��Ļ�������
     */
    @Column(name = "ex_integ_name")
    private String exIntegName;

    
    @Column(name = "charge_integ_cname")
    private String chargeIntegCname;
    
    @Column(name = "charge_integ_name")
    private String chargeIntegName;
    
    @Column(name ="charge_type")
    private Integer chargeType;
    
    @Column(name ="charge_percent")
    private BigDecimal chargePercent;
    
    @Column(name ="min_amount")
    private Integer minAmount;
    
    @Column(name ="resave_acc_name")
    private String resaveAccName;
    
    @Column(name ="resave_acc_Cname")
    private String resaveAccCname;
    
    @Column(name ="ex_acc_name")
    private String exAccName;
    
    @Column(name ="ex_acc_cname")
    private String exAccCname;
    
    @Column(name = "charge_acc_name")
    private String chargeAccName;

    @Column(name = "charge_acc_cname")
    private String chargeAccCname;
    
    @Column(name ="effective_time")
    private String effectiveTime;
    /**
     * ��Ч��
     */
    @Column(name = "expiry_time")
    private Timestamp expiryTime;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * ����ʱ��
     */
    @Column(name = "update_time")
    private Timestamp updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public String getExName() {
		return exName;
	}

	public void setExName(String exName) {
		this.exName = exName;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public String getIntegCname() {
		return integCname;
	}

	public void setIntegCname(String integCname) {
		this.integCname = integCname;
	}

	public String getIntegName() {
		return integName;
	}

	public void setIntegName(String integName) {
		this.integName = integName;
	}

	public BigDecimal getIntegPercent() {
		return integPercent;
	}

	public void setIntegPercent(BigDecimal integPercent) {
		this.integPercent = integPercent;
	}

	public String getExIntegCname() {
		return exIntegCname;
	}

	public void setExIntegCname(String exIntegCname) {
		this.exIntegCname = exIntegCname;
	}

	public String getExIntegName() {
		return exIntegName;
	}

	public void setExIntegName(String exIntegName) {
		this.exIntegName = exIntegName;
	}

	public String getChargeIntegCname() {
		return chargeIntegCname;
	}

	public void setChargeIntegCname(String chargeIntegCname) {
		this.chargeIntegCname = chargeIntegCname;
	}

	public String getChargeIntegName() {
		return chargeIntegName;
	}

	public void setChargeIntegName(String chargeIntegName) {
		this.chargeIntegName = chargeIntegName;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}
 

	public BigDecimal getChargePercent() {
		return chargePercent;
	}

	public void setChargePercent(BigDecimal chargePercent) {
		this.chargePercent = chargePercent;
	}

	public Integer getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}

	public String getResaveAccName() {
		return resaveAccName;
	}

	public void setResaveAccName(String resaveAccName) {
		this.resaveAccName = resaveAccName;
	}

	public String getResaveAccCname() {
		return resaveAccCname;
	}

	public void setResaveAccCname(String resaveAccCname) {
		this.resaveAccCname = resaveAccCname;
	}

	public String getExAccName() {
		return exAccName;
	}

	public void setExAccName(String exAccName) {
		this.exAccName = exAccName;
	}

	public String getExAccCname() {
		return exAccCname;
	}

	public void setExAccCname(String exAccCname) {
		this.exAccCname = exAccCname;
	}

	public String getChargeAccName() {
		return chargeAccName;
	}

	public void setChargeAccName(String chargeAccName) {
		this.chargeAccName = chargeAccName;
	}

	public String getChargeAccCname() {
		return chargeAccCname;
	}

	public void setChargeAccCname(String chargeAccCname) {
		this.chargeAccCname = chargeAccCname;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Timestamp getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Timestamp expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

    
    
    
 

   
}