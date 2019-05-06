package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "ddb_rule_rc")
public class DdbRuleRc {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "zid")
	private String zid;
	
	@Column(name = "rule_id")
	private String ruleId;
	
	@Column(name = "is_special")
	private Integer isSpecial;
	
	@Column(name = "gf_value")
	private Integer gfValue;
	
	@Column(name = "rc_value")
	private Integer rcValue;
	
	@Column(name = "effective_time")
	private Date effectiveTime;
	
	@Column(name = "expiry_time")
	private Date expiryTime;
	
	@Column(name = "create_time")
	private Date createTime;
	
}
